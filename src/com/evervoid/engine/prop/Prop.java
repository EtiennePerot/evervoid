package com.evervoid.engine.prop;

import com.evervoid.engine.map.Point;
import com.evervoid.engine.player.Player;

public abstract class Prop
{
	private Point aPoint;
	private final Player owner;

	protected Prop(final Player aPlayer, final Point point)
	{
		owner = aPlayer;
		aPoint = point;
	}

	public Point getLocation()
	{
		return aPoint;
	}

	void move(final Point point)
	{
		aPoint = point;
	}
}
