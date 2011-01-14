package com.evervoid.client.graphics;

import java.awt.Dimension;


import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.GridPoint;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GridNode extends EverNode
{
	protected final Grid aGrid;
	protected Dimension aGridDimension;
	protected GridPoint aGridLocation;
	protected final AnimatedTranslation aGridTranslation = getNewTranslationAnimation();

	public GridNode(final Grid grid, final GridPoint location)
	{
		this(grid, location, new Dimension(1, 1));
	}

	public GridNode(final Grid grid, final GridPoint location, final Dimension size)
	{
		aGrid = grid;
		aGridDimension = size;
		aGridLocation = constrainToGrid(location);
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
	protected GridPoint constrainToGrid(final GridPoint location)
	{
		return location.constrain(0, 0, aGrid.getColumns() - aGridDimension.width, aGrid.getRows()
				- aGridDimension.height);
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

	public void moveTo(final GridPoint destination)
	{
		unregisterFromGrid();
		aGridLocation = constrainToGrid(destination);
		registerToGrid();
		updateTranslation();
	}

	public void moveTo(final int row, final int column)
	{
		moveTo(new GridPoint(column, row));
	}

	/**
	 * Notifies the Grid about this object's occupied cells
	 */
	protected void registerToGrid()
	{
		for (int x = 0; x < aGridDimension.width; x++)
		{
			for (int y = 0; y < aGridDimension.height; y++)
			{
				aGrid.registerNode(this, aGridLocation.add(x, y));
			}
		}
	}

	public void smoothMoveTo(final GridPoint destination)
	{
		unregisterFromGrid();
		aGridLocation = constrainToGrid(destination);
		registerToGrid();
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

	public void smoothMoveTo(final int row, final int column)
	{
		smoothMoveTo(new GridPoint(column, row));
	}

	protected void unregisterFromGrid()
	{
		for (int x = 0; x < aGridDimension.width; x++)
		{
			for (int y = 0; y < aGridDimension.height; y++)
			{
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
