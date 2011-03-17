package com.evervoid.client.views.solar;

import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.Perspective;
import com.evervoid.state.SolarSystem;

public class SolarPerspective extends Perspective
{
	private final SolarMiniView aSolarMiniView;
	private final SolarPanel aSolarPanel;
	private final SolarView aSolarSystemView;

	public SolarPerspective(final GameView gameview, final SolarSystem solarsystem)
	{
		super(gameview);
		aSolarSystemView = new SolarView(solarsystem, this);
		aSolarPanel = new SolarPanel();
		aSolarMiniView = new SolarMiniView(gameview, solarsystem);
		setContent(aSolarSystemView);
		setPanel(aSolarPanel);
		setMini(aSolarMiniView);
	}

	@Override
	public void newTurn()
	{
		aSolarSystemView.newTurn();
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

	void setPanelUI(final UIControl ui)
	{
		aSolarPanel.setUI(ui);
	}
}
