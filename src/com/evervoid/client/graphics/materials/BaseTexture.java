package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.Sizable;
import com.jme3.math.Vector2f;
import com.jme3.texture.Image;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture2D;

/**
 * Base texture class. Holds a simple 2-dimensional texture
 */
public class BaseTexture implements Sizable
{
	/**
	 * The effective, displayed size of the texture, in pixels, stored as a 2-float vector. Takes horizontal/vertical portions
	 * into account
	 */
	private Vector2f aDimension;
	/**
	 * The horizontal portion of the texture that is actually meant to be displayed, from 0 (nothing) to 1 (full width)
	 */
	private float aHorizontalPortion = 1f;
	/**
	 * The size of the image itself, in pixels, stored as a 2-float vector. Doesn't take horizontal/vertical portions into
	 * account
	 */
	private final Vector2f aImageSize;
	/**
	 * The underlying jME3 {@link Texture2D} object.
	 */
	private final Texture2D aTexture;
	/**
	 * The vertical portion of the texture that is actually meant to be displayed, from 0 (nothing) to 1 (full height)
	 */
	private float aVerticalPortion = 1f;

	/**
	 * Wrap a jME3 {@link Texture2D}
	 * 
	 * @param texture
	 *            The {@link Texture2D} to wrap
	 * @throws TextureException
	 *             Raised when the texture is invalid
	 */
	public BaseTexture(final Texture2D texture) throws TextureException
	{
		if (texture == null) {
			throw new TextureException();
		}
		aTexture = texture;
		final Image img = aTexture.getImage();
		aImageSize = new Vector2f(img.getWidth(), img.getHeight());
		computeDimension();
	}

	/**
	 * Recompute the texture's effective dimensions
	 */
	private void computeDimension()
	{
		aDimension = new Vector2f(aImageSize.x * aHorizontalPortion, aImageSize.y * aVerticalPortion);
	}

	/**
	 * @return The texture's effective dimensions
	 */
	@Override
	public Vector2f getDimensions()
	{
		return aDimension;
	}

	/**
	 * @return The texture's effective height
	 */
	@Override
	public float getHeight()
	{
		return aDimension.y;
	}

	/**
	 * @return The horizontal fraction of the texture being used
	 */
	public float getHorizontalPortion()
	{
		return aHorizontalPortion;
	}

	/**
	 * @return The underlying jME3 {@link Texture2D} object
	 */
	public Texture2D getTexture()
	{
		return aTexture;
	}

	/**
	 * @return The vertical fraction of the texture being used
	 */
	public float getVerticalPortion()
	{
		return aVerticalPortion;
	}

	/**
	 * @return The effective width of the texture
	 */
	@Override
	public float getWidth()
	{
		return aDimension.x;
	}

	/**
	 * Set the magnification filter on this texture
	 * 
	 * @param filter
	 *            The {@link MagFilter} to use on this texture
	 */
	public void setMagFilter(final MagFilter filter)
	{
		aTexture.setMagFilter(filter);
	}

	/**
	 * Set the minification filter on this texture
	 * 
	 * @param filter
	 *            The {@link MinFilter} to use on this texture
	 */
	public void setMinFilter(final MinFilter filter)
	{
		aTexture.setMinFilter(filter);
	}

	/**
	 * Set the horizontal and vertical portions of the texture to use
	 * 
	 * @param horizontal
	 *            Horizontal portion to use, from 0 (nothing) to 1 (full width)
	 * @param vertical
	 *            Vertical portion to use, from 0 (nothing) to 1 (full height)
	 * @return This (for chainability)
	 */
	public BaseTexture setPortion(final float horizontal, final float vertical)
	{
		aHorizontalPortion = horizontal;
		aVerticalPortion = vertical;
		computeDimension();
		return this;
	}

	/**
	 * Apply the magnification and minification filters that are commonly used for sprites.
	 */
	public void setSpriteFilters()
	{
		setMagFilter(MagFilter.Nearest);
		setMinFilter(MinFilter.Trilinear);
	}
}
