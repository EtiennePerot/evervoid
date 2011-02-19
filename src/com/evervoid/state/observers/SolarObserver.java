package com.evervoid.state.observers;

import com.evervoid.state.prop.Ship;

public interface SolarObserver
{
	public void shipEntered(Ship newShip);

	public void shipLeft(Ship oldShip);
}
