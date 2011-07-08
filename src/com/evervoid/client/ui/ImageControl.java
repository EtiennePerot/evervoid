package com.evervoid.client.ui;

import com.evervoid.client.graphics.Sizable;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

/**
 * A {@link UIControl} that displays an image
 */
public class ImageControl extends UIControl implements Sizable
{
	/**
	 * The {@link Sprite} being displayed
	 */
	private Sprite aSprite = null;
	/**
	 * The {@link Transform} used to make the iamge be at the right place
	 */
	Transform aTransform;
	/**
	 * Whether setLength will resize the image vertically (true) or horizontally (false)
	 */
	boolean aVertical = false;

	/**
	 * Constructor; doesn't use any image
	 */
	public ImageControl()
	{
		this((SpriteData) null, false);
	}

	/**
	 * Constructor
	 * 
	 * @param sprite
	 *            The sprite to display, as a {@link SpriteData} data structure
	 */
	public ImageControl(final SpriteData sprite)
	{
		this(sprite, false);
	}

	/**
	 * Constructor
	 * 
	 * @param sprite
	 *            The sprite to display, as a {@link SpriteData} data structure
	 * @param vertical
	 *            Whether the image should be stretched horizontally or vertically when calling setLength
	 */
	public ImageControl(final SpriteData sprite, final boolean vertical)
	{
		setSprite(sprite);
		aTransform = getNewTransform();
		aVertical = vertical;
	}

	/**
	 * Constructor
	 * 
	 * @param image
	 *            The image to use, as a String
	 */
	public ImageControl(final String image)
	{
		this(image, false);
	}

	/**
	 * Constructor
	 * 
	 * @param image
	 *            The image to use, as a String
	 * @param vertical
	 *            Whether the image should be stretched horizontally or vertically when calling setLength
	 */
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

	/**
	 * @return The offset applied to the image compared to its usual position within its given {@link Bounds}.
	 */
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

	/**
	 * Resizes the image to a certain length. The direction of the stretch depends on the "vertical" parameter passed in the
	 * constructor
	 * 
	 * @param length
	 *            The length to give to the image, in pixels
	 * @return This, for chainability
	 */
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

	/**
	 * Set the offset of the image from the usual position it would have within its {@link Bounds}.
	 * 
	 * @param x
	 *            The x offset
	 * @param y
	 *            The y offset
	 * @return This, for chainability
	 */
	public ImageControl setOffset(final float x, final float y)
	{
		return setOffset(new Vector2f(x, y));
	}

	/**
	 * Set the offset of the image from the usual position it would have within its {@link Bounds}.
	 * 
	 * @param offset
	 *            The offset, as a {@link Vector2f}
	 * @return This, for chainability
	 */
	public ImageControl setOffset(final Vector2f offset)
	{
		aTransform.translate(offset);
		return this;
	}

	/**
	 * Set the sprite to use
	 * 
	 * @param sprite
	 *            The sprite to use, as a {@link SpriteData} data structure
	 */
	public void setSprite(final SpriteData sprite)
	{
		delNode(aSprite);
		if (sprite != null) {
			aSprite = new Sprite(sprite).bottomLeftAsOrigin();
			addNode(aSprite);
			setDesiredDimension(new Dimension((int) aSprite.getWidth(), (int) aSprite.getHeight()));
		}
		else {
			setDesiredDimension(new Dimension(0, 0));
		}
		recomputeAllBounds();
	}

	/**
	 * Set the sprite to use
	 * 
	 * @param sprite
	 *            The sprite to use, as a String
	 */
	public void setSprite(final String sprite)
	{
		setSprite(new SpriteData(sprite));
	}
}
