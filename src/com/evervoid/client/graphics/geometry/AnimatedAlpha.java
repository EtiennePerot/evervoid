package com.evervoid.client.graphics.geometry;

import com.evervoid.client.EverNode;

public class AnimatedAlpha extends AnimatedTransform
{
	private float aOriginAlpha = 1f;
	private float aTargetAlpha = 1f;

	public AnimatedAlpha(final EverNode node)
	{
		super(node);
	}

	@Override
	protected void getReady()
	{
		aOriginAlpha = aAlpha;
	}

	public float getTargetAlpha()
	{
		return aTargetAlpha;
	}

	/**
	 * Sets the target alpha of this animation's as the *current* alpha multiplied by the specified factor
	 * 
	 * @param multfactor
	 *            The factor to multiply the current alpha by
	 * @return This
	 */
	public AnimatedAlpha multCurrent(final float multfactor)
	{
		return setTargetAlpha(aAlpha * multfactor);
	}

	/**
	 * Multiplies this animation's target alpha by the specified factor
	 * 
	 * @param multfactor
	 *            The factor to multiply the target alpha by
	 * @return This
	 */
	public AnimatedAlpha multTarget(final float multfactor)
	{
		return setTargetAlpha(aTargetAlpha * multfactor);
	}

	@Override
	public void reset()
	{
		aOriginAlpha = 1f;
		setAlpha(1f);
	}

	/**
	 * Set the target alpha of this animation
	 * 
	 * @param scale
	 *            The alpha to reach
	 * @return This
	 */
	public AnimatedAlpha setTargetAlpha(final float scale)
	{
		aTargetAlpha = Math.max(0, scale);
		return this;
	}

	@Override
	protected void step(final float progress, final float antiProgress)
	{
		setAlpha(aOriginAlpha * antiProgress + aTargetAlpha * progress);
	}

	@Override
	public String toString()
	{
		return super.toString() + "; Alpha from " + aOriginAlpha + " to " + aTargetAlpha;
	}
}
