package com.evervoid.client.ui;

/**
 * A Box control. Makes a pretty border around the given element.
 */
public class BoxControl extends UIControl
{
	public BoxControl(final Resizeable contained)
	{
		super(BoxDirection.VERTICAL);
		addUI(new BorderedControl("ui/menubox/left_corner_bottom.png", "ui/menubox/horizontal_bottom.png",
				"ui/menubox/right_corner_bottom.png"));
		final UIControl centerControl = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/menubox/center.png");
		centerControl.addUI(new CenteredControl(contained), 1);
		addUI(new BorderedControl(new UIConnector("ui/menubox/vertical_left.png", true), centerControl, new UIConnector(
				"ui/menubox/vertical_right.png", true)), 1);
		addUI(new BorderedControl("ui/menubox/left_corner_top.png", "ui/menubox/horizontal_top.png",
				"ui/menubox/right_corner_top.png"));
	}
}
