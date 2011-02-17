package com.evervoid.client.views.galaxy;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.views.solar.UISolarBackgroundStar;
import com.evervoid.state.geometry.Point3D;

public class GalaxyStarfield extends EverNode
{
	List<String> aStarFiles = new ArrayList<String>();
	List<UISolarBackgroundStar> aStars = new ArrayList<UISolarBackgroundStar>();

	public GalaxyStarfield()
	{
		// this is approximate, but always true
		final int height = 25;
		final int width = 45;
		// create a good number of stars
		final int numOfStars = MathUtils.getRandomIntBetween(700, 1000);
		for (int i = 0; i < numOfStars; i++) {
			final Point3D point = new Point3D(MathUtils.getRandomFloatBetween(-width, width), MathUtils.getRandomFloatBetween(
					-height, height), -10f);
			final float size = MathUtils.getRandomFloatBetween(.01f, .05f);
			final UIGalaxyBackgroundStar star = new UIGalaxyBackgroundStar(point, size);
			// aStars.add(star);
			star.getNewTransform().translate(point.x, point.y, point.z);
			addNode(star);
		}
	}
}
