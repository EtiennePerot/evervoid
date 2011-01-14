package com.evervoid.client.graphics;

import java.awt.Dimension;

import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.geometry.AnimatedFloatingTranslation;
import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.client.graphics.geometry.GridPoint;
import com.evervoid.client.graphics.geometry.Transform;


public abstract class UIProp extends GridNode
{
	protected AnimatedRotation aFaceTowards = getNewRotationAnimation();
	protected GridPoint aFacing = null;
	protected AnimatedFloatingTranslation aFloatingAnimation;
	protected MultiSprite aSprite = new MultiSprite();
	protected Transform aSpriteOffset = aSprite.getNewTransform();
	protected boolean aSpriteReady = false;

	public UIProp(final Grid grid, final GridPoint location)
	{
		this(grid, location, new Dimension(1, 1));
	}

	public UIProp(final Grid grid, final GridPoint location, final Dimension size)
	{
		super(grid, location, size);
		buildSprite();
		aSpriteReady = true;
		// Offset for multi-cell sprites
		aSpriteOffset.translate((size.width - 1) * aGrid.getCellWidth() / 2, (size.height - 1) * aGrid.getCellHeight()
				/ 2);
		addNode(aSprite);
	}

	protected EverNode addSprite(final EverNode sprite)
	{
		return aSprite.addSprite(sprite);
	}

	protected EverNode addSprite(final EverNode sprite, final float x, final float y)
	{
		return aSprite.addSprite(sprite, x, y);
	}

	protected EverNode addSprite(final String image)
	{
		return aSprite.addSprite(new Sprite(image));
	}

	protected EverNode addSprite(final String image, final float x, final float y)
	{
		return aSprite.addSprite(new Sprite(image), x, y);
	}

	protected abstract void buildSprite();

	/**
	 * Called by subclasses when they desire to have a floating animation.
	 * Automatically starts the animation
	 * 
	 * @param duration
	 *            The duration of the floating animation
	 * @param offset
	 *            The maximum distance the animation may go
	 */
	protected void enableFloatingAnimation(final float duration, final float offset)
	{
		aFloatingAnimation = getNewFloatingTranslationAnimation();
		aFloatingAnimation.setToleratedOffset(offset).setDuration(duration).start();
	}

	public void faceTowards(final float angle)
	{
		aFacing = null;
		aFaceTowards.setTargetRotation(angle).start();
	}

	public void faceTowards(final GridPoint target)
	{
		if (target != null && !target.equals(aFacing))
		{
			aFaceTowards.setTargetPoint(aGrid.getCellCenter(target).subtract(getCellCenter())).start();
			aFacing = target;
		}
	}
}
