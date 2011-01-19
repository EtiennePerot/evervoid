package com.evervoid.client.graphics.materials;

import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;

public class PlainColor extends BaseMaterial
{
	public PlainColor(final ColorRGBA color)
	{
		super("PlainColor");
		setColor("m_Color", color);
		getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
	}

	public void setAlpha(final float alpha)
	{
		setBoolean("m_UseAlphaMultiplier", true);
		setFloat("m_AlphaMultiplier", alpha);
	}

	public void setColor(final ColorRGBA color)
	{
		setColor("m_Color", color);
	}
}
