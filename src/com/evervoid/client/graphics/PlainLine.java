package com.evervoid.client.graphics;

import com.evervoid.client.graphics.materials.PlainColor;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

/**
 * A simple line in 3D space.
 */
public class PlainLine extends EverNode
{
	/**
	 * The {@link Geometry} used for the line
	 */
	private final Geometry aLine;
	/**
	 * The material used for the line
	 */
	private final PlainColor aMaterial;

	/**
	 * 2D constructor; assumes Z = 0.
	 * 
	 * @param start
	 *            Point 1 of the line
	 * @param end
	 *            Point 2 of the line
	 * @param width
	 *            Thickness of the line
	 * @param color
	 *            Color of the line
	 */
	public PlainLine(final Vector2f start, final Vector2f end, final float width, final ColorRGBA color)
	{
		this(new Vector3f(start.x, start.y, 0), new Vector3f(end.x, end.y, 0), width, color);
	}

	/**
	 * 3D constructor
	 * 
	 * @param start
	 *            Point 1 of the line
	 * @param end
	 *            Point 2 of the line
	 * @param width
	 *            Thickness of the line
	 * @param color
	 *            Color of the line
	 */
	public PlainLine(final Vector3f start, final Vector3f end, final float width, final ColorRGBA color)
	{
		final Line l = new Line(start, end);
		l.setLineWidth(width);
		aLine = new Geometry("PlainLine-" + hashCode() + " (From " + start + " to " + end + ")", l);
		aMaterial = new PlainColor(color);
		aLine.setMaterial(aMaterial);
		attachChild(aLine);
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}

	/**
	 * Recolors this line with a new color.
	 * 
	 * @param newColor
	 *            The color to use
	 */
	public void setColor(final ColorRGBA newColor)
	{
		aLine.setMaterial(new PlainColor(newColor));
	}
}
