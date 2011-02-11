package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.jme3.math.Vector2f;

public class UIMiniStar extends Sprite
{
	private static final float[] sPulseDurationBounds = { 5, 45 };
	private static final float sPulseMaximumAlpha = 0.6f;
	private static final float sPulseMinimumAlpha = -0.2f;
	private static final float sStarScrollingLayerMultiplier = 0.1f;
	private float aAlpha;
	private final float aLayer;
	private boolean aPulseAscending = false;
	private final float aPulseTime;
	private final float aScrollSpeed;
	private final AnimatedTranslation aStarTranslation;

	public UIMiniStar(final String image)
	{
		super(image);
		aAlpha = MathUtils.getRandomFloatBetween(sPulseMaximumAlpha, sPulseMinimumAlpha);
		aLayer = MathUtils.getRandomFloatBetween(0.1, 0.45);
		aScrollSpeed = aLayer * sStarScrollingLayerMultiplier;
		aPulseTime = MathUtils.getRandomFloatBetween(sPulseDurationBounds[0], sPulseDurationBounds[1]);
		aStarTranslation = getNewTranslationAnimation();
		aStarTranslation.setDuration(SolarSystemView.sGridZoomDuration);
		aStarTranslation.setScale(aLayer);
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

	protected void scrollBy(final Vector2f scroll)
	{
		aStarTranslation.move(scroll.mult(aScrollSpeed));
	}
}
