package com.evervoid.client.ui;

import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

public class UIConnector extends Sprite implements Resizeable
{
	Transform aTransform;
	boolean aVertical = false;

	public UIConnector(final SpriteData sprite)
	{
		this(sprite, false);
	}

	public UIConnector(final SpriteData sprite, final boolean vertical)
	{
		super(sprite);
		bottomLeftAsOrigin();
		aTransform = getNewTransform();
		aVertical = vertical;
	}

	public UIConnector(final String image)
	{
		this(image, false);
	}

	public UIConnector(final String image, final boolean vertical)
	{
		this(new SpriteData(image), vertical);
	}

	@Override
	public Dimension getMinimumSize()
	{
		final Vector2f size = getDimensions();
		return new Dimension((int) size.x, (int) size.y);
	}

	public Vector2f getOffset()
	{
		return aTransform.getTranslation2f();
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

	@Override
	public void sizeTo(final Dimension dimension)
	{
		if (aVertical) {
			setLength(dimension.getHeightFloat());
		}
		else {
			setLength(dimension.getWidthFloat());
		}
	}

	@Override
	public String toString(final String prefix)
	{
		return prefix + super.toString();
	}

	@Override
	public void offsetBy(Vector2f offset)
	{
		setOffset(offset);
	}
}
