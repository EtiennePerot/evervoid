package com.evervoid.client.ui;

import com.jme3.math.ColorRGBA;

/**
 * A pretty panel with a title and proper margins
 */
public class PanelControl extends WrapperControl
{
	public static ColorRGBA sPanelTitleColor = new ColorRGBA(0.8f, 0.8f, 1f, 1f);
	private final BoxControl aInnerBox;
	private final StaticTextControl aPanelTitle;
	private final UIControl aPanelTitleBox;

	public PanelControl(final String title)
	{
		super(new UIControl(BoxDirection.VERTICAL));
		aInnerBox = new BoxControl(BoxDirection.VERTICAL);
		aPanelTitleBox = new UIControl(BoxDirection.HORIZONTAL);
		aPanelTitle = new StaticTextControl(title, sPanelTitleColor, "redensek", 28);
		aPanelTitle.setKeepBoundsOnChange(false);
		aPanelTitleBox.addUI(aPanelTitle);
		aPanelTitleBox.addFlexSpacer(1);
		aInnerBox.addUI(aPanelTitleBox);
		aInnerBox.addUI(new SpacerControl(1, 16));
		// TODO: Add pretty horizontal line here?
		aInnerBox.addUI(aContained, 1);
		final MarginSpacer margins = new MarginSpacer(8, 8, 8, 8, aInnerBox);
		addChildUI(margins, 1);
	}

	public float getBottomMargin()
	{
		return aInnerBox.getBottomMargin();
	}

	public float getLeftMargin()
	{
		return aInnerBox.getLeftMargin();
	}

	public float getRightMargin()
	{
		return aInnerBox.getRightMargin();
	}

	protected UIControl getTitleBox()
	{
		return aPanelTitleBox;
	}

	public float getTopMargin()
	{
		return aInnerBox.getTopMargin();
	}

	public void setTitle(final String title)
	{
		aPanelTitle.setText(title);
	}
}
