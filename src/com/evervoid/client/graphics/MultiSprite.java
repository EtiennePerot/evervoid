package com.evervoid.client.graphics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.data.SpriteData;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Vector2f;

public class MultiSprite extends EverNode implements Sizeable
{
	private static final float sDepthIncrement = 0.000001f;
	private float aDepth = 0f;
	private final Map<EverNode, Transform> aDepthOffsets = new HashMap<EverNode, Transform>();
	private final Set<Sizeable> aElements = new HashSet<Sizeable>(4);
	private int aNumberOfSprites = 0;
	private final Vector2f aTotalSize = new Vector2f(0, 0);

	public MultiSprite()
	{
		// Nothing
	}

	public MultiSprite(final String image)
	{
		addSprite(new SpriteData(image));
	}

	public final EverNode addSprite(final Sizeable sprite)
	{
		final EverNode node = addSprite(sprite, aDepth);
		aDepth += sDepthIncrement;
		return node;
	}

	public final EverNode addSprite(final Sizeable sprite, final float zOffset)
	{
		if (!(sprite instanceof EverNode)) {
			return null;
		}
		final EverNode node = (EverNode) sprite;
		addNode(node);
		aNumberOfSprites++;
		aElements.add(sprite);
		spriteAdded(node);
		final Transform depth = node.getNewTransform();
		aDepthOffsets.put(node, depth);
		depth.translate(0, 0, aDepth);
		aDepth += sDepthIncrement;
		recomputeTotalSize();
		return node;
	}

	public EverNode addSprite(final SpriteData sprite)
	{
		return addSprite(new Sprite(sprite));
	}

	public EverNode delSprite(final Sizeable sprite)
	{
		if (aElements.remove(sprite)) {
			final EverNode node = (EverNode) sprite;
			delNode(node);
			aDepthOffsets.get(node).delete();
			aDepthOffsets.remove(node);
			aDepth -= sDepthIncrement;
			spriteDeleted(node);
			recomputeTotalSize();
			return node;
		}
		return null;
	}

	@Override
	public Vector2f getDimensions()
	{
		return aTotalSize;
	}

	@Override
	public float getHeight()
	{
		return aTotalSize.y;
	}

	public int getNumberOfSprites()
	{
		return aNumberOfSprites;
	}

	@Override
	public float getWidth()
	{
		return aTotalSize.x;
	}

	private void recomputeTotalSize()
	{
		aTotalSize.set(0, 0);
		for (final Sizeable s : aElements) {
			MathUtils.clampVector2fUpLocal(s.getDimensions(), aTotalSize);
		}
	}

	protected void spriteAdded(final EverNode sprite)
	{
		// Overridden by subclasses
	}

	protected void spriteDeleted(final EverNode sprite)
	{
		// Overridden by subclasses
	}
}
