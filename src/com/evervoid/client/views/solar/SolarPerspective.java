package com.evervoid.client.views.solar;

import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.Perspective;
import com.evervoid.state.SolarSystem;
import com.jme3.math.ColorRGBA;

public class SolarPerspective extends Perspective
{
	private final UIControl aDefaultPanelUI;
	private UIControl aLastPanelUI = null;
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
		aDefaultPanelUI = new UIControl(BoxDirection.VERTICAL);
		aDefaultPanelUI.addString("Solar system:", ColorRGBA.LightGray, BoxDirection.HORIZONTAL);
		aDefaultPanelUI.addSpacer(1, 8);
		aDefaultPanelUI.addString(solarsystem.getName(), ColorRGBA.White, BoxDirection.HORIZONTAL);
		aDefaultPanelUI.addSpacer(1, 16);
		aDefaultPanelUI.addUI(UIStar.getStarUI(solarsystem.getStar()), 1);
		setPanelUI(null); // Init panel
	}

	public void clearPanel()
	{
		setPanelUI(null);
	}

	public void gameOver()
	{
		aSolarView.gameOver();
	}

	UIControl getLastPanelUI()
	{
		return aLastPanelUI;
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
		final UIControl newUI = ui == null ? aDefaultPanelUI : ui;
		if (newUI.equals(aLastPanelUI)) {
			return;
		}
		newUI.setBounds(aSolarPanel.getBounds());
		aSolarPanel.switchUI(newUI);
		aLastPanelUI = newUI;
	}
}
