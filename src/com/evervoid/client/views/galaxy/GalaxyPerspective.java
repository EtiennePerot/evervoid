package com.evervoid.client.views.galaxy;

import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.Perspective;
import com.evervoid.state.Galaxy;

public class GalaxyPerspective extends Perspective
{
	public GalaxyPerspective(final GameView gameview, final Galaxy galaxy, final Bounds bounds)
	{
		super(gameview);
		setContent(new GalaxyView(galaxy, bounds));
	}
}
