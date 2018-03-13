

import java.io.IOException;

import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.diskstorage.BackendException;

public class CleanupGraph {

	public static JanusGraph openGraph() {
		JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
		return graph;
	}
	
    @Deprecated
    public static void clear(JanusGraph graph) throws BackendException {
        JanusGraphFactory.drop(graph);
    }

    
    public static void main(String[] args) throws IOException, BackendException {
    	JanusGraph graph = openGraph();
    	clear(graph);
		
    }
}