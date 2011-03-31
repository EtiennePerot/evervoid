package com.evervoid.client.ui;

public class RowControl extends BorderedControl
{
	static String getRowBorderSprite(final boolean isFirst, final boolean isLast, final boolean left)
	{
		final String side = left ? "left" : "right";
		if (isFirst) {
			if (isLast) {
				return "ui/metalbox/" + side + "_round_20.png";
			}
			return "ui/metalbox/" + side + "_round_square_20.png";
		}
		if (isLast) {
			return "ui/metalbox/" + side + "_square_round_20.png";
		}
		return "ui/metalbox/" + side + "_square_20.png";
	}

	public RowControl()
	{
		super(getRowBorderSprite(true, true, true), new BackgroundedUIControl(BoxDirection.HORIZONTAL,
				"ui/metalbox/horizontal_20.png"), getRowBorderSprite(true, true, false));
	}

	@Override
	public UIControl addUI(final UIControl control)
	{
		aContained.addUI(new VerticalCenteredControl(control));
		aContained.addSpacer(8, 1);
		return this;
	}

	@Override
	public void addUI(final UIControl control, final int spring)
	{
		aContained.addUI(new VerticalCenteredControl(control), spring);
		aContained.addSpacer(8, 1);
	}

	public void updateRowSprites(final boolean isFirst, final boolean isLast)
	{
		setLeftSprite(getRowBorderSprite(isFirst, isLast, true));
		setRightSprite(getRowBorderSprite(isFirst, isLast, false));
	}
}
