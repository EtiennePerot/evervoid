package com.evervoid.client.views.lobby;

import com.evervoid.client.ui.BackgroundedUIControl;
import com.evervoid.client.ui.BorderedControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;

public class RowControl extends BorderedControl
{
	public RowControl()
	{
		super("ui/metalbox/left_round_20.png", new BackgroundedUIControl(BoxDirection.HORIZONTAL,
				"ui/metalbox/horizontal_20.png"), "ui/metalbox/right_round_20.png");
	}

	@Override
	public void addUI(final UIControl control)
	{
		aContained.addUI(new VerticalCenteredControl(control));
		aContained.addSpacer(8, 1);
	}

	@Override
	public void addUI(final UIControl control, final int spring)
	{
		aContained.addUI(new VerticalCenteredControl(control), spring);
		aContained.addSpacer(8, 1);
	}
}
