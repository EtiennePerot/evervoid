package com.evervoid.client.graphics;

import com.evervoid.client.graphics.materials.AlphaTextured;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.data.SpriteData;

public class Sprite extends BaseSprite implements Sizeable
{
	public Sprite(final SpriteData sprite)
	{
		super(sprite);
	}

	public Sprite(final SpriteData offSprite, final int x, final int y)
	{
		super(offSprite, x, y);
	}

	public Sprite(final String image)
	{
		super(image);
	}

	public Sprite(final String sprite, final int x, final int y)
	{
		super(sprite, x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sprite bottomLeftAsOrigin()
	{
		super.bottomLeftAsOrigin();
		return this;
	}

	@Override
	protected AlphaTextured buildMaterial(final SpriteData sprite) throws TextureException
	{
		return new AlphaTextured(sprite.sprite);
	}
}
