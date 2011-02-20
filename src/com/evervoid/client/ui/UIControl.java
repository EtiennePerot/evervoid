package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;

public class UIControl extends EverNode implements Resizeable
{
	public enum BoxDirection
	{
		HORIZONTAL, VERTICAL;
	}

	private final List<Resizeable> aControls = new ArrayList<Resizeable>();
	private final BoxDirection aDirection;
	private final Transform aOffset;
	private final Map<Resizeable, Integer> aSprings = new HashMap<Resizeable, Integer>();

	public UIControl()
	{
		this(BoxDirection.HORIZONTAL);
	}

	public UIControl(final BoxDirection direction)
	{
		aDirection = direction;
		aOffset = getNewTransform();
	}

	public void addUI(final Resizeable control)
	{
		addUI(control, 0);
	}

	public void addUI(final Resizeable control, final int spring)
	{
		aControls.add(control);
		aSprings.put(control, spring);
		addNode((EverNode) control);
	}

	@Override
	public Dimension getMinimumSize()
	{
		int totalWidth = 0;
		int totalHeight = 0;
		for (final Resizeable c : aControls) {
			final Dimension d = c.getMinimumSize();
			if (aDirection.equals(BoxDirection.HORIZONTAL)) {
				totalWidth += d.width;
				totalHeight = Math.max(totalHeight, d.height);
			}
			else {
				totalWidth = Math.max(totalWidth, d.width);
				totalHeight += d.height;
			}
		}
		return new Dimension(totalWidth, totalHeight);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		System.out.println(getClass().getSimpleName() + " bounded " + bounds);
		aOffset.translate(bounds.x, bounds.y);
		int availWidth = bounds.width;
		int availHeight = bounds.height;
		int totalSprings = 0;
		final Map<Resizeable, Dimension> minimumSizes = new HashMap<Resizeable, Dimension>();
		for (final Resizeable c : aControls) {
			final Dimension d = c.getMinimumSize();
			minimumSizes.put(c, d);
			availWidth -= d.width;
			availHeight -= d.height;
			totalSprings += aSprings.get(c);
		}
		float springSize = availWidth / Math.max(1, totalSprings);
		if (aDirection.equals(BoxDirection.VERTICAL)) {
			springSize = availHeight / Math.max(1, totalSprings);
		}
		int currentX = 0;
		int currentY = 0;
		for (final Resizeable c : aControls) {
			final Dimension d = minimumSizes.get(c);
			if (aDirection.equals(BoxDirection.HORIZONTAL)) {
				final int cWidth = (int) (d.width + aSprings.get(c) * springSize);
				c.setBounds(new Bounds(currentX, currentY, cWidth, bounds.height));
				currentX += cWidth;
			}
			else {
				final int cHeight = (int) (d.height + aSprings.get(c) * springSize);
				c.setBounds(new Bounds(currentX, currentY, bounds.width, cHeight));
				currentY += cHeight;
			}
		}
	}

	@Override
	public String toString()
	{
		return toString("");
	}

	@Override
	public String toString(final String prefix)
	{
		String str = getClass().getSimpleName() + " - " + getMinimumSize();
		if (aControls.isEmpty()) {
			return str;
		}
		str += " {\n";
		for (final Resizeable c : aControls) {
			str += prefix + "\t" + c.toString(prefix + "\t") + "\n";
		}
		return str + prefix + "}";
	}
}
