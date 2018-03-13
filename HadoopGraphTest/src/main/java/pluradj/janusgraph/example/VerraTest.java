package pluradj.janusgraph.example;



import java.util.Iterator;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;

public class VerraTest {
	public static JanusGraph create() {
		JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
		
	    return graph;
	}
	
	public static void initGraph(JanusGraph graph) {
		JanusGraphTransaction tx = graph.newTransaction();
		
		// add vertex and edge
		Vertex smartphone = tx.addVertex(T.label, "category", "name", "HP & Smartphone");
		Vertex tablet = tx.addVertex(T.label, "category", "name", "Tablet");
		smartphone.addEdge("complement", tablet);
		
		tx.commit();
	}
	
	public static void printGraphStructure(JanusGraph graph) {
		JanusGraphTransaction tx = graph.newTransaction();
		Iterator iterV = tx.getVertices().iterator();
		while(iterV.hasNext()){
			Vertex v = (Vertex) iterV.next();
			
			Iterator iterE = v.edges(Direction.OUT);
			while(iterE.hasNext()){
				Edge e = (Edge) iterE.next();
				Vertex outV = e.inVertex();
				System.out.println(v.label()+ " "+v.value("name") + " -- "+e.label()+" -- " +outV.label()+ " "+outV.value("name"));
				
			}
			
			iterE = v.edges(Direction.IN);
			while(iterE.hasNext()){
				Edge e = (Edge) iterE.next();
				Vertex outV = e.outVertex();
				System.out.println(v.label()+ " "+v.value("name") + " -- "+e.label()+" -- " +outV.label()+ " "+outV.value("name"));
			}
		}
		
	}
	
	public static void main(String[] args) {
		JanusGraph graph = create();
		
		JanusGraphTransaction tx = graph.newTransaction();
		Vertex v1 = tx.addVertex(T.label, "category", "name", "c11");
		Vertex v2 = tx.addVertex(T.label, "category", "name", "c21");
		v1.addEdge("1", v2);
		v2.addEdge("1", v1);
		tx.commit();
		
//		printGraphStructure(graph);
		
//		System.out.println("total edges: "+v1.edges(Direction.BOTH, "1").next().label());
		
		graph.close();
	}

}
