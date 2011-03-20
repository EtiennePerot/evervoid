package com.evervoid.client.ui;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.Sizeable;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.jme3.math.Vector2f;

public class RescalableControl extends UIControl
{
	private EverNode aNode;
	private Transform aResizing;
	private final Sizeable aSizeable;

	public RescalableControl(final Sizeable node)
	{
		aSizeable = node;
		if (node instanceof EverNode) {
			aNode = (EverNode) node;
			aResizing = aNode.getNewTransform();
			addNode(aNode);
		}
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		if (aResizing == null) {
			return;
		}
		final Vector2f nodeDim = aSizeable.getDimensions();
		aResizing.setScale(Math.min(1, Math.min(bounds.width / nodeDim.x, bounds.height / nodeDim.y))).translate(
				(float) bounds.width / 2, (float) bounds.height / 2);
	}
}
