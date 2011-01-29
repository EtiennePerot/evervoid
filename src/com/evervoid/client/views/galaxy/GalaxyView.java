package com.evervoid.client.views.galaxy;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.ClientView;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.EVFrameManager;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.UISolarSystem;
import com.evervoid.client.views.GameView;
import com.evervoid.client.views.GameView.GameViewType;
import com.evervoid.state.Galaxy;
import com.evervoid.state.Point3D;
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
			addNode(tempSS);
		}
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		// TODO Auto-generated method stub
	}

	public Point3D getPointFromVector(final Vector2f position)
	{
		final float yPosition = position.y / EverVoidClient.getWindowDimension().getHeight();
		final float xPosition = position.x / EverVoidClient.getWindowDimension().getWidth();
		for (final UISolarSystem tempSS : aSolarSet) {
			// tempSS.getlocation();
		}
		return null;
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		final Point3D point = getPointFromVector(position);
		GameView.changeView(GameViewType.SOLAR, aGalaxy.getSolarSystem(point));
		return super.onMouseClick(position, tpf);
	}
}
