package client.graphics.geometry;

public interface Transformable
{
	public AnimatedRotation getNewRotationAnimation();

	public Transform getNewTransform();

	public AnimatedTranslation getNewTranslationAnimation();

	public void registerAnimation(final AnimatedTransform animation);

	public void unregisterAnimation(final AnimatedTransform animation);
}
