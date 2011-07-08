package com.evervoid.client.ui;

/**
 * Multiple {@link SpacerControl}s in one convenient wrapper class
 */
public class MarginSpacer extends WrapperControl
{
	/**
	 * Constructor
	 * 
	 * @param left
	 *            Amount of spacing (in pixels) to apply to the left of the contained {@link UIControl}
	 * @param right
	 *            Amount of spacing (in pixels) to apply to the right of the contained {@link UIControl}
	 * @param top
	 *            Amount of spacing (in pixels) to apply to the top of the contained {@link UIControl}
	 * @param bottom
	 *            Amount of spacing (in pixels) to apply to the bottom of the contained {@link UIControl}
	 * @param contained
	 *            The contained {@link UIControl}
	 */
	public MarginSpacer(final int left, final int right, final int top, final int bottom, final UIControl contained)
	{
		super(contained);
		final UIControl center = new UIControl(BoxDirection.VERTICAL);
		center.addChildUI(new SpacerControl(1, top));
		center.addChildUI(contained, 1);
		center.addChildUI(new SpacerControl(1, bottom));
		addChildUI(new SpacerControl(left, 1));
		addChildUI(center, 1);
		addChildUI(new SpacerControl(right, 1));
	}
}
