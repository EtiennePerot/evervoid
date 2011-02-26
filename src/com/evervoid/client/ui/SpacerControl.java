package com.evervoid.client.ui;

import com.evervoid.state.geometry.Dimension;

/**
 * A blank control that does nothing but take up screen space. Used for specifying margins.
 */
public class SpacerControl extends UIControl
{
	private final Dimension aSpacerSize;

	public SpacerControl(final Dimension spacer)
	{
		aSpacerSize = spacer.clone();
	}

	public SpacerControl(final float width, final float height)
	{
		this((int) width, (int) height);
	}

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
