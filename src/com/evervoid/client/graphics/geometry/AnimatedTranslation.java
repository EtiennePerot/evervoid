package com.evervoid.client.graphics.geometry;

import com.evervoid.client.graphics.EverNode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * An animation over the translation of an {@link EverNode}; used for movement animations.
 */
public class AnimatedTranslation extends AnimatedTransform
{
	/**
	 * The translation prior to starting the animation
	 */
	private final Vector3f aOriginVector = new Vector3f(0, 0, 0);
	/**
	 * The target translation to reach when the animation is done
	 */
	private final Vector3f aTargetVector = new Vector3f(0, 0, 0);

	/**
	 * Constructor; DO NOT USE THIS, use {@link EverNode}'s getNewTranslationAnimation instead.
	 * 
	 * @param node
	 *            The {@link EverNode} that this animation will affect.
	 */
	public AnimatedTranslation(final EverNode node)
	{
		super(node);
	}

	/**
	 * @return The difference between the current translation and the translation prior to starting the animation.
	 */
	public Vector3f getCurrentDelta()
	{
		return aVector.subtract(aOriginVector);
	}

	/**
	 * @return The difference between the current translation and the translation prior to starting the animation, as a
	 *         {@link Vector2f} (Z component stripped)
	 */
	public Vector2f getCurrentDelta2f()
	{
		final Vector3f v = getCurrentDelta();
		return new Vector2f(v.x, v.y);
	}

	/**
	 * @return The speed (in progress (0 to 1) per second) of the animation, taking {@link Smoothing} into consideration
	 */
	public float getMovingSpeed()
	{
		return aSmoothing.derivative(aProgress);
	}

	@Override
	protected void getReady()
	{
		aOriginVector.set(aVector);
	}

	/**
	 * @return The translation to reach at the end of the animation
	 */
	public Vector3f getTargetTranslation()
	{
		return aTargetVector.clone();
	}

	/**
	 * @return The translation to reach at the end of the animation, as a {@link Vector2f} (Z component stripped)
	 */
	public Vector2f getTargetTranslation2f()
	{
		return new Vector2f(aTargetVector.x, aTargetVector.y);
	}

	@Override
	public void reset()
	{
		setTranslationNow(0, 0, 0);
		super.translate(0, 0, 0);
	}

	/**
	 * Go to a certain (x, y) location instantly, bypassing the animation. The Z component will be unchanged.
	 * 
	 * @param x
	 *            The x coordinate to go to
	 * @param y
	 *            The y coordinate to go to
	 * @return This, for chainability
	 */
	public AnimatedTranslation setTranslationNow(final float x, final float y)
	{
		return setTranslationNow(new Vector2f(x, y));
	}

	/**
	 * Go to a certain (x, y, z) location instantly, bypassing the animation.
	 * 
	 * @param x
	 *            The x coordinate to go to
	 * @param y
	 *            The y coordinate to go to
	 * @param z
	 *            The z coordinate to go to
	 * @return This, for chainability
	 */
	public AnimatedTranslation setTranslationNow(final float x, final float y, final float z)
	{
		return setTranslationNow(new Vector3f(x, y, z));
	}

	/**
	 * Go to a certain location (as a {@link Vector2f}) instantly, bypassing the animation. The Z component will be unchanged.
	 * 
	 * @param offset
	 *            The point to go to
	 * @return This, for chainability
	 */
	public AnimatedTranslation setTranslationNow(final Vector2f offset)
	{
		return setTranslationNow(new Vector3f(offset.x, offset.y, aTargetVector.z));
	}

	/**
	 * Go to a certain (x, y, z) location instantly, bypassing the animation.
	 * 
	 * @param offset
	 *            The point to go to
	 * @return This, for chainability
	 */
	public AnimatedTranslation setTranslationNow(final Vector3f offset)
	{
		aOriginVector.set(offset);
		aTargetVector.set(offset);
		translate(offset);
		return this;
	}

	/**
	 * Add an offset to the current target translation. The target Z coordinate will be unchanged.
	 * 
	 * @param x
	 *            X offset to add to the current target
	 * @param y
	 *            Y offset to add to the current target
	 * @return This, for chainability
	 */
	public AnimatedTranslation smoothMoveBy(final float x, final float y)
	{
		return smoothMoveBy(new Vector2f(x, y));
	}

	/**
	 * Add an offset to the current target translation.
	 * 
	 * @param x
	 *            X offset to add to the current target
	 * @param y
	 *            Y offset to add to the current target
	 * @param z
	 *            Z offset to add to the current target
	 * @return This, for chainability
	 */
	public AnimatedTranslation smoothMoveBy(final float x, final float y, final float z)
	{
		return smoothMoveBy(new Vector3f(x, y, z));
	}

	/**
	 * Add an offset (as a {@link Vector2f}) to the current target translation. The target Z coordinate will be unchanged.
	 * 
	 * @param offset
	 *            Offset to add to the current target
	 * @return This, for chainability
	 */
	public AnimatedTranslation smoothMoveBy(final Vector2f offset)
	{
		return smoothMoveBy(new Vector3f(offset.x, offset.y, 0));
	}

	/**
	 * Add an offset (as a {@link Vector3f}) to the current target translation.
	 * 
	 * @param offset
	 *            Offset to add to the current target
	 * @return This, for chainability
	 */
	public AnimatedTranslation smoothMoveBy(final Vector3f offset)
	{
		return smoothMoveTo(aVector.add(offset));
	}

	/**
	 * Set the target translation of this animation. The Z component will be unchanged.
	 * 
	 * @param x
	 *            The target X coordinate
	 * @param y
	 *            The target Y coordinate
	 * @return This, for chainability
	 */
	public AnimatedTranslation smoothMoveTo(final float x, final float y)
	{
		return smoothMoveTo(x, y, aTargetVector.z);
	}

	/**
	 * Set the target translation of this animation.
	 * 
	 * @param x
	 *            The target X coordinate
	 * @param y
	 *            The target Y coordinate
	 * @param z
	 *            The target Z coordinate
	 * @return This, for chainability
	 */
	public AnimatedTranslation smoothMoveTo(final float x, final float y, final float z)
	{
		return smoothMoveTo(new Vector3f(x, y, z));
	}

	/**
	 * Set the target translation of this animation, as a {@link Vector2f}. The Z component will be unchanged.
	 * 
	 * @param target
	 *            The target to go to
	 * @return This, for chainability
	 */
	public AnimatedTranslation smoothMoveTo(final Vector2f target)
	{
		return smoothMoveTo(new Vector3f(target.x, target.y, aTargetVector.z));
	}

	/**
	 * Set the target translation of this animation, as a {@link Vector3f}.
	 * 
	 * @param target
	 *            The target to go to
	 * @return This, for chainability
	 */
	public AnimatedTranslation smoothMoveTo(final Vector3f target)
	{
		aTargetVector.set(target);
		return this;
	}

	@Override
	protected void step(final float progress, final float antiProgress)
	{
		translate(aOriginVector.mult(antiProgress).add(aTargetVector.mult(progress)));
	}

	@Override
	public String toString()
	{
		return super.toString() + "Translation from " + aOriginVector + " to " + aTargetVector;
	}
}
