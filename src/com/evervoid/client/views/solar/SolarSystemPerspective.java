package com.evervoid.client.views.solar;

import com.evervoid.client.views.GameView;
import com.evervoid.client.views.Perspective;
import com.evervoid.state.SolarSystem;

public class SolarSystemPerspective extends Perspective
{
	public SolarSystemPerspective(final GameView parent, final SolarSystem solarsystem)
	{
		super(parent);
		setContent(new SolarSystemView(solarsystem));
	}
}
