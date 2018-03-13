

import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.attribute.Geo;
import org.janusgraph.core.attribute.Geoshape;

public class QueryGeoLocation {
	public static JanusGraph openGraph() {
		JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
		return graph;
	}


	public static void query(JanusGraph graph) {
		GraphTraversalSource g = graph.traversal();
		
		ArrayList vertices = (ArrayList) g.V().has(T.label, "user").has("userLocation", Geo.geoWithin(Geoshape.circle(37.97, 23.72, 10))).limit(10).toList();

		int totalV = vertices.size();
		System.out.println("----------------------------------------TotalV:"+totalV);
		
		for (int i=0; i<totalV; i++) {
			Vertex v = (Vertex) vertices.get(i);
			System.out.println("---------------------------------" 
					+ "\n" + v.property("userId")
					+ "\n" + v.property("userName")
					+ "\n" + v.property("userTotalSoldItem")
					+ "\n" + v.property("userLocation")
					+ "\n" + v.property("userScore"));
			
        }
		
		System.exit(0);
	}
	

	public static void main(String[] args) {
		JanusGraph graph = openGraph();
		query(graph);
		graph.close();
	}

}
