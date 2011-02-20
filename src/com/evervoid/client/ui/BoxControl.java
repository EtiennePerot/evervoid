package com.evervoid.client.ui;

public class BoxControl extends UIControl
{
	public BoxControl()
	{
		super(BoxDirection.VERTICAL);
		addUI(new BorderedControl("ui/menubox/left_corner_bottom.png", "ui/menubox/horizontal_bottom.png",
				"ui/menubox/right_corner_bottom.png"));
		addUI(new BorderedControl(new UIConnector("ui/menubox/vertical.png", true), new UIControl(), new UIConnector(
				"ui/menubox/vertical.png", true)), 1);
		addUI(new BorderedControl("ui/menubox/left_corner_top.png", "ui/menubox/horizontal_top.png",
				"ui/menubox/right_corner_top.png"));
	}
}
