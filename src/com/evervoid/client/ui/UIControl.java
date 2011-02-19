package com.evervoid.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;

public abstract class UIControl extends MultiSprite
{
	protected Bounds aBounds;
	protected Set<UIControl> aControls = new HashSet<UIControl>();

	public UIControl(final Bounds bounds)
	{
		aBounds = bounds;
	}

	public void addControl(final UIControl control)
	{
	}

	public Bounds getInnerBounds()
	{
		return aBounds;
	}

	public Dimension getMinimumSize()
	{
		return new Dimension(0, 0);
	}

	public Bounds getOuterBounds()
	{
		return aBounds;
	}
}
