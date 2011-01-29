package com.evervoid.client;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.geometry.TransformManager;

public class EVFrameManager
{
	private static final List<FrameObserver> sObservers = new ArrayList<FrameObserver>();
	private static TransformManager sTransformManager = null;

	public static void register(final FrameObserver observer)
	{
		sObservers.add(observer);
	}

	public static void setTransformManager(final TransformManager manager)
	{
		sTransformManager = manager;
	}

	public static void tick(final FrameUpdate f)
	{
		for (final FrameObserver o : sObservers) {
			o.frame(f);
		}
		// Always notify Transform manager last
		if (sTransformManager != null) {
			sTransformManager.frame(f);
		}
	}

	public static void unregister(final FrameObserver observer)
	{
		sObservers.remove(observer);
	}
}
