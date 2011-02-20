package com.evervoid.client.ui;

import com.evervoid.state.geometry.Dimension;

public class SpacerControl extends UIControl
{
	private final Dimension aSpacerSize;

	public SpacerControl(final Dimension spacer)
	{
		aSpacerSize = spacer.clone();
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
