package com.evervoid.client.graphics.materials;


import com.evervoid.client.graphics.GraphicManager;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class AlphaTextured extends BaseMaterial
{
	private final BaseTexture aTexture;

	public AlphaTextured(final String texture)
	{
		super("AlphaTextured");
		setTransparent(true);
		setFloat("m_HueMultiplier", 1.7f);
		setFloat("m_AlphaMultiplier", 1f);
		aTexture = GraphicManager.getTexture(texture);
		setTexture("m_ColorMap", aTexture.getTexture());
		getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
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
		setBoolean("m_UseAlphaMultiplier", true);
		setFloat("m_AlphaMultiplier", alpha);
	}

	public void setHue(final ColorRGBA hue)
	{
		if (hue == null)
		{
			setBoolean("m_UseHueColor", false);
		}
		else
		{
			setBoolean("m_UseHueColor", true);
			setColor("m_HueColor", hue);
		}
	}

	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		setHue(hue);
		setHueMultiplier(multiplier);
	}

	public void setHueMultiplier(final float multiplier)
	{
		setFloat("m_HueMultiplier", multiplier);
	}
}
