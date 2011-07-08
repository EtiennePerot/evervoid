package com.evervoid.client.ui;

import com.evervoid.client.graphics.PlainRectangle;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * A simple, no-frills, plain-colored rectangle as a {@link UIControl}
 */
public class PlainRectangleControl extends UIControl
{
	/**
	 * The {@link PlainRectangle} used to actually draw the plain-colored rectangle
	 */
	private final PlainRectangle aRectangle;
	/**
	 * The {@link Transform} used to morph the {@link PlainRectangle} to the desired size, in order to fit in the control's
	 * {@link Bounds}.
	 */
	private final Transform aRectangleSize;

	/**
	 * Constructor
	 * 
	 * @param color
	 *            The color of the rectangle
	 */
	public PlainRectangleControl(final ColorRGBA color)
	{
		aRectangle = new PlainRectangle(new Vector2f(0, 0), 1, 1, color);
		aRectangleSize = aRectangle.getNewTransform();
		addNode(aRectangle);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aRectangleSize.setScale(bounds.width, bounds.height);
	}
}
