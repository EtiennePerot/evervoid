package com.evervoid.client.graphics.geometry;

import com.evervoid.client.graphics.EverNode;
import com.jme3.math.Vector3f;

public class AnimatedScaling extends AnimatedTransform
{
	private final Vector3f aOriginScale = new Vector3f(1f, 1f, 1f);
	private final Vector3f aTargetScale = new Vector3f(1f, 1f, 1f);

	public AnimatedScaling(final EverNode node)
	{
		super(node);
	}

	@Override
	protected void getReady()
	{
		aOriginScale.set(aScale);
	}

	public Vector3f getTargetScale()
	{
		return aTargetScale;
	}

	public float getTargetScaleAverage()
	{
		return (aTargetScale.x + aTargetScale.y + aTargetScale.z) / 3.0f;
	}

	/**
	 * Sets the target scale of this animation's as the *current* scale multiplied by the specified factor
	 * 
	 * @param multfactor
	 *            The factor to multiply the current scale by
	 * @return This
	 */
	public AnimatedScaling multCurrent(final float multfactor)
	{
		return setTargetScale(aScale.mult(multfactor));
	}

	/**
	 * Multiplies this animation's *target* scale by the specified factor
	 * 
	 * @param multfactor
	 *            The factor to multiply the target scale by
	 * @return This
	 */
	public AnimatedScaling multTarget(final float multfactor)
	{
		return setTargetScale(aTargetScale.mult(multfactor));
	}

	@Override
	public void reset()
	{
		aOriginScale.set(1f, 1f, 1f);
		setScale(1f);
	}

	/**
	 * Set the target scale of this animation
	 * 
	 * @param scale
	 *            The scale to reach
	 * @return This
	 */
	public AnimatedScaling setTargetScale(final float scale)
	{
		return setTargetScale(scale, scale, scale);
	}

	public AnimatedScaling setTargetScale(final float xScale, final float yScale)
	{
		return setTargetScale(xScale, yScale, aScale.z);
	}

	public AnimatedScaling setTargetScale(final float xScale, final float yScale, final float zScale)
	{
		aTargetScale.set(Math.max(0, xScale), Math.max(0, yScale), Math.max(0, zScale));
		return this;
	}

	public AnimatedScaling setTargetScale(final Vector3f scale)
	{
		return setTargetScale(scale.x, scale.y, scale.z);
	}

	@Override
	protected void step(final float progress, final float antiProgress)
	{
		setScale(aOriginScale.mult(antiProgress).add(aTargetScale.mult(progress)));
	}

	@Override
	public String toString()
	{
		return super.toString() + "; Scaling from " + aOriginScale + " to " + aTargetScale;
	}
}
