package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.Colorable;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.Sizable;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * A textured material that will glow using the Glow postprocessing effect
 */
public class GlowTextured extends BaseMaterial implements Sizable, Colorable
{
	/**
	 * Alpha transparency value for this material
	 */
	private float aAlpha = 1f;
	/**
	 * The glow effect focuses around a certain color which will "bleed out" from the object.
	 */
	private ColorRGBA aGlowColor = ColorRGBA.Black;
	/**
	 * The {@link BaseTexture} to use on the material
	 */
	private final BaseTexture aTexture;
	/**
	 * The name of the texture file
	 */
	private final String aTextureFile;

	/**
	 * Constructor; only requires the texture file. To change the glow color, use setGlow.
	 * 
	 * @param texture
	 *            The texture to load
	 * @throws TextureException
	 *             Raised when the texture cannot be found or loaded
	 */
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

	/**
	 * The dimensions of the texture
	 */
	@Override
	public Vector2f getDimensions()
	{
		return aTexture.getDimensions();
	}

	/**
	 * The height of the texture
	 */
	@Override
	public float getHeight()
	{
		return aTexture.getHeight();
	}

	/**
	 * The width of the texture
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
	 *            The alpha transparency to use, from 0 to 1
	 */
	public void setAlpha(final float alpha)
	{
		aAlpha = alpha;
		setBoolean("UseAlphaMultiplier", true);
		setFloat("AlphaMultiplier", aAlpha);
		setColor("GlowColor", aGlowColor.mult(aAlpha));
	}

	/**
	 * Set the glow's "bleeding" color on this material
	 * 
	 * @param glowColor
	 *            The "bleeding color" of the glow effect. Will be multiplied with the current alpha value.
	 */
	public void setGlow(final ColorRGBA glowColor)
	{
		aGlowColor = glowColor;
		setColor("GlowColor", glowColor.mult(aAlpha));
	}

	@Override
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

	@Override
	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		setHue(hue);
		setHueMultiplier(multiplier);
	}

	@Override
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
