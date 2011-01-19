package com.evervoid.client.views.galaxy;

import java.util.Map;

import com.evervoid.client.FrameObserver;
import com.evervoid.client.GameView;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.state.galaxy.Point3D;
import com.evervoid.state.solar.SolarSystem;

public class GalaxyView extends GameView implements FrameObserver
{
	public GalaxyView(final Map<SolarSystem, Point3D> pGalaxy)
	{
		super();
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		// TODO Auto-generated method stub
	}
}
