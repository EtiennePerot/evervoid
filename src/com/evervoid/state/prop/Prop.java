package com.evervoid.state.prop;

import java.awt.Dimension;

import com.evervoid.state.player.Player;
import com.evervoid.state.solar.Point;

public abstract class Prop
{
	private final Dimension aDimmension;
	private final Player aOwner;
	private Point aPoint;

	protected Prop(final Player player, final Point point)
	{
		this(player, point, new Dimension(1, 1));
	}

	protected Prop(final Player player, final Point point, final Dimension dimension)
	{
		aOwner = player;
		aPoint = point;
		aDimmension = dimension;
	}

	public Dimension getDimension()
	{
		return aDimmension;
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
