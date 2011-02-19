package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;

public class Sizer
{
	public enum SizerDirection
	{
		HORIZONTAL, VERTICAL;
	}

	private final List<UIControl> aControls = new ArrayList<UIControl>();
	private final SizerDirection aDirection;
	private final UIControl aParent;
	private final Map<UIControl, Integer> aSprings = new HashMap<UIControl, Integer>();

	public Sizer(final SizerDirection direction, final UIControl parent)
	{
		aDirection = direction;
		aParent = parent;
	}

	void addControl(final UIControl control, final int spring)
	{
		aControls.add(control);
		aSprings.put(control, spring);
		recomputeSizes();
	}

	void recomputeSizes()
	{
		final Bounds bounds = aParent.getInnerBounds();
		int totalSprings = 0;
		int availableWidth = bounds.width;
		int availableHeight = bounds.height;
		// First, compute available size and total springs
		for (final UIControl control : aControls) {
			totalSprings += aSprings.get(control);
			if (aDirection.equals(SizerDirection.HORIZONTAL)) {
				availableWidth -= control.getMinimumSize().width;
			}
			else {
				availableHeight -= control.getMinimumSize().height;
			}
		}
		totalSprings = Math.max(1, totalSprings); // Must be at least one spring to avoid division by 0
		// Now that we know available dimensions and springs, iterate again to compute bounds
		int currentWidth = bounds.x;
		int currentHeight = bounds.y;
		final float springWidth = availableWidth / totalSprings;
		final float springHeight = availableHeight / totalSprings;
		for (final UIControl control : aControls) {
			final int spring = aSprings.get(control);
			final Dimension dim = control.getMinimumSize();
			if (aDirection.equals(SizerDirection.HORIZONTAL)) {
				final int controlWidth = (int) (springWidth * spring + dim.width);
				control.setOuterBounds(new Bounds(currentWidth, currentHeight, controlWidth, bounds.height));
				currentWidth += controlWidth;
			}
			else {
				final int controlHeight = (int) (springHeight * spring + dim.height);
				control.setOuterBounds(new Bounds(currentWidth, currentHeight, bounds.width, controlHeight));
				currentHeight += controlHeight;
			}
		}
	}
}
