package com.evervoid.client.views.solar;

import com.evervoid.client.EverNode;

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
