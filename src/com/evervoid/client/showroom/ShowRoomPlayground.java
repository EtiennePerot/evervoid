package com.evervoid.client.showroom;

import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;

public class ShowRoomPlayground extends PanelControl implements ButtonListener
{
	private final ButtonControl aCloseButton;
	private final ShowRoomPlaygroundArea aPlayground;
	private final ShowRoomView aView;

	public ShowRoomPlayground(final ShowRoomView view, final RaceData race, final ShipData ship)
	{
		super("Showroom: " + ship.getTitle());
		aView = view;
		aCloseButton = new ButtonControl("Close");
		aCloseButton.addButtonListener(this);
		getTitleBox().addUI(aCloseButton);
		aPlayground = new ShowRoomPlaygroundArea(race, ship);
		addUI(aPlayground, 1);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		aView.closePlayground();
	}
}
