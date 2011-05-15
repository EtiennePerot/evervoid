package com.evervoid.client.graphics;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * A graphical grid. has columns, rows, cells, etc.
 */
public class Grid extends EverNode
{
	/**
	 * Z offset applied to the grid's lines. Negative so that the lines are behind the elements on the grid.
	 */
	private static final float sLineZ = -5f;
	/**
	 * Height of each cell
	 */
	private final float aCellHeight;
	/**
	 * Width of each cell
	 */
	private final float aCellWidth;
	/**
	 * Number of columns in the Grid
	 */
	private final int aColumns;
	/**
	 * Set of all lines in the Grid
	 */
	private final Set<PlainLine> aLines = new HashSet<PlainLine>();
	/**
	 * {@link EverNode} containing all the lines of the Grid
	 */
	private final EverNode aLinesNode;
	/**
	 * Thickness of each line
	 */
	private final float aLineWidth;
	/**
	 * Number of rows in the Grid
	 */
	private final int aRows;

	/**
	 * Constructor; initializes the Grid and builds all the lines.
	 * 
	 * @param size
	 *            The {@link Dimension} of the grid. Used to determine the number of rows and columns.
	 * @param cellWidth
	 *            The width of each cell.
	 * @param cellHeight
	 *            The height of each cell.
	 * @param lineWidth
	 *            The thickness of the lines.
	 * @param gridLineColor
	 *            The color of the lines.
	 */
	public Grid(final Dimension size, final float cellWidth, final float cellHeight, final float lineWidth,
			final ColorRGBA gridLineColor)
	{
		aRows = size.height;
		aColumns = size.width;
		aCellWidth = cellWidth;
		aCellHeight = cellHeight;
		aLineWidth = lineWidth;
		aLinesNode = new EverNode();
		addNode(aLinesNode);
		for (int x = 0; x <= aColumns; x++) {
			final PlainLine p = new PlainLine(new Vector3f(x * cellWidth, 0, sLineZ), new Vector3f(x * cellWidth, aRows
					* cellHeight, sLineZ), lineWidth, gridLineColor);
			aLinesNode.addNode(p);
			aLines.add(p);
		}
		for (int y = 0; y <= aRows; y++) {
			final PlainLine p = new PlainLine(new Vector3f(0, y * cellHeight, sLineZ), new Vector3f(aColumns * cellWidth, y
					* cellHeight, sLineZ), lineWidth, gridLineColor);
			aLinesNode.addNode(p);
			aLines.add(p);
		}
	}

	/**
	 * Add a GridNode to this Grid. Called by GridNodes automatically. Do not call directly.
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
	 * Delete a GridNode from the Grid. Ideally, should be called by the GridNode itself. Do not call directly.
	 * 
	 * @param node
	 *            The GridNode to delete
	 */
	protected void delGridNode(final GridNode node)
	{
		delNode(node);
		// Additional behavior in subclasses
	}

	/**
	 * Given 2D grid-based coordinates and an expected {@link Dimension}, finds the closest matching {@link GridLocation}
	 * 
	 * @param xPosition
	 *            The 2D grid-based X offset
	 * @param yPosition
	 *            The 2D grid-based Y offset
	 * @param dimension
	 *            The expected {@link Dimension} of the selection
	 * @return The closest matching {@link GridLocation} of the given {@link Dimension}
	 */
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

	/**
	 * Given 2D grid-based coordinates and an expected {@link Dimension}, finds the closest matching {@link GridLocation}
	 * 
	 * @param vector
	 *            The 2D grid-based coordinates
	 * @param dimension
	 *            The expected {@link Dimension} of the selection
	 * @return The closest matching {@link GridLocation} of the given {@link Dimension}
	 */
	public GridLocation getCellAt(final Vector2f vector, final Dimension dimension)
	{
		return getCellAt(vector.x, vector.y, dimension);
	}

	/**
	 * Given a {@link GridLocation}, returns its grid-based boundary
	 * 
	 * @param gridPoint
	 *            The {@link GridLocation} in question
	 * @return The bounds of the {@link GridLocation}, as a {@link Rectangle}
	 */
	public Rectangle getCellBounds(final GridLocation gridPoint)
	{
		final Vector3f origin = getCellOrigin(gridPoint);
		return new Rectangle(origin.x, origin.y, gridPoint.getWidth() * aCellWidth, gridPoint.getHeight() * aCellHeight);
	}

