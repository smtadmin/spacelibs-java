package com.siliconmtn.data.tree;

// JDK
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.siliconmtn.core.HashCodeUtil;
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: Node.java
 * <b>Description: Manages the node object for a tree structure</b>  
 * 
 * <b>Copyright:</b> Copyright (c) 2005
 * <b>Company:</b> Silicon Mountain Technologies
 * @author James Camire
 * @version 1.0
 * @since Aug 31, 2011
 * <b>Changes: </b>
 * JC 11-08-12 fixed hashCode, equals, and clone methods to be properly implemented
 ****************************************************************************/
public class Node implements Serializable, Comparable<Node> {
	private static final long serialVersionUID = 1l;
	
	// Member Variables
	private String nodeId;
	private String nodeName;
	private String parentId;
	private String parentName;
	private int depthLevel;
	private int orderNo;
	private boolean leaf = true;
	private boolean root;
	private List<Node> children;
	private int totalChildren;
	private Object userObject;
	private String fullPath;
    
	/**
	 * Default Constructor
	 */
	public Node() {
		children = new ArrayList<>();
	}
	
	/**
	 * Copy constructor to clone a node
	 * @param n Node to assign
	 */
	public Node(Node n) {
		this.nodeId = n.nodeId;
		this.nodeName = n.nodeName;
		this.parentId = n.parentId;
		this.parentName = n.parentName;
		this.depthLevel = n.depthLevel;
		this.orderNo = n.orderNo;
		this.leaf = n.leaf;
		this.root = n.root;

		this.setChildren(new ArrayList<>());
		this.setUserObject(n.getUserObject());
	}
	
	/**
	 * sets the root node or node head
	 * @param nodeId Id of the node
	 * @param parentId id of the parent node
	 */
	public Node(String nodeId, String parentId) {
		this();
		this.nodeId = nodeId;
		this.parentId = parentId;
	}
	
	
	/**
	 * sets the root node or node head as well as the extended data
	 * @param nodeId Id of the node
	 * @param parentId id of the parent node
	 * @param userObject Object data of this node
	 */
	public Node(String nodeId, String parentId, Object userObject) {
		this();
		this.nodeId = nodeId;
		this.parentId = parentId;
		this.userObject = userObject;
	}
	
	/**
	 * sets the root node or node head
	 * @param nodeId Id of the node
	 * @param parentId id of the parent node
	 * @param parentName Name of the parent node
	 */
	public Node(String nodeId, String parentId, String parentName) {
		this(nodeId, parentId);
		this.parentName = parentName;
	}
	
	/**
	 * Adds a child node to this element
	 * @param child Child node to be added to the root element
	 */
	public void addChild(Node child) {
		child.setDepthLevel(depthLevel + 1);
		child.setRoot(false);
		child.setFullPath(fullPath + child.getFullPath());
		child.setParentName(nodeName);
		children.add(child);
		
		setLeaf(false);
	}

	/**
	 * Returns if this is a child node
	 * @param child Node to find
	 * @return Boolean if the node is a child node
	 */
	public boolean isChild(Node child) {
		return children.contains(child);
	}

	/**
	 * Gets the node id
	 * @return Id of this node
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * Sets the node id
	 * @param nodeId Id of this node
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Gets the parent id
	 * @return ID of the prent node.
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * Sets the parent id
	 * @param parentId Sets the parent id of this node
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * Gets the node name
	 * @return Name of this node
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * Sets the node name
	 * @param nodeName Sets the name of the nodes
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * gets the user object.  This allows the node to store any type
	 * of data elements as part of the tree/node
	 * @return User object assigned to this node
	 */
	public Object getUserObject() {
		return userObject;
	}

	/**
	 * Sets the user object.  This allows the node to store any type
	 * of data elements as part of the tree/node
	 * @param userObject Custom object associated with this node
	 */
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}
	
	/**
	 * Sets the node depth level from root
	 * @param treeLevel level in the tree hierarchy
	 */
	public void setDepthLevel(int treeLevel) { 
		this.depthLevel = treeLevel;
	}
	
	/**
	 * Gets the node depth level from root
	 * @return Depth level of the node.
	 */
	public int getDepthLevel() {
		return depthLevel;
	}

	/**
	 * Describe whether this node is a leaf (true) or branch (false)
	 * @return True if this node has no children.  False otherwise
	 */
	public boolean isLeaf() {
		return leaf;
	}

	/**
	 * Sets the leaf type
	 * @param leaf Stes this node as a leaf node
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * describes whether or not the node is root
	 * @return whether the node is a root node (no parent node)
	 */
	public boolean isRoot() {
		return root;
	}
	
	/**
	 * Sets the root node
	 * @param root sets the root element
	 */
	public void setRoot(boolean root) {
		this.root = root;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// do null check
		if (obj == null) return false;

		// do this check
		if (this == obj) return true;

		// check both objects belongs to same class or not
		// check that they're both Node objects, exclusive of sub-classes.
		if (this.getClass() != obj.getClass()) return false;

		Node n = (Node) obj;
		return (this.hashCode() == n.hashCode());
	}
	
	
	/**
	 * Revised to use HashCodeUtil instead of member variables for ORM and lazy-loading 
	 * 'know issues'.  Refer to com.silicontn.gis.Location
	 */
	@Override
	public int hashCode() {
		int result = HashCodeUtil.hash(this.getNodeId());
		result += HashCodeUtil.hash(this.getParentId());
		result += HashCodeUtil.hash(this.getChildren());
		result += HashCodeUtil.hash(this.getUserObject());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString() {
        return StringUtil.getToString(this);
    }
    
    /**
     * Returns the number of child nodes
     * @return Number of children of this node
     */
	public int getNumberChildren() {
		if (children.isEmpty()) return 0;
		else
			return children.size();
	}

	/**
	 * @return the children
	 */
	public List<Node> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Node> children) {
		this.children = children;
	}

	/**
	 * @return the fullPath of this node
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * @param fullPath the fullPath to set
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * Returns the number of child nodes for this node
	 * @return Number of children for the node
	 */
	public int getTotalChildren() {
		return totalChildren;
	}

	/**
	 * Sets the total number of children for this node
	 * @param totalChildren Sets the number of children
	 */
	public void setTotalChildren(int totalChildren) {
		this.totalChildren = totalChildren;
	}
	
	/**
	 * Returns the order number (ordering) for this node
	 * @return Gets the order number for the node
	 */
	public int getOrderNo() {
		return orderNo;
	}

	/**
	 * sets the order number (ordering) for this node
	 * @param orderNo Order of the display of this node
	 */
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Node n) {
		return Integer.compare(getOrderNo(),n.getOrderNo());
	}
}
