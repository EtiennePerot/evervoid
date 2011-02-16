package com.evervoid.client.views.solar;

import com.evervoid.client.views.GameView;
import com.evervoid.client.views.Perspective;
import com.evervoid.state.SolarSystem;

public class SolarSystemPerspective extends Perspective
{
	private final SolarSystemView aSolarSystemView;

	public SolarSystemPerspective(final GameView gameview, final SolarSystem solarsystem)
	{
		super(gameview);
		aSolarSystemView = new SolarSystemView(solarsystem);
		setContent(aSolarSystemView);
	}

	@Override
	public void onDefocus()
	{
		aSolarSystemView.onDefocus();
	}

	@Override
	public void onFocus()
	{
		aSolarSystemView.onFocus();
	}
}
