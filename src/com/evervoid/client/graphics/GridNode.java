package com.evervoid.client.graphics;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.jme3.math.Vector2f;

public abstract class GridNode extends EverNode
{
	protected final Grid aGrid;
	protected GridLocation aGridLocation;
	protected final AnimatedTranslation aGridTranslation = getNewTranslationAnimation();

	/**
	 * Constructor for GridNodes. Subclasses should call addToGrid() when done in the constructor
	 * 
	 * @param grid
	 *            The Grid to attach to
	 * @param location
	 *            The location of the node
	 */
	public GridNode(final Grid grid, final GridLocation location)
	{
		aGrid = grid;
		aGridLocation = constrainToGrid(location);
		updateTranslation();
	}

	/**
	 * Adds the node to the Grid
	 */
	protected void addToGrid()
	{
		aGrid.addGridNode(this);
	}

	/**
	 * Constraints the provided GridPoint to the grid size
	 * 
	 * @param location
	 *            The GridPoint to constrain
	 * @return The constrained GridPoint
	 */
	protected GridLocation constrainToGrid(final GridLocation location)
	{
		return location.constrain(0, 0, aGrid.getColumns(), aGrid.getRows());
	}

	/**
	 * Deletes the node from the Grid
	 */
	protected void delFromGrid()
	{
		aGrid.delGridNode(this);
	}

	public Vector2f getCellCenter()
	{
		return aGrid.getCellCenter(aGridLocation);
	}

	public Dimension getDimension()
	{
		return aGridLocation.dimension;
	}

	public Vector2f getGridTranslation()
	{
		return aGridTranslation.getTranslation2f();
	}

	public GridLocation getLocation()
	{
		return aGridLocation;
	}

	public Vector2f getTranslation()
	{
		return aGridTranslation.getTranslation2f();
	}

	protected abstract void finishedMoving();

	public void moveTo(final GridLocation destination)
	{
		aGridLocation = constrainToGrid(destination);
		updateTranslation();
	}

	public void smoothMoveTo(final GridLocation destination)
	{
		smoothMoveTo(destination, new Runnable()
		{
			@Override
			public void run()
			{
				if (!aGridTranslation.isInProgress()) {
					updateTranslation();
				}
			}
		});
	}

	public void smoothMoveTo(final GridLocation destination, final Runnable callback)
	{
		aGridLocation = constrainToGrid(destination);
		aGridTranslation.smoothMoveTo(aGrid.getCellCenter(destination)).start(callback);
	}

	protected void updateTranslation()
	{
		aGridTranslation.setTranslationNow(getCellCenter());
		finishedMoving();
	}
}
