package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.GraphicManager;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class AlphaShaded extends BaseMaterial
{
	private final BaseTexture aTexture;

	public AlphaShaded(final String texture)
	{
		super("AlphaShaded");
		setTransparent(true);
		setColor("m_ShadeColor", ColorRGBA.Black);
		aTexture = GraphicManager.getTexture(texture);
		setTexture("m_AlphaMap", aTexture.getTexture());
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

	public void setGradientPortion(final float gradientPortion)
	{
		setFloat("m_ShadeGradientPortion", gradientPortion);
	}

	public void setShadeAngle(final float shadeAngle)
	{
		setFloat("m_ShadeAngle", shadeAngle);
	}

	public void setShadePortion(final float shadePortion)
	{
		setFloat("m_ShadePortion", shadePortion);
	}
}
