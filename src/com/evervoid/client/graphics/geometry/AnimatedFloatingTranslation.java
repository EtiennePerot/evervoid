package com.evervoid.client.graphics.geometry;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.utils.MathUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * Wrapper for AnimatedTranslation, moving around the origin randomly on the Y axis, giving a "floating" effect.
 */
public class AnimatedFloatingTranslation extends AnimatedTranslation
{
	/**
	 * Floating state: Either on the ground, or floating in the "air".
	 */
	private static enum FloatingState
	{
		/**
		 * Floating state: Object is not touching the ground
		 */
		FLOATING,
		/**
		 * Ground state: Object is touching the ground
		 */
		GROUND;
		/**
		 * Computes a randomized duration of the animation in the given state, given a template duration
		 * 
		 * @param duration
		 *            The template duration
		 * @return A randomized duration to use
		 */
		public float getDuration(final float duration)
		{
			switch (this) {
				case FLOATING:
					return MathUtils.getRandomFloatBetween(1, 2.25) * duration;
				case GROUND:
					return MathUtils.getRandomFloatBetween(1, 1.75) * duration;
			}
			return duration;
		}

		/**
		 * Computes a randomized target vector to reach for this animation state
		 * 
		 * @param scale
		 *            The scale to multiply the target vector by
		 * @return The computed, randomized vector
		 */
		public Vector2f getVector(final float scale)
		{
			switch (this) {
				case FLOATING:
					return new Vector2f(MathUtils.getRandomFloatBetween(-0.25, 0.25), 1).mult(scale);
				case GROUND:
					return new Vector2f(0, 0); // No need to multiply
			}
			return null;
		}

		/**
		 * @return The next state in the animation cycle
		 */
		public FloatingState next()
		{
			switch (this) {
				case FLOATING:
					return GROUND;
				case GROUND:
					return FLOATING;
			}
			return null;
		}
	}

	/**
	 * Current target animation state. Alternates between the {@link FloatingState} values.
	 */
	protected FloatingState aNewFloatingTarget = FloatingState.GROUND;
	/**
	 * A target "tolerated" offset from the origin that the animation will try to stick to.
	 */
	protected float aToleratedOffset = 1f;
	/**
	 * Un-fuzzed version of the duration of the animation; the actual duration will be randomized around this value
	 */
	private float aUnFuzzyDuration = 1f;

	/**
	 * Constructor; DO NOT USE THIS, use {@link EverNode}'s getNewFloatingTranslationAnimation instead.
	 * 
	 * @param node
	 *            The {@link EverNode} that this animation will affect.
	 */
	public AnimatedFloatingTranslation(final EverNode node)
	{
		super(node);
		setDurationMode(DurationMode.REPETITIVE); // Floating animations are repetitive
	}

	@Override
	public AnimatedTransform done(final boolean resetProgress, final boolean runCallback)
	{
		super.done(resetProgress, runCallback);
		nextFloatingTarget();
		return this;
	}

	@Override
	public float getDuration()
	{
		return aUnFuzzyDuration;
	}

	/**
	 * Switch to the next {@link FloatingState} in the animation cycle
	 */
	private void nextFloatingTarget()
	{
		aNewFloatingTarget = aNewFloatingTarget.next();
		super.setDuration(aNewFloatingTarget.getDuration(aUnFuzzyDuration));
		smoothMoveTo(aNewFloatingTarget.getVector(aToleratedOffset));
	}

	/**
	 * Set the duration of the animation. Extra fuzz will be added to the given value automatically.
	 * 
	 * @param duration
	 *            The un-fuzzed duration of the animation
	 * @return This, for chainability
	 */
	@Override
	public AnimatedTransform setDuration(final float duration)
	{
		aUnFuzzyDuration = duration;
		return this;
	}

	/**
	 * Set the maximum target offset from the origin that will be tolerated while floating around
	 * 
	 * @param offset
	 *            The maximum offset
	 * @return This, for chainability
	 */
	public AnimatedFloatingTranslation setToleratedOffset(final float offset)
	{
		aToleratedOffset = FastMath.abs(offset);
		return this;
	}
}
