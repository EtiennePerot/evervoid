package com.evervoid.client.graphics.geometry;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.EverNode;


public class TransformManager
{
	private static TransformManager sInstance = null;

	private static TransformManager get()
	{
		if (sInstance == null)
		{
			sInstance = new TransformManager();
		}
		return sInstance;
	}

	public static void needUpdate(final EverNode node)
	{
		get().nodeNeedsUpdate(node);
	}

	/**
	 * Called by animation Transforms when an animation is started. Registers
	 * the specified animation Transform to receive frame update event
	 * 
	 * @param animation
	 *            The animated Transform to register
	 */
	public static void register(final AnimatedTransform animation)
	{
		get().add(animation);
	}

	public static void tick(final float tpf)
	{
		get().frame(tpf);
	}

	/**
	 * Unregister an animated Transform from this EverNode. Called by animated
	 * Transforms when they are finished and no longer need to receive frame
	 * update events
	 * 
	 * @param animation
	 *            The animated Transform to unregister
	 */
	public static void unregister(final AnimatedTransform animation)
	{
		get().remove(animation);
	}

	/**
	 * Set of undergoing animations
	 */
	private final Set<AnimatedTransform> aAnimations = new HashSet<AnimatedTransform>();
	/**
	 * Set of animations that finished during the last tick. Used for cleaning
	 * up the animation queue
	 */
	private final Set<AnimatedTransform> aFinishedAnimations = new HashSet<AnimatedTransform>();
	/**
	 * Set of animations that should be added on next tick
	 */
	private final Set<AnimatedTransform> aNewAnimations = new HashSet<AnimatedTransform>();
	/**
	 * Set of nodes that need to be updated independently on next frame
	 */
	private final Set<EverNode> aNodes = new HashSet<EverNode>();

	public void add(final AnimatedTransform t)
	{
		aNewAnimations.add(t);
	}

	public void frame(final float tpf)
	{
		final Set<EverNode> toRecompute = new HashSet<EverNode>();
		for (final AnimatedTransform t : aAnimations)
		{
			if (t.frame(tpf))
			{
				toRecompute.add(t.getNode());
			}
		}
		if (!aNodes.isEmpty()) // Clean up finished animations
		{
			toRecompute.addAll(aNodes);
			aNodes.clear();
		}
		for (final EverNode n : toRecompute)
		{
			n.computeTransforms();
		}
		if (!aFinishedAnimations.isEmpty()) // Clean up finished animations
		{
			for (final AnimatedTransform t : aFinishedAnimations)
			{
				aAnimations.remove(t);
			}
			aFinishedAnimations.clear();
		}
		if (!aNewAnimations.isEmpty()) // Clean up finished animations
		{
			for (final AnimatedTransform t : aNewAnimations)
			{
				aAnimations.add(t);
			}
			aNewAnimations.clear();
		}
	}

	public void nodeNeedsUpdate(final EverNode node)
	{
		aNodes.add(node);
	}

	public void remove(final AnimatedTransform t)
	{
		aFinishedAnimations.add(t);
	}
}
