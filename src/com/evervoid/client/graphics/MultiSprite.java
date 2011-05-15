package com.evervoid.client.graphics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.data.SpriteData;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Vector2f;

/**
 * An {@link EverNode} meant to contain multiple {@link BaseSprite}s (referred to as "elements" in the JavaDoc).
 */
public class MultiSprite extends EverNode implements Sizable
{
	/**
	 * Z offset between multiple elements in this MultiSprite, to avoid having two elements with overlapping Z values
	 */
	private static final float sDepthIncrement = 0.000001f;
	/**
	 * The current Z offset to apply to incoming elements
	 */
	private float aDepth = 0f;
	/**
	 * Maps elements to the {@link Transform} object used to offset them on the Z axis
	 */
	private final Map<EverNode, Transform> aDepthOffsets = new HashMap<EverNode, Transform>();
	/**
	 * All elements in this MultiSprite
	 */
	private final Set<Sizable> aElements = new HashSet<Sizable>(4);
	/**
	 * Number of elements in this MultiSprite
	 */
	private int aNumberOfSprites = 0;
	/**
	 * Total size of all elements in this MultiSprite. May not be accurate if non-{@link Sizable} elements have been inserted.
	 */
	private final Vector2f aTotalSize = new Vector2f(0, 0);

	/**
	 * Default constructor; no elements.
	 */
	public MultiSprite()
	{
		// Nothing
	}

	/**
	 * Constructor with a set of images to add
	 * 
	 * @param images
	 *            The images to add to this MultiSprite
	 */
	public MultiSprite(final String... images)
	{
		for (final String image : images) {
			addSprite(new SpriteData(image));
		}
	}

	/**
	 * Adds a sprite to this MultiSprite.
	 * 
	 * @param sprite
	 *            The sprite to add
	 * @return The sprite that was added (same as the one given), or null if the given sprite was not valid
	 */
	public final EverNode addSprite(final Sizable sprite)
	{
		final EverNode node = addSprite(sprite, aDepth);
		aDepth += sDepthIncrement;
		return node;
	}

	/**
	 * Adds a sprite to this MultiSprite with a specific Z offset
	 * 
	 * @param sprite
	 *            The sprite to add
	 * @param zOffset
	 *            The Z offset to use
	 * @return The sprite that was added (same as the one given), or null if the given sprite was not valid
	 */
	public final EverNode addSprite(final Sizable sprite, final float zOffset)
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

	/**
	 * Adds a sprite to this MultiSprite.
	 * 
	 * @param sprite
	 *            The sprite to add
	 * @return The sprite that was added (same as the one given), or null if the given sprite was not valid
	 */
	public EverNode addSprite(final SpriteData sprite)
	{
		return addSprite(new Sprite(sprite));
	}

	/**
	 * Deletes a sprite
	 * 
	 * @param sprite
	 *            The sprite to delete
	 * @return The deleted sprite, or null if the sprite wasn't in this MultiSprite in the first place
	 */
	public EverNode delSprite(final Sizable sprite)
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

	/**
	 * @return The number of sprites in this MultiSprite.
	 */
	public int getNumberOfSprites()
	{
		return aNumberOfSprites;
	}

	@Override
	public float getWidth()
	{
		return aTotalSize.x;
	}

	/**
	 * Recomputes the total size of this MultiSprite.
	 */
	private void recomputeTotalSize()
	{
		aTotalSize.set(0, 0);
		for (final Sizable s : aElements) {
			MathUtils.clampVector2fUpLocal(s.getDimensions(), aTotalSize);
		}
	}

	/**
	 * Called when a sprite is added.
	 * 
	 * @param sprite
	 *            The sprite that was added
	 */
	protected void spriteAdded(final EverNode sprite)
	{
		// Overridden by subclasses
	}

	/**
	 * Called when a sprite is deleted.
	 * 
	 * @param sprite
	 *            The sprite that was deleted
	 */
	protected void spriteDeleted(final EverNode sprite)
	{
		// Overridden by subclasses
	}
}
