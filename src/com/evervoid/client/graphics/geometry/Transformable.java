package com.evervoid.client.graphics.geometry;

public interface Transformable
{
	public AnimatedAlpha getNewAlphaAnimation();

	public AnimatedFloatingTranslation getNewFloatingTranslationAnimation();

	public AnimatedRotation getNewRotationAnimation();

	public AnimatedScaling getNewScalingAnimation();

	public Transform getNewTransform();

	public AnimatedTranslation getNewTranslationAnimation();
}
