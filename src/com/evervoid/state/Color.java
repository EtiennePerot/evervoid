package com.evervoid.state;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.math.FastMath;

public class Color implements Jsonable
{
	public static Color random()
	{
		return new Color(FastMath.rand.nextFloat(), FastMath.rand.nextFloat(), FastMath.rand.nextFloat(), 1, "random["
				+ FastMath.rand.nextDouble() + "]");
	}

	public final float alpha;
	public final float blue;
	public final float green;
	public final String name;
	public final float red;

	public Color(final float red, final float green, final float blue, final float alpha, final String colorName)
	{
		this.blue = blue;
		this.green = green;
		this.red = red;
		this.alpha = alpha;
		name = colorName;
	}

	public Color(final Json j)
	{
		this(null, j);
	}

	public Color(final String colorName, final Json j)
	{
		red = j.getFloatAttribute("red");
		green = j.getFloatAttribute("green");
		blue = j.getFloatAttribute("blue");
		alpha = j.getFloatAttribute("alpha");
		if (j.hasAttribute("name")) {
			name = j.getStringAttribute("name");
		}
		else {
			name = colorName;
		}
	}

	@Override
	public Json toJson()
	{
		return new Json().setFloatAttribute("red", red).setFloatAttribute("green", green).setFloatAttribute("blue", blue)
				.setFloatAttribute("alpha", alpha).setStringAttribute("name", name);
	}

	@Override
	public String toString()
	{
		return "Color " + name + " of rgba(" + red + ", + " + green + ", " + blue + ", " + alpha + ")";
	}
}
