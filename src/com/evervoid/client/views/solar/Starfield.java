package com.evervoid.client.views.solar;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.state.Dimension;

public class Starfield extends MultiSprite
{
	public Starfield()
	{
		resolutionChanged();
	}

	@Override
	public void resolutionChanged()
	{
		delAllNodes();
		final Dimension dim = EverVoidClient.getWindowDimension();
		for (int i = 0; i < 40; i++) {
			/*
			 * addSprite("space/star_big_red.png", MathUtils.getRandomFloatBetween(0, dim.width),
			 * MathUtils.getRandomFloatBetween(0, dim.height)).getNewTransform().setScale(0.2);
			 */
		}
	}

	void setPanning(final float x, final float y)
	{
	}
}
