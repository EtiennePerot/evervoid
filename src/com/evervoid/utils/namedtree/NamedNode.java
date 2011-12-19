package com.evervoid.utils.namedtree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.utils.BadInitialization;

/**
 * NamedNodes are the nodes of a tree with n nodes. Associations to children nodes are named and must be unique. Each node also
 * contains a reference to the parent node.
 * 
 * @param <T>
 *            The type of the values to be stored in each node within this tree.
 */
public class NamedNode<T>
{
	/**
	 * A map from names to children nodes.
	 */
	private final Map<String, NamedNode<T>> aChildren;
	/**
	 * The parent node; null if this node is the head of a tree.
	 */
	public NamedNode<T> aParent;
	/**
	 * The value stored by this node.
	 */
	private final T aValue;

	/**
	 * Creates a new node and notifies the parent.
	 * 
	 * @param pParent
	 *            The node to set as parent after creating this node.
	 * @param pName
	 *            The name associated with this node in the parent map.
	 * @param pValue
	 *            The value to be stored in this node.
	 * @throws BadInitialization
	 *             If the parent name is already taken in the parent node.
	 */
	public NamedNode(final NamedNode<T> pParent, final String pName, final T pValue) throws BadInitialization
	{
		this(pValue);
		if (pParent.addChild(pName, this)) {
			aParent = pParent;
		}
		else {
			throw new BadInitialization("Name taken in parent");
		}
	}

	/**
	 * Sets this Node as the head of a tree with the given value.
	 * 
	 * @param pValue
	 *            The value to be stored in this node.
	 */
	public NamedNode(final T pValue)
	{
		aValue = pValue;
		aChildren = new HashMap<String, NamedNode<T>>();
	}

	/**
	 * Adds the parameter node to this one as a child with the parameter name if it isn't already taken.
	 * 
	 * @param pName
	 *            The name of the node to add.
	 * @param pNode
	 *            The node to add as a child.
	 * @return Whether the node was added.
	 */
	public boolean addChild(final String pName, final NamedNode<T> pNode)
	{
		if (aChildren.containsKey(pName)) {
			return false;
		}
		aChildren.put(pName, pNode);
		return true;
	}

	/**
	 * Creates a node containing the value and adds it to this one as a child with the given name if it isn't already taken.
	 * 
	 * @param pName
	 *            The name of the node to add.
	 * @param pValue
	 *            The node to add as a child.
	 * @return The node if it was added, null otherwise.
	 */
	public NamedNode<T> addChild(final String pName, final T pValue)
	{
		NamedNode<T> node = null;
		try {
			node = new NamedNode<T>(this, pName, pValue);
		}
		catch (final BadInitialization e) {
			return null;
		}
		return node;
	}

	/**
	 * @param pValue
	 *            The value to check against.
	 * @return Whether any of this node's children contains the parameter value.
	 */
	public boolean childrenContain(final T pValue)
	{
		for (final NamedNode<T> node : aChildren.values()) {
			if (node.contains(pValue) || node.childrenContain(pValue)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param pValue
	 *            The value to check against.
	 * @return Whether this node contains the parameter value.
	 */
	public boolean contains(final T pValue)
	{
		return aValue.equals(pValue);
	}

	/**
	 * Searches iteratively through the tree for a node containing the value. Returns that node, or null if no such node exists.
	 * 
	 * @param pValue
	 *            The value to search for.
	 * @return The node containing pValue or null if no such node.
	 */
	public NamedNode<T> findNode(final T pValue)
	{
		if (aValue.equals(pValue)) {
			return this;
		}
		NamedNode<T> result = null;
		for (final NamedNode<T> node : aChildren.values()) {
			result = node.findNode(pValue);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * @param pName
	 *            The name of the child to return.
	 * @return The child node associated with the given name, null if it doesn't exist.
	 */
	public NamedNode<T> getChild(final String pName)
	{
		return aChildren.get(pName);
	}

	/**
	 * @return The names of this node's children
	 */
	public Set<String> getChildrenNames()
	{
		return aChildren.keySet();
	}

	/**
	 * @return The values of all this node's children.
	 */
	public Set<T> getChildrenValues()
	{
		final Set<T> values = new HashSet<T>();
		for (final NamedNode<T> node : aChildren.values()) {
			values.add(node.getValue());
		}
		return values;
	}

	/**
	 * @return The parent node, null if is head.
	 */
	public NamedNode<T> getParent()
	{
		return aParent;
	}

	/**
	 * @return the value stored by this node.
	 */
	public T getValue()
	{
		return aValue;
	}

	/**
	 * @return Whether the node is a head.
	 */
	public boolean isHead()
	{
		return aParent == null;
	}

	/**
	 * @return Whether the node is a leaf.
	 */
	public boolean isLeaf()
	{
		return aChildren.isEmpty();
	}
}
