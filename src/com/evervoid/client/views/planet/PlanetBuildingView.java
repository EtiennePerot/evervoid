package com.evervoid.client.views.planet;

import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.EverUIView;

public class PlanetBuildingView extends EverUIView
{
	public PlanetBuildingView(final PlanetView parent)
	{
		super(new UIControl());
		addUI(new PanelControl("Buildings"), 1);
	}
}
