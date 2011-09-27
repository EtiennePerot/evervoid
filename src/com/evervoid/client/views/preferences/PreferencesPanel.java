package com.evervoid.client.views.preferences;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.sound.EVSoundEngine;
import com.evervoid.client.sound.Sfx;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CheckboxControl;
import com.evervoid.client.ui.CheckboxListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.client.ui.TextInputListener;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

public class PreferencesPanel extends BoxControl implements ButtonListener, TextInputListener, CheckboxListener
{
	private static final int sButtonSpacing = 20;
	public static final int sMaxNicknameLength = 32;
	private final CheckboxControl aBGMCheckbox;
	private final ButtonControl aMainMenuButton;
	private final TextInputControl aNameInput;
	private final ButtonControl aSaveButton;
	private final CheckboxControl aSfxCheckbox;
	private final PanelControl aSoundPanel;
	private final StaticTextControl aStaticName;

	public PreferencesPanel()
	{
		super(BoxDirection.VERTICAL);
		aStaticName = new StaticTextControl(EverVoidClient.getSettings().getPlayerNickname(), ColorRGBA.Red);
		addUI(aStaticName);
		aNameInput = new TextInputControl(sMaxNicknameLength);
		aNameInput.setDesiredDimension(new Dimension(2, 2));
		aNameInput.setText(EverVoidClient.getSettings().getPlayerNickname());
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
		aBGMCheckbox = new CheckboxControl();
		aBGMCheckbox.addListener(this);
		background.addUI(aBGMCheckbox);
		aBGMCheckbox.setChecked(EverVoidClient.getSettings().shouldPlayMusic());
		aSoundPanel.addUI(background);
		final UIControl sfx = new UIControl(BoxDirection.HORIZONTAL);
		sfx.addUI(new StaticTextControl("Sound Effects", ColorRGBA.White));
		sfx.addFlexSpacer(1);
		aSfxCheckbox = new CheckboxControl();
		aSfxCheckbox.addListener(this);
		aSfxCheckbox.setChecked(EverVoidClient.getSettings().shouldPlaySfx());
		sfx.addUI(aSfxCheckbox);
		aSoundPanel.addUI(sfx);
		// the main menu button
		aMainMenuButton.addButtonListener(this);
		addUI(aMainMenuButton);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aMainMenuButton)) {
			EVSoundEngine.playEffect(Sfx.SOUND_EFFECT.BEEP_BACK);
			EVViewManager.switchTo(ViewType.MAINMENU);
		}
		else if (button.equals(aNameInput)) {
			aNameInput.onClick();
		}
		else if (button.equals(aSaveButton)) {
			EVSoundEngine.playEffect(Sfx.SOUND_EFFECT.DOUBLE_BEEP);
			save();
		}
	}

	@Override
	public void checkboxChecked(final CheckboxControl checkbox, final boolean checked)
	{
		if (checkbox.equals(aSfxCheckbox)) {
			EverVoidClient.getSettings().setShouldPlaySfx(checked);
			EverVoidClient.getSettings().writeToDisk();
		}
		else if (checkbox.equals(aBGMCheckbox)) {
			if (checked == false) {
				// stop current song
				EVSoundEngine.stopSound();
			}
			EverVoidClient.getSettings().setShouldPlayMusic(checked);
			EverVoidClient.getSettings().writeToDisk();
		}
		EverVoidClient.getSettings().writeToDisk();
	}

	@Override
	public void onTextInputDefocus(final TextInputControl control)
	{
		// Nothing
	}

	@Override
	public void onTextInputFocus(final TextInputControl control)
	{
		// Nothing
	}

	@Override
	public void onTextInputKey(final TextInputControl control, final KeyboardKey key)
	{
		if (key.equals(KeyboardKey.ENTER)) {
			save();
		}
		if (aNameInput.getText().equals("") || aNameInput.getText().equals(EverVoidClient.getSettings().getPlayerNickname())) {
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
			EverVoidClient.getSettings().setPlayerNickname(aNameInput.getText());
			// write settings to preferences document
			EverVoidClient.getSettings().writeToDisk();
		}
	}
}
