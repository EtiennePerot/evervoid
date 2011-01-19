package com.evervoid.state.player;

import com.jme3.math.FastMath;

public class PlayerColor
{
	public static PlayerColor random()
	{
		return new PlayerColor(FastMath.rand.nextFloat(), FastMath.rand.nextFloat(), FastMath.rand.nextFloat(), 1);
	}

	public float alpha;
	public float blue;
	public float green;
	public float red;

	public PlayerColor(final float red, final float green, final float blue, final float alpha)
	{
		this.blue = blue;
		this.green = green;
		this.red = red;
		this.alpha = alpha;
	}
}
