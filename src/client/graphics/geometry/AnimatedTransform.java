package client.graphics.geometry;

import client.EverNode;
import client.graphics.FrameUpdate;
import client.graphics.Smoothing;

public abstract class AnimatedTransform extends Transform
{
	public static enum DurationMode
	{
		CONTINUOUS, FIXED;
	}

	private Runnable aCallback = null;
	private float aDuration = 0f;
	protected DurationMode aDurationMode = DurationMode.FIXED;
	private float aProgress = 0f;
	private Smoothing aSmoothing = Smoothing.SINE;
	protected float aSpeed = 1f;
	private boolean aStarted = false;

	public AnimatedTransform(final EverNode node)
	{
		super(node);
	}

	public AnimatedTransform done(final boolean resetProgress)
	{
		aStarted = false;
		if (aCallback != null)
		{
			aCallback.run();
			aCallback = null;
		}
		if (resetProgress)
		{
			aProgress = 0;
		}
		return this;
	}

	public void frame(final FrameUpdate f)
	{
		if (!aStarted)
		{
			return;
		}
		aProgress = Math.min(1, aProgress + f.aTpf / aDuration);
		final float prog = aSmoothing.smooth(aProgress);
		step(prog, 1 - prog);
		if (aProgress >= 1)
		{
			done(true);
		}
	}

	abstract protected void getReady();

	protected void setBackContinuous(final float measure)
	{
		if (!aDurationMode.equals(DurationMode.CONTINUOUS))
		{
			return;
		}
		setDuration(measure / aSpeed);
		start(true);
	}

	public AnimatedTransform setDuration(final float duration)
	{
		aDuration = duration;
		return this;
	}

	public AnimatedTransform setDurationMode(final DurationMode durationMode)
	{
		aDurationMode = durationMode;
		if (aDurationMode.equals(DurationMode.CONTINUOUS))
		{
			setSmoothing(Smoothing.LINEAR);
		}
		return this;
	}

	public AnimatedTransform setSmoothing(final Smoothing smoothing)
	{
		aSmoothing = smoothing;
		return this;
	}

	public AnimatedTransform setSpeed(final float speed)
	{
		aSpeed = speed;
		return this;
	}

	public AnimatedTransform start()
	{
		return start(null, true);
	}

	public AnimatedTransform start(final boolean resetProgress)
	{
		return start(null, resetProgress);
	}

	public AnimatedTransform start(final Runnable callback)
	{
		return start(callback, true);
	}

	public AnimatedTransform start(final Runnable callback, final boolean resetProgress)
	{
		done(resetProgress);
		aCallback = callback;
		getReady();
		aStarted = true;
		return this;
	}

	abstract protected void step(float progress, float antiProgress);
}
