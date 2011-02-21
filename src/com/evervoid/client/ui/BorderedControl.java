package com.evervoid.client.ui;

/**
 * Wraps a UIControl to have a left and a right border image around it.
 */
public class BorderedControl extends WrapperControl
{
	public BorderedControl(final Resizeable left, final Resizeable middle, final Resizeable right)
	{
		super(middle, BoxDirection.HORIZONTAL);
		addChildUI(left);
		addChildUI(middle, 1);
		addChildUI(right);
	}

	public BorderedControl(final String leftSprite, final Resizeable middle, final String rightSprite)
	{
		this(new UIConnector(leftSprite), middle, new UIConnector(rightSprite));
	}

	public BorderedControl(final String leftSprite, final String middleSprite, final String rightSprite)
	{
		this(leftSprite, new UIConnector(middleSprite), rightSprite);
	}
}
