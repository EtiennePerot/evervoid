package com.evervoid.state.observers;

import java.util.List;

import com.evervoid.state.EVContainer;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;

public interface ShipObserver
{
	public void shipBombed(Ship ship, GridLocation bombLocation);

	public void shipDestroyed(Ship ship);

	public void shipJumped(Ship ship, EVContainer<Prop> oldContainer, List<GridLocation> leavingMove,
			EVContainer<Prop> newContainer, Portal portal);

	public void shipMoved(Ship ship, GridLocation oldLocation, List<GridLocation> path);

	public void shipShot(Ship ship, GridLocation shootLocation);

	public void shipTookDamage(Ship ship, int damageAmount);
}
