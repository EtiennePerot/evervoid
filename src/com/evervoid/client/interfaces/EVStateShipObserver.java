package com.evervoid.client.interfaces;

import com.evervoid.state.geometry.GridLocation;

public interface EVStateShipObserver
{
	public void shipBombed(GridLocation bombLocation);

	public void shipDestroyed();

	public void shipJumped();

	public void shipMoved(GridLocation toLocation);

	public void shipShot(GridLocation shootLocation);

	public void shipTookDamage(int damageAmount);
}
