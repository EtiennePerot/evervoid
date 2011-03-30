package com.evervoid.client.views.game;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.chat.ChatControl;
import com.evervoid.client.views.EverUIView;
import com.evervoid.state.Color;
import com.evervoid.state.geometry.Dimension;

public class InGameChatView extends EverUIView
{
	private static final float sChatCloseTimeout = 4;
	static final Dimension sChatDimension = new Dimension(512, 384);
	private final ChatControl aChatControl;
	private boolean aChatControlFocused = false;
	private boolean aChatControlOpen = false;
	private final FrameTimer aChatTimeout;

	public InGameChatView()
	{
		super(new UIControl());
		aChatControl = new ChatControl("Chat", false);
		getNewTransform().translate(0, 0, 10000); // Bring to front
		addUI(aChatControl, 1);
		setDisplayed(false); // Hidden by default
		setDisplayDuration(0.5);
		setDisplayMaxAlpha(0.7);
		aChatTimeout = new FrameTimer(new Runnable()
		{
			@Override
			public void run()
			{
				closeChat();
			}
		}, sChatCloseTimeout, 1);
	}

	private void closeChat()
	{
		aChatControl.defocus();
		aChatControlOpen = false;
		aChatControlFocused = false;
		setDisplayed(false);
	}

	public boolean isOpen()
	{
		return aChatControlOpen;
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (!aChatControlOpen && key.equals(KeyboardKey.T)) {
			openChat(true);
			return true;
		}
		if (aChatControlOpen && key.equals(KeyboardKey.ESCAPE)) {
			closeChat();
			return true;
		}
		if (aChatControlFocused) {
			aChatControl.onKeyPress(key);
			return true;
		}
		return super.onKeyPress(key, tpf);
	}

	private void openChat(final boolean focused)
	{
		if (!aChatControlOpen) {
			setDisplayed(true);
			aChatControlOpen = true;
		}
		aChatControlFocused = focused;
		if (aChatControlFocused) {
			aChatTimeout.stop();
			aChatControl.focus();
			aChatControl.setTitle("Chat");
		}
		else {
			aChatControl.setTitle("Press 'T' to focus");
			aChatTimeout.start();
		}
	}

	public void receivedChat(final String player, final Color playerColor, final String message)
	{
		aChatControl.messageReceived(player, GraphicsUtils.getColorRGBA(playerColor), message);
		if (!aChatControlOpen) {
			openChat(false);
		}
	}
}
