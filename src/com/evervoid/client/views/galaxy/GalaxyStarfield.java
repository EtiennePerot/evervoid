package com.evervoid.client.views.galaxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.views.solar.UISolarBackgroundStar;
import com.evervoid.state.geometry.Point3D;

public class GalaxyStarfield extends EverNode
{
	private static final List<String> sStarImagesIgnore = new ArrayList<String>(1);
	private static final String sStarImagesPath = "res/gfx/space/stars/";
	private static final String sStarSpritePath = "space/stars/";
	List<String> aStarFiles = new ArrayList<String>();
	List<UISolarBackgroundStar> aStars = new ArrayList<UISolarBackgroundStar>();

	public GalaxyStarfield(final float width, final float height)
	{
		sStarImagesIgnore.add(".svn");
		final File stars = new File(sStarImagesPath);
		for (final String f : stars.list()) {
			if (!sStarImagesIgnore.contains(f)) {
				aStarFiles.add(f);
			}
		}
		final int numOfStars = MathUtils.getRandomIntBetween(700, 1000);
		for (int i = 0; i < numOfStars; i++) {
			final String spriteInfo = (String) MathUtils.getRandomElement(aStarFiles);
			final int maxSize = 100;// EverVoidClient.getWindowDimension().width;
			final Point3D point = new Point3D(MathUtils.getRandomFloatBetween(-width, width), MathUtils.getRandomFloatBetween(
					-height, height), -maxSize);
			final float size = MathUtils.getRandomFloatBetween(.05f, .2f);
			final UIGalaxyBackgroundStar star = new UIGalaxyBackgroundStar(point, sStarSpritePath + spriteInfo, size);
			// aStars.add(star);
			star.getNewTransform().translate(point.x, point.y, point.z);
			addNode(star);
		}
	}
}
