package com.evervoid.client.views.perspectives;

import com.evervoid.client.views.GameView;
import com.evervoid.client.views.galaxy.GalaxyView;
import com.evervoid.state.Galaxy;

public class GalaxyPerspective extends Perspective
{
	public GalaxyPerspective(final GameView gameview, final Galaxy galaxy)
	{
		super(gameview);
		setContent(new GalaxyView(galaxy));
	}
}
