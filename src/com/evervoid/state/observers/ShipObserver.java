package com.evervoid.state.observers;

import com.evervoid.state.EVContainer;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Ship;

public interface ShipObserver
{
	public void shipBombed(GridLocation bombLocation);

	public void shipDestroyed();

	public void shipJumped(EVContainer<Ship> newContainer);

	public void shipMoved(GridLocation newLocation);

	public void shipShot(GridLocation shootLocation);

	public void shipTookDamage(int damageAmount);
}
