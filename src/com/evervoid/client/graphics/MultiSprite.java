package com.evervoid.client.graphics;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.state.data.SpriteData;
import com.jme3.math.Vector2f;

public class MultiSprite extends EverNode implements Sizeable
{
	private float aDepth = 0f;
	private final Set<Sizeable> aElements = new HashSet<Sizeable>();
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
		if (!(sprite instanceof EverNode)) {
			return null;
		}
		final EverNode node = (EverNode) sprite;
		addNode(node);
		aNumberOfSprites++;
		aElements.add(sprite);
		spriteAdded(node);
		node.getNewTransform().translate(0, 0, aDepth);
		aDepth += 0.000001f;
		recomputeTotalSize();
		return node;
	}

	public EverNode addSprite(final SpriteData sprite)
	{
		return addSprite(new Sprite(sprite));
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
			final Vector2f dimension = s.getDimensions();
			aTotalSize.set(Math.max(aTotalSize.x, dimension.x), Math.max(aTotalSize.y, dimension.y));
		}
	}

	protected void spriteAdded(final EverNode sprite)
	{
		// Overridden by suclasses
	}
}
