package com.evervoid.client.views.lobby;

import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.ui.CheckboxControl;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.MarginSpacer;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.state.data.GameData;
import com.jme3.math.ColorRGBA;

public class LobbyPlayerEntry extends MarginSpacer
{
	private static String sPlayerFontName = "redensek";
	private static int sPlayerFontSize = 24;
	private static ColorRGBA sPlayerNameColor = new ColorRGBA(0.75f, 0.75f, 0.75f, 1f);

	static String getRowBorderSprite(final boolean isFirst, final boolean isLast, final boolean left)
	{
		final String side = left ? "left" : "right";
		if (isFirst) {
			if (isLast) {
				return "ui/metalbox/" + side + "_round_20.png";
			}
			return "ui/metalbox/" + side + "_round_square_20.png";
		}
		if (isLast) {
			return "ui/metalbox/" + side + "_square_round_20.png";
		}
		return "ui/metalbox/" + side + "_square_20.png";
	}

	private final AnimatedAlpha aAlphaAnimation;
	private final StaticTextControl aPlayerName;
	private final ImageControl aRaceIcon;
	private final StaticTextControl aRaceName;
	private final CheckboxControl aReadyCheckbox;
	private final LobbyPlayerRowControl aRow;

	LobbyPlayerEntry()
	{
		super(4, 4, 0, 0, new LobbyPlayerRowControl());
		aRow = (LobbyPlayerRowControl) aContained;
		aAlphaAnimation = getNewAlphaAnimation();
		aPlayerName = new StaticTextControl("", sPlayerNameColor, sPlayerFontName, sPlayerFontSize);
		aReadyCheckbox = new CheckboxControl("icons/icon_ready.png", "icons/icon_ready_not.png");
		addUI(aReadyCheckbox);
		addUI(aPlayerName);
		addFlexSpacer(1);
		aRaceIcon = new ImageControl();
		aRaceName = new StaticTextControl("", ColorRGBA.Black, "redensek", 24);
		addUI(aRaceIcon);
		addUI(aRaceName);
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

	void updateEntry(final LobbyPlayer player, final GameData gamedata, final boolean isFirst, final boolean isLast)
	{
		aRow.setLeftSprite(getRowBorderSprite(isFirst, isLast, true));
		aRow.setRightSprite(getRowBorderSprite(isFirst, isLast, false));
		aPlayerName.setText(player.getNickname(), false);
		aPlayerName.setColor(GraphicsUtils.getColorRGBA(player.getColor()));
		aRaceIcon.setSprite("icons/races/" + player.getRace() + "/medium.png");
		aRaceName.setText(gamedata.getRaceData(player.getRace()).getTitle(), false);
	}
}
