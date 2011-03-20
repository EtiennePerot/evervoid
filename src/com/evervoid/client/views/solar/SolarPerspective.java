package com.evervoid.client.views.solar;

import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.Perspective;
import com.evervoid.state.SolarSystem;

public class SolarPerspective extends Perspective
{
	private final SolarMiniView aSolarMiniView;
	private final SolarPanel aSolarPanel;
	private final SolarView aSolarView;

	public SolarPerspective(final GameView gameview, final SolarSystem solarsystem)
	{
		super(gameview);
		aSolarView = new SolarView(solarsystem, this);
		aSolarPanel = new SolarPanel();
		aSolarMiniView = new SolarMiniView(gameview, solarsystem);
		setContent(aSolarView);
		setPanel(aSolarPanel);
		setMini(aSolarMiniView);
	}

	public void clearPanel()
	{
		setPanelUI(null);
	}

	public void delPanelUI(final UIControl panelUI)
	{
		aSolarPanel.delUI(panelUI);
	}

	@Override
	public void newTurn()
	{
		aSolarView.newTurn();
	}

	@Override
	public void onDefocus()
	{
		aSolarView.onDefocus();
	}

	@Override
	public void onFocus()
	{
		aSolarView.onFocus();
	}

	void setPanelUI(final UIControl ui)
	{
		aSolarPanel.switchUI(ui);
	}
}
