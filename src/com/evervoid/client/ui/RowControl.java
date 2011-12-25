package com.evervoid.client.ui;

/**
 * A RowControl is a UIControl with special borders to make it look like it is part of a continuous table.
 */
public class RowControl extends BorderedControl
{
	/**
	 * Returns the sprite to use for borders within this class.
	 * 
	 * @param isFirst
	 *            Whether the {@link RowControl} is the first of the table or not
	 * @param isLast
	 *            Whether the {@link RowControl} is the last of the table or not (note that a {@link RowControl} may be both
	 *            first and last if there is only one row)
	 * @param left
	 *            Whether the left border is desired (true) or the right one (false)
	 * @return The name of the sprite to use
	 */
	private static String getRowBorderSprite(final boolean isFirst, final boolean isLast, final boolean left)
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

	/**
	 * Constructor; assumes the {@link RowControl} is the only row of the table
	 */
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
	public UIControl addUI(final UIControl control, final int spring)
	{
		aContained.addUI(new VerticalCenteredControl(control), spring);
		aContained.addSpacer(8, 1);
		return this;
	}

	/**
	 * Update the border sprites of this row accoesing to its position
	 * 
	 * @param isFirst
	 *            Whether this {@link RowControl} is now first
	 * @param isLast
	 *            Whether this {@link RowControl} is now last (note that a {@link RowControl} may be both first and last if
	 *            there is only one row)
	 */
	public void updateRowSprites(final boolean isFirst, final boolean isLast)
	{
		setLeftSprite(getRowBorderSprite(isFirst, isLast, true));
		setRightSprite(getRowBorderSprite(isFirst, isLast, false));
	}
}
