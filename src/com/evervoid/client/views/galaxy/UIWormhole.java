package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.ui.PlainLine;
import com.evervoid.state.Wormhole;
import com.evervoid.state.observers.WormholeObserver;

public class UIWormhole extends EverNode implements WormholeObserver
{
	private final PlainLine aLine;
	private final Transform aTransform;
	private final Wormhole aWormhole;

	protected UIWormhole(final Wormhole pWormhole)
	{
		aWormhole = pWormhole;
		pWormhole.registerObserver(this);
		aLine = pWormhole.getLine(1f);
		// wormholes should be slightly transparent
		aLine.setAlpha(.9f);
		addNode(aLine);
		// store original transform for reference
		aTransform = getNewTransform();
	}

	public float getLength()
	{
		return aWormhole.getLength();
	}

	public Transform getOriginalTransform()
	{
		return aTransform;
	}
}
