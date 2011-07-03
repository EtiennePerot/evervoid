package com.evervoid.client.ui;

/**
 * Wraps a UIControl to have a left and a right border image around it.
 */
public class BorderedControl extends WrapperControl
{
	/**
	 * The {@link ImageControl} on the left of this control
	 */
	private final ImageControl aLeftControl;
	/**
	 * The {@link ImageControl} on the right of this control
	 */
	private final ImageControl aRightControl;

	/**
	 * Constructor
	 * 
	 * @param left
	 *            The {@link ImageControl} on the left of this control
	 * @param middle
	 *            The {@link UIControl} to wrap
	 * @param right
	 *            The {@link ImageControl} on the right of this control
	 */
	public BorderedControl(final ImageControl left, final UIControl middle, final ImageControl right)
	{
		super(middle, BoxDirection.HORIZONTAL);
		aLeftControl = left;
		aRightControl = right;
		addChildUI(aLeftControl);
		addChildUI(middle, 1);
		addChildUI(aRightControl);
	}

	/**
	 * Constructor
	 * 
	 * @param leftSprite
	 *            The file name of the image on the left of this control
	 * @param middleSprite
	 *            The file name of the image in the middle
	 * @param rightSprite
	 *            The file name of the image on the right of this control
	 */
	public BorderedControl(final String leftSprite, final String middleSprite, final String rightSprite)
	{
		this(leftSprite, new ImageControl(middleSprite), rightSprite);
	}

	/**
	 * Constructor
	 * 
	 * @param leftSprite
	 *            The file name of the image on the left of this control
	 * @param middle
	 *            The {@link UIControl} to wrap
	 * @param rightSprite
	 *            The file name of the image on the right of this control
	 */
	public BorderedControl(final String leftSprite, final UIControl middle, final String rightSprite)
	{
		this(new ImageControl(leftSprite), middle, new ImageControl(rightSprite));
	}

	/**
	 * Set the left sprite to a different one
	 * 
	 * @param sprite
	 *            The file name of the new sprite
	 */
	public void setLeftSprite(final String sprite)
	{
		aLeftControl.setSprite(sprite);
	}

	/**
	 * Set the right sprite to a different one
	 * 
	 * @param sprite
	 *            The file name of the new sprite
	 */
	public void setRightSprite(final String sprite)
	{
		aRightControl.setSprite(sprite);
	}
}
