package com.evervoid.client.ui;

public class BorderedControl extends UIControl
{
	public BorderedControl(final Resizeable left, final Resizeable middle, final Resizeable right)
	{
		super(BoxDirection.HORIZONTAL);
		addUI(left);
		addUI(middle, 1);
		addUI(right);
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
