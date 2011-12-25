package com.evervoid.client.ui;

import com.evervoid.state.geometry.Dimension;

/**
 * A blank control that does nothing but take up screen space. Used for specifying margins. Note that {@link UIControl} offers a
 * helper method addSpacer(), which should avoid the need for this class to be used directly.
 */
class SpacerControl extends UIControl
{
	/**
	 * The size to occupy.
	 */
	private final Dimension aSpacerSize;

	/**
	 * Constructor
	 * 
	 * @param spacer
	 *            The {@link Dimension} to occupy
	 */
	public SpacerControl(final Dimension spacer)
	{
		aSpacerSize = spacer.clone();
	}

	/**
	 * Constructor
	 * 
	 * @param width
	 *            The width to occupy
	 * @param height
	 *            The height to occupy
	 */
	public SpacerControl(final float width, final float height)
	{
		this((int) width, (int) height);
	}

	/**
	 * Constructor
	 * 
	 * @param width
	 *            The width to occupy
	 * @param height
	 *            The height to occupy
	 */
	public SpacerControl(final int width, final int height)
	{
		this(new Dimension(width, height));
	}

	@Override
	public Dimension getMinimumSize()
	{
		return aSpacerSize;
	}
}
