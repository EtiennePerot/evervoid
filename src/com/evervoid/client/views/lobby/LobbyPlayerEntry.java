package com.evervoid.client.views.lobby;

import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.MarginSpacer;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.server.LobbyPlayer;
import com.jme3.math.ColorRGBA;

public class LobbyPlayerEntry extends MarginSpacer
{
	private static String sPlayerFontName = "squarehead";
	private static int sPlayerFontSize = 24;
	private static ColorRGBA sPlayerNameColor = new ColorRGBA(0.75f, 0.75f, 0.75f, 1f);
	private final AnimatedAlpha aAlphaAnimation;
	private final StaticTextControl aPlayerName;

	LobbyPlayerEntry()
	{
		super(4, 4, 4, 4, new BoxControl());
		aAlphaAnimation = getNewAlphaAnimation();
		aPlayerName = new StaticTextControl("", sPlayerNameColor, sPlayerFontName, sPlayerFontSize);
		addUI(aPlayerName);
		aAlphaAnimation.setDuration(0.4f).setAlpha(0);
		aAlphaAnimation.setTargetAlpha(1).start();
	}

	void removeEntry()
	{
		aAlphaAnimation.setTargetAlpha(0).start(new Runnable()
		{
			@Override
			public void run()
			{
				deleteUI();
			}
		});
	}

	void updatePlayer(final LobbyPlayer player)
	{
		aPlayerName.setText(player.getNickname());
		aPlayerName.setColor(GraphicsUtils.getColorRGBA(player.getColor()));
	}
}
