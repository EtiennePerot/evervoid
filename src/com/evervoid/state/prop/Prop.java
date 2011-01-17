package com.evervoid.state.prop;

import com.evervoid.gamedata.Dimension;
import com.evervoid.state.player.Player;
import com.evervoid.state.solar.Point;

public abstract class Prop
{
	private final Dimension aDimension;
	private final Player aOwner;
	private Point aPoint;

	protected Prop(final Player player, final Point point)
	{
		this(player, point, new Dimension());
	}

	protected Prop(final Player player, final Point point, final Dimension dimension)
	{
		aOwner = player;
		aPoint = point;
		aDimension = dimension;
	}

	public Dimension getDimension()
	{
		return aDimension;
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
