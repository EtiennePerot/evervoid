package com.evervoid.client.ui;

import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.data.SpriteData;

/**
 * Same as {@link UIControl}, but also has a {@link Sprite} as background.
 */
public class BackgroundedUIControl extends UIControl
{
	/**
	 * The sprite to use as background
	 */
	private final Sprite aBackground;
	/**
	 * {@link Transform} for the background to be positioned and stretched correctly
	 */
	private final Transform aBackgroundTransform;

	/**
	 * Constructor
	 * 
	 * @param direction
	 *            The direction of the {@link UIControl}
	 * @param background
	 *            The background to use, as a {@link SpriteData} object
	 */
	public BackgroundedUIControl(final BoxDirection direction, final SpriteData background)
	{
		super(direction);
		aBackground = new Sprite(background).bottomLeftAsOrigin();
		addNode(aBackground);
		aBackgroundTransform = aBackground.getNewTransform();
		// Put background in background
		aBackgroundTransform.translate(0, 0, -UIControl.sChildZOffset);
		// Put self in foreground (this affects background too)
		getNewTransform().translate(0, 0, UIControl.sChildZOffset);
	}

	/**
	 * Constructor
	 * 
	 * @param direction
	 *            The direction of the {@link UIControl}
	 * @param background
	 *            The background to use, as a String (file name)
	 */
	public BackgroundedUIControl(final BoxDirection direction, final String background)
	{
		this(direction, new SpriteData(background));
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
