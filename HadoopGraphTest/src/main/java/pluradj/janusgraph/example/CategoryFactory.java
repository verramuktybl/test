package pluradj.janusgraph.example;

//Copyright 2017 JanusGraph Authors
	//
	// Licensed under the Apache License, Version 2.0 (the "License");
	// you may not use this file except in compliance with the License.
	// You may obtain a copy of the License at
	//
//	      http://www.apache.org/licenses/LICENSE-2.0
	//
	// Unless required by applicable law or agreed to in writing, software
	// distributed under the License is distributed on an "AS IS" BASIS,
	// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	// See the License for the specific language governing permissions and
	// limitations under the License.

import com.google.common.base.Preconditions;
import org.janusgraph.core.EdgeLabel;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Mapping;
import org.janusgraph.graphdb.database.StandardJanusGraph;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class CategoryFactory {
	
	/**
	 * Example Graph factory that creates a {@link JanusGraph} based on roman mythology.
	 * Used in the documentation examples and tutorials.
	 *
	 * @author Marko A. Rodriguez (http://markorodriguez.com)
	 */


	public static final String INDEX_NAME = "search";
	private static final String ERR_NO_INDEXING_BACKEND = 
	            "The indexing backend with name \"%s\" is not defined. Specify an existing indexing backend or " +
	            "use GraphOfTheGodsFactory.loadWithoutMixedIndex(graph,true) to load without the use of an " +
	            "indexing backend.";

	public static JanusGraph create(final String directory) {
		JanusGraphFactory.Builder config = JanusGraphFactory.build();
	    config.set("storage.backend", "berkeleyje");
	    config.set("storage.directory", directory);
	    config.set("index." + INDEX_NAME + ".backend", "elasticsearch");

	    JanusGraph graph = config.open();
	    CategoryFactory.load(graph);
	    return graph;
	}

	public static void loadWithoutMixedIndex(final JanusGraph graph, boolean uniqueNameCompositeIndex) {
	    load(graph, null, uniqueNameCompositeIndex);
	}

	public static void load(final JanusGraph graph) {
	    load(graph, INDEX_NAME, true);
	}

	private static boolean mixedIndexNullOrExists(StandardJanusGraph graph, String indexName) {
	    return indexName == null || graph.getIndexSerializer().containsIndex(indexName);
	}

	public static void load(final JanusGraph graph, String mixedIndexName, boolean uniqueNameCompositeIndex) {
	    if (graph instanceof StandardJanusGraph) {
	    	Preconditions.checkState(mixedIndexNullOrExists((StandardJanusGraph)graph, mixedIndexName), 
	                    ERR_NO_INDEXING_BACKEND, mixedIndexName);
	    }

	    //Create Schema
	    JanusGraphManagement management = graph.openManagement();
	    final PropertyKey name = management.makePropertyKey("name").dataType(String.class).make();
	    JanusGraphManagement.IndexBuilder nameIndexBuilder = management.buildIndex("name", Vertex.class)
	    		.addKey(name);
	    if (uniqueNameCompositeIndex)
	    	nameIndexBuilder.unique();
	    JanusGraphIndex nameIndex = nameIndexBuilder.buildCompositeIndex();
	    management.setConsistency(nameIndex, ConsistencyModifier.LOCK);
	    
	    
	    management.buildIndex("CategoryByName", Vertex.class).addKey(name, Mapping.TEXT.asParameter()).buildMixedIndex(INDEX_NAME);
	    
	    final PropertyKey time = management.makePropertyKey("time").dataType(Integer.class).make();
	    final PropertyKey weight = management.makePropertyKey("weight").dataType(Float.class).make();
	    
	    EdgeLabel complement = management.makeEdgeLabel("complement").signature(weight).make();
	    management.buildEdgeIndex(complement, "complementByWeight", Direction.BOTH, Order.decr, weight);
	    
	    management.makeEdgeLabel("samelevel").make();
	    
	    management.makeVertexLabel("category").make();
	    
	    management.commit();

	    JanusGraphTransaction tx = graph.newTransaction();
	   
	    Vertex smartphone = tx.addVertex(T.label, "category", "name", "HP & Smartphone");
        Vertex tablet = tx.addVertex(T.label, "category", "name", "Tablet");
        Vertex casinghp = tx.addVertex(T.label, "category", "name", "Casing & Cover Handphone");
        Vertex screenprotecthp = tx.addVertex(T.label, "category", "name", "Screen Protector HP");
        Vertex headset = tx.addVertex(T.label, "category", "name", "Headset (Earphone)");
        Vertex perdana = tx.addVertex(T.label, "category", "name", "Kartu Perdana");
        Vertex lensportable = tx.addVertex(T.label, "category", "name", "Lensa Portable");
        Vertex glove = tx.addVertex(T.label, "category", "name", "Glove Sepeda");
        Vertex sepeda = tx.addVertex(T.label, "category", "name", "Sepeda");
        Vertex helm = tx.addVertex(T.label, "category", "name", "Helm Sepeda");
        Vertex tas = tx.addVertex(T.label, "category", "name", "Tas Sepeda");
        Vertex gembok = tx.addVertex(T.label, "category", "name", "Gembok Sepeda");
        Vertex kacamata = tx.addVertex(T.label, "category", "name", "Kacamata Sepeda");
        Vertex laptop = tx.addVertex(T.label, "category", "name", "Laptop");
        Vertex camera = tx.addVertex(T.label, "category", "name", "Digital Camera");
        Vertex camerabag = tx.addVertex(T.label, "category", "name", "Tas & Case Kamera");
        Vertex charger = tx.addVertex(T.label, "category", "name", "Charger Handphone");
        Vertex tongsis = tx.addVertex(T.label, "category", "name", "Tongsis");
        Vertex powerbank = tx.addVertex(T.label, "category", "name", "Powerbank");
        Vertex mouse = tx.addVertex(T.label, "category", "name", "Mouse");
        Vertex printer = tx.addVertex(T.label, "category", "name", "Printer");
        Vertex handycam = tx.addVertex(T.label, "category", "name", "Handycam");
        Vertex keyboard = tx.addVertex(T.label, "category", "name", "Keyboard");
        Vertex caselaptop = tx.addVertex(T.label, "category", "name", "Tas & Case Komputer");
        Vertex printertint = tx.addVertex(T.label, "category", "name", "Tinta Printer");
        Vertex kok = tx.addVertex(T.label, "category", "name", "Kok Badminton");
        Vertex raketbadmin = tx.addVertex(T.label, "category", "name", "Raket Badminton");
        Vertex bolatenis = tx.addVertex(T.label, "category", "name", "Bola Tenis");
        Vertex rakettenis = tx.addVertex(T.label, "category", "name", "Raket Tenis");
        Vertex bolaping = tx.addVertex(T.label, "category", "name", "Bola Tenis Meja");
        Vertex raketping = tx.addVertex(T.label, "category", "name", "Raket Tenis Meja");
        
        
        
        screenprotecthp.addEdge("complement", smartphone);
        casinghp.addEdge("complement", smartphone);
        perdana.addEdge("complement", smartphone);
        glove.addEdge("complement", sepeda);
        helm.addEdge("complement", sepeda);
        bolatenis.addEdge("complement", rakettenis);
        bolaping.addEdge("complement", raketping);
        mouse.addEdge("complement", laptop);
        caselaptop.addEdge("complement", laptop);
        keyboard.addEdge("complement", laptop);
        camerabag.addEdge("complement", camera);
        screenprotecthp.addEdge("complement", tablet);
        casinghp.addEdge("complement", tablet);
        camerabag.addEdge("complement", handycam);
        kok.addEdge("complement", raketbadmin);
        
        headset.addEdge("secondary", smartphone);
        charger.addEdge("secondary", smartphone);
        perdana.addEdge("secondary", tablet);
        charger.addEdge("secondary", tablet);
        headset.addEdge("secondary", tablet);
        printertint.addEdge("secondary", printer);
        
        tongsis.addEdge("samelevel", lensportable);
        powerbank.addEdge("samelevel", lensportable);
        casinghp.addEdge("samelevel", lensportable);
        screenprotecthp.addEdge("samelevel", lensportable);
        tas.addEdge("samelevel", glove);
        kacamata.addEdge("samelevel", glove);
        helm.addEdge("samelevel", glove);
        glove.addEdge("samelevel", helm);
        tas.addEdge("samelevel", helm);
        kacamata.addEdge("samelevel", helm);
        glove.addEdge("samelevel", tas);
        kacamata.addEdge("samelevel", tas);
        helm.addEdge("samelevel", tas);
        glove.addEdge("samelevel", kacamata);
        tas.addEdge("samelevel", kacamata);
        helm.addEdge("samelevel", kacamata);
        tongsis.addEdge("samelevel", casinghp);
        lensportable.addEdge("samelevel", casinghp);
        screenprotecthp.addEdge("samelevel", casinghp);
        casinghp.addEdge("samelevel", screenprotecthp);
        powerbank.addEdge("samelevel", screenprotecthp);
        tongsis.addEdge("samelevel", screenprotecthp);
        lensportable.addEdge("samelevel", screenprotecthp);
        powerbank.addEdge("samelevel", tongsis);
        lensportable.addEdge("samelevel", tongsis);
        casinghp.addEdge("samelevel", tongsis);
        screenprotecthp.addEdge("samelevel", tongsis);
        tongsis.addEdge("samelevel", powerbank);
        lensportable.addEdge("samelevel", powerbank);
        screenprotecthp.addEdge("samelevel", powerbank);
        caselaptop.addEdge("samelevel", mouse);
        keyboard.addEdge("samelevel", mouse);
        caselaptop.addEdge("samelevel", keyboard);
        mouse.addEdge("samelevel", keyboard);
        keyboard.addEdge("samelevel", caselaptop);
        mouse.addEdge("samelevel", caselaptop);
        
        lensportable.addEdge("additional", tablet);
        lensportable.addEdge("additional", smartphone);
        tas.addEdge("additional", sepeda);
        gembok.addEdge("additional", sepeda);
        kacamata.addEdge("additional", sepeda);
        tongsis.addEdge("additional", tablet);
        tongsis.addEdge("additional", smartphone);
        powerbank.addEdge("additional", tablet);
        powerbank.addEdge("additional", smartphone);
		
        // commit the transaction to disk
        tx.commit();
    }

	/**
	 * Calls {@link JanusGraphFactory#open(String)}, passing the JanusGraph configuration file path
	 * which must be the sole element in the {@code args} array, then calls
	 * {@link #load(org.janusgraph.core.JanusGraph)} on the opened graph,
	 * then calls {@link org.janusgraph.core.JanusGraph#close()}
	 * and returns.
	 * <p/>
	 * This method may call {@link System#exit(int)} if it encounters an error, such as
	 * failure to parse its arguments.  Only use this method when executing main from
	 * a command line.  Use one of the other methods on this class ({@link #create(String)}
	 * or {@link #load(org.janusgraph.core.JanusGraph)}) when calling from
	 * an enclosing application.
	 *
	 * @param args a singleton array containing a path to a JanusGraph config properties file
	 */
	public static void main(String args[]) {
        if (null == args || 1 != args.length) {
        	System.err.println("Usage: GraphOfTheGodsFactory <janusgraph-config-file>");
	        System.exit(1);
	    }

        JanusGraph g = JanusGraphFactory.open(args[0]);
        load(g);
	    g.close();
    }
}
