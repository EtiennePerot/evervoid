package com.evervoid.client.graphics;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.graphics.materials.AlphaTextured;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.data.SpriteInfo;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class Sprite extends EverNode implements Sizeable
{
	public static double sGlobalSpriteDepth = 0f;
	public static final float sSpriteScale = 2f;

	public static float getNewZDepth()
	{
		final float current = (float) sGlobalSpriteDepth;
		sGlobalSpriteDepth += 0.1;
		return current;
	}

	private AlphaTextured aMaterial;
	private final SpriteInfo aSpriteInfo;
	protected Transform aSpriteTransform;
	private boolean aValidSprite = true;

	public Sprite(final SpriteInfo sprite)
	{
		aSpriteInfo = sprite;
		try {
			aMaterial = new AlphaTextured(sprite.sprite);
			final Quad q = new Quad(aMaterial.getWidth(), aMaterial.getHeight());
			final Geometry g = new Geometry("Sprite-" + sprite.sprite + " @ " + hashCode(), q);
			g.setMaterial(aMaterial);
			final EverNode image = new EverNode(g);
			addNode(image);
			// Offset image so that the origin is around the center of the image
			aSpriteTransform = image.getNewTransform();
			aSpriteTransform.translate(-aMaterial.getWidth() * Sprite.sSpriteScale / 2 + sprite.x,
					-aMaterial.getHeight() * Sprite.sSpriteScale / 2 + sprite.y).commit();
			aSpriteTransform.setScale(Sprite.sSpriteScale);
		}
		catch (final TextureException e) {
			// Do nothing; just a blank node
			aValidSprite = false;
			System.err.println("Warning: Could not load Sprite! Info = " + sprite);
		}
	}

	public Sprite(final SpriteInfo offSprite, final int x, final int y)
	{
		this(offSprite.add(x, y));
	}

	public Sprite(final String image)
	{
		this(new SpriteInfo(image));
	}

	public Sprite(final String sprite, final int x, final int y)
	{
		this(new SpriteInfo(sprite, x, y));
	}

	/**
	 * Cancels the centering offset on this Sprite
	 * 
	 * @return This
	 */
	public Sprite bottomLeftAsOrigin()
	{
		aSpriteTransform.translate(0, 0);
		return this;
	}

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
		return aMaterial.getHeight() * aSpriteTransform.getScaleAverage();
	}

	@Override
	public float getWidth()
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to get width of invalid sprite: " + this);
			return 0;
		}
		return aMaterial.getWidth() * aSpriteTransform.getScaleAverage();
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
