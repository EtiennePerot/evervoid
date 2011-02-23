package com.evervoid.client.views.lobby;

import com.evervoid.client.ui.BackgroundedUIControl;
import com.evervoid.client.ui.BorderedControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;

public class RowControl extends BorderedControl
{
	public RowControl()
	{
		super("ui/metalbox/left_round.png", new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/metalbox/horizontal.png"),
				"ui/metalbox/right_round.png");
	}

	@Override
	public void addUI(final UIControl control)
	{
		(aContained).addUI(new VerticalCenteredControl(control));
	}
}
