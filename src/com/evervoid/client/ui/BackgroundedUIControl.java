package com.evervoid.client.ui;

import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;

public class BackgroundedUIControl extends UIControl
{
	private final Sprite aBackground;
	private final Transform aBackgroundTransform;

	public BackgroundedUIControl(final BoxDirection direction, final String background)
	{
		aBackground = new Sprite(background).bottomLeftAsOrigin();
		addNode(aBackground);
		aBackgroundTransform = aBackground.getNewTransform();
		// Put background in background
		aBackgroundTransform.translate(0, 0, -1);
		// Put self in foreground (this affects background too)
		getNewTransform().translate(0, 0, 1);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aBackgroundTransform.setScale(bounds.width / aBackground.getWidth(), bounds.height / aBackground.getHeight());
	}

	@Override
	public String toString(final String prefix)
	{
		return super.toString(prefix) + " [Backgrounded by " + aBackground + "]";
	}
}
