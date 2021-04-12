package com.siliconmtn.data.tree;

//Junit5 
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: NodeTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b>Junit5 tests for the Node class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 14, 2021
 * @updates:
 ****************************************************************************/
class NodeTest {
	/**
	 * 
	 */
	private static Node rootNode = null;
	private static Node childNode = null;
	
	@BeforeEach
	void setUpBeforeClass() throws Exception {
		rootNode = new Node();
		rootNode.setNodeId("node_id");
		rootNode.setUserObject("Hello");
		rootNode.setNodeName("root_node");
		rootNode.setRoot(true);
		rootNode.setFullPath("/");
		
		childNode = new Node(rootNode);
		childNode.setNodeId("child_node_id");
		childNode.setParentId("node_id");
		childNode.setNodeName("child_node");
		childNode.setFullPath("support/");
    }
	
	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#hashCode()}.
	 */
	@Test
	void testHashCode() {
		assertEquals(1406, rootNode.hashCode());
		assertEquals(2739, childNode.hashCode());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#addChild(com.siliconmtn.data.tree.Node)}.
	 */
	@Test
	void testAddChild() {
		rootNode.addChild(childNode);
		assertEquals(1, childNode.getDepthLevel());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#isChild(com.siliconmtn.data.tree.Node)}.
	 */
	@Test
	void testIsChild() {
		rootNode.addChild(childNode);
		assertTrue(rootNode.isChild(childNode));
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getNodeId()}.
	 */
	@Test
	void testGetNodeId() {
		assertEquals("node_id", rootNode.getNodeId());
		assertEquals("child_node_id", childNode.getNodeId());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#setNodeId(java.lang.String)}.
	 */
	@Test
	void testSetNodeId() {
		Node n = new Node();
		assertEquals(null, n.getNodeId());
		
		n.setNodeId("ID");
		assertEquals("ID", n.getNodeId());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getParentId()}.
	 */
	@Test
	void testGetParentId() {
		assertEquals(null, rootNode.getParentId());
		assertEquals("node_id", childNode.getParentId());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#setParentId(java.lang.String)}.
	 */
	@Test
	void testSetParentId() {
		Node n = new Node();
		n.setParentId("parent_id");
		assertEquals("parent_id", n.getParentId());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getNodeName()}.
	 */
	@Test
	void testGetNodeName() {
		assertEquals("root_node", rootNode.getNodeName());
		assertEquals("child_node", childNode.getNodeName());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#setNodeName(java.lang.String)}.
	 */
	@Test
	void testSetNodeName() {
		Node n = new Node();
		n.setNodeName("test_node");
		assertEquals("test_node", n.getNodeName());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getUserObject()}.
	 */
	@Test
	void testGetUserObject() {
		assertEquals("Hello", rootNode.getUserObject());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#setUserObject(java.lang.Object)}.
	 */
	@Test
	void testSetUserObject() {
		Node n = new Node();
		n.setUserObject("World");
		assertEquals("World", n.getUserObject());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getDepthLevel()}.
	 */
	@Test
	void testGetDepthLevel() {
		rootNode.addChild(childNode);
		assertEquals(0, rootNode.getDepthLevel());
		assertEquals(1, childNode.getDepthLevel());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#isLeaf()}.
	 */
	@Test
	void testIsLeaf() {
		rootNode.addChild(childNode);
		assertFalse(rootNode.isLeaf());
		assertTrue(childNode.isLeaf());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#setLeaf(boolean)}.
	 */
	@Test
	void testSetLeaf() {
		Node n = new Node();
		assertTrue(n.isLeaf());
		
		n.setLeaf(false);
		assertFalse(n.isLeaf());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#isRoot()}.
	 */
	@Test
	void testIsRoot() {
		assertTrue(rootNode.isRoot());
		assertTrue(childNode.isRoot());
		
		rootNode.addChild(childNode);
		assertFalse(childNode.isRoot());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#setRoot(boolean)}.
	 */
	@Test
	void testSetRoot() {
		Node n = new Node();
		assertFalse(n.isRoot());
		
		n.setRoot(true);
		assertTrue(n.isRoot());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#equals(java.lang.Object)}.
	 */
	@Test
	void testEqualsObject() {
		assertEquals(rootNode, rootNode);
		assertNotEquals(rootNode, childNode);
		
		rootNode.setNodeId(null);
		assertNotEquals(rootNode, childNode);
		
		Object o = new Object();
		assertNotEquals(rootNode, o);
		assertNotEquals(null, rootNode);
		assertNotEquals(childNode, new Node());
		
		Node n = new Node("one", "two", "three");
		Node n1 = new Node("one", "two");
		assertEquals(n, n1);
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#toString()}.
	 */
	@Test
	void testToString() {
		assertTrue(rootNode.toString().contains("node_id"));
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getNumberChildren()}.
	 */
	@Test
	void testGetNumberChildren() {
		assertEquals(0, rootNode.getNumberChildren());
		
		rootNode.addChild(childNode);
		assertEquals(1, rootNode.getNumberChildren());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getChildren()}.
	 */
	@Test
	void testGetChildren() {
		rootNode.addChild(childNode);
		assertEquals(1, rootNode.getChildren().size());
		assertEquals(0, childNode.getChildren().size());
	}


	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getFullPath()}.
	 */
	@Test
	void testGetFullPath() {
		assertEquals("/", rootNode.getFullPath());
		assertEquals("support/", childNode.getFullPath());
		
		rootNode.addChild(childNode);
		assertEquals("/support/", childNode.getFullPath());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getParentName()}.
	 */
	@Test
	void testGetParentName() {
		assertEquals(null, rootNode.getParentName());
		assertEquals(null, childNode.getParentName());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#setParentName(java.lang.String)}.
	 */
	@Test
	void testSetParentName() {
		assertEquals(null, rootNode.getParentName());
		
		rootNode.addChild(childNode);
		assertEquals("root_node", childNode.getParentName());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getTotalChildren()}.
	 */
	@Test
	void testGetTotalChildren() {
		assertEquals(0, rootNode.getTotalChildren());
		
		rootNode.setTotalChildren(2);
		assertEquals(2, rootNode.getTotalChildren());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#setTotalChildren(int)}.
	 */
	@Test
	void testSetTotalChildren() {
		rootNode.setTotalChildren(2);
		assertEquals(2, rootNode.getTotalChildren());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#getOrderNo()}.
	 */
	@Test
	void testGetOrderNo() {
		assertEquals(0, rootNode.getOrderNo());
		
		rootNode.setOrderNo(2);
		assertEquals(2, rootNode.getOrderNo());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.tree.Node#compareTo(com.siliconmtn.data.tree.Node)}.
	 */
	@Test
	void testCompareTo() {
		
		assertEquals(0, rootNode.compareTo(rootNode));
		
		rootNode.setOrderNo(3);
		assertEquals(1, rootNode.compareTo(childNode));
	}

	/**
	 * Test method for {@link com.smt.data.tree.Node#(java.lang.String, java.lang.String, java.lang.Object)}.
	 */
	@Test
	void testNodeStringStringObject() throws Exception {
		Object o = new Object();
		Node n = new Node("one", "two", o);
		assertEquals("one", n.getNodeId());
		assertEquals("two", n.getParentId());
		assertEquals(o, n.getUserObject());
	}

	@Test
	void testEquals() throws Exception {
		boolean isEqual = rootNode.equals(null);
		assertFalse(isEqual);
		
		assertNotEquals(childNode, rootNode);
		assertEquals(rootNode, rootNode);
		
		isEqual = rootNode.equals(null);
		assertFalse(isEqual);
	}

}
