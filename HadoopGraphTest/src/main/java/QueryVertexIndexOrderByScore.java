

import static org.apache.tinkerpop.gremlin.process.traversal.Order.decr;

import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

public class QueryVertexIndexOrderByScore {
	public static JanusGraph openGraph() {
		JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
		return graph;
	}


	
	public static void query(JanusGraph graph) {
		GraphTraversalSource g = graph.traversal();
		
		ArrayList vertices = (ArrayList) g.V().has(T.label, "user").has("userId", "1")
				.inE("buy")
				.order().by("buyScore", decr)
				.limit(10).toList();
		
		
		int totalV = vertices.size();
		System.out.println("----------------------------------------TotalV:"+totalV);
		
		for (int i=0; i<totalV; i++) {
			Edge e = (Edge) vertices.get(i);
			Vertex v = e.outVertex();
			System.out.println("---------------------------------" 
					+ "\n" + v.property("productId")
					+ "\n" + v.property("productName"));
			
			v = e.inVertex();
			System.out.println("---------------------------------" 
					+ "\n" + v.property("userId")
					+ "\n" + v.property("userName"));
			
        }
		
		System.exit(0);
	}
	

	public static void main(String[] args) {
		JanusGraph graph = openGraph();
		query(graph);
		graph.close();
	}

}
