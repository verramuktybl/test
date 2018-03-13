package pluradj.janusgraph.example;

import static org.janusgraph.core.attribute.Text.*;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.*;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.attribute.Geo;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.example.GraphOfTheGodsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JavaExample {
    //private static final Logger LOGGER = LoggerFactory.getLogger(JavaExample.class);

    public static void main(String[] args) throws IOException {
        JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
        GraphTraversalSource g = graph.traversal();

	    JanusGraphTransaction tx = graph.newTransaction();
	    
        //To populate the database with initial schema if the graph database is empty
        if (g.V().count().next() == 0) {
        	
        	CategoryFactory.load(graph);
        	//GraphOfTheGodsFactory.load(graph);
        }
        
        double start,end;
        double sum = 0;
        double max=0;
        
        for (int i=0 ; i<10000; i++) {
        	
        	start = System.currentTimeMillis();
        	
        	//TESTING 1 - ADD VERTEX    	
        	//tx.addVertex(T.label,"category","name","Bird"+i);
        	//tx.commit();
        	
        	//TESTING 2 - REMOVE VERTEX
        	//g.V().has("name","Bird"+i).next().remove();
        	//g.tx().commit();
        	
        	//TESTING 3 - ADD EDGE [CHANGE THE LOOP TO 9999 FOR THIS ONE]
        	//g.V().has("name","Bird"+i).next().addEdge("complement", g.V().has("name","Bird"+(i+1)).next());
        	//g.tx().commit();
        	
        	//TESTING 4 - REMOVE EDGE [CHANGE THE LOOP TO 9999 FOR THIS ONE]
        	//Edge ed = (Edge)g.V().has("name", "Bird"+i).outE("complement").
    		//	as("e").inV().has("name", "Bird"+(i+1)).select("e").next();
            //ed.remove();
        	//g.tx().commit();
        	
        	end = System.currentTimeMillis();
            sum = sum + (end-start);
            if ((end-start)>max) {
            	max = end-start;
            }
            
        }

        System.exit(0);
    }
    
}
