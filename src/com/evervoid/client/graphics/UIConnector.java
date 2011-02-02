package com.evervoid.client.graphics;

import com.evervoid.client.graphics.geometry.Transform;
import com.jme3.math.Vector2f;

public class UIConnector extends Sprite
{
	Transform aTransform;
	boolean aVertical = false;

	public UIConnector(final String image, final boolean vertical)
	{
		super(image);
		bottomLeftAsOrigin();
		aTransform = getNewTransform();
		aVertical = vertical;
	}

	public UIConnector setLength(final float length)
	{
		if (aVertical) {
			aTransform.setScale(1, length / getHeight());
		}
		else {
			aTransform.setScale(length / getWidth(), 1);
		}
		return this;
	}

	public UIConnector setOffset(final float x, final float y)
	{
		return setOffset(new Vector2f(x, y));
	}

	public UIConnector setOffset(final Vector2f offset)
	{
		aTransform.translate(offset);
		return this;
	}
}
