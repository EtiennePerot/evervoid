package com.evervoid.client.ui;

public class MarginSpacer extends WrapperControl
{
	public MarginSpacer(final int left, final int right, final int top, final int bottom, final Resizeable contained)
	{
		super(contained, BoxDirection.HORIZONTAL);
		addChildUI(new SpacerControl(left, 1));
		final UIControl center = new UIControl(BoxDirection.VERTICAL);
		center.addChildUI(new SpacerControl(1, top));
		center.addChildUI(contained, 1);
		center.addChildUI(new SpacerControl(1, bottom));
		addChildUI(center, 1);
		addChildUI(new SpacerControl(right, 1));
	}
}
