package com.evervoid.client.graphics.geometry;

import com.jme3.math.FastMath;

/**
 * Animations can use different kinds of smoothing effects.
 */
public enum Smoothing
{
	/**
	 * Simple, linear animation.
	 */
	LINEAR,
	/**
	 * Root-based animation: Starts fast, ends slow
	 */
	ROOT,
	/**
	 * Sine-based animation: Starts slow, gets fast in the middle, and ends slow. Generally the most natural-looking type of
	 * animation.
	 */
	SINE,
	/**
	 * Square-based animation: Starts slow, ends fast
	 */
	SQUARE;
	/**
	 * The derivative of the smoothing function at a particular point. Can be used to determine the current animation speed.
	 * 
	 * @param x
	 *            The point on the time scale
	 * @return The derivative of the smoothing curve at the given point
	 */
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

	/**
	 * Computes the current smoothed progress, given a certain time value
	 * 
	 * @param x
	 *            The point on the time scale of the animation, from 0 to 1
	 * @return The smoothed progress of the animation
	 */
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