


import java.io.IOException;
import java.util.Iterator;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.attribute.Geoshape;

public class PrintGraph {
	public static JanusGraph create() {
		JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
		
	    return graph;
	}
	
	public static void printGraphStructure(JanusGraph graph) {
		JanusGraphTransaction tx = graph.newTransaction();
		Iterator iterV = tx.getVertices().iterator();
		while(iterV.hasNext()){
			Vertex v = (Vertex) iterV.next();
			
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
			
			
			// print edges
//			Iterator iterE = v.edges(Direction.OUT);
//			while(iterE.hasNext()){
//				Edge e = (Edge) iterE.next();
//				Vertex outV = e.inVertex();
//				System.out.println(v.label()+ " "+v.value("name") + " -- "+e.label()+" -- " +outV.label()+ " "+outV.value("name"));
//				
//			}
//			
//			iterE = v.edges(Direction.IN);
//			while(iterE.hasNext()){
//				Edge e = (Edge) iterE.next();
//				Vertex outV = e.outVertex();
//				System.out.println(v.label()+ " "+v.value("name") + " -- "+e.label()+" -- " +outV.label()+ " "+outV.value("name"));
//			}
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		JanusGraph graph = create();
		
		printGraphStructure(graph);
		
		graph.close();
	}
	
}
