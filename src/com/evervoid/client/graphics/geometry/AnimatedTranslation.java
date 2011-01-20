package com.evervoid.client.graphics.geometry;

import com.evervoid.client.EverNode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class AnimatedTranslation extends AnimatedTransform
{
	private final Vector3f aOriginVector = new Vector3f(0, 0, 0);
	private final Vector3f aTargetVector = new Vector3f(0, 0, 0);

	public AnimatedTranslation(final EverNode node)
	{
		super(node);
	}

	public float getMovingSpeed()
	{
		return aSmoothing.derivative(aProgress);
	}

	@Override
	protected void getReady()
	{
		aOriginVector.set(aVector);
	}

	@Override
	public void reset()
	{
		setTranslationNow(0, 0, 0);
		super.translate(0, 0, 0);
	}

	public void setTranslationNow(final float x, final float y, final float z)
	{
		setTranslationNow(new Vector3f(x, y, z));
	}

	public void setTranslationNow(final Vector2f offset)
	{
		setTranslationNow(new Vector3f(offset.x, offset.y, 0));
	}

	public void setTranslationNow(final Vector3f offset)
	{
		aOriginVector.set(offset);
		aTargetVector.set(offset);
		super.translate(offset);
	}

	public AnimatedTransform smoothMoveBy(final float x, final float y)
	{
		return smoothMoveBy(new Vector2f(x, y));
	}

	public AnimatedTransform smoothMoveBy(final float x, final float y, final float z)
	{
		return smoothMoveBy(new Vector3f(x, y, z));
	}

	public AnimatedTransform smoothMoveBy(final Vector2f target)
	{
		return smoothMoveBy(new Vector3f(target.x, target.y, 0));
	}

	public AnimatedTransform smoothMoveBy(final Vector3f offset)
	{
		return smoothMoveTo(aVector.add(offset));
	}

	public AnimatedTransform smoothMoveTo(final float x, final float y)
	{
		return smoothMoveTo(x, y, 0);
	}

	public AnimatedTransform smoothMoveTo(final float x, final float y, final float z)
	{
		return smoothMoveTo(new Vector3f(x, y, z));
	}

	public AnimatedTransform smoothMoveTo(final Vector2f target)
	{
		return smoothMoveTo(new Vector3f(target.x, target.y, 0));
	}

	public AnimatedTransform smoothMoveTo(final Vector3f target)
	{
		aTargetVector.set(target);
		return this;
	}

	@Override
	protected void step(final float progress, final float antiProgress)
	{
		super.translate(aOriginVector.mult(antiProgress).add(aTargetVector.mult(progress)));
	}

	@Override
	public String toString()
	{
		return super.toString() + "Translation from " + aOriginVector + " to " + aTargetVector;
	}
}
