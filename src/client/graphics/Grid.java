package client.graphics;

import java.util.HashMap;
import java.util.Map;

import client.everNode;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

public class Grid extends everNode
{
	/**
	 * Layers of elements on the grid
	 */
	public static enum CellLayer
	{
		BACKGROUND, FOREGROUND, HOVER;
		public float getZOffset()
		{
			switch (this)
			{
				case BACKGROUND:
					return -0.1f;
				case FOREGROUND:
					return 0.01f;
				case HOVER:
					return 0.1f;
			}
			return 0;
		}
	};

	/**
	 * Equivalent to a boolean right now, but may add more modes in the future
	 * (Hover color mask mode, overwrite mode, color blend mode, etc)
	 */
	public static enum HoverMode
	{
		OFF, ON
	};

	private final float aCellHeight;
	private final Map<Vector3f, GridCell> aCells = new HashMap<Vector3f, GridCell>();
	private final float aCellWidth;
	private final int aColumns;
	private HoverMode aHandleOver = HoverMode.OFF;
	private ColorRGBA aHoverColor;
	private GridCell aHoveredCell = null;
	private final float aLineWidth;
	private final int aRows;

	public Grid(final int rows, final int columns, final float cellWidth, final float cellHeight,
			final float lineWidth, final ColorRGBA gridLineColor)
	{
		aRows = rows;
		aColumns = columns;
		aCellWidth = cellWidth;
		aCellHeight = cellHeight;
		aLineWidth = lineWidth;
		final Material gridMat = GraphicManager.getPlainMaterial(gridLineColor);
		for (int x = 0; x <= columns; x++)
		{
			final Line l = new Line(new Vector3f(x * cellWidth, 0, 0),
					new Vector3f(x * cellWidth, rows * cellHeight, 0));
			l.setLineWidth(lineWidth);
			final Geometry g = new Geometry("Grid-" + hashCode() + " (Col " + x + ")", l);
			g.setMaterial(gridMat);
			attachChild(g);
		}
		for (int y = 0; y <= rows; y++)
		{
			final Line l = new Line(new Vector3f(0, y * cellHeight, 0), new Vector3f(columns * cellWidth, y
					* cellHeight, 0));
			l.setLineWidth(lineWidth);
			final Geometry g = new Geometry("Grid-" + hashCode() + " (Row " + y + ")", l);
			g.setMaterial(gridMat);
			attachChild(g);
		}
	}

	public GridCell delBackgroundColor(final int row, final int column)
	{
		return delColor(row, column, CellLayer.BACKGROUND);
	}

	public GridCell delColor(final int row, final int column, final CellLayer layer)
	{
		return setColor(row, column, null, layer);
	}

	public GridCell delForegroundColor(final int row, final int column)
	{
		return delColor(row, column, CellLayer.FOREGROUND);
	}

	public int[] getCellAt(final float x, final float y)
	{
		if (x < 0 || y < 0 || x > getTotalWidth() || y > getTotalHeight())
		{
			return null;
		}
		int iX = (int) x;
		int iY = (int) y;
		iX = (int) ((iX - (iX % aCellWidth)) / aCellWidth);
		iY = (int) ((iY - (iY % aCellHeight)) / aCellHeight);
		final int[] point = { iX, iY };
		return point;
	}

	public int[] getCellAt(final Vector2f vector)
	{
		return getCellAt(vector.x, vector.y);
	}

	public Vector3f getCellOrigin(final int row, final int column)
	{
		return new Vector3f(row * aCellWidth, column * aCellHeight, 0);
	}

	public float getTotalHeight()
	{
		return aRows * aCellHeight + aLineWidth;
	}

	public float getTotalWidth()
	{
		return aColumns * aCellWidth + aLineWidth;
	}

	public void handleOver(final Vector2f position)
	{
		if (aHandleOver.equals(HoverMode.OFF))
		{
			return;
		}
		final int[] newSquare = getCellAt(position);
		if (aHoveredCell != null)
		{
			final int[] oldSquare = aHoveredCell.getGridLocation();
			if (newSquare != null)
			{
				if (newSquare[0] == oldSquare[0] && newSquare[1] == oldSquare[1])
				{
					return;
				}
			}
			delColor(oldSquare[0], oldSquare[1], CellLayer.HOVER);
			aHoveredCell = null;
		}
		if (newSquare != null)
		{
			aHoveredCell = setColor(newSquare[0], newSquare[1], aHoverColor, CellLayer.HOVER);
		}
	}

	public GridCell setBackgroundColor(final int row, final int column, final ColorRGBA color)
	{
		return setColor(row, column, color, CellLayer.BACKGROUND);
	}

	public GridCell setColor(final int row, final int column, final ColorRGBA color, final CellLayer layer)
	{
		final Vector3f origin = getCellOrigin(row, column).add(0, 0, layer.getZOffset());
		if (aCells.containsKey(origin))
		{
			if (color == null)
			{
				detachChild(aCells.get(origin));
				aCells.remove(origin);
				return null;
			}
			else
			{
				aCells.get(origin).setColor(color);
			}
		}
		else if (color != null)
		{
			final GridCell cellBG = new GridCell(row, column, origin, aCellWidth, aCellHeight, color);
			attachChild(cellBG);
			aCells.put(origin, cellBG);
		}
		return aCells.get(origin);
	}

	public GridCell setForegroundColor(final int row, final int column, final ColorRGBA color)
	{
		return setColor(row, column, color, CellLayer.FOREGROUND);
	}

	public void setHandleHover(final HoverMode hoverMode)
	{
		aHandleOver = hoverMode;
	}

	public void setHoverColor(final ColorRGBA color)
	{
		aHoverColor = color;
	}
}
