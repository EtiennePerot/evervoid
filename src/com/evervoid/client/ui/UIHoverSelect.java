package com.evervoid.client.ui;

import com.evervoid.client.views.Bounds;

public class UIHoverSelect extends WrapperControl
{
	private static final int sXYHoverOffset = 4;
	private final UIControl aParent;

	public UIHoverSelect(final UIControl parent)
	{
		super(new MarginSpacer(0, 0, sXYHoverOffset, sXYHoverOffset, new UIControl()), BoxDirection.HORIZONTAL);
		aParent = parent;
		final BackgroundedUIControl bg = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/selectedbackground.png");
		bg.addChildUI(aContained, 1);
		addChildUI(bg, 1);
		getNewTransform().translate(-sXYHoverOffset, -sXYHoverOffset, -UIControl.sChildZOffset / 2f);
		parentBoundsChanged();
		parent.addNode(this);
	}

	void parentBoundsChanged()
	{
		final Bounds parentBounds = aParent.getAbsoluteComputedBounds();
		setBounds(new Bounds(0, 0, parentBounds.width + 2 * sXYHoverOffset, parentBounds.height + 2 * sXYHoverOffset));
	}
}
