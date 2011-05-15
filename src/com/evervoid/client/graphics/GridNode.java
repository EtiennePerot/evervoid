package com.evervoid.client.graphics;

import java.util.List;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.utils.EVUtils;
import com.jme3.math.Vector2f;

/**
 * A node in a {@link Grid}. Same properties as an {@link EverNode}, but with the additional knowledge that it is in a
 * {@link Grid}.
 */
public abstract class GridNode extends EverNode
{
	/**
	 * The {@link Grid} that this GridNode is in
	 */
	protected final Grid aGrid;
	/**
	 * The {@link GridLocation} of this GridNode
	 */
	protected GridLocation aGridLocation;
	/**
	 * {@link AnimatedTranslation} used for moving this GridNode from a cell to another. Moving doesn't have to be animated, but
	 * it can be.
	 */
	protected final AnimatedTranslation aGridTranslation = getNewTranslationAnimation();

	/**
	 * Constructor for GridNode. Subclasses should call addToGrid() when done in the constructor.
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
	 * Constrains the provided GridPoint to the grid size
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

	/**
	 * Called when the GridNode has finished moving to a new {@link GridLocation}.
	 */
	protected abstract void finishedMoving();

	/**
	 * @return The grid-based 2D coordinates to the middle point of this GridNode.
	 */
	public Vector2f getCellCenter()
	{
		return aGrid.getCellCenter(aGridLocation);
	}

	/**
	 * @return The {@link Dimension} of this GridNode
	 */
	public Dimension getDimension()
	{
		return aGridLocation.dimension;
	}

	/**
	 * @return The grid-based 2D coordinates used for translating this GridNode from the {@link Grid}'s (0, 0) location to where
	 *         the GridNode is right now
	 */
	public Vector2f getGridTranslation()
	{
		return aGridTranslation.getTranslation2f();
	}

	/**
	 * @return The {@link GridLocation} of this GridNode
	 */
	public GridLocation getLocation()
	{
		return aGridLocation;
	}

	/**
	 * Instantly move to a target {@link GridLocation} on the {@link Grid}.
	 * 
	 * @param destination
	 *            The {@link GridLocation} to move to.
	 */
	public void moveTo(final GridLocation destination)
	{
		aGridLocation = constrainToGrid(destination);
		updateTranslation();
	}

	/**
	 * Smoothly move through a list of {@link GridLocation}s on the {@link Grid}
	 * 
	 * @param moves
	 *            The list of {@link GridLocation} to go through
	 * @param callback
	 *            A {@link Runnable} to call once all the moving is done
	 */
	public void smoothMoveTo(final List<GridLocation> moves, final Runnable callback)
	{
		if (moves.isEmpty()) {
			updateTranslation();
			EVUtils.runCallback(callback);
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

	/**
	 * Called to apply the Grid translation instantly. Calls the finishedMoving callback.
	 */
	protected void updateTranslation()
	{
		aGridTranslation.setTranslationNow(getCellCenter());
		finishedMoving();
	}
}
