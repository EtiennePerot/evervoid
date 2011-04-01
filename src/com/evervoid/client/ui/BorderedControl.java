package com.evervoid.client.ui;

/**
 * Wraps a UIControl to have a left and a right border image around it.
 */
public class BorderedControl extends WrapperControl
{
	private final ImageControl aLeftControl;
	private final ImageControl aRightControl;

	public BorderedControl(final ImageControl left, final UIControl middle, final ImageControl right)
	{
		this(left, middle, right, BoxDirection.HORIZONTAL);
	}

	public BorderedControl(final ImageControl left, final UIControl middle, final ImageControl right,
			final BoxDirection direction)
	{
		super(middle, direction);
		aLeftControl = left;
		aRightControl = right;
		addChildUI(aLeftControl);
		addChildUI(middle, 1);
		addChildUI(aRightControl);
	}

	public BorderedControl(final String leftSprite, final String middleSprite, final String rightSprite)
	{
		this(leftSprite, new ImageControl(middleSprite), rightSprite);
	}

	public BorderedControl(final String leftSprite, final String middleSprite, final String rightSprite,
			final BoxDirection direction)
	{
		this(new ImageControl(leftSprite), new ImageControl(middleSprite), new ImageControl(rightSprite), direction);
	}

	public BorderedControl(final String leftSprite, final UIControl middle, final String rightSprite)
	{
		this(new ImageControl(leftSprite), middle, new ImageControl(rightSprite));
	}

	public BorderedControl(final String leftSprite, final UIControl middle, final String rightSprite,
			final BoxDirection direction)
	{
		this(new ImageControl(leftSprite), middle, new ImageControl(rightSprite), direction);
	}

	public void setLeftSprite(final String sprite)
	{
		aLeftControl.setSprite(sprite);
	}

	public void setRightSprite(final String sprite)
	{
		aRightControl.setSprite(sprite);
	}
}
