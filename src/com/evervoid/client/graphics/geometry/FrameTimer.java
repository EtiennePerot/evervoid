package com.evervoid.client.graphics.geometry;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.interfaces.EVFrameObserver;

public class FrameTimer implements EVFrameObserver
{
	private int aCurrentIteration = 0;
	private final float aInterval;
	private boolean aIsDone = false;
	private boolean aStarted = false;
	private final Runnable aTask;
	private float aTime = 0f;
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

	public boolean isDone()
	{
		return aIsDone;
	}

	public FrameTimer restart()
	{
		stop();
		return start();
	}

	/**
	 * Force an iteration to run right now
	 */
	public FrameTimer runNow()
	{
		aTime = 0f;
		aCurrentIteration++;
		if (aTask != null) {
			aTask.run();
		}
		if (aTotalIterations > 0 && aCurrentIteration >= aTotalIterations) {
			stop();
		}
		return this;
	}

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
