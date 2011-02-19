package com.evervoid.client.views.solar;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.ui.PlainRectangle;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class SolarGridHighlightLocations extends EverNode
{
	private static final ColorRGBA sValidLocationsColor = new ColorRGBA(0f, 0.3f, 0.05f, 0.5f);
	private final AnimatedAlpha aAlphaAnimation = getNewAlphaAnimation();
	private final Set<Point> aPoints = new HashSet<Point>();

	public SolarGridHighlightLocations(final Iterable<GridLocation> locations)
	{
		for (final GridLocation loc : locations) {
			for (final Point p : loc.getPoints()) {
				addPoint(p);
			}
		}
		aAlphaAnimation.setAlpha(0);
		aAlphaAnimation.setTargetAlpha(1).setDuration(0.25f).start();
	}

	public SolarGridHighlightLocations(final Set<Point> points)
	{
		for (final Point p : points) {
			addPoint(p);
		}
	}

	private void addPoint(final Point p)
	{
		if (aPoints.add(p)) {
			addNode(new PlainRectangle(new Vector2f(SolarGrid.sCellSize * p.x, SolarGrid.sCellSize * p.y), SolarGrid.sCellSize,
					SolarGrid.sCellSize, sValidLocationsColor));
		}
	}

	void fadeOut()
	{
		aAlphaAnimation.setTargetAlpha(0).start(new Runnable()
		{
			@Override
			public void run()
			{
				removeFromParent();
			}
		});
	}
}
