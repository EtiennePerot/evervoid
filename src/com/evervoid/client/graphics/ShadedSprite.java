package com.evervoid.client.graphics;

import com.evervoid.state.data.SpriteData;
import com.jme3.math.ColorRGBA;

/**
 * A shaded sprite. Essentially a {@link MultiSprite} containing both the {@link BaseSprite} and its {@link Shade}.
 */
public class ShadedSprite extends MultiSprite implements Shadable
{
	/**
	 * The {@link Shade} of the sprite
	 */
	private final Shade aShade;

	/**
	 * Constructor; builds the {@link MultiSprite} from the given sprite and its shadow.
	 * 
	 * @param offSprite
	 *            The information on the sprite to use; its shadow will automatically be detected.
	 */
	public ShadedSprite(final SpriteData offSprite)
	{
		addSprite(offSprite);
		aShade = new Shade(offSprite);
		aShade.setShadePortion(0.6f).setGradientPortion(0.5f).setShadeAngle(0);
		addSprite(aShade);
	}

	/**
	 * Convenience constructor; builds the {@link MultiSprite} from the given image file and its shadow.
	 * 
	 * @param image
	 *            The filename of the sprite to use; its shadow will automatically be detected.
	 */
	public ShadedSprite(final String image)
	{
		this(new SpriteData(image));
	}

	@Override
	public Shadable setGradientPortion(final float gradientPortion)
	{
		aShade.setGradientPortion(gradientPortion);
		return this;
	}

	@Override
	public Shadable setShadeAngle(final float shadeAngle)
	{
		aShade.setShadeAngle(shadeAngle);
		return this;
	}

	@Override
	public Shadable setShadeColor(final ColorRGBA glowColor)
	{
		aShade.setShadeColor(glowColor);
		return this;
	}

	@Override
	public Shadable setShadePortion(final float shadePortion)
	{
		aShade.setShadePortion(shadePortion);
		return this;
	}
}
