package com.evervoid.client.views.galaxy;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.ClientView;
import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.UISolarSystem;
import com.evervoid.client.views.GameView;
import com.evervoid.client.views.GameView.GameViewType;
import com.evervoid.state.Galaxy;
import com.evervoid.state.Point3D;
import com.evervoid.state.SolarSystem;
import com.jme3.math.Vector2f;

public class GalaxyView extends ClientView implements FrameObserver
{
	private final Galaxy aGalaxy;
	private final Set<UISolarSystem> aSolarSet;
	private final float scaleFactor;

	public GalaxyView(final Galaxy pGalaxy)
	{
		aGalaxy = pGalaxy;
		EVFrameManager.register(this);
		aSolarSet = new HashSet<UISolarSystem>();
		final Set<Point3D> pointSet = pGalaxy.getSolarPoints();
		final float scale = Math.min(EverVoidClient.getWindowDimension().getHeight(), EverVoidClient.getWindowDimension()
				.getWidth()) * 7;
		scaleFactor = scale / pGalaxy.getSize();
		for (final Point3D point : pointSet) {
			final UISolarSystem tempSS = new UISolarSystem(pGalaxy.getSolarSystem(point));
			tempSS.setLocation(point.x * scaleFactor, point.y * scaleFactor, point.z * scaleFactor);
			addSolarNode(tempSS);
		}
	}

	public void addSolarNode(final UISolarSystem pSolar)
	{
		aSolarSet.add(pSolar);
		addNode(pSolar);
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		// TODO Auto-generated method stub
	}

	public SolarSystem getSolarSystemFromVector(final Vector2f position)
	{
		// TODO - deal with rotations
		for (final UISolarSystem tempSS : aSolarSet) {
			if (tempSS.containsPosition(position)) {
				return tempSS.getSolarSystem();
			}
		}
		return null;
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		GameView.changeView(GameViewType.SOLAR, getSolarSystemFromVector(position));
		return super.onMouseClick(position, tpf);
	}
}
