package com.evervoid.client.graphics.geometry;

import com.evervoid.client.graphics.EverNode;
import com.jme3.math.Vector3f;

/**
 * Represents an animation of the scale of an {@link EverNode}.
 */
public class AnimatedScaling extends AnimatedTransform
{
	/**
	 * Scale prior to starting the animation
	 */
	private final Vector3f aOriginScale = new Vector3f(1f, 1f, 1f);
	/**
	 * Target scale to reach at the end of the animation
	 */
	private final Vector3f aTargetScale = new Vector3f(1f, 1f, 1f);

	/**
	 * Constructor; DO NOT USE THIS, use {@link EverNode}'s getNewScalingAnimation instead.
	 * 
	 * @param node
	 *            The {@link EverNode} that this animation will affect.
	 */
	public AnimatedScaling(final EverNode node)
	{
		super(node);
	}

	@Override
	protected void getReady()
	{
		aOriginScale.set(aScale);
	}

	/**
	 * @return The scale that will be reached at the end of the animation
	 */
	public Vector3f getTargetScale()
	{
		return aTargetScale;
	}

	/**
	 * @return The average scale between the x, y and z component of the target scale.
	 */
	public float getTargetScaleAverage()
	{
		return (aTargetScale.x + aTargetScale.y + aTargetScale.z) / 3.0f;
	}

	/**
	 * Sets the target scale of this animation's as the *current* scale multiplied by the specified factor
	 * 
	 * @param multfactor
	 *            The factor to multiply the current scale by
	 * @return This, for chainability
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
	 * @return This, for chainability
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
	 *            The scale to reach on all axes
	 * @return This, for chainability
	 */
	public AnimatedScaling setTargetScale(final float scale)
	{
		return setTargetScale(scale, scale, scale);
	}

	/**
	 * Set the target X and Y scale of this animation. The Z scale will be untouched.
	 * 
	 * @param xScale
	 *            The scale to reach on the X axis
	 * @param yScale
	 *            The scale to reach on the Y axis
	 * @return This, for chainability
	 */
	public AnimatedScaling setTargetScale(final float xScale, final float yScale)
	{
		return setTargetScale(xScale, yScale, aScale.z);
	}

	/**
	 * Set the target scale of this animation.
	 * 
	 * @param xScale
	 *            The scale to reach on the X axis
	 * @param yScale
	 *            The scale to reach on the Y axis
	 * @param zScale
	 *            The scale to reach on the Z axis
	 * @return This, for chainability
	 */
	public AnimatedScaling setTargetScale(final float xScale, final float yScale, final float zScale)
	{
		aTargetScale.set(Math.max(0, xScale), Math.max(0, yScale), Math.max(0, zScale));
		return this;
	}

	/**
	 * Set the target scale of this animation as a {@link Vector3f}
	 * 
	 * @param scale
	 *            The scale to reach, as a {@link Vector3f}
	 * @return This, for chainability
	 */
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
