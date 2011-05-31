package com.evervoid.client.graphics.geometry;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.utils.EVUtils;

/**
 * A threadless timer. It runs on the main (UI) thread, ticking away, thanks to the {@link EVFrameManager}. All its actions are
 * also executed on the UI thread. Highly recommended for client-side, threadsafe timed events.
 */
public class FrameTimer implements EVFrameObserver
{
	/**
	 * For repeating timers, this is the number of iterations that the timer ahs run for
	 */
	private int aCurrentIteration = 0;
	/**
	 * Amount of time for a timer iteration
	 */
	private final float aInterval;
	/**
	 * Whether the timer is done or not
	 */
	private boolean aIsDone = false;
	/**
	 * Whether the timer is active or not
	 */
	private boolean aStarted = false;
	/**
	 * The callback to execute when an iteration completes, as a {@link Runnable}
	 */
	private final Runnable aTask;
	/**
	 * The elapsed time in the current iteration
	 */
	private float aTime = 0f;
	/**
	 * Target number of iterations to reach
	 */
	private final int aTotalIterations;

	/**
	 * Regular timer; does nothing when elapsed, but can still be queried about "done" status
	 * 
	 * @param interval
	 *            Interval between iterations
	 * @param iterations
	 *            Number of iterations (0 for infinite, but this would make no sense in this case)
	 */
	public FrameTimer(final float interval, final int iterations)
	{
		this(null, interval, iterations);
	}

	/**
	 * Infinitely-recurring task (until .stop() is called)
	 * 
	 * @param task
	 *            Task to be executed during each iteration (can be null, but this would make no sense in this case)
	 * @param interval
	 *            Interval between iterations
	 */
	public FrameTimer(final Runnable task, final float interval)
	{
		this(task, interval, 0);
	}

	/**
	 * Standard constructor
	 * 
	 * @param task
	 *            Task to be executed during each iteration
	 * @param interval
	 *            Interval between iterations
	 * @param iterations
	 *            Number of iterations (0 for infinite)
	 */
	public FrameTimer(final Runnable task, final float interval, final int iterations)
	{
		aInterval = interval;
		aTotalIterations = iterations;
		aTask = task;
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		aTime += f.aTpf;
		if (aTime >= aInterval) {
			runNow();
		}
	}

	/**
	 * @return Whether this time is done or not (has reached the target number of iterations)
	 */
	public boolean isDone()
	{
		return aIsDone;
	}

	/**
	 * Restart this timer, stopping it first if it was running.
	 * 
	 * @return This, for chainability
	 */
	public FrameTimer restart()
	{
		stop();
		return start();
	}

	/**
	 * Force an iteration to run right now
	 * 
	 * @return This, for chainability
	 */
	public FrameTimer runNow()
	{
		aTime = 0f;
		aCurrentIteration++;
		EVUtils.runCallback(aTask);
		if (aTotalIterations > 0 && aCurrentIteration >= aTotalIterations) {
			stop();
		}
		return this;
	}

	/**
	 * Start the timer now.
	 * 
	 * @return This, for chainability
	 */
	public FrameTimer start()
	{
		if (aStarted) {
			return this;
		}
		aStarted = true;
		aTime = 0f;
		aCurrentIteration = 0;
		aIsDone = false;
		EVFrameManager.register(this);
		return this;
	}

	/**
	 * Interrupt the timer. Does not call the callback.
	 * 
	 * @return This, for chainability
	 */
	public FrameTimer stop()
	{
		if (!aStarted) {
			return this;
		}
		aStarted = false;
		aIsDone = true;
		EVFrameManager.deregister(this);
		return this;
	}
}
