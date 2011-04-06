package com.evervoid.client.views.game;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.EverUIView;
import com.evervoid.state.player.Player;

public class VictoryView extends EverUIView implements ButtonListener
{
	final ButtonControl aBackButton;
	final ButtonControl aLeaveButton;
	final ButtonControl aQuitButton;

	public VictoryView(final Player winner)
	{
		super(new CenteredControl(new UIControl(BoxDirection.VERTICAL)));
		final PanelControl panel = new PanelControl("Game over");
		if (winner.equals(GameView.getLocalPlayer())) {
			panel.addString("Contratulations, you have won the game!");
		}
		else {
			panel.addString("Player " + winner.getNickname() + " has won the game.");
		}
		panel.addSpacer(1, 16);
		final UIControl buttonRow = new UIControl(BoxDirection.HORIZONTAL);
		aBackButton = new ButtonControl("Back");
		aBackButton.addButtonListener(this);
		buttonRow.addUI(aBackButton);
		buttonRow.addFlexSpacer(1);
		aLeaveButton = new ButtonControl("Main Menu");
		aLeaveButton.addButtonListener(this);
		buttonRow.addUI(aLeaveButton);
		buttonRow.addFlexSpacer(1);
		aQuitButton = new ButtonControl("Quit");
		aQuitButton.addButtonListener(this);
		buttonRow.addUI(aQuitButton);
		panel.addUI(buttonRow);
		addUI(panel, 1);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aBackButton)) {
			setDisplayed(false);
		}
		else if (button.equals(aLeaveButton)) {
			GameView.leave();
		}
		else if (button.equals(aQuitButton)) {
			EverVoidClient.quit();
		}
	}
}
