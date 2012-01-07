package com.evervoid.client.graphics;

import com.evervoid.state.data.SpriteData;
import com.jme3.math.ColorRGBA;

/**
 * A shaded spherical sprite. Essentially a {@link MultiSprite} containing both the {@link SphericalSprite} and its
 * {@link Shade}.
 */
public class ShadedSphericalSprite extends MultiSprite implements Shadable, Spherical
{
	/**
	 * The {@link Shade} of the sprite
	 */
	private final Shade aShade;
	/**
	 * The {@link SphericalSprite} underneath the shadow
	 */
	private final SphericalSprite aSphericalSprite;

	/**
	 * Constructor; builds the {@link MultiSprite} from the given sprite and its shadow.
	 * 
	 * @param offSprite
	 *            The information on the sprite to use; its shadow will automatically be detected.
	 */
	public ShadedSphericalSprite(final SpriteData offSprite)
	{
		aSphericalSprite = new SphericalSprite(offSprite);
		addSprite(aSphericalSprite);
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
	public ShadedSphericalSprite(final String image)
	{
		this(new SpriteData(image));
	}

	@Override
	public void setClipPixels(final int pixels)
	{
		aSphericalSprite.setClipPixels(pixels);
	}

	@Override
	public void setClipRadius(final float radius)
	{
		aSphericalSprite.setClipRadius(radius);
	}

	@Override
	public Shadable setGradientPortion(final float gradientPortion)
	{
		aShade.setGradientPortion(gradientPortion);
		return this;
	}

	@Override
	public void setRotationTime(final float time)
	{
		aSphericalSprite.setRotationTime(time);
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
