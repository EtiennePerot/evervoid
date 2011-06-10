package com.evervoid.client.graphics.geometry;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.utils.EVUtils;

/**
 * Represents an abstract animation over the {@link Transform} system. Is essentially a {@link Transform} that changes
 * automatically over time. This base class takes care of timing, smoothing and synchronization, while its subclasses only take
 * care of actually changing the right {@link Transform} properties.
 */
public abstract class AnimatedTransform extends Transform
{
	/**
	 * An animation may have multiple "duration modes"
	 */
	public static enum DurationMode
	{
		/**
		 * Continuous: The animation will be able to have its target change over time. It is constrained to use the Linear
		 * {@link Smoothing} type, however.
		 */
		CONTINUOUS,
		/**
		 * Default animation type: The animation will play ocne and finish.
		 */
		FIXED,
		/**
		 * The animation, once it finishes, will be played again.
		 */
		REPETITIVE;
	}

	/**
	 * A {@link Runnable} that will be executed when the animation finishes
	 */
	private Runnable aCallback = null;
	/**
	 * The duration of the animation
	 */
	private float aDuration = 0f;
	/**
	 * The {@link DurationMode} of the animation (see above)
	 */
	protected DurationMode aDurationMode = DurationMode.FIXED;
	/**
	 * Current progress of the animation, from 0 to 1
	 */
	protected float aProgress = 0f;
	/**
	 * The {@link Smoothing} type of the animation
	 */
	protected Smoothing aSmoothing = Smoothing.SINE;
	/**
	 * The speed of the animation; used for animations using the Continuous {@link DurationMode} only.
	 */
	protected float aSpeed = 1f;
	/**
	 * Whether the animation is actually being played or not
	 */
	private boolean aStarted = false;

	/**
	 * Constructor; same as {@link Transform}'s constructor
	 * 
	 * @param node
	 *            The node that this animation will affect
	 */
	protected AnimatedTransform(final EverNode node)
	{
		super(node);
	}

	@Override
	public void delete()
	{
		stop();
		super.delete();
	}

	/**
	 * Deregister this animation from the {@link TransformManager}. Called when the animation doesn't need to receive frame
	 * events anymore (animation complete or interrupted).
	 */
	protected void deregister()
	{
		if (!aDurationMode.equals(DurationMode.REPETITIVE)) {
			TransformManager.unregister(this);
		}
	}

	/**
	 * Called when the animation is done.
	 * 
	 * @param resetProgress
	 *            Whether we should reset the progress variable to 0
	 * @param runCallback
	 *            Whether we should run the callback (if there is one)
	 * @return This (for chainability)
	 */
	protected AnimatedTransform done(final boolean resetProgress, final boolean runCallback)
	{
		final Runnable oldCallback = aCallback;
		aCallback = null;
		aStarted = aDurationMode.equals(DurationMode.REPETITIVE);
		if (resetProgress || aDurationMode.equals(DurationMode.REPETITIVE)) {
			aProgress = 0;
			getReady();
		}
		if (runCallback) {
			EVUtils.runCallback(oldCallback);
		}
		setNotifyOnChange(!aStarted);
		return this;
	}

	/**
	 * Called on every frame by the {@link TransformManager}. Only called when the animation is actually active, for efficiency.
	 * 
	 * @param tpf
	 *            The time-per-frame
	 * @return Whether the animation is being played or not
	 */
	public boolean frame(final float tpf)
	{
		if (!aStarted) {
			return false;
		}
		aProgress = Math.min(1, aProgress + tpf / aDuration);
		final float prog = aSmoothing.smooth(aProgress);
		step(prog, 1 - prog);
		if (aProgress >= 1) {
			stop();
		}
		return true;
	}

	/**
	 * @return The duration of the animation
	 */
	public float getDuration()
	{
		return aDuration;
	}

	/**
	 * Called when the animation is about to start playing. Subclasses can override this to initialize themselves prior to
	 * playing.
	 */
	abstract protected void getReady();

	/**
	 * @return Whether the animation is currently being played or not
	 */
	public boolean isInProgress()
	{
		return aStarted;
	}

	/**
	 * Register to the {@link TransformManager} in order to start receiving frame events
	 */
	protected void register()
	{
		TransformManager.register(this);
	}

	/**
	 * Reset this to the default state. Subclasses should override this.
	 */
	public abstract void reset();

