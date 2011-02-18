package com.evervoid.client.graphics;

import java.util.Set;

import com.evervoid.client.graphics.materials.PlainColor;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

/**
 * Geometry that covers a set of Points on a grid.
 */
public class GridCells extends EverNode
{
	private final Geometry aGeometry;

	public GridCells(final Grid grid, final GridLocation location, final ColorRGBA color)
	{
		this(grid, location.getPoints(), color);
	}

	public GridCells(final Grid grid, final Set<Point> points, final ColorRGBA color)
	{
		final GridCellMesh mesh = new GridCellMesh(grid, points);
		aGeometry = new Geometry("GridCellsNode-" + hashCode(), mesh);
		aGeometry.setMaterial(new PlainColor(color));
		attachChild(aGeometry);
	}
}
