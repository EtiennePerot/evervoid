package com.evervoid.client.ui;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.EverVoidClient.NodeType;
import com.evervoid.client.views.Bounds;

public class UIHoverSelect extends WrapperControl
{
	private static final int sXYHoverOffset = 4;

	public UIHoverSelect(final UIControl parent)
	{
		super(new MarginSpacer(0, 0, sXYHoverOffset, sXYHoverOffset, new UIControl()), BoxDirection.HORIZONTAL);
		final BackgroundedUIControl bg = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/selectionbox/middle.png");
		bg.addChildUI(aContained, 1);
		addChildUI(bg, 1);
		final Bounds parentBounds = parent.getAbsoluteComputedBounds();
		getNewTransform().translate(-sXYHoverOffset, -sXYHoverOffset,
				parent.getWorldTranslation().z - 3 * UIControl.sChildZOffset);
		setBounds(new Bounds(parentBounds.x, parentBounds.y, parentBounds.width + 2 * sXYHoverOffset, parentBounds.height + 2
				* sXYHoverOffset));
		EverVoidClient.addRootNode(NodeType.TWODIMENSION, this);
	}
}
