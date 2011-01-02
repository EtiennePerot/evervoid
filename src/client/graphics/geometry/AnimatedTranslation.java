package client.graphics.geometry;

import client.EverNode;

import com.jme3.math.Vector3f;

public class AnimatedTranslation extends AnimatedTransform
{
	private final Vector3f aOriginVector = new Vector3f(0, 0, 0);
	private final Vector3f aTargetVector = new Vector3f(0, 0, 0);

	public AnimatedTranslation(final EverNode node)
	{
		super(node);
	}

	@Override
	protected void getReady()
	{
		aOriginVector.set(aVector);
	}

	@Override
	protected void register()
	{
		aNode.registerTranslationAnimation(true);
	}

	public AnimatedTransform setTargetTranslation(final Vector3f target)
	{
		aTargetVector.set(target);
		return this;
	}

	@Override
	protected void step(final float progress, final float antiProgress)
	{
		translate(aOriginVector.mult(antiProgress).add(aTargetVector).mult(progress));
	}

	@Override
	protected void unregister()
	{
		aNode.registerTranslationAnimation(false);
	}
}
