package com.evervoid.client.graphics.geometry;

import com.evervoid.client.EverNode;

public class AnimatedScaling extends AnimatedTransform
{
	private float aOriginScale = 1f;
	private float aTargetScale = 1f;

	public AnimatedScaling(final EverNode node)
	{
		super(node);
	}

	@Override
	protected void getReady()
	{
		aOriginScale = aScale;
	}

	public float getTargetScale()
	{
		return aTargetScale;
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
		return setTargetScale(aScale * multfactor);
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
		return setTargetScale(aTargetScale * multfactor);
	}

	@Override
	public void reset()
	{
		aOriginScale = 1f;
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
		aTargetScale = Math.max(0, scale);
		return this;
	}

	@Override
	protected void step(final float progress, final float antiProgress)
	{
		setScale(aOriginScale * antiProgress + aTargetScale * progress);
	}

	@Override
	public String toString()
	{
		return super.toString() + "; Scaling from " + aOriginScale + " to " + aTargetScale;
	}
}
