package com.evervoid.client.graphics.geometry;

import com.jme3.math.FastMath;

public enum Smoothing
{
	LINEAR, ROOT, SINE, SQUARE;
	public float derivative(final float x)
	{
		switch (this) {
			case LINEAR:
				return 1;
			case SQUARE:
				return 2 * x;
			case ROOT:
				return 1 / FastMath.sqrt(x);
			case SINE:
				return (FastMath.cos((float) ((x - 0.5) * FastMath.PI)));
		}
		return x;
	}

	public float smooth(final float x)
	{
		switch (this) {
			case LINEAR:
				return x;
			case SQUARE:
				return FastMath.sqr(x);
			case ROOT:
				return FastMath.sqrt(x);
			case SINE:
				return (FastMath.sin((float) ((x - 0.5) * FastMath.PI)) + 1.0f) / 2.0f;
		}
		return x;
	}
}
