package com.evervoid.client.graphics.geometry;

import com.evervoid.client.graphics.EverNode;

/**
 * An animation over the alpha transparency of an {@link EverNode}.
 */
public class AnimatedAlpha extends AnimatedTransform
{
	/**
	 * Alpha value prior to starting the animation
	 */
	private float aOriginAlpha = 1f;
	/**
	 * Target alpha value at the end of the animation
	 */
	private float aTargetAlpha = 1f;

	/**
	 * Constructor; DO NOT USE THIS, use {@link EverNode}'s getNewAlphaAnimation instead.
	 * 
	 * @param node
	 *            The {@link EverNode} that this animation will affect.
	 */
	public AnimatedAlpha(final EverNode node)
	{
		super(node);
	}

	@Override
	protected void getReady()
	{
		aOriginAlpha = aAlpha;
	}

	/**
	 * @return The target alpha value that will be reached at the end of the animation
	 */
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
	 * @param alpha
	 *            The alpha to reach
	 * @return This
	 */
	public AnimatedAlpha setTargetAlpha(final double alpha)
	{
		return setTargetAlpha((float) alpha);
	}

	/**
	 * Set the target alpha of this animation
	 * 
	 * @param alpha
	 *            The alpha to reach
	 * @return This
	 */
	public AnimatedAlpha setTargetAlpha(final float alpha)
	{
		aTargetAlpha = Math.max(0, alpha);
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
