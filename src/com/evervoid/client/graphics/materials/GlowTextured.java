package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.GraphicManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class GlowTextured extends BaseMaterial
{
	private float aAlpha = 1f;
	private ColorRGBA aGlowColor = ColorRGBA.Black;
	private final BaseTexture aTexture;
	private final String aTextureFile;

	public GlowTextured(final String texture) throws TextureException
	{
		super("GlowTextured");
		aTextureFile = texture;
		setFloat("HueMultiplier", 1.7f);
		setFloat("AlphaMultiplier", aAlpha);
		aTexture = GraphicManager.getTexture(texture);
		setTexture("ColorMap", aTexture.getTexture());
		setFloat("TexturePortionX", aTexture.getHorizontalPortion());
		setFloat("TexturePortionY", aTexture.getVerticalPortion());
		setColor("GlowColor", aGlowColor);
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
		aAlpha = alpha;
		setBoolean("UseAlphaMultiplier", true);
		setFloat("AlphaMultiplier", aAlpha);
		setColor("GlowColor", aGlowColor.mult(aAlpha));
	}

	public void setGlow(final ColorRGBA glowColor)
	{
		aGlowColor = glowColor;
		setColor("GlowColor", glowColor.mult(aAlpha));
	}

	public void setHue(final ColorRGBA hue)
	{
		if (hue == null) {
			setBoolean("UseHueColor", false);
		}
		else {
			setBoolean("UseHueColor", true);
			setColor("HueColor", hue);
		}
	}

	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		setHue(hue);
		setHueMultiplier(multiplier);
	}

	public void setHueMultiplier(final float multiplier)
	{
		setFloat("HueMultiplier", multiplier);
	}

	@Override
	public String toString()
	{
		return "AlphaTextured(" + aTextureFile + ")";
	}
}
