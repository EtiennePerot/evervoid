package com.evervoid.state.player;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.math.FastMath;

public class PlayerColor implements Jsonable
{
	public static PlayerColor fromJson(final Json j)
	{
		return new PlayerColor(j.getFloatAttribute("red"), j.getFloatAttribute("green"), j.getFloatAttribute("blue"),
				j.getFloatAttribute("alpha"));
	}

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

	@Override
	public Json toJson()
	{
		return new Json().setFloatAttribute("red", red).setFloatAttribute("green", green).setFloatAttribute("blue", blue)
				.setFloatAttribute("alpha", alpha);
	}
}
