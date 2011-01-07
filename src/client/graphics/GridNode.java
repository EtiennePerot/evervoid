package client.graphics;

import java.awt.Point;

import client.EverNode;
import client.graphics.geometry.AnimatedTranslation;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GridNode extends EverNode
{
	protected final Grid aGrid;
	protected Point aGridLocation;
	protected final AnimatedTranslation aGridTranslation = getNewTranslationAnimation();

	public GridNode(final Grid grid, final Point location)
	{
		aGrid = grid;
		aGrid.registerNode(this, location);
		aGridLocation = location;
		updateTranslation();
		aGridTranslation.setDuration(1); // FIXME: temp
	}

	public Vector3f getCellCenter()
	{
		return aGrid.getCellCenter(aGridLocation);
	}

	public Vector2f getTranslation()
	{
		return aGridTranslation.getTranslation2f();
	}

	protected void hasMoved()
	{
		// Overridden by subclasses
	}

	public void moveTo(final int row, final int column)
	{
		moveTo(new Point(column, row));
	}

	public void moveTo(final Point destination)
	{
		aGrid.nodeMoved(this, aGridLocation, destination);
		aGridLocation = destination;
		updateTranslation();
	}

	public void smoothMoveTo(final int row, final int column)
	{
		smoothMoveTo(new Point(column, row));
	}

	public void smoothMoveTo(final Point destination)
	{
		aGrid.nodeMoved(this, aGridLocation, destination);
		aGridLocation = destination;
		aGridTranslation.smoothMoveTo(aGrid.getCellCenter(destination)).start(new Runnable()
		{
			@Override
			public void run()
			{
				if (!aGridTranslation.isInProgress())
				{
					updateTranslation();
				}
			}
		});
	}

	protected void updateTranslation()
	{
		aGridTranslation.setTranslationNow(getCellCenter());
		hasMoved();
	}
}
