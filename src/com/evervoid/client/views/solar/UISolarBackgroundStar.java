package com.evervoid.client.views.solar;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

public class UISolarBackgroundStar extends Sprite
{
	private static final float[] sPulseDurationBounds = { 5, 45 };
	private static final float sPulseMaximumAlpha = 0.5f;
	private static final float sPulseMinimumAlpha = 0.1f;
	private static final float sStarScrollingLayerMultiplier = 0.1f;
	private float aAlpha;
	private Vector2f aBoundaries = new Vector2f(1, 1);
	private final float aLayer;
	private boolean aPulseAscending = false;
	private final float aPulseTime;
	private final float aScrollSpeed;
	private final AnimatedTranslation aStarTranslation;

	public UISolarBackgroundStar(final String image, final Dimension bounds)
	{
		super(image);
		aAlpha = MathUtils.getRandomFloatBetween(sPulseMaximumAlpha, sPulseMinimumAlpha);
		aLayer = MathUtils.getRandomFloatBetween(0.1, 0.45);
		aScrollSpeed = aLayer * sStarScrollingLayerMultiplier;
		aPulseTime = MathUtils.getRandomFloatBetween(sPulseDurationBounds[0], sPulseDurationBounds[1]);
		aStarTranslation = getNewTranslationAnimation();
		aStarTranslation.setDuration(SolarView.sGridZoomDuration).setScale(aLayer)
				.translate(MathUtils.getRandomFloatBetween(0, bounds.width), MathUtils.getRandomFloatBetween(0, bounds.height));
		resolutionChanged();
	}

	protected void pulse(final float tpf)
	{
		if (aPulseAscending) {
			aAlpha += tpf / aPulseTime;
			if (aAlpha >= sPulseMaximumAlpha) {
				aPulseAscending = false;
			}
		}
		else {
			aAlpha -= tpf / aPulseTime;
			if (aAlpha <= sPulseMinimumAlpha) {
				aPulseAscending = true;
			}
		}
		aStarTranslation.setAlpha(aAlpha);
	}

	@Override
	public void resolutionChanged()
	{
		super.resolutionChanged();
		final Dimension dim = EverVoidClient.getWindowDimension();
		aBoundaries = new Vector2f(dim.width, dim.height);
		scrollBy(new Vector2f(0, 0));
	}

	protected void scrollBy(final Vector2f scroll)
	{
		aStarTranslation.translate(MathUtils.moduloVector2f(aStarTranslation.getTranslation2f().add(scroll.mult(aScrollSpeed)),
				aBoundaries));
	}
}
