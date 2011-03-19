package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.ui.PlainLine;
import com.evervoid.state.Wormhole;
import com.evervoid.state.observers.WormholeObserver;
import com.jme3.math.ColorRGBA;

public class UIWormhole extends EverNode implements WormholeObserver
{
	private final PlainLine aLine;
	private final Transform aTransform;
	private final Wormhole aWormhole;

	protected UIWormhole(final Wormhole pWormhole)
	{
		aWormhole = pWormhole;
		pWormhole.registerObserver(this);
		aLine = new PlainLine(MathUtils.point3DToVector3f(pWormhole.getSolarSystem1().getPoint3D()),
				MathUtils.point3DToVector3f(pWormhole.getSolarSystem2().getPoint3D()), 1f, ColorRGBA.Red);
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

	public PlainLine getLine(final float scale)
	{
		return aLine;
	}

	public Transform getOriginalTransform()
	{
		return aTransform;
	}

	public void setScale(final float scale)
	{
		aTransform.setScale(scale);
	}
}
