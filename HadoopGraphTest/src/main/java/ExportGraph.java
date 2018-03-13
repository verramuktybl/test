import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONIo;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONTokens;
import org.apache.tinkerpop.gremlin.structure.util.Attachable;
import org.apache.tinkerpop.gremlin.structure.util.star.StarGraph;
import org.apache.tinkerpop.gremlin.structure.util.star.StarGraphGraphSONDeserializer;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.tinkerpop.gremlin.util.function.FunctionUtils;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.decr;

public class ExportGraph {
	public static JanusGraph create() {
		JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
		
	    return graph;
	}
	
	public static void main(String[] args) throws IOException {
		JanusGraph graph = create();
		
		// import
//		graph.io(IoCore.graphson()).readGraph("test2.json");
		
		// export whole graph
//		graph.io(IoCore.graphson()).writeGraph("test_filter_graph.json");
		
		// export graph based on query
		GraphTraversalSource g = graph.traversal();
		FileOutputStream f = new FileOutputStream("vertex-1.json");
////		Iterator<Vertex> iterV = g.V().has(T.label, "user").order().by("userScore", decr).limit(10);
		Iterator<Vertex> iterV = g.V().has(T.label, "user");
//		graph.io(IoCore.graphson()).writer().create().writeVertices(f, iterV);
		
		
		
		// import the whole graph....
//		FileInputStream f = new FileInputStream("vertex-2.json");
////		graph.io(IoCore.graphson()).reader().create().readGraph(f, graph);

		
//		Function<Attachable<Property>, Property> propertyAttachMethod = 		
//				FunctionUtils.wrapFunction(
//						line -> readVertex(new ByteArrayInputStream(line.getBytes()), null, null, Direction.IN)
//						);
//
//		FileInputStream f = new FileInputStream("vertex-2.json");
//		graph.io(IoCore.graphson()).reader().create().readVertices(f, null, null, null);
//		
		
		
		graph.close();
	}

//	public static Property readVertex(final InputStream inputStream,
//            final Function<Attachable<Vertex>, Vertex> vertexAttachMethod,
//            final Function<Attachable<Edge>, Edge> edgeAttachMethod,
//            final Direction attachEdgesOfThisDirection) throws IOException {
//final Map<String, Object> vertexData = mapper.readValue(inputStream, mapTypeReference);
//final StarGraph starGraph = StarGraphGraphSONDeserializer.readStarGraphVertex(vertexData);
//if (vertexAttachMethod != null) vertexAttachMethod.apply(starGraph.getStarVertex());
//
//if (vertexData.containsKey(GraphSONTokens.OUT_E) && (attachEdgesOfThisDirection == Direction.BOTH || attachEdgesOfThisDirection == Direction.OUT))
//StarGraphGraphSONDeserializer.readStarGraphEdges(edgeAttachMethod, starGraph, vertexData, GraphSONTokens.OUT_E);
//
//if (vertexData.containsKey(GraphSONTokens.IN_E) && (attachEdgesOfThisDirection == Direction.BOTH || attachEdgesOfThisDirection == Direction.IN))
//StarGraphGraphSONDeserializer.readStarGraphEdges(edgeAttachMethod, starGraph, vertexData, GraphSONTokens.IN_E);
//
//return starGraph.getStarVertex();
//}
	
}
