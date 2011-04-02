package com.evervoid.client.graphics.geometry;

import java.util.HashSet;
import java.util.Set;

/**
 * Generic animation class; can add multiple steps, and the callback will be called when all are complete.
 */
public class Animation
{
	private class AnimationStep
	{
		private final FrameTimer aTimer;

		private AnimationStep(final float delay, final Runnable action)
		{
			final AnimationStep oldThis = this;
			aTimer = new FrameTimer(new Runnable()
			{
				@Override
				public void run()
				{
					stepDone(oldThis);
					if (action != null) {
						action.run();
					}
				}
			}, delay, 1);
		}

		private void start()
		{
			aTimer.start();
		}
	}

	private final Runnable aCallback;
	private final Set<AnimationStep> aSteps = new HashSet<AnimationStep>();

	public Animation(final Runnable callback)
	{
		aCallback = callback;
	}

	public void addStep(final float delay, final Runnable action)
	{
		aSteps.add(new AnimationStep(delay, action));
	}

	public void start()
	{
		if (aSteps.isEmpty()) {
			// Nothing to do, call callback now
			if (aCallback != null) {
				aCallback.run();
			}
			return;
		}
		for (final AnimationStep step : aSteps) {
			step.start();
		}
	}

	private void stepDone(final AnimationStep done)
	{
		aSteps.remove(done);
		if (aSteps.isEmpty() && aCallback != null) {
			// We're all done
			aCallback.run();
		}
	}
}
