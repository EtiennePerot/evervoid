package com.evervoid.client.views.lobby;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.client.ui.TextInputListener;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.preferences.PreferencesPanel;

public class PlayerRenamingDialog extends UIControl implements TextInputListener, ButtonListener
{
	private final TextInputControl aName;
	private final LobbyView aView;

	public PlayerRenamingDialog(final LobbyView view)
	{
		aView = view;
		final CenteredControl root = new CenteredControl(new UIControl(BoxDirection.VERTICAL));
		final PanelControl mainBox = new PanelControl("Change name");
		mainBox.addString("This server allows you to change your name.");
		mainBox.addString("Please enter a new name below:");
		final UIControl bottomRow = new UIControl(BoxDirection.HORIZONTAL);
		bottomRow.addString("Name: ");
		bottomRow.addSpacer(8, 1);
		aName = new TextInputControl(PreferencesPanel.sMaxNicknameLength);
		aName.setText(aView.getLocalPlayerName());
		aName.addTextInputListener(this);
		bottomRow.addUI(aName, 1);
		bottomRow.addSpacer(8, 1);
		final ButtonControl okButton = new ButtonControl("OK");
		okButton.addButtonListener(this);
		bottomRow.addUI(okButton);
		mainBox.addUI(bottomRow);
		root.addUI(mainBox);
		addUI(root, 1);
		aName.onClick();
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		confirm();
	}

	private void confirm()
	{
		aView.setPlayerName(aName.getText());
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
			confirm();
		}
	}
}
