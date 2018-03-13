

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.EdgeLabel;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.JanusGraphManagement;

public class CreateGraph {
	public static JanusGraph openGraph() {
		JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-cassandra-es.properties");
		return graph;
	}

	public static void createUserSchema(JanusGraph graph) {
		JanusGraphManagement mgmt = graph.openManagement();
		
		/*
		 * User's properties schema
		 * 
		 */
		
		mgmt.makeVertexLabel("user").make();
		
		final PropertyKey userId = mgmt.makePropertyKey("userId").dataType(String.class).make();
		
		mgmt.makePropertyKey("userName").dataType(String.class).make();
		
		mgmt.makePropertyKey("userTotalSoldItem").dataType(Integer.class).make();
		
		final PropertyKey userLocation = mgmt.makePropertyKey("userLocation").dataType(Geoshape.class).make();
		
		// use integer for score data, because integer calculation is cheaper than double/float
		final PropertyKey userScore = mgmt.makePropertyKey("userScore").dataType(Integer.class).make();
		
		// define user property to store set of location has been searched by user.
		mgmt.makePropertyKey("userSearchLocationPref").dataType(String.class).cardinality(Cardinality.SET).make();

		
		/*
		 * User's Index Schema
		 *  
		 */
		
		// composite index for user.userId
		mgmt.buildIndex("userIdIndexComposite", Vertex.class).addKey(userId).buildCompositeIndex();
		
		// "search" is the name of index backend defined on /conf/janusgraph-cassandra-es.properties
		
		// mixed index for user.score
		mgmt.buildIndex("userScoreIndexMixed",Vertex.class).addKey(userScore).buildMixedIndex("search");
		
		// mixed index for user.userLocation
		mgmt.buildIndex("userLocationIndexMixed",Vertex.class).addKey(userLocation).buildMixedIndex("search");
		
		mgmt.commit();
	}
	
	public static void createProductSchema(JanusGraph graph) {
		JanusGraphManagement mgmt = graph.openManagement();
		
		/*
		 * Product's properties schema
		 * 
		 */
		
		mgmt.makeVertexLabel("product").make();
		
		final PropertyKey productId = mgmt.makePropertyKey("productId").dataType(String.class).make();
		
		mgmt.makePropertyKey("productName").dataType(String.class).make();
		
		mgmt.makePropertyKey("productTotalSoldItem").dataType(Integer.class).make();
		
		mgmt.makePropertyKey("productTotalViewedItem").dataType(Integer.class).make();
		
		final PropertyKey productCategoryId = mgmt.makePropertyKey("productCategoryId").dataType(String.class).make();
		
		// use integer for score data, because integer calculation is cheaper than double/float
		final PropertyKey productScore = mgmt.makePropertyKey("productScore").dataType(Integer.class).make();
		
		
		/*
		 * Product's Index Schema
		 *  
		 */
		
		// composite index for product.productId
		mgmt.buildIndex("productIdIndexComposite", Vertex.class).addKey(productId).buildCompositeIndex();
		
		// index product by category
		mgmt.buildIndex("productCategoryIdIndexComposite", Vertex.class).addKey(productCategoryId).buildCompositeIndex();
		
		// mixed index for product.score
		mgmt.buildIndex("productScoreIndexMixed",Vertex.class).addKey(productScore).buildMixedIndex("search");
		
		mgmt.commit();
	}
	
	public static void createBuySchema(JanusGraph graph) {
		JanusGraphManagement mgmt = graph.openManagement();
		
		/*
		 * Buy's properties schema
		 * 
		 */
		
		EdgeLabel buyEdgeLabel = mgmt.makeEdgeLabel("buy").make();
		
		mgmt.makePropertyKey("userTotalBuy").dataType(Integer.class).make();
		
		mgmt.makePropertyKey("userTotalView").dataType(Integer.class).make();
		
		final PropertyKey buyScore = mgmt.makePropertyKey("buyScore").dataType(Integer.class).make();
		
		
		/*
		 * Buy's index schema
		 * 
		 */
		
		// create vertex-based index
		mgmt.buildEdgeIndex(buyEdgeLabel, "buyScoreIndex", Direction.OUT, Order.decr, buyScore);
		
		mgmt.commit();
	}
	
	public static void createEdgeBuy(JanusGraph graph) {
		GraphTraversalSource g = graph.traversal();
		
		Vertex user1 = g.V().has(T.label, "user").has("userId", 1).next();
		
		// add vertex and edge
		for (int i=0; i<5; i++) {
			
			Vertex user = g.V().has(T.label, "user").has("userId", i).next();
			Vertex product = g.V().has(T.label, "product").has("productId", i).next();
			
			Edge buyEdge = product.addEdge("buy", user);
			buyEdge.property("userTotalBuy", i);
			buyEdge.property("userTotalView", i);
			buyEdge.property("buyScore", i);
			
			// try adding all product to relate with user1
			Edge buyEdge2 = product.addEdge("buy", user1);
			buyEdge2.property("userTotalBuy", i);
			buyEdge2.property("userTotalView", i);
			buyEdge2.property("buyScore", i);
		}
		
		graph.tx().commit();
	}
	
	public static void createUser(JanusGraph graph) {
		JanusGraphTransaction tx = graph.newTransaction();
		
		double latitude = 37.97;
		double longitude = 23.72;
		
		// create vertex user
		for (int i=0; i<5; i++) {
			Vertex user1 = tx.addVertex(T.label, "user");
			user1.property("userId", ""+i);
			user1.property("userName", "user"+i);
			user1.property("userTotalSoldItem", "1");
			
			latitude += 0.05;
			longitude += 0.05;
			
			user1.property("userLocation", Geoshape.point(latitude, longitude));
			user1.property("userScore", i);
			
			user1.property("userSearchLocationPref", "jakarta");
			user1.property("userSearchLocationPref", "bandung");
		}
		
		tx.commit();
	}

	public static void createProduct(JanusGraph graph) {
		JanusGraphTransaction tx = graph.newTransaction();
		
		// create vertex product
		for (int i=0; i<5; i++) {
			Vertex product = tx.addVertex(T.label, "product");
			product.property("productId", ""+i);
			product.property("productName", "product"+i);
			product.property("productTotalSoldItem", "1");
			product.property("productTotalViewedItem", i);
			product.property("productCategoryId", i % 5);
			product.property("productScore", i);
		}
		
		tx.commit();
	}
	
	public static void main(String[] args) {
		JanusGraph graph = openGraph();
		
		createUserSchema(graph);
		createProductSchema(graph);
		createBuySchema(graph);
		
		createUser(graph);
		createProduct(graph);
		createEdgeBuy(graph);
		graph.close();
	}

}
