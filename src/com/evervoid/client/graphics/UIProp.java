package com.evervoid.client.graphics;

import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.geometry.AnimatedFloatingTranslation;
import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.gamedata.Dimension;
import com.evervoid.state.solar.Point;

public abstract class UIProp extends GridNode
{
	protected AnimatedRotation aFaceTowards = getNewRotationAnimation();
	protected Point aFacing = null;
	protected AnimatedFloatingTranslation aFloatingAnimation;
	protected MultiSprite aSprite = new MultiSprite();
	protected boolean aSpriteReady = false;

	public UIProp(final Grid grid, final Point location)
	{
		this(grid, location, new Dimension());
	}

	public UIProp(final Grid grid, final Point location, final Dimension size)
	{
		super(grid, location, size);
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

	/**
	 * Called by subclasses when they have obtained sufficient data to be able to build their sprite.
	 */
	protected void buildProp()
	{
		buildSprite();
		aSpriteReady = true;
	}

	/**
	 * Overridden by subclasses; called when the sprite should be built.
	 */
	protected abstract void buildSprite();

	/**
	 * Called by subclasses when they desire to have a floating animation. Automatically starts the animation
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

	public void faceTowards(final Point target)
	{
		if (target != null && !target.equals(aFacing)) {
			aFaceTowards.setTargetPoint(aGrid.getCellCenter(target).subtract(getCellCenter())).start();
			aFacing = target;
		}
	}
}
