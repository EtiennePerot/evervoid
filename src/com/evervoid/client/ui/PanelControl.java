package com.evervoid.client.ui;

import com.jme3.math.ColorRGBA;

public class PanelControl extends BoxControl
{
	private static ColorRGBA sPanelTitleColor = new ColorRGBA(0.6f, 0.6f, 1f, 1f);

	public PanelControl(final String title, final Resizeable control)
	{
		final MarginSpacer margins = new MarginSpacer(8, 8, 8, 8, new UIControl(BoxDirection.VERTICAL));
		addUI(margins);
		margins.addUI(new StaticTextControl(title, sPanelTitleColor));
		margins.addUI(new SpacerControl(1, 8));
		// Add line here
		margins.addUI(aContained);
	}
}
