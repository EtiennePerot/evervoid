package client.graphics.geometry;

import client.EverNode;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * Wrapper for AnimatedTranslation, moving around the origin randomly.
 */
public class AnimatedFloatingTranslation extends AnimatedTranslation
{
	private static enum FloatingState
	{
		FLOATING, GROUND;
		public float getDuration(final float duration)
		{
			switch (this)
			{
				case FLOATING:
					return Geometry.getRandomFloatBetween(1, 2.25) * duration;
				case GROUND:
					return Geometry.getRandomFloatBetween(1, 1.75) * duration;
			}
			return duration;
		}

		public Vector2f getVector(final float scale)
		{
			switch (this)
			{
				case FLOATING:
					return new Vector2f(Geometry.getRandomFloatBetween(-0.25, 0.25), 1).mult(scale);
				case GROUND:
					return new Vector2f(0, 0); // No need to multiply
			}
			return null;
		}

		public FloatingState next()
		{
			switch (this)
			{
				case FLOATING:
					return GROUND;
				case GROUND:
					return FLOATING;
			}
			return null;
		}
	}

	private float aFuzzyDuration = 1f;
	protected FloatingState aNewFloatingTarget = FloatingState.GROUND;
	protected float aToleratedOffset = 1f;

	public AnimatedFloatingTranslation(final EverNode node)
	{
		super(node);
		setDurationMode(DurationMode.REPETITIVE);
	}

	@Override
	public AnimatedTransform done(final boolean resetProgress)
	{
		super.done(resetProgress);
		nextFloatingTarget();
		return this;
	}

	@Override
	public float getDuration()
	{
		return aFuzzyDuration;
	}

	private void nextFloatingTarget()
	{
		aNewFloatingTarget = aNewFloatingTarget.next();
		super.setDuration(aNewFloatingTarget.getDuration(aFuzzyDuration));
		smoothMoveTo(aNewFloatingTarget.getVector(aToleratedOffset));
	}

	@Override
	public AnimatedTransform setDuration(final float duration)
	{
		aFuzzyDuration = duration;
		return this;
	}

	public AnimatedFloatingTranslation setToleratedOffset(final float offset)
	{
		aToleratedOffset = FastMath.abs(offset);
		return this;
	}
}
