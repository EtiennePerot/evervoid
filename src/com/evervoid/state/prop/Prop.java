package com.evervoid.state.prop;

import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;

public abstract class Prop
{
	protected GridLocation aLocation;
	protected final Player aPlayer;

	protected Prop(final Player player, final GridLocation location)
	{
		if (player != null) {
			aPlayer = player;
		}
		else {
			aPlayer = Player.getNullPlayer();
		}
		aLocation = location;
	}

	public GridLocation getLocation()
	{
		return aLocation;
	}

	void move(final GridLocation location)
	{
		aLocation = location;
	}
}
