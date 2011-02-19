package com.evervoid.client.ui;

import com.evervoid.client.ui.Sizer.SizerDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;

public class BoxControl extends UIControl
{
	public BoxControl(final Bounds bounds, final SizerDirection direction)
	{
		super(bounds, direction);
	}

	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(8, 8);
	}
}
