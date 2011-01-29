package com.evervoid.client.views.galaxy;

import java.util.Set;

import com.evervoid.client.ClientView;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.FrameManager;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.UISolarSystem;
import com.evervoid.state.Galaxy;
import com.evervoid.state.Point3D;

public class GalaxyView extends ClientView implements FrameObserver
{
	// private final GalaxyGrid aGrid;
	public GalaxyView(final Galaxy galaxy)
	{
		FrameManager.register(this);
		// aGrid = new GalaxyGrid(this);
		// addNode(aGrid);
		final Set<Point3D> pointSet = galaxy.getSolarPoints();
		final float scale = Math.min(EverVoidClient.getWindowDimension().getHeight(), EverVoidClient.getWindowDimension()
				.getWidth()) * 7;
		final float scaleFactor = scale / galaxy.getSize();
		for (final Point3D point : pointSet) {
			final UISolarSystem tempSS = new UISolarSystem(galaxy.getSolarSystem(point));
			tempSS.setLocation(point.x * scaleFactor, point.y * scaleFactor, point.z * scaleFactor);
			addNode(tempSS);
		}
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		// TODO Auto-generated method stub
	}
}
