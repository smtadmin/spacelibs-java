package com.siliconmtn.data.tree;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// JDK 11.x
import java.util.ArrayList;
import java.util.List;

/****************************************************************************
 * <b>Title</b>: TreeTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Tests the tree and node classes to ensure our 
 * hierarchies work correctly
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 14, 2021
 * @updates:
 ****************************************************************************/
class TreeTest {
	
	private Node root;
	private List<Node> nodes = new ArrayList<>();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUpBeforeEach() throws Exception {
		root = new Node("root_node_id", null);
		root.setNodeName("Root Node");
		Node firstChild = new Node("child_node_1", "root_node_id");
		Node secondChild = new Node("child_node_2", "root_node_id");
		Node firstFirstChild = new Node("child_node_3", "child_node_1");

		nodes.add(root);
		nodes.add(firstChild);
		nodes.add(secondChild);
		nodes.add(firstFirstChild);
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#hashCode()}.
	 */
	@Test
	void testHashCode() {
		Tree t = new Tree(nodes, root);
		assertEquals(91381, t.hashCode());	
		
		t = new Tree(nodes, null);
		assertEquals(21254, t.hashCode());
		
		t = new Tree(nodes);
		assertEquals(21254, t.hashCode());	
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#findNode(java.lang.String)}.
	 */
	@Test
	void testFindNode() {
		Tree t = new Tree(nodes, root);
		assertEquals("child_node_1", t.findNode("child_node_1").getNodeId());
		assertEquals(null, t.findNode("no_node_id"));
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#preorderList()}.
	 */
	@Test
	void testPreorderList() {
		Tree t = new Tree(nodes, root);
		assertEquals(3, t.preorderList().size());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#getPreorderList()}.
	 */
	@Test
	void testGetPreorderList() {
		Tree t = new Tree(nodes, root);
		assertEquals(3, t.getPreorderList().size());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#preorderList(boolean)}.
	 */
	@Test
	void testPreorderListBoolean() {
		Tree t = new Tree(nodes, root);
		assertEquals(4, t.preorderList(true).size());
		assertEquals(3, t.preorderList(false).size());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#preorderList(com.siliconmtn.data.tree.Node)}.
	 */
	@Test
	void testPreorderListNode() {
		Tree t = new Tree(nodes, root);
		Node nullNode = null;
		assertEquals(0, t.preorderList(nullNode).size());
		assertEquals(3, t.preorderList(root).size());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#createPreorder(java.util.List, java.util.List)}.
	 */
	@Test
	void testCreatePreorder() {
		Tree t = new Tree(nodes, root);
		//t.createPreorder(nodes, new ArrayList<Node>());
		assertEquals(3, t.preorderList(root).size());
		
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#calculateTotalChildren(com.siliconmtn.data.tree.Node)}.
	 */
	@Test
	void testCalculateTotalChildren() {
		Node firstChild = new Node("child_node_1", "root_node_id");
		Node secondChild = new Node("child_node_2", "root_node_id");
		Node firstFirstChild = new Node("child_node_3", "child_node_1");
		firstChild.addChild(secondChild);
		secondChild.addChild(firstFirstChild);

		assertEquals(0, Tree.calculateTotalChildren(null));
		assertEquals(0, Tree.calculateTotalChildren(root));
		assertEquals(2, Tree.calculateTotalChildren(firstChild));
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#getDepth()}.
	 */
	@Test
	void testGetDepth() {
		Tree t = new Tree(nodes, root);
		assertEquals(3, t.getDepth());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#getRootNode()}.
	 */
	@Test
	void testGetRootNode() {
		Tree t = new Tree(nodes);
		t.setRootNode(root);
		assertEquals("root_node_id", t.getRootNode().getNodeId());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#buildNodePaths()}.
	 */
	@Test
	void testBuildNodePaths() {
		Node firstChild = new Node("child_node_1", "root_node_id");
		firstChild.setNodeName("first child");
		
		Node secondChild = new Node("child_node_2", "root_node_id");
		secondChild.setNodeName("my second child");
		
		Node firstFirstChild = new Node("child_node_3", "child_node_1");
		firstFirstChild.setNodeName("first first child");
		
		firstChild.addChild(secondChild);
		secondChild.addChild(firstFirstChild);
		
		List<Node> pathNodes = new ArrayList<Node>();
		pathNodes.add(firstChild);
		
		Tree t = new Tree(pathNodes, root);
		t.buildNodePaths(root, "/", true);
		System.out.println(firstFirstChild.getFullPath());
		assertEquals(2, firstFirstChild.getDepthLevel());
		
		t = new Tree(nodes, root);
		t.buildNodePaths("/", true);
		
		assertEquals(1, secondChild.getDepthLevel());
		
		t = new Tree(nodes, root);
		t.buildNodePaths();
		assertEquals(1, secondChild.getDepthLevel());		
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#buildNodePaths(java.lang.String)}.
	 */
	@Test
	void testBuildNodePathsString() {
		Tree t = new Tree(nodes, root);
		t.buildNodePaths("/");
		assertEquals(2, root.getNumberChildren());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#buildNodePaths(java.lang.String, boolean)}.
	 */
	@Test
	void testBuildNodePathsStringBoolean() {
		root.setNodeName("root node");
		
		Tree t = new Tree(nodes, root);
		t.buildNodePaths("/", true);
		t.buildNodePaths("/", false);
		assertEquals(0, nodes.get(0).getDepthLevel());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#buildNodePaths(com.siliconmtn.data.tree.Node, java.lang.String, boolean)}.
	 */
	@Test
	void testBuildNodePathsNodeStringBoolean() {
		Node firstChild = new Node("child_node_1", "root_node_id");
		firstChild.setNodeName("my first child");
		Node secondChild = new Node("child_node_2", "root_node_id");
		secondChild.setNodeName("my second child");
		Node firstFirstChild = new Node("child_node_3", "child_node_1");
		firstFirstChild.setNodeName("my first first child");
		firstFirstChild.setNodeId(null);
		
		firstChild.addChild(secondChild);
		secondChild.addChild(firstFirstChild);
		
		List<Node> pathNodes = new ArrayList<Node>();
		pathNodes.add(firstChild);
		
		Tree t = new Tree(pathNodes, root);
		t.buildNodePaths(firstChild, "/", true);
		t.buildNodePaths(firstChild, "/", false);
		assertEquals(2, firstFirstChild.getDepthLevel());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Tree#equals(java.lang.Object)}.
	 */
	@Test
	void testEqualsObject() {
		Tree t = new Tree(nodes, root);
		assertNotNull(t);
	}

	/**
	 * Tests the equals method
	 * @throws Exception
	 */
	@Test
	void testEquals() throws Exception {
		Tree t = new Tree(nodes);
		boolean isEqual = t.equals(null);
		assertFalse(isEqual);
	}

}
