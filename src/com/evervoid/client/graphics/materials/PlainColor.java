package com.evervoid.client.graphics.materials;

import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;

public class PlainColor extends BaseMaterial
{
	public PlainColor(final ColorRGBA color)
	{
		super("PlainColor");
		setTransparent(true);
		setColor("Color", color);
		getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
	}

	public void setAlpha(final float alpha)
	{
		setBoolean("UseAlphaMultiplier", true);
		setFloat("AlphaMultiplier", alpha);
	}

	public void setColor(final ColorRGBA color)
	{
		setColor("Color", color);
	}
}
