package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

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
			totalWidth += d.width;
			totalHeight += d.height;
		}
		return new Dimension(totalWidth, totalHeight);
	}

	@Override
	public void offsetBy(final Vector2f offset)
	{
		aOffset.translate(offset);
	}

	@Override
	public void sizeTo(final Dimension dimension)
	{
		int availWidth = dimension.width;
		int totalSprings = 0;
		final Map<Resizeable, Dimension> minimumSizes = new HashMap<Resizeable, Dimension>();
		for (final Resizeable c : aControls) {
			final Dimension d = c.getMinimumSize();
			minimumSizes.put(c, d);
			availWidth -= d.width;
			totalSprings += aSprings.get(c);
		}
		final float springSize = availWidth / Math.max(1, totalSprings);
		int currentX = 0;
		final int currentY = 0;
		for (final Resizeable c : aControls) {
			final Dimension d = minimumSizes.get(c);
			final int cWidth = (int) (d.width + aSprings.get(c) * springSize);
			c.sizeTo(new Dimension(cWidth, d.height));
			c.offsetBy(new Vector2f(currentX, currentY));
			currentX += cWidth;
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
			str += prefix + c.toString(prefix + "\t") + "\n";
		}
		return str + prefix + "}";
	}
}
