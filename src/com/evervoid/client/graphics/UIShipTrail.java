package com.evervoid.client.graphics;

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
