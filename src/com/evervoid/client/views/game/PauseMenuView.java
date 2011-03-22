package com.evervoid.client.views.game;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;

class PauseMenuView extends EverUIView implements ButtonListener
{
	private final ButtonControl aLeaveButton;
	private final ButtonControl aResumeButton;

	PauseMenuView()
	{
		super(new CenteredControl(new UIControl()));
		final PanelControl mainpanel = new PanelControl("Pause");
		aResumeButton = new ButtonControl("Resume game");
		aResumeButton.addButtonListener(this);
		mainpanel.addUI(aResumeButton);
		mainpanel.addSpacer(1, 8);
		aLeaveButton = new ButtonControl("Leave game");
		aLeaveButton.addButtonListener(this);
		mainpanel.addUI(aLeaveButton);
		addUI(mainpanel);
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
			setDisplayed(!isDisplayed());
			return true;
		}
		return super.onKeyPress(key, tpf);
	}
}
