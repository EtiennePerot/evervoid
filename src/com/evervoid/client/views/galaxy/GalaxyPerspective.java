package com.evervoid.client.views.galaxy;

import com.evervoid.client.views.GameView;
import com.evervoid.client.views.Perspective;
import com.evervoid.state.Galaxy;

public class GalaxyPerspective extends Perspective
{
	public GalaxyPerspective(final GameView gameview, final Galaxy galaxy)
	{
		super(gameview);
		setContent(new GalaxyView(galaxy));
	}
}
