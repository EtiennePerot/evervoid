package com.evervoid.client.views.solar;

import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.EverUIView;

/**
 * Bottom bar on the solar system view
 */
public class SolarPanel extends EverUIView
{
	public SolarPanel()
	{
		super(new UIControl());
	}

	public void setUI(final UIControl ui)
	{
		delAllChildUIs();
		addUI(ui, 1);
	}
}
