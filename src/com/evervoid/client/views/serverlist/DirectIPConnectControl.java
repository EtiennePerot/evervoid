package com.evervoid.client.views.serverlist;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.sound.EVSoundEngine;
import com.evervoid.client.sound.Sfx;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.client.ui.TextInputListener;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

public class DirectIPConnectControl extends PanelControl implements TextInputListener, ButtonListener
{
	private final TextInputControl aIPTextBox;

	public DirectIPConnectControl()
	{
		super("Direct IP connection");
		final UIControl ipBox = new UIControl(BoxDirection.HORIZONTAL);
		ipBox.addString("IP: ", new ColorRGBA(0.8f, 0.8f, 0.8f, 1), "bitvoid", 20, BoxDirection.VERTICAL);
		ipBox.addSpacer(8, 1);
		aIPTextBox = new TextInputControl(64);
		aIPTextBox.addTextInputListener(this);
		aIPTextBox.setDesiredDimension(new Dimension(256, 16));
		ipBox.addUI(new VerticalCenteredControl(aIPTextBox), 1);
		ipBox.addSpacer(8, 1);
		final ButtonControl connectButton = new ButtonControl("Connect");
		connectButton.addButtonListener(this);
		ipBox.addUI(new VerticalCenteredControl(connectButton));
		addUI(ipBox);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		EVSoundEngine.playEffect(Sfx.SOUND_EFFECT.BEEP);
		connect();
	}

	private void connect()
	{
		final String ip = aIPTextBox.getText();
		if (ip.isEmpty()) {
			aIPTextBox.onClick();
		}
		else {
			// TODO: Add error checking if it's a bad IP
			EVClientEngine.connect(ip);
		}
	}

	@Override
	public void onTextInputDefocus(final TextInputControl control)
	{
		// Do nothing
	}

	@Override
	public void onTextInputFocus(final TextInputControl control)
	{
		// Do nothing
	}

	@Override
	public void onTextInputKey(final TextInputControl control, final KeyboardKey key)
	{
		if (key.equals(KeyboardKey.ENTER)) {
			connect();
		}
	}
}
