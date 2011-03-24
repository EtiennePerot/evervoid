package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Vector2f;

public class SphericalMapped extends BaseMaterial
{
	private final BaseTexture aTexture;
	private float aTextureOffset = 0f;

	public SphericalMapped(final String texture) throws TextureException
	{
		super("SphericalMapped");
		aTexture = GraphicManager.getTexture(texture);
		setTexture("ColorMap", aTexture.getTexture());
		setFloat("TextureOffset", 0f);
		setFloat("ClipRadius", 1f);
		setFloat("TexturePortion", aTexture.getHorizontalPortion());
	}

	public void addOffset(final float offset)
	{
		aTextureOffset = (aTextureOffset + offset / 2f) % 1f;
		setFloat("TextureOffset", aTextureOffset);
	}

	public Vector2f getDimensions()
	{
		return aTexture.getDimension();
	}

	public float getHeight()
	{
		return aTexture.getHeight();
	}

	public float getWidth()
	{
		// Spherical texture
		return aTexture.getHeight();
	}

	public void setAlpha(final float alpha)
	{
		setBoolean("UseAlphaMultiplier", true);
		setFloat("AlphaMultiplier", alpha);
	}

	/**
	 * Shaves a certain number of rendered pixels off the edge of this sphere
	 * 
	 * @param pixels
	 *            Number of pixels to shave
	 */
	public void setClipPixels(final int pixels)
	{
		setClipRadius(1f - pixels / (getWidth() / 2));
	}

	/**
	 * Limits the rendered radius of the sphere
	 * 
	 * @param radius
	 *            The radius (from 0 to 1) to render
	 */
	public void setClipRadius(final float radius)
	{
		setFloat("ClipRadius", MathUtils.clampFloat(0f, radius, 1f));
	}
}
