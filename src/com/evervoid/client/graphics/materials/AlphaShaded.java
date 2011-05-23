package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.Shade;
import com.evervoid.client.graphics.Sizable;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * Implements a 2D {@link Shade} using a shadow map.
 */
public class AlphaShaded extends BaseMaterial implements Sizable
{
	/**
	 * The {@link Shade}'s shadow map
	 */
	private BaseTexture aTexture;

	/**
	 * Constructor; only requires the name of the teture to shade.
	 * 
	 * @param texture
	 *            Name of the texture to shade. NOT the shadow map. The shadow map image will be detected if it exists;
	 *            otherwise, the texture to hsade will be used as its own shadow map.
	 * @throws TextureException
	 *             Raised when the texture cannot be found or loaded
	 */
	public AlphaShaded(final String texture) throws TextureException
	{
		super("AlphaShaded");
		setColor("ShadeColor", ColorRGBA.Black);
		// Try to see if there is a shadow map
		try {
			aTexture = GraphicManager.getTexture(texture.replace(".png", ".shadow.png"));
			setBoolean("IsShadowMap", true);
		}
		catch (final TextureException e) {
			aTexture = GraphicManager.getTexture(texture);
			setBoolean("IsShadowMap", false);
		}
		setTexture("AlphaMap", aTexture.getTexture());
		setFloat("ShadowMapMultiplier", 1.0f);
		setFloat("TexturePortionX", aTexture.getHorizontalPortion());
		setFloat("TexturePortionY", aTexture.getVerticalPortion());
	}

	/**
	 * @return The dimensions of the shadow map
	 */
	@Override
	public Vector2f getDimensions()
	{
		return aTexture.getDimensions();
	}

	/**
	 * @return The height of the shadow map
	 */
	@Override
	public float getHeight()
	{
		return aTexture.getHeight();
	}

	/**
	 * @return The width of the shadow map
	 */
	@Override
	public float getWidth()
	{
		return aTexture.getWidth();
	}

	/**
	 * Set the alpha transparency of this material
	 * 
	 * @param alpha
	 *            The alpha transparency to use
	 */
	public void setAlpha(final float alpha)
	{
		setBoolean("UseAlphaMultiplier", true);
		setFloat("AlphaMultiplier", alpha);
	}

	/**
	 * Set the portion of the total shade area that will be used for the "gradient" (middle) part of the shade
	 * 
	 * @param gradientPortion
	 *            The portion of the "gradient" part (from 0 (no gradient) to 1 (the whole shade is a gradient))
	 */
	public void setGradientPortion(final float gradientPortion)
	{
		setFloat("ShadeGradientPortion", gradientPortion);
	}

	/**
	 * Set the angle at which the light is coming from
	 * 
	 * @param shadeAngle
	 *            The angle of the light, in radians
	 */
	public void setShadeAngle(final float shadeAngle)
	{
		// Negate for convenience
		// Fragment shader requires angle where the shade actually is
		// In the engine, it's easier to get the angle where the light is instead
		setFloat("ShadeAngle", (-shadeAngle) % FastMath.TWO_PI);
	}

	/**
	 * Set the glow color of the shade. Usually black for regular shadows, but can be slightly tinted for a cool effect.
	 * 
	 * @param glowColor
	 *            The glow color of the shade
	 */
	public void setShadeColor(final ColorRGBA glowColor)
	{
		setColor("ShadeColor", glowColor);
	}

	/**
	 * Set the portion of the total shade area that will be used for the "opaque" (last) part of the shade
	 * 
	 * @param shadePortion
	 *            The portion of the "opaque" part (from 0 (no opaque part) to 1 (the whole shade is opaque))
	 */
	public void setShadePortion(final float shadePortion)
	{
		setFloat("ShadePortion", MathUtils.clampFloat(0, shadePortion, 1));
	}

	/**
	 * Set the muliplier applied to determine how much impact the shadow map has on the final shade
	 * 
	 * @param multiplier
	 *            The shadow map multiplier
	 */
	public void setShadowMapMultiplier(final float multiplier)
	{
		setFloat("ShadowMapMultiplier", multiplier);
	}
}
