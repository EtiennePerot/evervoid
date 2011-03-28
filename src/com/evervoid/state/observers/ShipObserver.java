package com.evervoid.state.observers;

import com.evervoid.state.EVContainer;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.ShipPath;

public interface ShipObserver
{
	public void shipBombed(Ship ship, GridLocation bombLocation);

	public void shipDestroyed(Ship ship);

	public void shipHealthChanged(Ship ship, int health);

	public void shipJumped(Ship ship, EVContainer<Prop> oldContainer, ShipPath leavingMove, EVContainer<Prop> newContainer,
			Portal portal);

	public void shipMoved(Ship ship, GridLocation oldLocation, ShipPath path);

	public void shipShot(Ship ship, GridLocation shootLocation);
}
