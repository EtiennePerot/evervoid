package com.evervoid.client.ui;

import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.data.SpriteData;
import com.jme3.math.Vector2f;

public class UIConnector extends UIControl
{
	private final Sprite aSprite;
	Transform aSpriteTransform;
	boolean aVertical = false;

	public UIConnector(final SpriteData sprite)
	{
		this(sprite, false);
	}

	public UIConnector(final SpriteData sprite, final boolean vertical)
	{
		aSprite = new Sprite(sprite).bottomLeftAsOrigin();
		addNode(aSprite);
		setOuterBounds(new Bounds(0, 0, aSprite.getWidth(), aSprite.getHeight()));
		aSpriteTransform = getNewTransform();
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

	public Vector2f getOffset()
	{
		return aSpriteTransform.getTranslation2f();
	}

	public UIConnector setLength(final float length)
	{
		if (aVertical) {
			aSpriteTransform.setScale(1, length / getHeight());
		}
		else {
			aSpriteTransform.setScale(length / getWidth(), 1);
		}
		return this;
	}

	public UIConnector setOffset(final float x, final float y)
	{
		return setOffset(new Vector2f(x, y));
	}

	public UIConnector setOffset(final Vector2f offset)
	{
		aSpriteTransform.translate(offset);
		return this;
	}
}
