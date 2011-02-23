package com.evervoid.client.ui;

import com.evervoid.client.graphics.Sizeable;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

public class ImageControl extends UIControl implements Sizeable
{
	private Sprite aSprite = null;
	Transform aTransform;
	boolean aVertical = false;

	public ImageControl()
	{
		this((SpriteData) null, false);
	}

	public ImageControl(final SpriteData sprite)
	{
		this(sprite, false);
	}

	public ImageControl(final SpriteData sprite, final boolean vertical)
	{
		setSprite(sprite);
		aTransform = getNewTransform();
		aVertical = vertical;
	}

	public ImageControl(final String image)
	{
		this(image, false);
	}

	public ImageControl(final String image, final boolean vertical)
	{
		this(new SpriteData(image), vertical);
	}

	@Override
	public Vector2f getDimensions()
	{
		return new Vector2f(getWidth(), getHeight());
	}

	@Override
	public float getHeight()
	{
		return getDesiredSize().getHeightFloat();
	}

	public Vector2f getOffset()
	{
		return aTransform.getTranslation2f();
	}

	@Override
	public float getWidth()
	{
		return getDesiredSize().getWidthFloat();
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		if (aVertical) {
			setLength(bounds.height);
		}
		else {
			setLength(bounds.width);
		}
	}

	@Override
	protected void setControlOffset(final float x, final float y)
	{
		setOffset(x, y);
	}

	public ImageControl setLength(final float length)
	{
		if (aSprite != null) {
			if (aVertical) {
				aTransform.setScale(1, length / aSprite.getHeight());
			}
			else {
				aTransform.setScale(length / aSprite.getWidth(), 1);
			}
		}
		return this;
	}

	public ImageControl setOffset(final float x, final float y)
	{
		return setOffset(new Vector2f(x, y));
	}

	public ImageControl setOffset(final Vector2f offset)
	{
		aTransform.translate(offset);
		return this;
	}

	public void setSprite(final SpriteData sprite)
	{
		delNode(aSprite);
		if (sprite != null) {
			aSprite = new Sprite(sprite).bottomLeftAsOrigin();
			addNode(aSprite);
			setMinimumDimension(new Dimension((int) aSprite.getWidth(), (int) aSprite.getHeight()));
		}
		else {
			setMinimumDimension(new Dimension(0, 0));
		}
		recomputeAllBounds();
	}

	public void setSprite(final String sprite)
	{
		setSprite(new SpriteData(sprite));
	}
}
