package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.Colorable;
import com.evervoid.client.graphics.MultiSprite;

public abstract class UIShipTrail extends MultiSprite implements Colorable
{
	protected UIShip aShip;

	public UIShipTrail(final UIShip ship)
	{
		super();
		aShip = ship;
	}

	void shipMove()
	{
		// Overriden by subclasses
	}

	void shipMoveEnd()
	{
		// Overriden by subclasses
	}

	void shipMoveStart()
	{
		// Overriden by subclasses
	}
}
