package com.evervoid.client.views.preferences;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

public class PreferencePanel extends BoxControl implements ButtonListener
{
	private static int sButtonSpacing = 20;
	final ButtonControl aMainMenuButton;
	final TextInputControl aNameInput;
	final StaticTextControl aStaticName;

	public PreferencePanel()
	{
		super(BoxDirection.VERTICAL);
		aStaticName = new StaticTextControl(EverVoidClient.getSettings().getNickname(), ColorRGBA.Red);
		addUI(aStaticName);
		aNameInput = new TextInputControl(20);
		aNameInput.setDesiredDimension(new Dimension(2, 2));
		aNameInput.setText(EverVoidClient.getSettings().getNickname());
		addUI(aNameInput);
		addSpacer(1, sButtonSpacing * 2);
		aMainMenuButton = new ButtonControl("Main Menu");
		aMainMenuButton.addButtonListener(this);
		addUI(aMainMenuButton);
	}

	@Override
	public void buttonClicked(final ButtonControl button)
	{
		if (button.equals(aMainMenuButton)) {
			EVViewManager.switchTo(ViewType.MAINMENU);
		}
	}

	@Override
	public void onKeyPress(final KeyboardKey key)
	{
		if (key.equals(KeyboardKey.BACKSPACE) && !aNameInput.getText().equals("")) {
			aNameInput.setText(aNameInput.getText().substring(0, aNameInput.getText().length() - 1));
		}
		else if (key.equals(KeyboardKey.ENTER)) {
			System.out.print("print");
			aStaticName.setText(aNameInput.getText());
			EverVoidClient.getSettings().setNickname(aNameInput.getText());
		}
		else {
			aNameInput.setText(aNameInput.getText() + key.getLetter());
		}
	}
}
