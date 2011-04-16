package com.evervoid.client.showroom;

import com.evervoid.client.ui.PanelControl;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;

public class ShowRoomPlayground extends PanelControl
{
	public ShowRoomPlayground(final RaceData race, final ShipData ship)
	{
		super("Showroom: " + ship.getTitle());
	}
}
