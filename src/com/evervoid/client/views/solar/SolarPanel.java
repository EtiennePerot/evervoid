package com.evervoid.client.views.solar;

import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.Bounds;
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

	@Override
	protected Bounds getBounds()
	{
		// Overwriting this to extend visibility to package
		return super.getBounds();
	}
}
