package com.evervoid.state;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.math.FastMath;

public class Color implements Jsonable
{
	public static Color fromJson(final Json j)
	{
		return new Color(j.getFloatAttribute("red"), j.getFloatAttribute("green"), j.getFloatAttribute("blue"),
				j.getFloatAttribute("alpha"));
	}

	public static Color random()
	{
		return new Color(FastMath.rand.nextFloat(), FastMath.rand.nextFloat(), FastMath.rand.nextFloat(), 1);
	}

	public float alpha;
	public float blue;
	public float green;
	public float red;

	public Color(final float red, final float green, final float blue, final float alpha)
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

	@Override
	public String toString()
	{
		return "rgba(" + red + ", + " + green + ", " + blue + ", " + alpha + ")";
	}
}
