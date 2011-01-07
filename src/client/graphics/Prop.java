package client.graphics;

import java.awt.Dimension;

import client.EverNode;
import client.graphics.geometry.AnimatedRotation;
import client.graphics.geometry.GridPoint;
import client.graphics.geometry.AnimatedTransform.DurationMode;

public abstract class Prop extends GridNode
{
	protected AnimatedRotation aFaceTowards = getNewRotationAnimation();
	protected GridPoint aFacing = null;
	protected MultiSprite aSprite = new MultiSprite();

	public Prop(final Grid grid, final GridPoint location)
	{
		this(grid, location, new Dimension(1, 1));
	}

	public Prop(final Grid grid, final GridPoint location, final Dimension size)
	{
		super(grid, location, size);
		buildSprite();
		addNode(aSprite);
		aFaceTowards.setSpeed(1.2f).setDurationMode(DurationMode.CONTINUOUS);
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
