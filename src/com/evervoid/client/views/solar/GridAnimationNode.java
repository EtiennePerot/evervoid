package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.EverNode;

public class GridAnimationNode extends EverNode
{
	public GridAnimationNode(final SolarGrid grid)
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
