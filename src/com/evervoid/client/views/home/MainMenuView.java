package com.evervoid.client.views.home;

import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;

public class MainMenuView extends EverUIView
{
	public MainMenuView()
	{
		super(new UIControl(BoxDirection.VERTICAL));
		addUI(new CenteredControl(new MainMenuPanel()), 1);
		setBounds(Bounds.getWholeScreenBounds());
	}
}
