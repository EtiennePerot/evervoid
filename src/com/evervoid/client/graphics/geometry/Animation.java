package com.evervoid.client.graphics.geometry;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.utils.EVUtils;

/**
 * Generic animation class; can add multiple steps, and the callback will be called when all are complete.
 */
public class Animation
{
	/**
	 * Data structure used to hold multiple steps in the Animation
	 */
	private class AnimationStep
	{
		/**
		 * The {@link FrameTimer} used to time this step properly
		 */
		private final FrameTimer aTimer;

		/**
		 * Constructor
		 * 
		 * @param delay
		 *            The delay at which the step should be fired
		 * @param action
		 *            The action to execute when the step is fired, as a {@link Runnable}
		 */
		private AnimationStep(final float delay, final Runnable action)
		{
			final AnimationStep oldThis = this;
			aTimer = new FrameTimer(new Runnable()
			{
				@Override
				public void run()
				{
					stepDone(oldThis);
					EVUtils.runCallback(action);
				}
			}, delay, 1);
		}

		/**
		 * Start this step's timer
		 */
		private void start()
		{
			aTimer.start();
		}
	}

	/**
	 * The callback to run at the end of all steps of the animation
	 */
	private final Runnable aCallback;
	/**
	 * All the steps of the animation
	 */
	private final Set<AnimationStep> aSteps = new HashSet<AnimationStep>();

	/**
	 * Creates a callback-less animation
	 */
	public Animation()
	{
		this(null);
	}

	/**
	 * Creates a new, step-less animation
	 * 
	 * @param callback
	 *            The callback to run when the entire animation is done
	 */
	public Animation(final Runnable callback)
	{
		aCallback = callback;
	}

	/**
	 * Add a step to the animation
	 * 
	 * @param delay
	 *            The delay after which the step should be fired
	 * @param action
	 *            The action to execute when the step is fired, as a {@link Runnable}
	 */
	public void addStep(final float delay, final Runnable action)
	{
		aSteps.add(new AnimationStep(delay, action));
	}

	/**
	 * Start the entire Animation. If the Animation has no steps, the callback will be called instantly. Otherwise, all steps
	 * will start their timer, and the final callback will run when they are all done
	 */
	public void start()
	{
		if (aSteps.isEmpty()) {
			// Nothing to do, call callback now
			EVUtils.runCallback(aCallback);
			return;
		}
		for (final AnimationStep step : aSteps) {
			step.start();
		}
	}

	/**
	 * Called by {@link AnimationStep} when it is done
	 * 
	 * @param done
	 *            The {@link AnimationStep} that just finished running
	 */
	private void stepDone(final AnimationStep done)
	{
		aSteps.remove(done);
		if (aSteps.isEmpty() && aCallback != null) {
			// We're all done
			EVUtils.runCallback(aCallback);
		}
	}
}
