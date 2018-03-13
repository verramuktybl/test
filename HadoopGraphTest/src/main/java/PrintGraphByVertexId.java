


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.attribute.Geo;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.graphdb.database.StandardJanusGraph;

public class PrintGraphByVertexId {
	public static JanusGraph create() {
		JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
		
	    return graph;
	}
	
	public static void printGraphStructure(JanusGraph graph) {
		GraphTraversalSource g = graph.traversal();
		long vertexId = ((StandardJanusGraph) graph).getIDManager().toVertexId(96);
		Vertex v =  g.V(vertexId).next();
		
		// print vertex properties
					System.out.println("---------------------------------" 
					+ "\n" + v.id()
					+ "\n" + v.property("userId")
					+ "\n" + v.property("userName")
					+ "\n" + v.property("userTotalSoldItem")
					+ "\n" + v.property("userLocation")
					+ "\n" + v.property("userScore"));
					
					// print user search location preference list
					Iterator locationPref = v.properties("userSearchLocationPref");
					while(locationPref.hasNext()){
						System.out.println(locationPref.next());
					}
					
		
	}
	
	public static void main(String[] args) throws IOException {
		JanusGraph graph = create();
		
		printGraphStructure(graph);
		
		graph.close();
	}
	
}
