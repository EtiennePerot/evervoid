package com.evervoid.client.graphics;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.client.ui.PlainLine;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class Grid extends EverNode
{
	/**
	 * Layers of elements on the grid
	 */
	public static enum CellLayer
	{
		BACKGROUND, FOREGROUND, HOVER;
		public float getZOffset()
		{
			switch (this) {
				case BACKGROUND:
					return -0.1f;
				case FOREGROUND:
					return 0.01f;
				case HOVER:
					return 100f;
			}
			return 0;
		}
	}

	/**
	 * Equivalent to a boolean right now, but may add more modes in the future (Hover color mask mode, overwrite mode, color
	 * blend mode, etc)
	 */
	public static enum HoverMode
	{
		OFF, ON
	}

	/**
	 * Given a GridLocation, iterates over the cells in it
	 * 
	 * @param location
	 *            A GridLocation
	 * @return An Iterable over the set of points in it
	 */
	public static Iterable<Point> getPoints(final GridLocation location)
	{
		final List<Point> points = new ArrayList<Point>();
		for (int x = 0; x < location.getWidth(); x++) {
			for (int y = 0; y < location.getHeight(); y++) {
				points.add(location.origin.add(x, y));
			}
		}
		return points;
	}

	private final float aCellHeight;;
	private final float aCellWidth;;
	private final int aColumns;
	protected EverNode aLines;
	private final float aLineWidth;
	private final int aRows;

	public Grid(final Dimension size, final float cellWidth, final float cellHeight, final float lineWidth,
			final ColorRGBA gridLineColor)
	{
		aRows = size.height;
		aColumns = size.width;
		aCellWidth = cellWidth;
		aCellHeight = cellHeight;
		aLineWidth = lineWidth;
		aLines = new EverNode();
		addNode(aLines);
		for (int x = 0; x <= aColumns; x++) {
			aLines.addNode(new PlainLine(new Vector3f(x * cellWidth, 0, 0), new Vector3f(x * cellWidth, aRows * cellHeight, 0),
					lineWidth, gridLineColor));
		}
		for (int y = 0; y <= aRows; y++) {
			aLines.addNode(new PlainLine(new Vector3f(0, y * cellHeight, 0), new Vector3f(aColumns * cellWidth, y * cellHeight,
					0), lineWidth, gridLineColor));
		}
	}

	/**
	 * Add a GridNode to this Grid. Called by GridNodes automatically, do not call!
	 * 
	 * @param node
	 *            The GridNode to add
	 */
	protected void addGridNode(final GridNode node)
	{
		addNode(node);
		// Additional behavior in subclasses
	}

	/**
	 * Delete a GridNode from the Grid. Ideally, should be called by the GridNode itself.
	 * 
	 * @param node
	 *            The GridNode to delete
	 */
	protected void delGridNode(final GridNode node)
	{
		delNode(node);
		// Additional behavior in subclasses
	}

	public GridLocation getCellAt(final float xPosition, final float yPosition, final Dimension dimension)
	{
		if (!dimension.sameAs(1, 1)) {
			final Point centered = getPointAt(xPosition - aCellWidth * (dimension.getWidthFloat() - 1) / 2, yPosition
					- aCellHeight * (dimension.getHeightFloat() - 1) / 2);
			if (centered != null) {
				return new GridLocation(centered, dimension).constrain(aColumns, aRows);
			}
		}
		final Point point = getPointAt(xPosition, yPosition);
		if (point == null) {
			return null;
		}
		return new GridLocation(point, dimension).constrain(aColumns, aRows);
	}

	public Point getPointAt(final Vector2f vector)
	{
		final GridLocation loc = getCellAt(vector.x, vector.y, new Dimension(1, 1));
		if (loc == null) {
			return null;
		}
		return loc.origin;
	}

	public GridLocation getCellAt(final Vector2f vector, final Dimension dimension)
	{
		return getCellAt(vector.x, vector.y, dimension);
	}

	public Rectangle getCellBounds(final GridLocation gridPoint)
	{
		final Vector3f origin = getCellOrigin(gridPoint);
		return new Rectangle(origin.x, origin.y, gridPoint.getWidth() * aCellWidth, gridPoint.getHeight() * aCellHeight);
	}

	public Rectangle getCellBounds(final Point point)
	{
		return getCellBounds(new GridLocation(point));
	}

	public Vector2f getCellCenter(final GridLocation gridPoint)
	{
		return getCellBounds(gridPoint).getCenter2f();
	}

	public float getCellHeight()
	{
		return aCellHeight;
	}

	public Vector3f getCellOrigin(final GridLocation gridPoint)
	{
		return new Vector3f(gridPoint.getX() * aCellWidth, gridPoint.getY() * aCellHeight, 0);
	}

	public float getCellWidth()
	{
		return aCellWidth;
	}

	public int getColumns()
	{
		return aColumns;
	}

	public float getHalfDiagonal()
	{
		return FastMath.sqrt(FastMath.sqr(getTotalWidth()) + FastMath.sqr(getTotalHeight())) / 2f;
	}

	private Point getPointAt(final float xPosition, final float yPosition)
	{
		if (xPosition < 0 || yPosition < 0 || xPosition > getTotalWidth() || yPosition > getTotalHeight()) {
			return null;
		}
		int iX = (int) xPosition;
		int iY = (int) yPosition;
		iX = (int) ((iX - (iX % aCellWidth)) / aCellWidth);
		iY = (int) ((iY - (iY % aCellHeight)) / aCellHeight);
		return new Point(new Point(iX, iY));
	}

	public int getRows()
	{
		return aRows;
	}

	public float getTotalHeight()
	{
		return aRows * aCellHeight + aLineWidth;
	}

	public float getTotalWidth()
	{
		return aColumns * aCellWidth + aLineWidth;
	}
}
