package com.evervoid.client.ui;

import com.jme3.math.ColorRGBA;

/**
 * A pretty panel with a title and proper margins
 */
public class PanelControl extends WrapperControl
{
	/**
	 * The color of the font to use for the title of the panel
	 */
	public static final ColorRGBA sPanelTitleColor = new ColorRGBA(0.8f, 0.8f, 1f, 1f);
	/**
	 * The {@link BoxControl} used to give the panel its look
	 */
	private final BoxControl aInnerBox;
	/**
	 * The {@link StaticTextControl} displaying the title of the panel
	 */
	private final StaticTextControl aPanelTitle;
	/**
	 * The {@link UIControl} in which the panel title {@link StaticTextControl} is
	 */
	private final UIControl aPanelTitleBox;

	/**
	 * Constructor
	 * 
	 * @param title
	 *            The title displayed at the top of the panel
	 */
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
		aInnerBox.addUI(aContained, 1);
		final MarginSpacer margins = new MarginSpacer(8, 8, 8, 8, aInnerBox);
		addChildUI(margins, 1);
	}

	/**
	 * @return The bottom margin applied to the panel
	 */
	public float getBottomMargin()
	{
		return aInnerBox.getBottomMargin();
	}

	/**
	 * @return The left margin applied to the panel
	 */
	public float getLeftMargin()
	{
		return aInnerBox.getLeftMargin();
	}

	/**
	 * @return The right margin applied to the panel
	 */
	public float getRightMargin()
	{
		return aInnerBox.getRightMargin();
	}

	/**
	 * @return The {@link UIControl} in which the panel title is. Useful in order to add elements next to the title.
	 */
	public UIControl getTitleBox()
	{
		return aPanelTitleBox;
	}

	/**
	 * @return The top margin applied to the panel
	 */
	public float getTopMargin()
	{
		return aInnerBox.getTopMargin();
	}

	/**
	 * Change the title of this panel
	 * 
	 * @param title
	 *            The new title of the panel
	 */
	public void setTitle(final String title)
	{
		aPanelTitle.setText(title);
	}
}
