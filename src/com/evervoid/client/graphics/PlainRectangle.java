package com.evervoid.client.graphics;

import com.evervoid.client.graphics.materials.PlainColor;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 * A simple rectangle in 3D space.
 */
public class PlainRectangle extends EverNode
{
	/**
	 * The material used for the rectangle
	 */
	private final PlainColor aMaterial;
	/**
	 * The {@link Geometry} used for the rectangle
	 */
	private final Geometry aRectangle;

	/**
	 * 2D constructor; assumes Z = 0.
	 * 
	 * @param origin
	 *            Bottom-left corner of the rectangle
	 * @param width
	 *            Width of the rectangle; extends on the X axis, towards the right
	 * @param height
	 *            Height of the rectangle; extends on the Y axis, upwards
	 * @param fill
	 *            Color filling the rectangle
	 */
	public PlainRectangle(final Vector2f origin, final float width, final float height, final ColorRGBA fill)
	{
		this(new Vector3f(origin.x, origin.y, 0), width, height, fill);
	}

	/**
	 * 3D constructor
	 * 
	 * @param origin
	 *            Bottom-left corner of the rectangle
	 * @param width
	 *            Width of the rectangle; extends on the X axis, towards the right
	 * @param height
	 *            Height of the rectangle; extends on the Y axis, upwards
	 * @param fill
	 *            Color filling the rectangle
	 */
	public PlainRectangle(final Vector3f origin, final float width, final float height, final ColorRGBA fill)
	{
		getNewTransform().translate(origin);
		final Quad q = new Quad(width, height);
		aRectangle = new Geometry("Quad-" + hashCode(), q);
		aMaterial = new PlainColor(fill);
		aRectangle.setMaterial(aMaterial);
		attachChild(aRectangle);
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}

	/**
	 * Recolors this rectangle with a new color.
	 * 
	 * @param newColor
	 *            The color to use
	 */
	public void setColor(final ColorRGBA newColor)
	{
		aRectangle.setMaterial(new PlainColor(newColor));
	}
}