	/**
	 * Given a cell, returns its grid-based boundary
	 * 
	 * @param point
	 *            The cell in question
	 * @return The bounds of the cell, as a {@link Rectangle}
	 */
	public Rectangle getCellBounds(final Point point)
	{
		return getCellBounds(new GridLocation(point));
	}

	/**
	 * Returns the grid-based 2D coordinates to the middle of the given {@link GridLocation}
	 * 
	 * @param gridPoint
	 *            The {@link GridLocation} in question
	 * @return The 2D grid-based coordinates of its middle point
	 */
	public Vector2f getCellCenter(final GridLocation gridPoint)
	{
		return getCellBounds(gridPoint).getCenter2f();
	}

	/**
	 * @return The height of each cell in the grid.
	 */
	public float getCellHeight()
	{
		return aCellHeight;
	}

	/**
	 * Returns the grid-based 3D coordinates to the bottom-left corner of the given {@link GridLocation}
	 * 
	 * @param gridPoint
	 *            The {@link GridLocation} in question
	 * @return The 3D grid-based coordinates of its lower-left corner
	 */
	public Vector3f getCellOrigin(final GridLocation gridPoint)
	{
		return new Vector3f(gridPoint.getX() * aCellWidth, gridPoint.getY() * aCellHeight, 0);
	}

	/**
	 * @return The width of each cell in the Grid.
	 */
	public float getCellWidth()
	{
		return aCellWidth;
	}

	/**
	 * @return The number of columns in the Grid.
	 */
	public int getColumns()
	{
		return aColumns;
	}

	/**
	 * @return The length of the diagonal of the grid, divided by 2.
	 */
	public float getHalfDiagonal()
	{
		return FastMath.sqrt(FastMath.sqr(getTotalWidth()) + FastMath.sqr(getTotalHeight())) / 2f;
	}

	/**
	 * @return AnimatedAlpha pointer to the node hosting the lines of the grid
	 */
	public AnimatedAlpha getLineAlphaAnimation()
	{
		return aLinesNode.getNewAlphaAnimation();
	}

	/**
	 * Given grid-based 2D coordinates, finds the cell corresponding to it.
	 * 
	 * @param xPosition
	 *            The grid-based X position to look at
	 * @param yPosition
	 *            The grid-based Y position to look at
	 * @return The cell at the given coordinates
	 */
	private Point getPointAt(final float xPosition, final float yPosition)
	{
		if (xPosition < 0 || yPosition < 0 || xPosition > getTotalWidth() || yPosition > getTotalHeight()) {
			return null;
		}
		int iX = (int) xPosition;
		int iY = (int) yPosition;
		iX = (int) ((iX - (iX % aCellWidth)) / aCellWidth);
		iY = (int) ((iY - (iY % aCellHeight)) / aCellHeight);
		return new Point(iX, iY);
	}

	/**
	 * Given grid-based 2D coordinates, finds the cell corresponding to it.
	 * 
	 * @param vector
	 *            The grid-based 2D location to look at
	 * @return The cell at the given vector
	 */
	public Point getPointAt(final Vector2f vector)
	{
		final GridLocation loc = getCellAt(vector.x, vector.y, new Dimension(1, 1));
		if (loc == null) {
			return null;
		}
		return loc.origin;
	}

	/**
	 * @return The number of rows in the grid
	 */
	public int getRows()
	{
		return aRows;
	}

	/**
	 * @return The total height of the grid
	 */
	public float getTotalHeight()
	{
		return aRows * aCellHeight + aLineWidth;
	}

	/**
	 * @return The total width of the grid
	 */
	public float getTotalWidth()
	{
		return aColumns * aCellWidth + aLineWidth;
	}

	/**
	 * Sets the color of all the lines in the grid
	 * 
	 * @param color
	 *            The new color of the lines
	 */
	public void setLineColor(final ColorRGBA color)
	{
		for (final PlainLine line : aLines) {
			line.setColor(color);
		}
	}
}
