package com.evervoid.client.graphics;

import com.evervoid.client.EverNode;
import com.evervoid.client.views.solar.SolarSystemGrid;

public class ShipTrailManager extends EverNode
{
	public ShipTrailManager(final SolarSystemGrid grid)
	{
		grid.addNode(this);
	}

	public void addTrail(final UIShipTrail trail)
	{
		addNode(trail);
	}

	public void delTrail(final UIShipTrail trail)
	{
		delNode(trail);
	}
}
