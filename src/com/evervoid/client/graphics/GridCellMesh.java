package com.evervoid.client.graphics;

import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.state.solar.Point;

public class GridCellMesh extends EasyMesh
{
	public GridCellMesh(final Grid grid, final Iterable<Point> cells)
	{
		for (final Point p : cells) {
			final Rectangle cellBounds = grid.getCellBounds(p);
			connect(cellBounds.getBottomLeft(), cellBounds.getTopRight(), cellBounds.getBottomRight());
			connect(cellBounds.getBottomLeft(), cellBounds.getTopLeft(), cellBounds.getTopRight());
		}
		apply();
	}
}
