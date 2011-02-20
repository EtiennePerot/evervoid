package com.evervoid.client.ui;

import com.evervoid.client.ui.Sizer.SizerDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;

public class BoxControl extends UIControl
{
	private final UIControl aBottomBar;
	private final UIControl aTopBar;

	public BoxControl(final Bounds bounds, final SizerDirection direction)
	{
		super(bounds, direction);
		aTopBar = new UIBorderedControl(direction, "ui/menubox/left_corner_top.png", "ui/menubox/right_corner_top.png");
		addControl(aTopBar);
		aBottomBar = new UIBorderedControl(direction, "ui/menubox/left_corner_bottom.png", "ui/menubox/right_corner_bottom.png");
		addControl(aBottomBar);
	}

	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(8, 8);
	}
}
