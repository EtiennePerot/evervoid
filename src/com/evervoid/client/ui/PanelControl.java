package com.evervoid.client.ui;

import com.jme3.math.ColorRGBA;

/**
 * A pretty panel with a title and proper margins
 */
public class PanelControl extends WrapperControl
{
	private static ColorRGBA sPanelTitleColor = new ColorRGBA(0.8f, 0.8f, 1f, 1f);

	public PanelControl(final String title)
	{
		super(new UIControl(BoxDirection.VERTICAL));
		final BoxControl box = new BoxControl(BoxDirection.VERTICAL);
		final MarginSpacer margins = new MarginSpacer(8, 8, 8, 8, box);
		addChildUI(margins, 1);
		box.addUI(new StaticTextControl(title, sPanelTitleColor));
		box.addUI(new SpacerControl(1, 8));
		// Add line here
		box.addUI(aContained, 1);
	}
}
