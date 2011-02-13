package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.GraphicManager;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector2f;

public class SphericalMapped extends BaseMaterial
{
	private final BaseTexture aTexture;
	private float aTextureOffset = 0f;
	private final float aTexturePortion = 1f;

	public SphericalMapped(final String texture) throws TextureException
	{
		super("SphericalMapped");
		setTransparent(true);
		getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		aTexture = GraphicManager.getTexture(texture);
		setTexture("ColorMap", aTexture.getTexture());
		setFloat("TextureOffset", 0f);
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
		return aTexture.getWidth();
	}

	public void setAlpha(final float alpha)
	{
		setBoolean("UseAlphaMultiplier", true);
		setFloat("AlphaMultiplier", alpha);
	}
}
