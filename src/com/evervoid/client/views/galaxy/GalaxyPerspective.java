package com.evervoid.client.views.galaxy;

import com.evervoid.client.views.GameView;
import com.evervoid.client.views.Perspective;
import com.evervoid.state.Galaxy;

public class GalaxyPerspective extends Perspective
{
	public GalaxyPerspective(final GameView parent, final Galaxy galaxy)
	{
		super(parent);
		setContent(new GalaxyView(galaxy));
	}
}
