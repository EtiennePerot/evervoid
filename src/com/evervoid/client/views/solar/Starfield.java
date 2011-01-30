package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.jme3.math.Vector2f;

public class Starfield extends MultiSprite
{
	public Starfield(final Vector2f maxTranslation)
	{
		for (int i = 0; i < 40; i++) {
			addSprite("space/star_big_red.png", MathUtils.getRandomFloatBetween(0, maxTranslation.x),
					MathUtils.getRandomFloatBetween(0, maxTranslation.y));
		}
	}
}
