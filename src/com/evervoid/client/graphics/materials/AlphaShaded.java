package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.GraphicManager;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class AlphaShaded extends BaseMaterial
{
	private BaseTexture aTexture;

	public AlphaShaded(final String texture)
	{
		super("AlphaShaded");
		setTransparent(true);
		setColor("m_ShadeColor", ColorRGBA.Black);
		// Try to see if there is a shadow map
		aTexture = GraphicManager.getTexture(texture.replace(".png", ".shadow.png"));
		setBoolean("m_IsShadowMap", true);
		// Otherwise fall back to regular graphic as map
		if (aTexture == null) {
			aTexture = GraphicManager.getTexture(texture);
			setBoolean("m_IsShadowMap", false);
		}
		setTexture("m_AlphaMap", aTexture.getTexture());
		setFloat("m_ShadowMapMultiplier", 1.0f);
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
		// Negate for convenience
		// Fragment shader requires angle where the shade actually is
		// In the engine, it's easier to get the angle where the light is instead
		setFloat("m_ShadeAngle", (-shadeAngle) % FastMath.TWO_PI);
	}

	public void setShadePortion(final float shadePortion)
	{
		setFloat("m_ShadePortion", shadePortion);
	}

	public void setShadowMapMultiplier(final float multiplier)
	{
		setFloat("m_ShadowMapMultiplier", multiplier);
	}
}
