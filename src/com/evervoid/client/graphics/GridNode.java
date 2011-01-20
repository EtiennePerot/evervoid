package com.evervoid.client.graphics;

import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.gamedata.Dimension;
import com.evervoid.state.Point;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GridNode extends EverNode
{
	protected final Grid aGrid;
	protected Dimension aGridDimension;
	private final Transform aGridDimensionOffset = getNewTransform();
	protected Point aGridLocation;
	protected final AnimatedTranslation aGridTranslation = getNewTranslationAnimation();

	public GridNode(final Grid grid, final Point location)
	{
		this(grid, location, new Dimension(1, 1));
	}

	public GridNode(final Grid grid, final Point location, final Dimension size)
	{
		aGrid = grid;
		aGridDimension = size;
		aGridLocation = constrainToGrid(location);
		aGridDimensionOffset.translate((size.width - 1) * aGrid.getCellWidth() / 2, (size.height - 1) * aGrid.getCellHeight()
				/ 2);
		registerToGrid();
		updateTranslation();
	}

	/**
	 * Constraints the provided GridPoint to the grid size
	 * 
	 * @param location
	 *            The GridPoint to constrain
	 * @return The constrained GridPoint
	 */
	protected Point constrainToGrid(final Point location)
	{
		return location.constrain(0, 0, aGrid.getColumns() - aGridDimension.width, aGrid.getRows() - aGridDimension.height);
	}

	public Vector3f getCellCenter()
	{
		return aGrid.getCellCenter(aGridLocation);
	}

	public Dimension getDimension()
	{
		return aGridDimension;
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
		unregisterFromGrid();
		aGridLocation = constrainToGrid(destination);
		registerToGrid();
		updateTranslation();
	}

	/**
	 * Notifies the Grid about this object's occupied cells
	 */
	protected void registerToGrid()
	{
		for (int x = 0; x < aGridDimension.width; x++) {
			for (int y = 0; y < aGridDimension.height; y++) {
				aGrid.registerNode(this, aGridLocation.add(x, y));
			}
		}
	}

	public void smoothMoveTo(final int row, final int column)
	{
		smoothMoveTo(new Point(column, row));
	}

	public void smoothMoveTo(final Point destination)
	{
		unregisterFromGrid();
		aGridLocation = constrainToGrid(destination);
		registerToGrid();
		aGridTranslation.smoothMoveTo(aGrid.getCellCenter(destination)).start(new Runnable()
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

	protected void unregisterFromGrid()
	{
		for (int x = 0; x < aGridDimension.width; x++) {
			for (int y = 0; y < aGridDimension.height; y++) {
				aGrid.unregisterNode(this, aGridLocation.add(x, y));
			}
		}
	}

	protected void updateTranslation()
	{
		aGridTranslation.setTranslationNow(getCellCenter());
		hasMoved();
	}
}
