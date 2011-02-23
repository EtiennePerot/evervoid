package com.evervoid.client.ui;

import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

public class StaticTextControl extends UIControl
{
	private Dimension aDimension;
	private final BaseText aLabel;

	public StaticTextControl(final String text, final ColorRGBA color)
	{
		this(text, color, "squarehead", 24);
	}

	public StaticTextControl(final String text, final ColorRGBA color, final String font, final int size)
	{
		aLabel = new BaseText("", color, font, size);
		addNode(aLabel);
		setText(text);
	}

	public float getHeight()
	{
		return aDimension.height;
	}

	public float getLineHeight()
	{
		return aLabel.getLineHeight();
	}

	public int getLines()
	{
		return aLabel.getLines();
	}

	@Override
	public Dimension getMinimumSize()
	{
		return aDimension;
	}

	public float getWidth()
	{
		return aDimension.width;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		// Do not pass x and y because the label is already a child of this node.
		// Passing it x and y would translate it further.
		aLabel.setRenderBounds(new Bounds(0, 0, bounds.width, bounds.height));
	}

	public void setColor(final ColorRGBA color)
	{
		aLabel.setColor(color);
	}

	public void setColor(final int start, final int end, final ColorRGBA color)
	{
		aLabel.setColor(start, end, color);
	}

	public void setText(final String text)
	{
		setText(text, true);
	}

	public void setText(final String text, final boolean keepBounds)
	{
		if (!keepBounds) {
			aLabel.setRenderBounds(null); // Reset bounds on the text, otherwise dimension computation is wrong
		}
		aLabel.setText(text);
		aDimension = new Dimension((int) aLabel.getWidth(), (int) aLabel.getHeight());
		System.out.println("Dimension of text " + text + " is now " + aDimension);
		recomputeAllBounds();
	}
}
