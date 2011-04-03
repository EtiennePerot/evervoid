package com.evervoid.client.views.game;

import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.EverUIView;
import com.evervoid.state.player.Player;
import com.jme3.math.ColorRGBA;

public class VictoryView extends EverUIView implements ButtonListener
{
	public VictoryView(final Player winner)
	{
		super(new CenteredControl(new UIControl(BoxDirection.VERTICAL)));
		final PanelControl panel = new PanelControl("Game over");
		panel.addUI(new StaticTextControl("Player " + winner.getNickname() + " has won the game.", ColorRGBA.White));
		panel.addSpacer(1, 16);
		final UIControl buttonRow = new UIControl(BoxDirection.HORIZONTAL);
		buttonRow.addFlexSpacer(1);
		final ButtonControl okButton = new ButtonControl("OK");
		okButton.addButtonListener(this);
		buttonRow.addUI(okButton);
		panel.addUI(buttonRow);
		addUI(panel, 1);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		setDisplayed(false);
	}
}
