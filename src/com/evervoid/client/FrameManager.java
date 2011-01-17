package com.evervoid.client;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.graphics.FrameUpdate;

public class FrameManager
{
	private static final Set<FrameObserver> sObservers = new HashSet<FrameObserver>();

	public static void register(final FrameObserver observer)
	{
		sObservers.add(observer);
	}

	public static void tick(final FrameUpdate f)
	{
		for (final FrameObserver o : sObservers) {
			o.frame(f);
		}
	}

	public static void unregister(final FrameObserver observer)
	{
		sObservers.remove(observer);
	}
}
