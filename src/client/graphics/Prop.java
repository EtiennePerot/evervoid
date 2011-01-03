package client.graphics;

import java.awt.Point;

import client.graphics.geometry.AnimatedRotation;
import client.graphics.geometry.AnimatedTransform.DurationMode;

public abstract class Prop extends GridNode
{
	protected AnimatedRotation aFaceTowards = getNewRotationAnimation();
	protected Point aFacing = null;
	protected MultiSprite aSprite = new MultiSprite();

	public Prop(final Grid grid, final Point location)
	{
		super(grid, location);
		buildSprite();
		addNode(aSprite);
		aFaceTowards.setSpeed(1.2f).setDurationMode(DurationMode.CONTINUOUS);
	}

	protected Sprite addSprite(final String image)
	{
		return aSprite.addSprite(image);
	}

	protected Sprite addSprite(final String image, final float x, final float y)
	{
		return aSprite.addSprite(image, x, y);
	}

	protected abstract void buildSprite();

	public void faceTowards(final float angle)
	{
		aFacing = null;
		aFaceTowards.setTargetRotation(angle).start();
	}

	public void faceTowards(final Point target)
	{
		if (target != null && !target.equals(aFacing))
		{
			aFaceTowards.setTargetPoint(aGrid.getCellCenter(target).subtract(getCellCenter())).start();
			aFacing = target;
		}
	}
}
