package com.evervoid.state.observers;

import com.evervoid.state.Wormhole;

/**
 * WormholeObserver is a template for Objects wishing to observe {@link Wormhole}. Wormholes will broadcast to all their
 * observers when appropriate. The first parameter of the broadcasting methods will be the broadcasting Wormhole, in order to
 * allow Objects to observer multiple Wormholes at once.
 */
public interface WormholeObserver
{
}
