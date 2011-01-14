package com.evervoid.client.graphics.geometry;

import com.evervoid.client.EverNode;

public abstract class AnimatedTransform extends Transform
{
	public static enum DurationMode
	{
		CONTINUOUS, FIXED, REPETITIVE;
	}

	private Runnable aCallback = null;
	private float aDuration = 0f;
	protected DurationMode aDurationMode = DurationMode.FIXED;
	protected float aProgress = 0f;
	protected Smoothing aSmoothing = Smoothing.SINE;
	protected float aSpeed = 1f;
	private boolean aStarted = false;

	public AnimatedTransform(final EverNode node)
	{
		super(node);
	}

	public AnimatedTransform done(final boolean resetProgress)
	{
		if (aCallback != null)
		{
			aCallback.run();
			aCallback = null;
		}
		aStarted = aDurationMode.equals(DurationMode.REPETITIVE);
		if (resetProgress || aDurationMode.equals(DurationMode.REPETITIVE))
		{
			aProgress = 0;
			getReady();
		}
		setNotifyOnChange(!aStarted);
		return this;
	}

	public boolean frame(final float tpf)
	{
		if (!aStarted)
		{
			return false;
		}
		aProgress = Math.min(1, aProgress + tpf / aDuration);
		final float prog = aSmoothing.smooth(aProgress);
		step(prog, 1 - prog);
		if (aProgress >= 1)
		{
			// Need to modify aStarted here so that callback knows the animation
			// is done.
			aStarted = false;
			done(true);
			unregister();
		}
		return true;
	}

	public float getDuration()
	{
		return aDuration;
	}

	abstract protected void getReady();

	public boolean isInProgress()
	{
		return aStarted;
	}

	protected void register()
	{
		TransformManager.register(this);
	}

	public abstract void reset();

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
		register();
		setNotifyOnChange(false);
		return this;
	}

	abstract protected void step(float progress, float antiProgress);

	protected void unregister()
	{
		if (!aDurationMode.equals(DurationMode.REPETITIVE))
		{
			TransformManager.unregister(this);
		}
	}
}