package com.evervoid.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.views.Bounds;

public class Sizer
{
	public enum SizerDirection
	{
		HORIZONTAL, VERTICAL;
	}

	private final Map<UIControl, Integer> aControls = new HashMap<UIControl, Integer>();
	private final SizerDirection aDirection;
	private final UIControl aParent;
	private final Map<UIControl, Bounds> aSizes = new HashMap<UIControl, Bounds>();

	public Sizer(final SizerDirection direction, final UIControl parent)
	{
		aDirection = direction;
		aParent = parent;
	}

	void addControl(final UIControl control, final int proportion)
	{
		aControls.put(control, proportion);
		recomputeSizes();
	}

	void recomputeSizes()
	{
		final Bounds bounds = aParent.getInnerBounds();
		final int totalSprings = 0;
		final int availableWidth = bounds.width;
		final int availableHeight = bounds.height;
		for (final UIControl control : aControls.keySet()) {
			final int spring = aControls.get(control);
			switch (aDirection) {
				case HORIZONTAL:
					if (spring == 0) {
						// TODO
					}
			}
		}
	}
}
