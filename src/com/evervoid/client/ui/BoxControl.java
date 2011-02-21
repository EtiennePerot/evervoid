package com.evervoid.client.ui;

/**
 * A Box control. Makes a pretty border around the given element.
 */
public class BoxControl extends WrapperControl
{
	public BoxControl()
	{
		this(BoxDirection.VERTICAL);
	}

	public BoxControl(final BoxDirection direction)
	{
		super(new UIControl(direction), BoxDirection.VERTICAL);
		addUI(new BorderedControl("ui/menubox/left_corner_top.png", "ui/menubox/horizontal_top.png",
				"ui/menubox/right_corner_top.png"));
		final BackgroundedUIControl bg = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/menubox/center.png");
		bg.addUI(new CenteredControl(aContained), 1);
		addUI(new BorderedControl(new UIConnector("ui/menubox/vertical_left.png", true), bg, new UIConnector(
				"ui/menubox/vertical_right.png", true)), 1);
		addUI(new BorderedControl("ui/menubox/left_corner_bottom.png", "ui/menubox/horizontal_bottom.png",
				"ui/menubox/right_corner_bottom.png"));
	}
}
