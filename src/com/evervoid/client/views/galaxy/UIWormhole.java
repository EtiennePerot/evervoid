package com.evervoid.client.views.galaxy;

import com.evervoid.state.Wormhole;
import com.evervoid.state.observers.WormholeObserver;

public class UIWormhole implements WormholeObserver
{
	protected UIWormhole(final Wormhole pWormhole)
	{
		pWormhole.registerObserver(this);
	}
}
