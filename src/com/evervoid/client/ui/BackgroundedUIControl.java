package com.evervoid.client.ui;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;

public class BackgroundedUIControl extends EverNode implements Resizeable
{
	private final UIConnector aBackground;
	private final Resizeable aContained;

	public BackgroundedUIControl(final Resizeable contained, final String background)
	{
		aContained = contained;
		aBackground = new UIConnector(background);
		// Put background in background
		aBackground.getNewTransform().translate(0, 0, -1);
		addNode((EverNode) aContained);
		addNode(aBackground);
	}

	@Override
	public Dimension getMinimumSize()
	{
		return aContained.getMinimumSize();
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		aBackground.setBounds(bounds);
		aContained.setBounds(bounds);
	}

	@Override
	public String toString(final String prefix)
	{
		return aContained.toString(prefix) + " [Backgrounded by " + aBackground + "]";
	}
}
