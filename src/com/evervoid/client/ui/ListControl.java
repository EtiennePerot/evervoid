package com.evervoid.client.ui;

import java.util.List;

import com.evervoid.utils.namedtree.NamedNode;

/**
 * A control that provides an easy way to build a list menu. New rows are automatically indented based on their location in the
 * menu. Leaf rows call the controlClicked method to inform concrete implementations of the event. All other rows toggle their
 * graphical elements as well as those of their children based on what is currently showing. The control is made such that users
 * can scroll to look at all rows even if there are more rows than can fit on the area of the overall control. Note that rows
 * cannot be partially show, so large rows will no appear if any part is off the screen. The ListControl does this on a
 * row-per-row basis so as to allow a maximum to be displayed on the screen at any given time.
 */
public abstract class ListControl extends ScrollingControl implements ClickObserver
{
	/**
	 * The root node containing the entire tree of possible sprites.
	 */
	private final NamedNode<UIControl> aRootNode;

	/**
	 * Constructor with configurable desired size
	 * 
	 * @param minWidth
	 *            Minimum width to occupy
	 * @param minHeight
	 *            Minimum height to occupy
	 */
	public ListControl(final float minWidth, final float minHeight)
	{
		super(minWidth, minHeight);
		aRootNode = new NamedNode<UIControl>("root", this);
	}

	/**
	 * Adds a title row as a child at the given place in the menu hierarchy.
	 * 
	 * @param pName
	 *            The name to be used for this control in the menu hierarchy.
	 * @param pRow
	 *            The control to add.
	 * @param pHierarchy
	 *            The parent under which to add this title row.
	 */
	public void addChildRow(final String pName, final UIControl pRow, final List<String> pHierarchy)
	{
		NamedNode<UIControl> curr = aRootNode;
		for (final String s : pHierarchy) {
			curr = curr.getChild(s);
		}
		addControlToNode(pName, pRow, curr);
	}

	/**
	 * Adds a title row as a child at the given place in the menu hierarchy.
	 * 
	 * @param pName
	 *            The name to be used for this control in the menu hierarchy.
	 * @param pTitle
	 *            The string to display for this row.
	 * @param pHierarchy
	 *            The parent under which to add this title row.
	 */
	public void addChildTitleRow(final String pName, final String pTitle, final List<String> pHierarchy)
	{
		final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
		for (int i = 0; i < 1 + pHierarchy.size(); i++) {
			row.addSpacer(4, 1);
		}
		row.addString(pTitle);
		addChildRow(pName, row, pHierarchy);
	}

	/**
	 * Adds the control as a child to the node. Also registers the list control as a click observer and sets it to be hoverable
	 * so that user interaction is enabled.
	 * 
	 * @param pName
	 *            The name to give the new node.
	 * @param pControl
	 *            The control to be contained in the new node.
	 * @param pNode
	 *            The parent node under which to add this new node.
	 */
	private void addControlToNode(final String pName, final UIControl pControl, final NamedNode<UIControl> pNode)
	{
		pNode.addChild(pName, pControl);
		pControl.registerClickObserver(this);
		pControl.setHoverSelectable(true);
	}

	/**
	 * Adds a control to the top of the menu.
	 * 
	 * @param pName
	 *            The name to be used for this control in the menu hierarchy.
	 * @param pRow
	 *            The control to add.
	 */
	public void addTopMenuRow(final String pName, final UIControl pRow)
	{
		addControlToNode(pName, pRow, aRootNode);
	}

	/**
	 * Adds a Title row to the top layer of the menu.
	 * 
	 * @param pName
	 *            The name to be used for this control in the menu hierarchy.
	 * @param pTitle
	 *            The string to display for this row.
	 */
	public void addTopTitleRow(final String pName, final String pTitle)
	{
		final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
		row.addSpacer(4, 1);
		row.addString(pTitle);
		addTopMenuRow(pName, row);
	}

	/**
	 * Called when a leaf node is clicked in the list.
	 * 
	 * @param control
	 *            The control that was clicked
	 * @param hierarchy
	 *            A list of strings representing the location of the control in the menu hierarchy.
	 * @return Whether the observer did anything with the click event.
	 */
	protected abstract boolean controlClicked(final UIControl control, List<String> hierarchy);

	/**
	 * Hides all controls below the one contained in the node.
	 * 
	 * @param node
	 *            The node at which to start.
	 */
	private void hideAllChildrenControls(final NamedNode<UIControl> node)
	{
		for (final NamedNode<UIControl> child : node.getChildren()) {
			if (hasChild(child.getValue())) {
				deleteChildUI(child.getValue());
				hideAllChildrenControls(child);
			}
		}
	}

	/**
	 * Displays the direct children of the top node in the tree.
	 */
	protected void reset()
	{
		delAllChildUIs();
		setAutomaticSpacer(6);
		for (final NamedNode<UIControl> node : aRootNode.getChildren()) {
			addUI(node.getValue());
		}
	}

	/**
	 * Shows or hides all direct children of the given node.
	 * 
	 * @param pNode
	 *            The node from which to toggle all children.
	 */
	private void toggleElement(final NamedNode<UIControl> pNode)
	{
		int startIndex = getChildUIIndex(pNode.getValue());
		for (final NamedNode<UIControl> node : pNode.getChildren()) {
			final UIControl control = node.getValue();
			if (hasChild(control)) {
				// your control is showing
				deleteChildUI(control);
				hideAllChildrenControls(node);
			}
			else {
				// time to add the child
				insertUI(++startIndex, control);
			}
		}
	}

	/**
	 * Shows or hides all direct children of the given node.
	 * 
	 * @param pNodeName
	 *            The name of the node from which to toggle all children.
	 */
	void toggleElement(final String pNodeName)
	{
		toggleElement(aRootNode.getChild(pNodeName));
	}

	@Override
	public boolean uiClicked(final UIControl clicked)
	{
		final List<String> hierarchy = aRootNode.findNodeHierarchy(clicked);
		final NamedNode<UIControl> node = aRootNode.findNode(hierarchy);
		if (node.isHead()) {
			// this one really shouldn't be hidden...
			return true;
		}
		else if (node.isLeaf()) {
			return controlClicked(node.getValue(), hierarchy);
		}
		else {
			toggleElement(node);
			return true;
		}
	}
}
