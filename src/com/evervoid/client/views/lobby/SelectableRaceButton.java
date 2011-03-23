package com.evervoid.client.views.lobby;

import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIInputListener;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.jme3.math.ColorRGBA;

public class SelectableRaceButton extends UIControl implements UIInputListener
{
	private final RaceSelectionControl aListener;
	private final String aRace;
	private final ImageControl aRaceIcon;
	private final StaticTextControl aRaceTitle;
	private final ColorRGBA sRaceNormalColor = new ColorRGBA(0.5f, 0.5f, 0.6f, 1f);
	private final ColorRGBA sRaceSelectedColor = new ColorRGBA(0.9f, 0.9f, 0.975f, 1f);

	public SelectableRaceButton(final String race, final String racetitle, final RaceSelectionControl listener)
	{
		super(BoxDirection.HORIZONTAL);
		aRace = race;
		aListener = listener;
		aRaceIcon = new ImageControl("icons/races/" + aRace + "/medium_black.png");
		addUI(new VerticalCenteredControl(aRaceIcon));
		addSpacer(10, 1);
		aRaceTitle = new StaticTextControl(racetitle, sRaceNormalColor, "redensek", 22);
		addUI(new VerticalCenteredControl(aRaceTitle));
	}

	@Override
	public void onClick()
	{
		if (!isEnabled()) {
			return;
		}
		aListener.setRace(aRace, true);
	}

	@Override
	public void onDefocus()
	{
		setFocusedNode(null);
	}

	void setActive(final boolean active)
	{
		if (active) {
			aRaceIcon.setSprite("icons/races/" + aRace + "/medium.png");
			aRaceTitle.setColor(sRaceSelectedColor);
		}
		else {
			aRaceIcon.setSprite("icons/races/" + aRace + "/medium_black.png");
			aRaceTitle.setColor(sRaceNormalColor);
		}
	}
}
