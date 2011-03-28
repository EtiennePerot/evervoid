package com.evervoid.client.views.research;

import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;

public class ResearchView extends EverUIView
{
	public ResearchView(final Bounds bounds)
	{
		super(new UIControl(BoxDirection.VERTICAL));
		addUI(new CenteredControl(new ResearchPanel()), 1);
		setBounds(bounds);
	}
}
