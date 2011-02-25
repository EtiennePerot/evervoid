package com.evervoid.client.graphics;

import java.util.List;

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
	public void delFromGrid()
	{
		aGrid.delGridNode(this);
	}

	protected abstract void finishedMoving();

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

	public void moveTo(final GridLocation destination)
	{
		aGridLocation = constrainToGrid(destination);
		updateTranslation();
	}

	public void smoothMoveTo(final List<GridLocation> moves, final Runnable callback)
	{
		if (moves.isEmpty()) {
			if (callback != null) {
				callback.run();
			}
			updateTranslation();
			return;
		}
		final GridLocation next = moves.remove(0);
		aGridLocation = next.clone();
		aGridTranslation.smoothMoveTo(aGrid.getCellCenter(next)).start(new Runnable()
		{
			@Override
			public void run()
			{
				smoothMoveTo(moves, callback);
			}
		});
	}

	protected void updateTranslation()
	{
		aGridTranslation.setTranslationNow(getCellCenter());
		finishedMoving();
	}
}
