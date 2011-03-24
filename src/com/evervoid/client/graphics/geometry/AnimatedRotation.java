package com.evervoid.client.graphics.geometry;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.utils.MathUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class AnimatedRotation extends AnimatedTransform
{
	private final Vector3f aOriginRotation = new Vector3f(0f, 0f, 0f);
	private final Vector3f aTargetDirection = new Vector3f(1f, 1f, 1f);
	private final Vector3f aTargetDistance = new Vector3f(0f, 0f, 0f);
	private final Vector3f aTargetRotation = new Vector3f(0f, 0f, 0f);

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

	public AnimatedRotation setTargetPitch(final float angle)
	{
		return setTargetRotation(null, null, angle);
	}

	public AnimatedRotation setTargetPoint2D(final Vector2f point)
	{
		final Float angle = MathUtils.getAngleTowards(point);
		if (angle != null) {
			setTargetPitch(angle);
		}
		return this;
	}

	public AnimatedRotation setTargetPoint2D(final Vector3f point)
	{
		return setTargetPoint2D(new Vector2f(point.x, point.y));
	}

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
