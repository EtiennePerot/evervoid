package com.evervoid.client.graphics;

import com.evervoid.client.graphics.materials.PlainColor;
import com.evervoid.state.geometry.GridLocation;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

/**
 * Geometry that covers a set of points on a grid.
 */
public class GridCellsNode extends EverNode
{
	private final Geometry aGeometry;
	private final GridLocation aLocation;

	public GridCellsNode(final Grid grid, final GridLocation location, final ColorRGBA color)
	{
		super();
		aLocation = location;
		final GridCellMesh mesh = new GridCellMesh(grid, Grid.getPoints(location));
		aGeometry = new Geometry("GridCellsNode-" + hashCode(), mesh);
		aGeometry.setMaterial(new PlainColor(color));
		attachChild(aGeometry);
	}

	public boolean equivalentTo(final GridLocation other)
	{
		return aLocation.sameAs(other);
	}

	public GridLocation getLocation()
	{
		return aLocation;
	}
}
