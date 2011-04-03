package com.evervoid.client.views.preferences;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CheckboxControl;
import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.client.ui.TextInputListener;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

public class PreferencePanel extends BoxControl implements ButtonListener, TextInputListener, ClickObserver
{
	private static int sButtonSpacing = 20;
	private final ButtonControl aMainMenuButton;
	private final TextInputControl aNameInput;
	private final ButtonControl aSaveButton;
	private final PanelControl aSoundPanel;
	private final StaticTextControl aStaticName;
	private final CheckboxControl backCheck;
	private final CheckboxControl sfxCheck;

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
		aNameInput.onClick();
		addSpacer(1, sButtonSpacing);
		aSaveButton = new ButtonControl("Save");
		aSaveButton.addButtonListener(this);
		aSaveButton.disable();
		addUI(aSaveButton);
		addSpacer(1, sButtonSpacing * 2);
		// create the sound panel
		aSoundPanel = new PanelControl("Sounds");
		addUI(aSoundPanel);
		addSpacer(1, sButtonSpacing * 2);
		aMainMenuButton = new ButtonControl("Main Menu");
		final UIControl background = new UIControl(BoxDirection.HORIZONTAL);
		background.addUI(new StaticTextControl("Background Music", ColorRGBA.White));
		background.addFlexSpacer(1);
		backCheck = new CheckboxControl();
		backCheck.registerClickObserver(this);
		background.addUI(backCheck);
		backCheck.setChecked(EverVoidClient.getSettings().getSound());
		aSoundPanel.addUI(background);
		final UIControl sfx = new UIControl(BoxDirection.HORIZONTAL);
		sfx.addUI(new StaticTextControl("Sound Effects", ColorRGBA.White));
		sfx.addFlexSpacer(1);
		sfxCheck = new CheckboxControl();
		sfxCheck.registerClickObserver(this);
		sfxCheck.setChecked(EverVoidClient.getSettings().getSfx());
		sfx.addUI(sfxCheck);
		aSoundPanel.addUI(sfx);
		// the main menu button
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
			save();
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
		if (key.equals(KeyboardKey.ENTER)) {
			save();
		}
		if (aNameInput.getText().equals("") || aNameInput.getText().equals(EverVoidClient.getSettings().getNickname())) {
			aSaveButton.disable();
		}
		else {
			aSaveButton.enable();
		}
	}

	private void save()
	{
		if (aSaveButton.isEnabled()) {
			// set name in UI
			aStaticName.setText(aNameInput.getText());
			// change Save button alpha
			aSaveButton.disable();
			// change nick in settings
			EverVoidClient.getSettings().setNickname(aNameInput.getText());
			// write settings to prefrences document
			EverVoidClient.getSettings().writeSettings();
		}
	}

	@Override
	public void uiClicked(final UIControl clicked)
	{
		if (clicked.equals(sfxCheck)) {
			// TODO - toggle sfx
			EverVoidClient.getSettings().toggleSfx();
			EverVoidClient.getSettings().writeSettings();
		}
		else if (clicked.equals(backCheck)) {
			// TODO - toggle sound
			EverVoidClient.getSettings().toggleSound();
			EverVoidClient.getSettings().writeSettings();
		}
	}
}
