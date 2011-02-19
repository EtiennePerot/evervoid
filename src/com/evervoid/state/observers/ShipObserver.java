package com.evervoid.state.observers;

import java.util.List;

import com.evervoid.state.EVContainer;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Ship;

public interface ShipObserver
{
	public void shipBombed(GridLocation bombLocation);

	public void shipDestroyed(Ship ship);

	public void shipJumped(EVContainer<Ship> newContainer);

	public void shipMoved(Ship ship, GridLocation oldLocation, List<GridLocation> path);

	public void shipShot(GridLocation shootLocation);

	public void shipTookDamage(int damageAmount);
}
