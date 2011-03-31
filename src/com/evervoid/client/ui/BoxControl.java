package com.evervoid.client.ui;

/**
 * A Box control. Makes a pretty border around the given element.
 */
public class BoxControl extends WrapperControl
{
	private final ImageControl aBottomImage;
	private final ImageControl aLeftImage;
	private final ImageControl aRightImage;
	private final ImageControl aTopImage;

	public BoxControl()
	{
		this(BoxDirection.VERTICAL);
	}

	public BoxControl(final BoxDirection direction)
	{
		super(new UIControl(direction), BoxDirection.VERTICAL);
		aBottomImage = new ImageControl("ui/menubox/horizontal_bottom.png", false);
		aLeftImage = new ImageControl("ui/menubox/vertical_left.png", true);
		aRightImage = new ImageControl("ui/menubox/vertical_right.png", true);
		aTopImage = new ImageControl("ui/menubox/horizontal_top.png", false);
		addChildUI(new BorderedControl("ui/menubox/left_corner_top.png", aTopImage, "ui/menubox/right_corner_top.png"));
		final BackgroundedUIControl bg = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/menubox/center.png");
		bg.addChildUI(aContained, 1);
		addChildUI(new BorderedControl(aLeftImage, bg, aRightImage), 1);
		addChildUI(new BorderedControl("ui/menubox/left_corner_bottom.png", aBottomImage, "ui/menubox/right_corner_bottom.png"));
	}

	public float getBottomMargin()
	{
		return aBottomImage.getHeight();
	}

	public float getLeftMargin()
	{
		return aLeftImage.getWidth();
	}

	public float getRightMargin()
	{
		return aRightImage.getWidth();
	}

	public float getTopMargin()
	{
		return aTopImage.getHeight();
	}
}
