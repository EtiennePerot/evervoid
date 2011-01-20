package com.evervoid.client.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.client.graphics.materials.PlainColor;
import com.evervoid.gamedata.Dimension;
import com.evervoid.state.GridLocation;
import com.evervoid.state.Point;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

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
					return 1f;
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
	};

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
	};

	private final Map<Point, Set<GridNode>> aCellContents = new HashMap<Point, Set<GridNode>>();
	private final float aCellHeight;
	private final float aCellWidth;
	private final int aColumns;
	private HoverMode aHandleOver = HoverMode.OFF;
	private ColorRGBA aHoverColor;
	private GridCellsNode aHoveredCell = null;
	private final float aLineWidth;
	private final int aRows;

	public Grid(final int rows, final int columns, final float cellWidth, final float cellHeight, final float lineWidth,
			final ColorRGBA gridLineColor)
	{
		aRows = rows;
		aColumns = columns;
		aCellWidth = cellWidth;
		aCellHeight = cellHeight;
		aLineWidth = lineWidth;
		final Material gridMat = new PlainColor(gridLineColor);
		for (int x = 0; x <= columns; x++) {
			final Line l = new Line(new Vector3f(x * cellWidth, 0, 0), new Vector3f(x * cellWidth, rows * cellHeight, 0));
			l.setLineWidth(lineWidth);
			final Geometry g = new Geometry("Grid-" + hashCode() + " (Col " + x + ")", l);
			g.setMaterial(gridMat);
			attachChild(g);
		}
		for (int y = 0; y <= rows; y++) {
			final Line l = new Line(new Vector3f(0, y * cellHeight, 0), new Vector3f(columns * cellWidth, y * cellHeight, 0));
			l.setLineWidth(lineWidth);
			final Geometry g = new Geometry("Grid-" + hashCode() + " (Row " + y + ")", l);
			g.setMaterial(gridMat);
			attachChild(g);
		}
	}

	public Point getCellAt(final float xPosition, final float yPosition)
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

	public Point getCellAt(final Vector2f vector)
	{
		return getCellAt(vector.x, vector.y);
	}

	public Rectangle getCellBounds(final Point gridPoint)
	{
		final Vector3f origin = getCellOrigin(gridPoint);
		return new Rectangle(origin.x, origin.y, aCellWidth, aCellHeight);
	}

	public Vector3f getCellCenter(final int row, final int column)
	{
		return getCellOrigin(row, column).add(aCellWidth / 2, aCellHeight / 2, 0);
	}

	public Vector3f getCellCenter(final Point gridPoint)
	{
		return getCellCenter(gridPoint.y, gridPoint.x);
	}

	public float getCellHeight()
	{
		return aCellHeight;
	}

	public Vector3f getCellOrigin(final int row, final int column)
	{
		return new Vector3f(column * aCellWidth, row * aCellHeight, 0);
	}

	public Vector3f getCellOrigin(final Point gridPoint)
	{
		return getCellOrigin(gridPoint.y, gridPoint.x);
	}

	public float getCellWidth()
	{
		return aCellWidth;
	}

	public int getColumns()
	{
		return aColumns;
	}

	private Set<GridNode> getNodeList(final Point point)
	{
		if (!aCellContents.containsKey(point)) {
			final Set<GridNode> l = new HashSet<GridNode>();
			aCellContents.put(point, l);
			return l;
		}
		return aCellContents.get(point);
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

	public GridLocation handleOver(final Vector2f position, final Dimension dimension)
	{
		if (aHandleOver.equals(HoverMode.OFF)) {
			return null;
		}
		final Point newSquare = getCellAt(position);
		if (aHoveredCell != null) {
			if (newSquare != null) {
				final GridLocation newLocation = new GridLocation(newSquare, dimension);
				if (aHoveredCell.equivalentTo(newLocation)) {
					return newLocation;
				}
			}
			detachChild(aHoveredCell);
			aHoveredCell = null;
		}
		if (newSquare != null) {
			final GridLocation newLocation = new GridLocation(newSquare, dimension);
			aHoveredCell = new GridCellsNode(this, newLocation, aHoverColor);
			attachChild(aHoveredCell);
		}
		if (aHoveredCell == null) {
			return null;
		}
		return aHoveredCell.getLocation();
	}

	public GridNode registerNode(final GridNode node, final int row, final int column)
	{
		return registerNode(node, new Point(column, row));
	}

	public GridNode registerNode(final GridNode node, final Point location)
	{
		getNodeList(location).add(node);
		addNode(node);
		return node;
	}

	// TODO: Rewrite this with multi-cell support
	/*
	 * public void setColor(final Point location, final ColorRGBA color, final CellLayer layer) { final Vector3f origin =
	 * getCellOrigin(location).add(0, 0, layer.getZOffset()); if (aCells.containsKey(origin)) { if (color == null) {
	 * detachChild(aCells.get(origin)); aCells.remove(origin); return null; } else { aCells.get(origin).setColor(color); } }
	 * else if (color != null) { final GridCell cellBG = new GridCell(location, origin, aCellWidth, aCellHeight, color);
	 * attachChild(cellBG); aCells.put(origin, cellBG); } return aCells.get(origin); }
	 */
	public void setHandleHover(final HoverMode hoverMode)
	{
		aHandleOver = hoverMode;
	}

	public void setHoverColor(final ColorRGBA color)
	{
		aHoverColor = color;
	}

	public void unregisterNode(final GridNode node, final int row, final int column)
	{
		unregisterNode(node, new Point(column, row));
	}

	public void unregisterNode(final GridNode node, final Point location)
	{
		getNodeList(location).remove(node);
		delNode(node);
	}
}
