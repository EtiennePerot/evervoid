package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.GridNode;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedFloatingTranslation;
import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.GridLocation;

public abstract class UIProp extends GridNode
{
	protected AnimatedRotation aFaceTowards = getNewRotationAnimation();
	protected GridLocation aFacing = null;
	protected AnimatedFloatingTranslation aFloatingAnimation;
	protected SolarGrid aSolarSystemGrid;
	protected MultiSprite aSprite = new MultiSprite();
	protected boolean aSpriteReady = false;

	public UIProp(final SolarGrid grid, final GridLocation location)
	{
		super(grid, location);
		addNode(aSprite);
		aSolarSystemGrid = grid;
	}

	protected EverNode addSprite(final EverNode sprite)
	{
		return aSprite.addSprite(sprite);
	}

	protected EverNode addSprite(final EverNode sprite, final float x, final float y)
	{
		return aSprite.addSprite(sprite, x, y);
	}

	protected EverNode addSprite(final SpriteData info)
	{
		return aSprite.addSprite(info);
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
		aFaceTowards.setTargetPitch(angle).start();
	}

	public void faceTowards(final GridLocation target)
	{
		if (target != null && !target.equals(aFacing)) {
			aFaceTowards.setTargetPoint2D(aGrid.getCellCenter(target).subtract(getCellCenter())).start();
			aFacing = target;
		}
	}

	public float getFacingDirection()
	{
		return aFaceTowards.getRotationPitch();
	}

	public SolarGrid getSolarSystemGrid()
	{
		return aSolarSystemGrid;
	}
}
