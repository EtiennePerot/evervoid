package com.evervoid.client.ui;

/**
 * A Box control. Makes a pretty border around the given element.
 */
public class BoxControl extends WrapperControl
{
	/**
	 * The image to use as botttom border
	 */
	private final ImageControl aBottomImage;
	/**
	 * The image to use as left border
	 */
	private final ImageControl aLeftImage;
	/**
	 * The image to use as right border
	 */
	private final ImageControl aRightImage;
	/**
	 * The image to use as top border
	 */
	private final ImageControl aTopImage;

	/**
	 * Constructor; defaults to vertical alignment
	 */
	public BoxControl()
	{
		this(BoxDirection.VERTICAL);
	}

	/**
	 * Constructor
	 * 
	 * @param direction
	 *            The direction of the inner {@link UIControl}
	 */
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

	/**
	 * @return The height of the bottom margin area
	 */
	public float getBottomMargin()
	{
		return aBottomImage.getHeight();
	}

	/**
	 * @return The width of the left margin area
	 */
	public float getLeftMargin()
	{
		return aLeftImage.getWidth();
	}

	/**
	 * @return The width of the right margin area
	 */
	public float getRightMargin()
	{
		return aRightImage.getWidth();
	}

	/**
	 * @return The height of the top margin area
	 */
	public float getTopMargin()
	{
		return aTopImage.getHeight();
	}
}
