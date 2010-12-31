package client.graphics;

import client.everNode;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

public class Grid extends everNode
{
	private final float aCellHeight;
	private final float aCellWidth;
	private final int aColumns;
	private final ColorRGBA aGridColor;
	private final float aLineWidth;
	private final int aRows;

	public Grid(final int rows, final int columns, final float cellWidth, final float cellHeight,
			final float lineWidth, final ColorRGBA gridColor)
	{
		aRows = rows;
		aColumns = columns;
		aCellWidth = cellWidth;
		aCellHeight = cellHeight;
		aLineWidth = lineWidth;
		aGridColor = gridColor;
		final Material gridMat = GraphicManager.getMaterial("PlainColor");
		gridMat.setColor("m_Color", gridColor);
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

	public float getTotalHeight()
	{
		return aRows * aCellHeight + aLineWidth;
	}

	public float getTotalWidth()
	{
		return aColumns * aCellWidth + aLineWidth;
	}
}
