package com.evervoid.client.views.credits;

import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;

public class CreditsView extends EverUIView
{
	public CreditsView()
	{
		super(new UIControl(BoxDirection.VERTICAL));
		addUI(new CenteredControl(new CreditsPanel()), 1);
		setBounds(Bounds.getWholeScreenBounds());
	}
}
