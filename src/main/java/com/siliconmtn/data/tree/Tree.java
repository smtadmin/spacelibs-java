package com.siliconmtn.data.tree;

// JDK 11
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Spacelibs 1.x
import com.siliconmtn.core.HashCodeUtil;
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: Tree.java
 * <b>Description: Builds a Tree structure that can take data input with a parent id
 * and store in a branch and node configuration.  Will export data in a preorder list
 * format for display or other uses
 * </b>  
 * 
 * <b>Copyright:</b> Copyright (c) 2005
 * <b>Company:</b> Silicon Mountain Technologies
 * @author James Camire
 * @version 1.0
 * @since Jan 14, 2021
 * <b>Changes: </b>
 ****************************************************************************/
public class Tree implements Serializable {
	private static final long serialVersionUID = 1l;

	public static final String DEFAULT_DELIMITER = "/";

	private Node rootNode = null;
	private int depth = 0;
	private Node tempNode = null;

	/**
	 * Creates a Tree of nodes based upon a Collection of unlinked nodes.  
	 * Uses the assigned Node as the root node.
	 * @param data Collection of unlinked Node objects
	 * @param root Root Node object
	 */
	public Tree(List<Node> data,  Node root) {
		List<Node> children = new ArrayList<>();
		depth = 0;
		if (root == null) {
			rootNode = new Node(null, null);
			rootNode.setRoot(true);
		} else {
			rootNode = root;
		}

		children.add(rootNode);
		//need to use a new list here or the passed list is compromised (build removes items from the list). -JM- 11.03.2017
		build(new ArrayList<>(data), children);
	}

	/**
	 * Creates a Tree of nodes based upon a Collection of unlinked nodes
	 * @param data Collection of unlinked Node objects
	 */
	public Tree(List<Node> data) {
		this(data, null);
	}

	/**
	 * Returns a node and all of it's children based upon the node ID
	 * @param nodeId Id to find
	 * @return Node within the tree.  Null if not found
	 */
	public Node findNode(String nodeId) {
		tempNode = null;
		locateNode(rootNode.getChildren(), nodeId);
		return tempNode;
	}

	/**
	 * Recursive method that traverses the tree looking for the appropriate 
	 * Node id.
	 * @param nodes List of nodes to use as the starting point of the search
	 * @param id Node within the tree.  Null if not found
	 */
	private void locateNode(List<Node> nodes, String id) {
		if (!nodes.isEmpty()) {
			for (Node n : nodes) {
				if (id.equalsIgnoreCase(n.getNodeId())) {
					tempNode = n;
					return;
				} else locateNode(n.getChildren(), id);
			}
		} 
	}

	/**
	 * Creates and returns a List that traverses the subtree rooted at the 
	 * rootnode in preorder.  Does NOT include the rootNode in the Collection
	 * @return Collection of Nodes in the appropriate order
	 */
	public List<Node> preorderList() {
		return preorderList(false);
	}

	/**
	 * for JSTL's "I need a getter" inflexability
	 * @return List of nodeds in the preorder format
	 */
	public List<Node> getPreorderList() {
		return preorderList(false);
	}

	/**
	 * Creates and returns a List that traverses the subtree rooted at the 
	 * rootnode in preorder.  May include the rootNode in the Collection
	 * @param root Defines whether the rootNode should be returned in the List
	 * @return Collection of Nodes in the appropriate order
	 */
	public List<Node> preorderList(boolean root) {
		List<Node> hldr = new ArrayList<>();
		if (root) hldr.add(rootNode);
		createPreorder(rootNode.getChildren(), hldr);
		return hldr;
	}

	/**
	 * Creates and returns a List that traverses the subtree rooted at the 
	 * passed node in preorder.
	 * @param n Node to use as the root element in the tree
	 * @return Collection of Nodes in the appropriate order
	 */
	public List<Node> preorderList(Node n) {
		if (n == null) return Collections.emptyList();

		List<Node> hldr = new ArrayList<>();
		createPreorder(n.getChildren(), hldr);
		return hldr;

	}

	/**
	 * Recursive method that orders the elements in the appropriate order
	 * This method is stateless; changed to public/static to be usable as a utility.  -JM 05/14/13
	 * @param nodes List of nodes in this tree
	 * @param hldr List of nodes in preorder form
	 */
	public void createPreorder(List<Node> nodes, List<Node> hldr) {
		if (!nodes.isEmpty()) {
			for (Node n : nodes) {
				hldr.add(n);
				createPreorder(n.getChildren(), hldr);
			}
		}
	}

