package com.evervoid.client.ui;

import com.jme3.math.ColorRGBA;

/**
 * A pretty panel with a title and proper margins
 */
public class PanelControl extends WrapperControl
{
	private static ColorRGBA sPanelTitleColor = new ColorRGBA(0.8f, 0.8f, 1f, 1f);
	private final StaticTextControl aPanelTitle;

	public PanelControl(final String title)
	{
		super(new UIControl(BoxDirection.VERTICAL));
		final BoxControl box = new BoxControl(BoxDirection.VERTICAL);
		aPanelTitle = new StaticTextControl(title, sPanelTitleColor);
		box.addUI(aPanelTitle);
		box.addUI(new SpacerControl(1, 8));
		// Add line here
		box.addUI(aContained, 1);
		final MarginSpacer margins = new MarginSpacer(8, 8, 8, 8, box);
		addChildUI(margins, 1);
	}

	public void setTitle(final String title)
	{
		aPanelTitle.setText(title);
	}
}
