package com.evervoid.client.views.solar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.geometry.AnimatedScaling;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

public class SolarStarfield extends MultiSprite implements EVFrameObserver
{
	private static SolarStarfield sInstance = null;
	private static final List<String> sStarImagesIgnore = new ArrayList<String>(1);
	private static final String sStarImagesPath = "res/gfx/space/stars/";
	private static final String sStarSpritePath = "space/stars/";

	public static SolarStarfield getInstance()
	{
		if (sInstance == null) {
			sInstance = new SolarStarfield();
		}
		return sInstance;
	}

	private final AnimatedScaling aFieldTransform;
	List<String> aStarFiles = new ArrayList<String>();
	List<UISolarBackgroundStar> aStars = new ArrayList<UISolarBackgroundStar>();

	private SolarStarfield()
	{
		sStarImagesIgnore.add(".svn");
		final File stars = new File(sStarImagesPath);
		for (final String f : stars.list()) {
			if (!sStarImagesIgnore.contains(f)) {
				aStarFiles.add(f);
			}
		}
		EVFrameManager.register(this);
		aFieldTransform = getNewScalingAnimation();
		aFieldTransform.setDuration(SolarView.sGridZoomDuration).translate(0, 0, -100);
		resolutionChanged();
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		for (final UISolarBackgroundStar star : aStars) {
			star.pulse(f.aTpf);
		}
	}

	@Override
	public void resolutionChanged()
	{
		super.resolutionChanged();
		delAllNodes();
		final Dimension dim = EverVoidClient.getWindowDimension();
		aFieldTransform.translate(0, 0, -5); // Be in the background
		final int numOfStars = MathUtils.getRandomIntBetween(500, 700);
		for (int i = 0; i < numOfStars; i++) {
			final UISolarBackgroundStar star = new UISolarBackgroundStar(sStarSpritePath + (String) MathUtils.getRandomElement(aStarFiles), dim);
			aStars.add(star);
			addSprite(star);
		}
	}

	void scrollBy(final Vector2f translation)
	{
		for (final UISolarBackgroundStar star : aStars) {
			star.scrollBy(translation);
		}
	}
}
