package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.ui.Sizer.SizerDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;

public class UIControl extends MultiSprite
{
	public static void main(final String[] args)
	{
		final UIControl root = new UIControl(new Bounds(5, 10, 50, 25));
		root.addControl(new UIControl(new Bounds(0, 0, 0, 0)), 2);
		final UIControl child = new UIControl(new Bounds(0, 0, 0, 0));
		root.addControl(child, 3);
		root.addControl(new UIControl(new Bounds(0, 0, 0, 0)), 1);
		System.out.println(root);
	}

	protected List<UIControl> aControls = new ArrayList<UIControl>();
	protected Bounds aMaximumBounds;
	private UIControl aParent = null;
	private final Sizer aSizer;

	public UIControl()
	{
		this(new Bounds(0, 0, 0, 0));
	}

	public UIControl(final Bounds bounds)
	{
		this(bounds, SizerDirection.HORIZONTAL);
	}

	public UIControl(final Bounds bounds, final SizerDirection direction)
	{
		aMaximumBounds = bounds;
		aSizer = new Sizer(direction, this);
	}

	public void addControl(final UIControl control)
	{
		addControl(control, 0);
	}

	public void addControl(final UIControl control, final int springs)
	{
		aControls.add(control);
		control.setControlParent(this);
		aSizer.addControl(control, springs);
		addNode(control);
	}

	public Bounds getInnerBounds()
	{
		return aMaximumBounds;
	}

	public Dimension getMinimumSize()
	{
		return new Dimension(0, 0);
	}

	public Bounds getOuterBounds()
	{
		return aMaximumBounds;
	}

	private void setControlParent(final UIControl parent)
	{
		aParent = parent;
		aParent.aSizer.recomputeSizes();
	}

	void setDirection(final SizerDirection direction)
	{
		aSizer.setDirection(direction);
	}

	void setOuterBounds(final Bounds bounds)
	{
		aMaximumBounds = bounds;
		aSizer.recomputeSizes();
	}

	@Override
	public String toString()
	{
		String str = getClass().getName() + "[" + aMaximumBounds + "]";
		if (aControls.isEmpty()) {
			return str;
		}
		str += "{";
		for (final UIControl control : aControls) {
			str += control.toString() + ", ";
		}
		return str.substring(0, str.length() - 2) + "}";
	}
}
