package com.evervoid.client.graphics.geometry;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.utils.MathUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * An animation over the rotation of an {@link EverNode}.
 */
public class AnimatedRotation extends AnimatedTransform
{
	/**
	 * Rotation prior to starting the animation
	 */
	private final Vector3f aOriginRotation = new Vector3f(0f, 0f, 0f);
	/**
	 * Direction of the rotation; for each axis, this will either be 1 (positive direction) or -1 (negative direction)
	 */
	private final Vector3f aTargetDirection = new Vector3f(1f, 1f, 1f);
	/**
	 * Absolute value of the angle of the animation, on each axis of the rotation
	 */
	private final Vector3f aTargetDistance = new Vector3f(0f, 0f, 0f);
	/**
	 * Target rotation to reach at the end of the animation
	 */
	private final Vector3f aTargetRotation = new Vector3f(0f, 0f, 0f);

	/**
	 * Constructor; DO NOT USE THIS, use {@link EverNode}'s getNewRotationAnimation instead.
	 * 
	 * @param node
	 *            The {@link EverNode} that this animation will affect.
	 */
	public AnimatedRotation(final EverNode node)
	{
		super(node);
	}

	@Override
	protected void getReady()
	{
		aOriginRotation.set(aRotation);
		while (aOriginRotation.x > aTargetRotation.x) {
			aTargetRotation.setX(aTargetRotation.x + FastMath.TWO_PI);
		}
		aTargetDistance.x = Math.abs(aTargetRotation.x - aOriginRotation.x);
		aTargetDirection.x = 1;
		if (aTargetDistance.x > FastMath.PI) {
			aTargetDistance.x = FastMath.TWO_PI - aTargetDistance.x;
			aTargetDirection.x = -1;
		}
		while (aOriginRotation.y > aTargetRotation.y) {
			aTargetRotation.setY(aTargetRotation.y + FastMath.TWO_PI);
		}
		aTargetDistance.y = Math.abs(aTargetRotation.y - aOriginRotation.y);
		aTargetDirection.y = 1;
		if (aTargetDistance.y > FastMath.PI) {
			aTargetDistance.y = FastMath.TWO_PI - aTargetDistance.y;
			aTargetDirection.y = -1;
		}
		while (aOriginRotation.z > aTargetRotation.z) {
			aTargetRotation.setZ(aTargetRotation.z + FastMath.TWO_PI);
		}
		aTargetDistance.z = Math.abs(aTargetRotation.z - aOriginRotation.z);
		aTargetDirection.z = 1;
		if (aTargetDistance.z > FastMath.PI) {
			aTargetDistance.z = FastMath.TWO_PI - aTargetDistance.z;
			aTargetDirection.z = -1;
		}
	}

	/**
	 * Computes the best (smallest) angle to use in order to go from an angle to another
	 * 
	 * @param from
	 *            The starting angle
	 * @param to
	 *            The target angle
	 * @return The difference between the angles, and the sign to indicate in which direction to turn.
	 */
	private float getRotationDelta(final float from, final float to)
	{
		final float angleFrom = MathUtils.mod(from, FastMath.TWO_PI);
		float angleTo = MathUtils.mod(to, FastMath.TWO_PI);
		final float angleDifference = Math.abs(angleTo - angleFrom) - Math.abs(angleTo - FastMath.TWO_PI - angleFrom);
		if (angleDifference == 0) {
			if (FastMath.rand.nextBoolean() && angleFrom != angleTo) {
				// Randomly choose to turn clockwise or counterclockwise
				angleTo -= FastMath.TWO_PI;
			}
		}
		else if (angleDifference > 0) {
			angleTo -= FastMath.TWO_PI;
		}
		return angleTo;
	}

	@Override
	public void reset()
	{
		aTargetRotation.set(0f, 0f, 0f);
		rotateTo(0f, 0f, 0f);
	}

	/**
	 * Set the target pitch of the rotation
	 * 
	 * @param angle
	 *            The target pitch
	 * @return This, for chainability
	 */
	public AnimatedRotation setTargetPitch(final float angle)
	{
		return setTargetRotation(null, null, angle);
	}

	/**
	 * Set the target pitch of the animation by giving a point in 2D space as target
	 * 
	 * @param point
	 *            The point (as a {@link Vector2f}) to look at, considering (0, 0) to be the node's origin
	 * @return This, for chainability
	 */
	public AnimatedRotation setTargetPoint2D(final Vector2f point)
	{
		final Float angle = MathUtils.getAngleTowards(point);
		if (angle != null) {
			setTargetPitch(angle);
		}
		return this;
	}

	/**
	 * Set the target pitch of the animation by giving a point in 2D space as target. The point is passed as a {@link Vector3f},
	 * but its Z component is ignored.
	 * 
	 * @param point
	 *            The point (as a {@link Vector3f}) to look at, considering (0, 0) to be the node's origin
	 * @return This, for chainability
	 */
	public AnimatedRotation setTargetPoint2D(final Vector3f point)
	{
		return setTargetPoint2D(new Vector2f(point.x, point.y));
	}

	/**
	 * Set the target angles of the rotation
	 * 
	 * @param yaw
	 *            The target yaw, or null to keep the current target yaw
	 * @param roll
	 *            The target roll, or null to keep the current target roll
	 * @param pitch
	 *            The target pitch, or null to keep the current target pitch
	 * @return This, for chainability
	 */
	public AnimatedRotation setTargetRotation(final Float yaw, final Float roll, final Float pitch)
	{
		float continuous = 0f;
		if (yaw != null) {
			final float targetYaw = getRotationDelta(aTargetRotation.x, yaw);
			continuous += FastMath.sqr((aRotation.x - targetYaw) / FastMath.PI);
			aTargetRotation.setX(targetYaw);
		}
		if (roll != null) {
			final float targetRoll = getRotationDelta(aTargetRotation.y, roll);
			continuous += FastMath.sqr((aRotation.y - targetRoll) / FastMath.PI);
			aTargetRotation.setY(targetRoll);
		}
		if (pitch != null) {
			final float targetPitch = getRotationDelta(aTargetRotation.z, pitch);
			continuous += FastMath.sqr((aRotation.z - targetPitch) / FastMath.PI);
			aTargetRotation.setZ(targetPitch);
		}
		if (yaw != null || roll != null || pitch != null) {
			setBackContinuous(Math.sqrt(continuous));
		}
		else {
			setBackContinuous(0);
		}
		return this;
	}

	@Override
	protected void step(final float progress, final float antiProgress)
	{
		rotateTo(aOriginRotation.add(aTargetDistance.mult(aTargetDirection).mult(progress)));
	}

	@Override
	public String toString()
	{
		return super.toString() + "; Rotation from " + aOriginRotation + " to " + aTargetRotation;
	}
}
