package com.evervoid.client.graphics;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.graphics.materials.AlphaTextured;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.data.SpriteData;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public abstract class BaseSprite extends EverNode implements Sizeable
{
	private AlphaTextured aMaterial;
	private final SpriteData aSpriteInfo;
	protected Transform aSpriteTransform;
	private boolean aValidSprite = true;

	public BaseSprite(final SpriteData sprite)
	{
		aSpriteInfo = sprite;
		try {
			aMaterial = buildMaterial(sprite);
			final Quad q = new Quad(aMaterial.getWidth(), aMaterial.getHeight());
			final Geometry g = new Geometry("Sprite-" + sprite.sprite + " @ " + hashCode(), q);
			g.setMaterial(aMaterial);
			final EverNode image = new EverNode(g);
			addNode(image);
			// Offset image so that the origin is around the center of the image
			aSpriteTransform = image.getNewTransform();
			aSpriteTransform.translate(-aMaterial.getWidth() * sprite.scale / 2 + sprite.x,
					-aMaterial.getHeight() * sprite.scale / 2 + sprite.y).commit();
			aSpriteTransform.setScale(sprite.scale).commit();
		}
		catch (final TextureException e) {
			// Do nothing; just a blank node
			aValidSprite = false;
			System.err.println("Warning: Could not load Sprite! Info = " + sprite);
		}
	}

	public BaseSprite(final SpriteData offSprite, final int x, final int y)
	{
		this(offSprite.add(x, y));
	}

	public BaseSprite(final String image)
	{
		this(new SpriteData(image));
	}

	public BaseSprite(final String sprite, final int x, final int y)
	{
		this(new SpriteData(sprite, x, y));
	}

	/**
	 * Cancels the centering offset on this Sprite
	 * 
	 * @return This
	 */
	public BaseSprite bottomLeftAsOrigin()
	{
		aSpriteTransform.translate(0, 0);
		return this;
	}

	protected abstract AlphaTextured buildMaterial(SpriteData sprite) throws TextureException;

	@Override
	public Vector2f getDimensions()
	{
		return new Vector2f(getWidth(), getHeight());
	}

	@Override
	public float getHeight()
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to get height of invalid sprite: " + this);
			return 0;
		}
		return aMaterial.getHeight() * aSpriteTransform.getScaleY();
	}

	public ColorRGBA getHue()
	{
		return aMaterial.getHue();
	}

	public AlphaTextured getMaterial()
	{
		return aMaterial;
	}

	@Override
	public float getWidth()
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to get width of invalid sprite: " + this);
			return 0;
		}
		return aMaterial.getWidth() * aSpriteTransform.getScaleX();
	}

	@Override
	protected void setAlpha(final float alpha)
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to set alpha of invalid sprite: " + this);
			return;
		}
		aMaterial.setAlpha(alpha);
	}

	public void setHue(final ColorRGBA hue)
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to set hue of invalid sprite: " + this);
			return;
		}
		aMaterial.setHue(hue);
	}

	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to set hue of invalid sprite: " + this);
			return;
		}
		aMaterial.setHue(hue, multiplier);
	}

	@Override
	public String toString()
	{
		String s = "Sprite " + aSpriteInfo.sprite;
		if (aSpriteInfo.x != 0 || aSpriteInfo.y != 0) {
			s += " @ " + aSpriteInfo.x + "; " + aSpriteInfo.y;
		}
		if (!aValidSprite) {
			s = "INVALID " + s;
		}
		return s;
	}
}
