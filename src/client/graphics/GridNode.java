package client.graphics;

import java.awt.Point;

import client.EverNode;
import client.Translation;

public class GridNode extends EverNode
{
	private final Grid aGrid;
	private final Translation aGridTranslation = getNewTranslation();
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

	public EverNode getNode()
	{
		return aNode;
	}

	public void moveTo(final Point destination)
	{
		aGrid.nodeMoved(this, aLocation, destination);
		aLocation = destination;
		updateTranslation();
	}

	private void updateTranslation()
	{
		aGridTranslation.translate(aGrid.getCellOrigin(aLocation));
		System.out.println("Set translation to " + aGridTranslation.get());
	}
}
