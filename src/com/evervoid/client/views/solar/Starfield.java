package com.evervoid.client.views.solar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.geometry.AnimatedScaling;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.state.Dimension;
import com.jme3.math.Vector2f;

public class Starfield extends MultiSprite implements FrameObserver
{
	private static final List<String> sStarImagesIgnore = new ArrayList<String>(1);
	private static final String sStarImagesPath = "res/gfx/space/stars/";
	private static final String sStarSpritePath = "space/stars/";
	private final AnimatedScaling aFieldTransform;
	List<String> aStarFiles = new ArrayList<String>();
	List<UIMiniStar> aStars = new ArrayList<UIMiniStar>();

	public Starfield()
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
		aFieldTransform.setDuration(SolarSystemView.sGridZoomDuration);
		resolutionChanged();
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		for (final UIMiniStar star : aStars) {
			star.pulse(f.aTpf);
		}
	}

	@Override
	public void resolutionChanged()
	{
		delAllNodes();
		final Dimension dim = EverVoidClient.getWindowDimension();
		aFieldTransform.translate(dim.width / 2, dim.height / 2, -5); // Be in the background
		final int numOfStars = MathUtils.getRandomIntBetween(500, 700);
		for (int i = 0; i < numOfStars; i++) {
			final UIMiniStar star = new UIMiniStar(sStarSpritePath + (String) MathUtils.getRandomElement(aStarFiles));
			aStars.add(star);
			addSprite(star, MathUtils.getRandomFloatBetween(-dim.width / 2, dim.width / 2),
					MathUtils.getRandomFloatBetween(-dim.height / 2, dim.height / 2));
		}
	}

	void scrollBy(final Vector2f translation)
	{
		for (final UIMiniStar star : aStars) {
			star.scrollBy(translation);
		}
	}
}
