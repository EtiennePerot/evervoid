package com.evervoid.client.graphics;

import com.evervoid.client.graphics.materials.AlphaTextured;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.data.SpriteData;

/**
 * "Mainstream" sprite class; uses a plain AlphaTextured material. Usually the class you want to use for most sprites.
 */
public class Sprite extends BaseSprite
{
	/**
	 * Main constructor; builds a Sprite from a SpriteData object
	 * 
	 * @param sprite
	 *            The sprite information to build from
	 */
	public Sprite(final SpriteData sprite)
	{
		super(sprite);
	}

	/**
	 * Extra offset constructor; takes a SpriteData object, adds a specified offset to it, and builds the resulting sprite
	 * 
	 * @param offSprite
	 *            The sprite information to build from
	 * @param x
	 *            X offset to add
	 * @param y
	 *            Y offset to add
	 */
	public Sprite(final SpriteData offSprite, final int x, final int y)
	{
		super(offSprite, x, y);
	}

	/**
	 * Convenience constructor; builds a sprite from an image file name
	 * 
	 * @param image
	 *            The image to load
	 */
	public Sprite(final String image)
	{
		super(image);
	}

	/**
	 * Convenience constructor; builds a sprite from an image filename and an offset
	 * 
	 * @param sprite
	 *            The image to load
	 * @param x
	 *            X offset to add
	 * @param y
	 *            Y offset to add
	 */
	public Sprite(final String sprite, final int x, final int y)
	{
		super(sprite, x, y);
	}

	/**
	 * {@inheritDoc}; Overriden to change the return type to Sprite.
	 */
	@Override
	public Sprite bottomLeftAsOrigin()
	{
		super.bottomLeftAsOrigin();
		return this;
	}

	/**
	 * The Sprite class uses a plain AlphaTextured material, which should serve fine for most cases.
	 */
	@Override
	protected AlphaTextured buildMaterial(final SpriteData sprite) throws TextureException
	{
		return new AlphaTextured(sprite.sprite);
	}
}
