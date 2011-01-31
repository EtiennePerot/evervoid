package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.MultiSprite;

public abstract class UIShipTrail extends MultiSprite
{
	protected UIShip aShip;

	public UIShipTrail(final UIShip ship)
	{
		super();
		aShip = ship;
	}

	public void shipMove()
	{
	}

	public void shipMoveEnd()
	{
	}

	public void shipMoveStart()
	{
	}
}
