package com.evervoid.client.views.game;

import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.EverUIView;
import com.evervoid.state.player.Player;
import com.jme3.math.ColorRGBA;

public class VictoryView extends EverUIView
{
	public VictoryView(final Player winner)
	{
		super(new CenteredControl(new UIControl(BoxDirection.VERTICAL)));
		final PanelControl panel = new PanelControl("Game over");
		panel.addUI(new StaticTextControl("Player " + winner.getNickname() + " has won the game.", ColorRGBA.White));
		addUI(panel, 1);
	}
}
