package client.graphics;

import java.awt.Point;

import client.EverNode;
import client.graphics.geometry.Transform;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GridNode extends EverNode
{
	private final Grid aGrid;
	private final Transform aGridTranslation = getNewTransform();
	private Point aLocation;
	private final EverNode aNode;

	public GridNode(final Grid grid, final Point location, final EverNode node)
	{
		aGrid = grid;
		aLocation = location;
		aNode = node;
		addNode(node);
		updateTranslation();
	}

	public Vector3f getCellCenter()
	{
		return aGrid.getCellCenter(aLocation);
	}

	public EverNode getNode()
	{
		return aNode;
	}

	public Vector2f getTranslation()
	{
		return aGridTranslation.getTranslation2f();
	}

	public void moveTo(final Point destination)
	{
		aGrid.nodeMoved(this, aLocation, destination);
		aLocation = destination;
		updateTranslation();
	}

	private void updateTranslation()
	{
		aGridTranslation.translate(getCellCenter());
	}
}
