package com.evervoid.state.prop;

import com.evervoid.gamedata.Dimension;
import com.evervoid.state.Point;
import com.evervoid.state.player.Player;

public abstract class Prop
{
	private final Dimension aDimension;
	protected final Player aPlayer;
	private Point aPoint;

	protected Prop(final Player player, final Point point)
	{
		this(player, point, new Dimension());
	}

	protected Prop(final Player player, final Point point, final Dimension dimension)
	{
		if (player != null) {
			aPlayer = player;
		}
		else {
			aPlayer = Player.getNullPlayer();
		}
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
