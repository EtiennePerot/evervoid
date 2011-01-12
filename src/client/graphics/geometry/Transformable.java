package client.graphics.geometry;

public interface Transformable
{
	public AnimatedFloatingTranslation getNewFloatingTranslationAnimation();

	public AnimatedRotation getNewRotationAnimation();

	public AnimatedScaling getNewScalingAnimation();

	public Transform getNewTransform();

	public AnimatedTranslation getNewTranslationAnimation();
}
