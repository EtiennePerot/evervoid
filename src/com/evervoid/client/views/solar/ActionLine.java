package com.evervoid.client.views.solar;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.Grid;
import com.evervoid.client.ui.PlainLine;
import com.evervoid.state.geometry.GridLocation;
import com.jme3.math.ColorRGBA;

public class ActionLine extends EverNode
{
	private static Iterable<GridLocation> flattenGridLocations(final GridLocation... locations)
	{
		final List<GridLocation> locs = new ArrayList<GridLocation>(locations.length);
		for (final GridLocation l : locations) {
			locs.add(l);
		}
		return locs;
	}

	private static Iterable<GridLocation> flattenGridLocations(final GridLocation start, final Iterable<GridLocation> path,
			final GridLocation end)
	{
		final List<GridLocation> allLocs = new ArrayList<GridLocation>();
		if (start != null) {
			allLocs.add(start);
		}
		if (path != null) {
			for (final GridLocation l : path) {
				allLocs.add(l);
			}
		}
		if (end != null) {
			allLocs.add(end);
		}
		return allLocs;
	}

	public ActionLine(final Grid grid, final float width, final ColorRGBA color, final GridLocation... locations)
	{
		this(grid, width, color, flattenGridLocations(locations));
	}

	public ActionLine(final Grid grid, final float width, final ColorRGBA color, final GridLocation start,
			final Iterable<GridLocation> path)
	{
		this(grid, width, color, flattenGridLocations(start, path, null));
	}

	public ActionLine(final Grid grid, final float width, final ColorRGBA color, final GridLocation start,
			final List<GridLocation> path, final GridLocation end)
	{
		this(grid, width, color, flattenGridLocations(start, path, end));
	}

	public ActionLine(final Grid grid, final float width, final ColorRGBA color, final Iterable<GridLocation> locations)
	{
		GridLocation previous = null;
		for (final GridLocation l : locations) {
			if (previous != null) {
				addNode(new PlainLine(grid.getCellCenter(previous), grid.getCellCenter(l), width, color));
			}
			previous = l;
		}
	}
}
