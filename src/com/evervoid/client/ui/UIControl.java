package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.Collections;
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

	private Bounds aComputedBounds;
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

	void addChildUI(final Resizeable control)
	{
		addChildUI(control, 0);
	}

	void addChildUI(final Resizeable control, final int spring)
	{
		aControls.add(control);
		aSprings.put(control, spring);
		addNode((EverNode) control);
	}

	/**
	 * Add a spacer to the inner UIControl, with no spring.
	 * 
	 * @param width
	 *            The width of the spacer
	 * @param height
	 *            The height of the spacer
	 */
	public void addSpacer(final int width, final int height)
	{
		addUI(new SpacerControl(width, height));
	}

	/**
	 * Add a control to the inner UIControl with no spring
	 * 
	 * @param control
	 *            The control to add
	 */
	public void addUI(final Resizeable control)
	{
		addUI(control, 0);
	}

	/**
	 * Add a control to the inner UIControl. Overridden by container subclasses
	 * 
	 * @param control
	 *            The control to add
	 * @param spring
	 *            The spring value
	 */
	public void addUI(final Resizeable control, final int spring)
	{
		addChildUI(control, spring);
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
		aComputedBounds = bounds;
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
		final List<Resizeable> controls = new ArrayList<Resizeable>(aControls);
		if (aDirection.equals(BoxDirection.VERTICAL)) {
			springSize = availHeight / Math.max(1, totalSprings);
			// If this is vertical, we want the first control to be at the top, so reverse the
			Collections.reverse(controls);
		}
		int currentX = 0;
		int currentY = 0;
		for (final Resizeable c : controls) {
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
		String str = getClass().getSimpleName() + " - " + aComputedBounds + " with minimum " + getMinimumSize() + " ("
				+ aDirection.toString().toLowerCase() + ")";
		if (aControls.isEmpty()) {
			return str;
		}
		str += " {\n";
		for (final Resizeable c : aControls) {
			str += prefix + "\tSpring " + aSprings.get(c) + ": " + c.toString(prefix + "\t") + "\n";
		}
		return str + prefix + "}";
	}
}