	/**
	 * Iterate from the given node through all it's children and set their total
	 * number of children to be inclusive of 
	 * @param node Node of this tree
	 * @return Total children (includes grandchildren)
	 */
	public static int calculateTotalChildren(Node node) {
		if (node == null) return 0;
		int total = 0;
		for (Node n : node.getChildren()) {
			total += calculateTotalChildren(n);
			total++;
		}
		
		node.setTotalChildren(total);

		return total;
	}

	/**
	 * <p>Builds a Tree of Nodes using the ParentId and the NodeId to determine 
	 * hierarchical structure of the tree.  Recursion is used to iteratively
	 * add as many tree layers as desirable to the structure.</p>
	 * <p> The tree structure can have from 0...n children per node.  
	 * Each child can also have from 0 ... n children per node.  This allows 
	 * the tree to grow in an unbalanced and arbitrary manner.</p>
	 * @param data Collection of Node elements to be added to the tree.  
	 * The list can be in any order as long as the nodeId and parentId 
	 * relationships are intact
	 * @param children List containing the children to be processed.  On the 
	 * initial call, the List should contain the root Node.  The root node 
	 * must have a nodeId and ParentId of NULL.
	 */
	private void build(List<Node> data,  List<Node> children) {
		depth++;
		List<Node> newChildren = new ArrayList<>();
		List<Node> removed = new ArrayList<>();
		for (Node dataNode : data) {
			for (Node childNode : children) {
				if (StringUtil.defaultString(dataNode.getParentId()).equalsIgnoreCase(childNode.getNodeId())) {
					// To avoid recursive pointers within the nodes, assign the 
					// Data node to a new node object. Also assign the 
					// current depth level
					Node node = new Node(dataNode);
					node.setDepthLevel(depth);
					node.setParentName(childNode.getNodeName());
					childNode.addChild(node);
					newChildren.add(node);


					// Add the data node to the removed list.  Once a node has
					// Been assigned to a parent, it won't be called again.  
					// We will eventually remove it from the data list to
					// improve performance
					removed.add(dataNode);
				}
			}
		}
		// Remove the assigned elements from the data list
		data.removeAll(removed);
		if (! newChildren.isEmpty()) build(data, newChildren);
	}

	/**
	 * Returns the total depth of the tree
	 * @return Depth level of the tree
	 */
	public int getDepth() { return depth; }
	
	/**
	 * Assigns the root node to the tree
	 * @param rootNode Node to set as the root of the tree
	 */
	public void setRootNode(Node rootNode) { this.rootNode = rootNode; }
	
	/**
	 * 
	 * @return Gets the root node
	 */
	public Node getRootNode() {
		return rootNode;
	}

	/**
	 * Set the fullPath variable of all nodes in the tree
	 * using the tree's default delimiter
	 */
	public void buildNodePaths() {
		buildNodePaths(rootNode, DEFAULT_DELIMITER, false);
	}


	/**
	 * Set the fullPath variable of all nodes in the tree
	 * using a custom external delimiter
	 * @param delimiter delimiter/character to use in the path
	 */
	public void buildNodePaths(String delimiter) {
		buildNodePaths(rootNode, delimiter, false);
	}

	/**
	 * Set the fullPath variable of all nodes in the tree
	 * using a custom external delimiter
	 * @param delimiter delimiter/character to use in the path
	 * @param useName determines if the name or id of the node is used for the path
	 */
	public void buildNodePaths(String delimiter, boolean useName) {
		buildNodePaths(rootNode, delimiter, useName);
	}


	/**
	 * Recursively loop over all nodes and their children, setting the fullPath
	 * variable for each Node in the Tree, starting from the supplied Node and
	 * moving downwards.  Pass a boolean usage value if the hierarchy should be
	 * based off names or nodeIds.
	 * @param parentNode Node representing the parent node
	 * @param delimiter delimiter/character to use in the path
	 * @param useName Deternmines whether to use the name or id in the path
	 */
	public void buildNodePaths(Node parentNode, String delimiter, boolean useName) {
		if (StringUtil.isEmpty(parentNode.getFullPath())) parentNode.setFullPath(delimiter);
		
		for (Node child : parentNode.getChildren()) {
			StringBuilder path = new StringBuilder(50);
			child.setFullPath(child.getFullPath().replace("null", ""));

			if (StringUtil.isEmpty(child.getFullPath())) path.append(parentNode.getFullPath());
			path.append(child.getFullPath());
			path.append(useName && !StringUtil.isEmpty(child.getNodeName()) ? child.getNodeName() : child.getNodeId());

			child.setFullPath(path.toString());
			buildNodePaths(child, delimiter, useName);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = HashCodeUtil.hash(rootNode);
		result += HashCodeUtil.hash(depth);
		result += HashCodeUtil.hash(tempNode);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		return false;
	}
}
