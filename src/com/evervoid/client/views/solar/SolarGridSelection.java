package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.AnimatedScaling;
import com.evervoid.client.graphics.geometry.AnimatedTransform.DurationMode;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.PlainRectangle;
import com.evervoid.state.geometry.GridLocation;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class SolarGridSelection extends EverNode
{
	private static final ColorRGBA sErrorFlashColor = new ColorRGBA(0.5f, 0f, 0f, 0.5f);
	private static final int sFlashIterations = 2;
	private static final float sFlashSeconds = 0.1f;
	private static final ColorRGBA sSelectionColor = new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f);
	private final AnimatedAlpha aAlpha;
	private final AnimatedAlpha aFlashAlpha;
	private GridLocation aLocation;
	private final AnimatedScaling aScale;
	private final AnimatedTranslation aTranslation;

	SolarGridSelection()
	{
		addNode(new PlainRectangle(new Vector2f(0, 0), SolarGrid.sCellSize, SolarGrid.sCellSize, sSelectionColor));
		aScale = getNewScalingAnimation();
		aAlpha = getNewAlphaAnimation();
		aTranslation = getNewTranslationAnimation();
		aTranslation.setDuration(0.075f).setDurationMode(DurationMode.CONTINUOUS).translate(0, 0, -20);
		aScale.setDuration(0.075f).setDurationMode(DurationMode.CONTINUOUS);
		aAlpha.setDuration(0.25f).setDurationMode(DurationMode.CONTINUOUS).setAlpha(0);
		final PlainRectangle flash = new PlainRectangle(new Vector2f(0, 0), SolarGrid.sCellSize, SolarGrid.sCellSize,
				sErrorFlashColor);
		aFlashAlpha = flash.getNewAlphaAnimation();
		aFlashAlpha.setDuration(sFlashSeconds).setAlpha(0);
		addNode(flash);
		aLocation = new GridLocation(0, 0);
	}

	void fadeIn()
	{
		aAlpha.setTargetAlpha(1).start();
	}

	void fadeOut()
	{
		aAlpha.setTargetAlpha(0).start();
	}

	void flash()
	{
		flash(sFlashIterations);
	}

	private void flash(final int iterations)
	{
		aFlashAlpha.setTargetAlpha(1).start(new Runnable()
		{
			@Override
			public void run()
			{
				aFlashAlpha.setTargetAlpha(0).start(new Runnable()
				{
					@Override
					public void run()
					{
						if (iterations > 1) {
							flash(iterations - 1);
						}
					}
				});
			}
		});
	}

	GridLocation getLocation()
	{
		return aLocation;
	}

	void goTo(final GridLocation location)
	{
		if (location == null || aLocation.equals(location)) {
			return;
		}
		aLocation = location.clone();
		aTranslation.smoothMoveTo(location.origin.x * SolarGrid.sCellSize, location.origin.y * SolarGrid.sCellSize).start();
		aScale.setTargetScale(location.dimension.width, location.dimension.height).start();
	}
}
