package client.graphics.geometry;

import client.EverNode;

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
	 * Multiplies this animation's target scale by the specified factor
	 * 
	 * @param multfactor
	 *            The factor to multiply the target scale by
	 * @return This
	 */
	public AnimatedTransform multTarget(final float multfactor)
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
	 * Multiplies this animation's current scale by the specified factor
	 * 
	 * @param multfactor
	 *            The factor to multiply the current scale by
	 * @return This
	 */
	public AnimatedTransform setTargetFactor(final float multfactor)
	{
		return setTargetScale(aScale * multfactor);
	}

	/**
	 * Set the target scale of this animation
	 * 
	 * @param scale
	 *            The scale to reach
	 * @return This
	 */
	public AnimatedTransform setTargetScale(final float scale)
	{
		aTargetScale = Math.max(0, scale);
		return this;
	}

	@Override
	protected void step(final float progress, final float antiProgress)
	{
		setScale(aOriginScale * antiProgress + aTargetScale * progress);
	}
}
