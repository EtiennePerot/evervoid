package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.Colorable;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.Sizable;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * AlphaTexture is a generic, all-purpose material for regular texturing. It supports alpha transparency and hue coloring.
 */
public class AlphaTextured extends BaseMaterial implements Sizable, Colorable
{
	/**
	 * Holds the hue color that the material uses. Null whenever the material doesn't use a custom hue.
	 */
	private ColorRGBA aLastHue = null;
	/**
	 * The {@link BaseTexture} currently displayed
	 */
	private final BaseTexture aTexture;
	/**
	 * Name of the texture file
	 */
	private final String aTextureFile;

	/**
	 * Public constructor; used to create actual AlphaTextured materials.
	 * 
	 * @param texture
	 *            The texture to use
	 * @throws TextureException
	 *             Raised when the texture cannot be found or loaded
	 */
	public AlphaTextured(final String texture) throws TextureException
	{
		this(texture, "AlphaTextured");
	}

	/**
	 * Package-private constructor; used by subclasses of AlphaTextured to add more functionality while using a different j3md
	 * file
	 * 
	 * @param texture
	 *            The texture to use
	 * @param material
	 *            The .j3md material to load; assumed to have at least the same properties as AlphaTextured.j3md
	 * @throws TextureException
	 *             Raised when the texture cannot be found or loaded
	 */
	AlphaTextured(final String texture, final String material) throws TextureException
	{
		super(material);
		aTextureFile = texture;
		setFloat("HueMultiplier", 1.7f);
		setFloat("AlphaMultiplier", 1f);
		aTexture = GraphicManager.getTexture(texture);
		setTexture("ColorMap", aTexture.getTexture());
		setFloat("TexturePortionX", aTexture.getHorizontalPortion());
		setFloat("TexturePortionY", aTexture.getVerticalPortion());
	}

	/**
	 * @return The dimensions of the texture
	 */
	@Override
	public Vector2f getDimensions()
	{
		return aTexture.getDimensions();
	}

	/**
	 * @return The height of the texture
	 */
	@Override
	public float getHeight()
	{
		return aTexture.getHeight();
	}

	/**
	 * @return The hue that this material is colored with, or null if this material isn't being hue-colored
	 */
	public ColorRGBA getHue()
	{
		return aLastHue;
	}

	/**
	 * @return A reference to the {@link BaseTexture} in use
	 */
	public BaseTexture getTexture()
	{
		return aTexture;
	}

	/**
	 * @return The width of the texture
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
		setBoolean("UseAlphaMultiplier", true);
		setFloat("AlphaMultiplier", MathUtils.clampFloat(0, alpha, 1));
	}

	@Override
	public void setHue(final ColorRGBA hue)
	{
		aLastHue = hue;
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
