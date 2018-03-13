

import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.computer.GraphComputer;
import org.apache.tinkerpop.gremlin.process.computer.bulkloading.BulkLoaderVertexProgram;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.EdgeLabel;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.VertexLabel;
import org.janusgraph.core.attribute.Geo;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.JanusGraphManagement;

import org.apache.tinkerpop.gremlin.spark.process.computer.SparkGraphComputer;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.hdfs.*;

public class HadoopGraphTestMain {
	public static JanusGraph openGraph() {
		JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
		return graph;
	}
	
	public static void createUserSchema(JanusGraph graph) {
		JanusGraphManagement mgmt = graph.openManagement();
		mgmt.makeVertexLabel("user").make();
		
		final PropertyKey userId = mgmt.makePropertyKey("userId").dataType(String.class).make();
		
		mgmt.makePropertyKey("userName").dataType(String.class).make();
		
		mgmt.makePropertyKey("userTotalSoldItem").dataType(Integer.class).make();
		
		final PropertyKey userLocation = mgmt.makePropertyKey("userLocation").dataType(Geoshape.class).make();
		
		// use integer for score data, because integer calculation is cheaper than double/float
		final PropertyKey userScore = mgmt.makePropertyKey("userScore").dataType(Integer.class).make();
		
		// define user property to store set of location has been searched by user.
		mgmt.makePropertyKey("userSearchLocationPref").dataType(String.class).cardinality(Cardinality.SET).make();

		
		// composite index for user.userId
		mgmt.buildIndex("userIdIndexComposite", Vertex.class).addKey(userId).buildCompositeIndex();
		
		// "search" is the name of index backend defined on /conf/janusgraph-cassandra-es.properties
		
		// mixed index for user.score
		mgmt.buildIndex("userScoreIndexMixed",Vertex.class).addKey(userScore).buildMixedIndex("search");
		
		// mixed index for user.userLocation
		mgmt.buildIndex("userLocationIndexMixed",Vertex.class).addKey(userLocation).buildMixedIndex("search");
		
		mgmt.commit();
	}
	
	public static void defineGratefulDeadSchema(JanusGraph graph) {
		JanusGraphManagement m = graph.openManagement();
		
	    // vertex labels
		VertexLabel artist = m.makeVertexLabel("artist").make();
		VertexLabel song   = m.makeVertexLabel("song").make();
	    
		// edge labels
	    EdgeLabel sungBy     = m.makeEdgeLabel("sungBy").make();
	    EdgeLabel writtenBy  = m.makeEdgeLabel("writtenBy").make();
	    EdgeLabel followedBy = m.makeEdgeLabel("followedBy").make();
	    
	    // vertex and edge properties
	    PropertyKey blid         = m.makePropertyKey("bulkLoader.vertex.id").dataType(Long.class).make();
	    PropertyKey name         = m.makePropertyKey("name").dataType(String.class).make();
	    PropertyKey songType     = m.makePropertyKey("songType").dataType(String.class).make();
	    PropertyKey performances = m.makePropertyKey("performances").dataType(Integer.class).make();
	    PropertyKey weight       = m.makePropertyKey("weight").dataType(Integer.class).make();
	    
	    // global indices
	    m.buildIndex("byBulkLoaderVertexId", Vertex.class).addKey(blid).buildCompositeIndex();
	    m.buildIndex("artistsByName", Vertex.class).addKey(name).indexOnly(artist).buildCompositeIndex();
	    m.buildIndex("songsByName", Vertex.class).addKey(name).indexOnly(song).buildCompositeIndex();
	    
	    // vertex centric indices
	    m.buildEdgeIndex(followedBy, "followedByWeight", Direction.BOTH, Order.decr, weight);
	    m.commit();
	}
	

	public static void main(String[] args) throws ConfigurationException {
		JanusGraph graph = openGraph();
		defineGratefulDeadSchema(graph);
		graph.close();
		
		
		Graph tinkerGraph = GraphFactory.open("conf/hadoop-load.properties");
		
		BulkLoaderVertexProgram blvp = BulkLoaderVertexProgram.build().writeGraph("conf/janusgraph-cassandra-es.properties").create(tinkerGraph);
		
		tinkerGraph.compute(SparkGraphComputer).program(blvp).submit().get();
	}

	public static GraphComputer newSparkGraphComputer(Graph graph, String cassandraAddress, String sparkMasterAddress, String sparkOutputLocation, String fsDefaultFS) {
        SparkGraphComputer computer = graph.compute(SparkGraphComputer.class);

        computer.configure("spark.master", sparkMasterAddress);
        computer.configure("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        computer.configure("janusgraphmr.ioformat.conf.storage.backend", "cassandra");
        computer.configure("janusgraphmr.ioformat.conf.storage.hostname", cassandraAddress);
        computer.configure("janusgraphmr.ioformat.conf.storage.cassandra.keyspace", keyspace);
        computer.configure("cassandra.input.partitioner.class", "org.apache.cassandra.dht.Murmur3Partitioner");
        computer.configure("gremlin.hadoop.outputLocation", sparkOutputLocation);
        computer.configure("gremlin.hadoop.graphWriter", "org.apache.tinkerpop.gremlin.hadoop.structure.io.gryo.GryoOutputFormat");
        computer.configure("gremlin.hadoop.graphReader", "org.janusgraph.hadoop.formats.cassandra.CassandraInputFormat");
        computer.configure("fs.defaultFS", fsDefaultFS);

        return computer;
    }
}
