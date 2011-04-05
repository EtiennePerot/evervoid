package com.evervoid.client.ui;

import com.evervoid.client.graphics.geometry.AnimatedScaling;

public class ProgressBarControl extends BorderedControl
{
	private final AnimatedScaling aFillLength;

	public ProgressBarControl()
	{
		this(0);
	}

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

	public ProgressBarControl setProgress(final float progress)
	{
		aFillLength.setTargetScale(progress).start();
		return this;
	}
}
