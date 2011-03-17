package com.evervoid.client.views.preferences;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.client.ui.TextInputListener;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

public class PreferencePanel extends BoxControl implements ButtonListener, TextInputListener
{
	private static int sButtonSpacing = 20;
	private final ButtonControl aMainMenuButton;
	private final TextInputControl aNameInput;
	private final ButtonControl aSaveButton;
	private final AnimatedAlpha aSaveButtonAlpha;
	private final StaticTextControl aStaticName;

	public PreferencePanel()
	{
		super(BoxDirection.VERTICAL);
		aStaticName = new StaticTextControl(EverVoidClient.getSettings().getNickname(), ColorRGBA.Red);
		addUI(aStaticName);
		aNameInput = new TextInputControl(20);
		aNameInput.setDesiredDimension(new Dimension(2, 2));
		aNameInput.setText(EverVoidClient.getSettings().getNickname());
		aNameInput.addTextInputListener(this);
		addUI(aNameInput);
		addSpacer(1, sButtonSpacing);
		aSaveButton = new ButtonControl("Save");
		aSaveButton.addButtonListener(this);
		aSaveButtonAlpha = aSaveButton.getNewAlphaAnimation();
		aSaveButtonAlpha.setDuration(0.25f).setAlpha(.5f);
		addUI(aSaveButton);
		addSpacer(1, sButtonSpacing * 2);
		aMainMenuButton = new ButtonControl("Main Menu");
		aMainMenuButton.addButtonListener(this);
		addUI(aMainMenuButton);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aMainMenuButton)) {
			EVViewManager.switchTo(ViewType.MAINMENU);
		}
		else if (button.equals(aNameInput)) {
			aNameInput.onClick();
		}
		else if (button.equals(aSaveButton)) {
			EverVoidClient.getSettings().writeSettings();
			// TODO - reset save button alpha
		}
	}

	@Override
	public void onTextInputDefocus(final TextInputControl control)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextInputFocus(final TextInputControl control)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextInputKey(final TextInputControl control, final KeyboardKey key)
	{
		if (key.equals(KeyboardKey.ENTER) && !aNameInput.equals("")) {
			aStaticName.setText(aNameInput.getText());
			EverVoidClient.getSettings().setNickname(aNameInput.getText());
			aSaveButtonAlpha.setTargetAlpha(1).start();
		}
	}
}