	/**
	 * For continuous animations, override the duration back by a certain measure/speed.
	 * 
	 * @param measure
	 *            A measure proportional to how the duration will change when considering the new target of the continuous
	 *            animation
	 */
	protected void setBackContinuous(final double measure)
	{
		setBackContinuous((float) measure);
	}

	/**
	 * For continuous animations, override the duration back by a certain measure/speed.
	 * 
	 * @param measure
	 *            A measure proportional to how the duration will change when considering the new target of the continuous
	 *            animation
	 */
	protected void setBackContinuous(final float measure)
	{
		if (!aDurationMode.equals(DurationMode.CONTINUOUS)) {
			return;
		}
		setDuration(measure / aSpeed);
		start(true);
	}

	/**
	 * Set the duration of this animation
	 * 
	 * @param duration
	 *            The duration (in seconds)
	 * @return This (for chainability)
	 */
	public AnimatedTransform setDuration(final double duration)
	{
		return setDuration((float) duration);
	}

	/**
	 * Set the duration of this animation
	 * 
	 * @param duration
	 *            The duration (in seconds)
	 * @return This (for chainability)
	 */
	public AnimatedTransform setDuration(final float duration)
	{
		aDuration = duration;
		return this;
	}

	/**
	 * Set the {@link DurationMode} of this animation
	 * 
	 * @param durationMode
	 *            The {@link DurationMode} of the animation; see individual values' javadoc for details
	 * @return This (for chainability)
	 */
	public AnimatedTransform setDurationMode(final DurationMode durationMode)
	{
		aDurationMode = durationMode;
		if (aDurationMode.equals(DurationMode.CONTINUOUS)) {
			setSmoothing(Smoothing.LINEAR);
		}
		return this;
	}

	/**
	 * Set the {@link Smoothing} type of this animation
	 * 
	 * @param smoothing
	 *            The {@link Smoothing} type of the animation; see individual values' javadoc for details
	 * @return This (for chainability)
	 */
	public AnimatedTransform setSmoothing(final Smoothing smoothing)
	{
		aSmoothing = smoothing;
		return this;
	}

	/**
	 * Set the speed of the animation (for continuous animations)
	 * 
	 * @param speed
	 *            The speed of the animation
	 * @return This (for chainability)
	 */
	public AnimatedTransform setSpeed(final float speed)
	{
		aSpeed = speed;
		return this;
	}

	/**
	 * Start the animation.
	 * 
	 * @return This (for chainability)
	 */
	public AnimatedTransform start()
	{
		return start(null, true, true);
	}

	/**
	 * Start the animation.
	 * 
	 * @param resetProgress
	 *            Whether to reset the animation's progress to 0 or not
	 * @return This (for chainability)
	 */
	public AnimatedTransform start(final boolean resetProgress)
	{
		return start(null, resetProgress, true);
	}

	/**
	 * Start the animation.
	 * 
	 * @param callback
	 *            The callback to run when the animation is done
	 * @return This (for chainability)
	 */
	public AnimatedTransform start(final Runnable callback)
	{
		return start(callback, true, true);
	}

	/**
	 * Start the animation.
	 * 
	 * @param resetProgress
	 *            Whether to reset the animation's progress to 0 or not
	 * @param callback
	 *            The callback to run when the animation is done
	 * @param runCallback
	 *            Whether to run the callback of the already-in-progress animation, if there is one
	 * @return This (for chainability)
	 */
	public AnimatedTransform start(final Runnable callback, final boolean resetProgress, final boolean runCallback)
	{
		done(resetProgress, runCallback);
		aCallback = callback;
		getReady();
		aStarted = true;
		register();
		setNotifyOnChange(false);
		return this;
	}

	/**
	 * Main method of the animation. This is called on every frame with the current progress value. Subclasses should override
	 * this and set the {@link Transform}'s properties here.
	 * 
	 * @param progress
	 *            The progress of the animation (from 0 to 1), already smoothed
	 * @param antiProgress
	 *            Equal to 1-progress (passed for convenience)
	 */
	abstract protected void step(float progress, float antiProgress);

	/**
	 * Interrupt the animation, if there is one
	 * 
	 * @return This (for chainability)
	 */
	public AnimatedTransform stop()
	{
		if (!isInProgress()) {
			return this;
		}
		aStarted = false;
		done(true, true);
		deregister();
		return this;
	}

	@Override
	public String toString()
	{
		String active = "(off)";
		if (aStarted) {
			active = " @ " + (aProgress * 100) + "%";
		}
		return super.toString() + active;
	}
}
