package com.evervoid.client.ui;

public class DarkBoxControl extends WrapperControl
{
	public DarkBoxControl(final BoxDirection direction, final UIControl contained)
	{
		super(contained, BoxDirection.VERTICAL);
		addChildUI(new BorderedControl("ui/dropbox/left_corner_top.png", "ui/dropbox/horizontal_top.png",
				"ui/dropbox/right_corner_top.png"));
		final BackgroundedUIControl bg = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/dropbox/center.png");
		bg.addChildUI(aContained, 1);
		addChildUI(new BorderedControl(new ImageControl("ui/dropbox/vertical_left.png", true), bg, new ImageControl(
				"ui/dropbox/vertical_right.png", true)), 1);
		addChildUI(new BorderedControl("ui/dropbox/left_corner_bottom.png", "ui/dropbox/horizontal_bottom.png",
				"ui/dropbox/right_corner_bottom.png"));
	}

	public DarkBoxControl(final UIControl contained)
	{
		this(BoxDirection.VERTICAL, contained);
	}
}
