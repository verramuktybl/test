package pluradj.janusgraph.example;



import java.io.IOException;
import java.util.Iterator;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.Io.Builder;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;

public class VerraTest2 {
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
	
	public static void main(String[] args) throws IOException {
		JanusGraph graph = create();
		graph.io(IoCore.graphml()).readGraph("graph.graphml");
		
		graph.close();
	}

}
