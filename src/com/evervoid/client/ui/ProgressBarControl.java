package com.evervoid.client.ui;

import com.evervoid.client.graphics.geometry.AnimatedScaling;

/**
 * A progress bar control, used to display the progress of an operation
 */
public class ProgressBarControl extends BorderedControl
{
	/**
	 * {@link AnimatedScaling} animation used to modify the size of the bar
	 */
	private final AnimatedScaling aFillLength;

	/**
	 * Constructor; assumes that the progress bar starts at 0% progress
	 */
	public ProgressBarControl()
	{
		this(0);
	}

	/**
	 * Constructor
	 * 
	 * @param initialProgress
	 *            The initial progress, as a float from 0 (0%) to 1 (100%)
	 */
	public ProgressBarControl(final float initialProgress)
	{
		super("ui/progressbar/left.png", new BackgroundedUIControl(BoxDirection.VERTICAL, "ui/progressbar/middle.png"),
				"ui/progressbar/right.png");
		final ImageControl fill = new ImageControl("ui/progressbar/fill.png", false);
		aFillLength = fill.getNewScalingAnimation();
		aContained.addUI(fill, 1);
		aFillLength.setDuration(0.4).setScale(0, 1);
		aFillLength.setTargetScale(initialProgress, 1).start();
	}

	/**
	 * Update the progress bar's meter
	 * 
	 * @param progress
	 *            The current progress, as a float from 0 (0%) to 1 (100%)
	 * @return This, for chainability
	 */
	public ProgressBarControl setProgress(final float progress)
	{
		aFillLength.setTargetScale(progress).start();
		return this;
	}
}
