package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.views.Bounds;
import com.jme3.math.Vector2f;

public class ScrollingArea extends UIControl
{
	private static final float sScrollMultiplier = 12;
	private float aMaxHeight = Float.MAX_VALUE;
	private float aOffset = 0;
	private final List<UIControl> aScrollingChildren = new ArrayList<UIControl>();
	private float aTotalHeight = 0;

	public ScrollingArea()
	{
		super(BoxDirection.VERTICAL);
	}

	@Override
	void addChildUI(final UIControl control, int spring)
	{
		if (control == null) {
			return;
		}
		if (spring != 0) {
			System.err.println("CAUTION: Trying to add a non-zero-spring control to a ScrollingArea. Overriding to 0 spring!");
			spring = 0;
		}
		if (aScrollingChildren.contains(control)) {
			System.err.println("Warning: Trying to add the same UIControl twice.");
		}
		aScrollingChildren.add(control);
		aTotalHeight += control.getMinimumHeight();
		recomputeAllBounds();
	}

	@Override
	protected boolean inBounds(final Vector2f point)
	{
		return point != null
				&& aComputedBounds != null
				&& (aComputedBounds.x <= point.x && aComputedBounds.y <= point.y
						&& aComputedBounds.x + aComputedBounds.width > point.x && aComputedBounds.y + aComputedBounds.height > point.y);
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final Vector2f position)
	{
		if (!inBounds(position)) {
			return false;
		}
		aOffset = Math.min(aTotalHeight - aComputedBounds.height, aOffset + delta * sScrollMultiplier);
		setBounds(getComputedBounds());
		return true;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final Vector2f position)
	{
		if (!inBounds(position)) {
			return false;
		}
		aOffset = Math.max(0, aOffset - delta * sScrollMultiplier);
		setBounds(getComputedBounds());
		return true;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		// Never increase aMaxHeight
		aMaxHeight = Math.min(aMaxHeight, bounds.height);
		aComputedBounds = new Bounds(bounds.x, bounds.y, bounds.width, aMaxHeight);
		float heightSoFar = 0;
		int firstChild = 0;
		while (heightSoFar < aOffset && firstChild < aScrollingChildren.size()) {
			heightSoFar += aScrollingChildren.get(firstChild).getMinimumHeight();
			firstChild++;
		}
		int lastChild = firstChild;
		while (heightSoFar < aOffset + aMaxHeight && lastChild < aScrollingChildren.size()) {
			heightSoFar += aScrollingChildren.get(lastChild).getMinimumHeight();
			lastChild++;
		}
		delAllNodes();
		float childrenYOffset = 0;
		for (int i = firstChild; i < lastChild; i++) {
			final UIControl child = aScrollingChildren.get(i);
			addNode(child);
			final int minHeight = child.getMinimumHeight();
			childrenYOffset += minHeight;
			child.setBounds(new Bounds(0, bounds.height - childrenYOffset, bounds.width, minHeight));
		}
	}
}
