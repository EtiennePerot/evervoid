package com.evervoid.client.ui;

import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

public class StaticTextControl extends UIControl
{
	private final Dimension aDimension;
	private final BaseText aLabel;

	public StaticTextControl(final String text, final ColorRGBA color)
	{
		aLabel = new BaseText(text, color);
		aDimension = new Dimension((int) aLabel.getWidth(), (int) aLabel.getHeight());
		addNode(aLabel);
	}

	@Override
	public Dimension getMinimumSize()
	{
		return aDimension;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aLabel.setRenderBounds(bounds);
	}

	public void setText(final String text)
	{
		aLabel.setText(text);
	}
}
