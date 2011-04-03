package com.evervoid.client.views.game;

import java.io.File;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.FilePicker;
import com.evervoid.client.ui.FilePicker.FilePickerMode;
import com.evervoid.client.ui.FilePickerListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.EverUIView;
import com.jme3.math.Vector2f;

class PauseMenuView extends EverUIView implements ButtonListener, FilePickerListener
{
	private final ButtonControl aLeaveButton;
	private final PanelControl aPanelControl;
	private final ButtonControl aQuitButton;
	private final ButtonControl aResumeButton;
	private final ButtonControl aSaveButton;

	PauseMenuView()
	{
		super(new CenteredControl(new UIControl()));
		aPanelControl = new PanelControl("Pause");
		aResumeButton = new ButtonControl("Resume game");
		aResumeButton.addButtonListener(this);
		aPanelControl.addUI(aResumeButton);
		aPanelControl.addSpacer(1, 8);
		aSaveButton = new ButtonControl("Save game");
		aSaveButton.addButtonListener(this);
		aPanelControl.addUI(aSaveButton);
		aPanelControl.addSpacer(1, 8);
		aLeaveButton = new ButtonControl("Leave game");
		aLeaveButton.addButtonListener(this);
		aPanelControl.addUI(aLeaveButton);
		aPanelControl.addSpacer(1, 16);
		aQuitButton = new ButtonControl("Quit everVoid");
		aQuitButton.addButtonListener(this);
		aPanelControl.addUI(aQuitButton);
		addUI(aPanelControl);
		setDisplayed(false); // Hidden by default
		setDisplayDuration(0.5);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aResumeButton)) {
			setDisplayed(false);
		}
		else if (button.equals(aSaveButton)) {
			final FilePicker picker = new FilePicker(FilePickerMode.SAVE);
			picker.registerListener(this);
			pushUI(picker);
		}
		else if (button.equals(aLeaveButton)) {
			GameView.leave();
		}
		else if (button.equals(aQuitButton)) {
			EverVoidClient.quit();
		}
	}

	@Override
	public void filePicked(final FilePicker picker, final FilePickerMode mode, final File file)
	{
		deleteUI();
		GameView.save(file);
		setDisplayed(false);
	}

	@Override
	public void filePickerCancelled(final FilePicker picker, final FilePickerMode mode)
	{
		deleteUI();
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (key.equals(KeyboardKey.ESCAPE)) {
			toggleVisible();
			return true;
		}
		return super.onKeyPress(key, tpf);
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (!isDisplayed()) {
			return false;
		}
		super.onLeftClick(position, tpf);
		if (getNumOfUIs() == 1 && !aPanelControl.getAbsoluteComputedBounds().contains(position.x, position.y)) {
			// If user clicks outside while the pause menu is displayed, close the pause menu
			toggleVisible();
		}
		return true;
	}

	public void toggleVisible()
	{
		setDisplayed(!isDisplayed());
	}
}
