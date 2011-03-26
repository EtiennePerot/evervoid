package com.evervoid.client.ui;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class PlainRectangleControl extends UIControl
{
	private final PlainRectangle aRectangle;
	private final Transform aRectangleSize;

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
