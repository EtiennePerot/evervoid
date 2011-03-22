package com.evervoid.client.views.game;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.jme3.math.Vector2f;

class PauseMenuView extends EverUIView implements ButtonListener
{
	private final ButtonControl aLeaveButton;
	private final PanelControl aPanelControl;
	private final ButtonControl aResumeButton;

	PauseMenuView()
	{
		super(new CenteredControl(new UIControl()));
		aPanelControl = new PanelControl("Pause");
		aResumeButton = new ButtonControl("Resume game");
		aResumeButton.addButtonListener(this);
		aPanelControl.addUI(aResumeButton);
		aPanelControl.addSpacer(1, 8);
		aLeaveButton = new ButtonControl("Leave game");
		aLeaveButton.addButtonListener(this);
		aPanelControl.addUI(aLeaveButton);
		addUI(aPanelControl);
		setBounds(Bounds.getWholeScreenBounds());
		setDisplayed(false); // Hidden by default
		setDisplayDuration(0.5);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aResumeButton)) {
			setDisplayed(false);
		}
		else if (button.equals(aLeaveButton)) {
			GameView.leave();
		}
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
		if (!aPanelControl.getAbsoluteComputedBounds().contains(position.x, position.y)) {
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
