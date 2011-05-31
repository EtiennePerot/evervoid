package com.evervoid.client.graphics.geometry;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.utils.MathUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * Base class of the Transform system. It works like this: Every {@link EverNode} has multiple {@link Transform}s (or subclasses
 * of {@link Transform}), each of which applies a transformation to add, adding up. This allows for non-conflicting animations
 * and offsets while keeping an easy interface.
 */
public class Transform
{
	/**
	 * Alpha transparency of the {@link EverNode}
	 */
	protected float aAlpha = 1f;
	/**
	 * Whether the next call to updated() is the first one or not. If it is, the Transform's properties will be committed
	 * instantly.
	 */
	private boolean aFirstUpdate = true;
	/**
	 * Upper bound on the {@link EverNode}'s scale
	 */
	private Vector3f aMaximumScale = null;
	/**
	 * Upper bound on the {@link EverNode}'s translation
	 */
	private Vector3f aMaximumVector = null;
	/**
	 * Lower bound on the {@link EverNode}'s scale
	 */
	private Vector3f aMinimumScale = null;
	/**
	 * Lower bound on the {@link EverNode}'s translation
	 */
	private Vector3f aMinimumVector = null;
	/**
	 * The {@link EverNode} that this Transform affects
	 */
	protected final EverNode aNode;
	/**
	 * Whether to notify the {@link EverNode} every time this Transform is changed. Wasteful during animation, but useful for
	 * one-shot Transforms.
	 */
	private boolean aNotifyOnChange = true;
	/**
	 * Previously-stored alpha value, for comparison purposes with a new alpha value
	 */
	protected float aOldAlpha = 1f;
	/**
	 * Previously-stored rotation, for comparison purposes with a new rotation value
	 */
	protected Vector3f aOldRotation = new Vector3f(0f, 0f, 0f);
	/**
	 * Previously-stored scale, for comparison purposes with a new scale value
	 */
	protected Vector3f aOldScale = new Vector3f(1f, 1f, 1f);
	/**
	 * Previously-stored translation, for comparison purposes with a new translation value
	 */
	protected final Vector3f aOldVector = new Vector3f(0f, 0f, 0f);
	/**
	 * The rotation of the {@link EverNode}
	 */
	protected Vector3f aRotation = new Vector3f(0f, 0f, 0f);
	/**
	 * The scale of the {@link EverNode}
	 */
	protected Vector3f aScale = new Vector3f(1f, 1f, 1f);
	/**
	 * The translation of the {@link EverNode}
	 */
	protected final Vector3f aVector = new Vector3f(0f, 0f, 0f);

	/**
	 * Constructor. Warning: NEVER call this directly; call parent.getNewTransform() instead!
	 * 
	 * @param parent
	 *            The EverNode that this transformation will affect
	 */
	public Transform(final EverNode parent)
	{
		aNode = parent;
	}

	/**
	 * Force synchronous recomputation of this Transform
	 * 
	 * @return This
	 */
	public Transform commit()
	{
		aNode.computeTransforms();
		return this;
	}

	/**
	 * Delete this Transform, and revert its changes on the {@link EverNode}
	 */
	public void delete()
	{
		aNode.removeTransform(this);
		commit();
	}

	/**
	 * Rotate towards a certain point, considering the (0, 0) point to correspond to the origin point of the {@link EverNode}
	 * managed by this Transform
	 * 
	 * @param point
	 *            The point to face towards
	 * @return This
	 */
	public Transform faceTowards(final Vector2f point)
	{
		final Float angle = MathUtils.getAngleTowards(point);
		if (angle != null) {
			rotatePitchTo(angle);
		}
		return this;
	}

	/**
	 * @return The alpha transparency of this valuw
	 */
	public float getAlpha()
	{
		return aAlpha;
	}

	/**
	 * Returns the EverNode that this Transform is attached to
	 * 
	 * @return The attached EverNode
	 */
	public EverNode getNode()
	{
		return aNode;
	}

	/**
	 * @return Current rotation
	 */
	public Vector3f getRotation()
	{
		return aRotation;
	}

	/**
	 * @return Current rotation pitch (rotation around Z axis)
	 */
	public float getRotationPitch()
	{
		return aRotation.z;
	}

	/**
	 * @return Current rotation roll (rotation around Y axis)
	 */
	public float getRotationRoll()
	{
		return aRotation.y;
	}

	/**
	 * @return Current rotation yaw (rotation around X axis)
	 */
	public float getRotationYaw()
	{
		return aRotation.x;
	}

	/**
	 * @return The current scale of the {@link EverNode}
	 */
	public Vector3f getScale()
	{
		return aScale;
	}

	/**
	 * @return The average of the three scale components (x, y, z)
	 */
	public float getScaleAverage()
	{
		return (aScale.x + aScale.y + aScale.z) / 3f;
	}

	/**
	 * @return The X component of the {@link EverNode}'s scale
	 */
	public float getScaleX()
	{
		return aScale.x;
	}

	/**
	 * @return The Y component of the {@link EverNode}'s scale
	 */
	public float getScaleY()
	{
		return aScale.y;
	}

	/**
	 * @return The Z component of the {@link EverNode}'s scale
	 */
	public float getScaleZ()
	{
		return aScale.z;
	}

	/**
	 * @return The translation currently applied to the {@link EverNode}
	 */
	public Vector3f getTranslation()
	{
		return aVector;
	}

	/**
	 * @return The (X, Y) components of the translation currently applied to the {@link EverNode}
	 */
	public Vector2f getTranslation2f()
	{
		return new Vector2f(aVector.x, aVector.y);
	}

	/**
	 * Add a certain 2D offset to the current translation of the {@link EverNode}. The Z translation will be unaffected.
	 * 
	 * @param x
	 *            The X offset to add
	 * @param y
	 *            The Y offset to add
	 * @return This, for chainability
	 */
	public Transform move(final double x, final double y)
	{
		return move((float) x, (float) y);
	}

	/**
	 * Add a certain offset to the current translation of the {@link EverNode}.
	 * 
	 * @param x
	 *            The X offset to add
	 * @param y
	 *            The Y offset to add
	 * @param z
	 *            The Z offset to add
	 * @return This, for chainability
	 */
	public Transform move(final double x, final double y, final double z)
	{
		return move((float) x, (float) y, (float) z);
	}

	/**
	 * Add a certain 2D offset to the current translation of the {@link EverNode}.. The Z translation will be unaffected.
	 * 
	 * @param x
	 *            The X offset to add
	 * @param y
	 *            The Y offset to add
	 * @return This, for chainability
	 */
	public Transform move(final float x, final float y)
	{
		return move(new Vector3f(x, y, 0));
	}

	/**
	 * Add a certain offset to the current translation of the {@link EverNode}.
	 * 
	 * @param x
	 *            The X offset to add
	 * @param y
	 *            The Y offset to add
	 * @param z
	 *            The Z offset to add
	 * @return This, for chainability
	 */
	public Transform move(final float x, final float y, final float z)
	{
		return move(new Vector3f(x, y, z));
	}

	/**
	 * Add a certain 2D offset to the current translation of the {@link EverNode}. The Z translation will be unaffected.
	 * 
	 * @param offset
	 *            The 2D offset to add
	 * @return This, for chainability
	 */
	public Transform move(final Vector2f offset)
	{
		return move(new Vector3f(offset.x, offset.y, 0));
	}

	/**
	 * Add a certain offset to the current translation of the {@link EverNode}.
	 * 
	 * @param offset
	 *            The offset to add
	 * @return This, for chainability
	 */
	public Transform move(final Vector3f offset)
	{
		aVector.addLocal(offset);
		updated();
		return this;
	}

	/**
	 * Multiply the current scale by another factor
	 * 
	 * @param scale
	 *            The factor to multiply the scale by
	 * @return This, for chainability
	 */
	public Transform multScale(final double scale)
	{
		return multScale((float) scale);
	}

	/**
	 * Multiply the current scale by another factor
	 * 
	 * @param scale
	 *            The factor to multiply the scale by
	 * @return This, for chainability
	 */
	public Transform multScale(final float scale)
	{
		return setScale(new Vector3f(aScale.x * scale, aScale.y * scale, aScale.z * scale));
	}

	/**
	 * Add an angle to the current pitch component of the rotation
	 * 
	 * @param angle
	 *            The angle to add, in radians
	 * @return This, for chainability
	 */
	public Transform rotatePitchBy(final double angle)
	{
		return rotatePitchBy((float) angle);
	}

	/**
	 * Add an angle to the current pitch component of the rotation
	 * 
	 * @param angle
	 *            The angle to add, in radians
	 * @return This, for chainability
	 */
	public Transform rotatePitchBy(final float angle)
	{
		return rotatePitchTo(aRotation.z + angle);
	}

	/**
	 * Set the pitch component of the translation
	 * 
	 * @param angle
	 *            The angle to set the pitch to to, in radians
	 * @return This, for chainability
	 */
	public Transform rotatePitchTo(final double angle)
	{
		return rotatePitchTo((float) angle);
	}

	/**
	 * Set the pitch component of the translation
	 * 
	 * @param angle
	 *            The angle to set the pitch to to, in radians
	 * @return This, for chainability
	 */
	public Transform rotatePitchTo(final float angle)
	{
		return rotateTo(null, null, angle);
	}

	/**
	 * Rotate to a certain angle
	 * 
	 * @param yaw
	 *            Rotation yaw (Around X axis); use null to keep current value
	 * @param roll
	 *            Rotation roll (Around Y axis); use null to keep current value
	 * @param pitch
	 *            Rotation pitch (Around Z axis); use null to keep current value
	 * @return this
	 */
	public Transform rotateTo(final Float yaw, final Float roll, final Float pitch)
	{
		if (yaw != null) {
			aRotation.setX(MathUtils.mod(yaw, FastMath.TWO_PI));
		}
		if (roll != null) {
			aRotation.setY(MathUtils.mod(roll, FastMath.TWO_PI));
		}
		if (pitch != null) {
			aRotation.setZ(MathUtils.mod(pitch, FastMath.TWO_PI));
		}
		updated();
		return this;
	}

	/**
	 * Set the rotation of the Transform
	 * 
	 * @param rotation
	 *            The rotation to set to
	 * @return This, for chainability
	 */
	public Transform rotateTo(final Vector3f rotation)
	{
		return rotateTo(rotation.x, rotation.y, rotation.z);
	}

	/**
	 * Set the alpha transparency of the Transform
	 * 
	 * @param alpha
	 *            The alpha transparency
	 * @return This, for chainability
	 */
	public Transform setAlpha(final double alpha)
	{
		return setAlpha((float) alpha);
	}

	/**
	 * Set the alpha transparency of the Transform
	 * 
	 * @param alpha
	 *            The alpha transparency
	 * @return This, for chainability
	 */
	public Transform setAlpha(final float alpha)
	{
		aAlpha = MathUtils.clampFloat(0, alpha, 1);
		updated();
		return this;
	}

	/**
	 * Set an upper bound on the possible scale of the Transform. The same value will be applied to all axes.
	 * 
	 * @param max
	 *            The maximum scale on all axes
	 * @return This, for chainability
	 */
	public Transform setMaximumScale(final float max)
	{
		return setMaximumScale(new Vector3f(max, max, max));
	}

	/**
	 * Set an upper bound on the possible scale of the Transform
	 * 
	 * @param max
	 *            The maximum scale on each axis as a {@link Vector3f}, or null to remove the bound
	 * @return This, for chainability
	 */
	public Transform setMaximumScale(final Vector3f max)
	{
		aMaximumScale = max;
		updated();
		return this;
	}

	/**
	 * Set an upper bound on the possible translation of the Transform. The Z component will be unbounded.
	 * 
	 * @param x
	 *            The maximum translation on the X axis
	 * @param y
	 *            The maximum translation on the Y axis
	 * @return This, for chainability
	 */
	public Transform setMaximumTranslation(final float x, final float y)
	{
		return setMaximumTranslation(new Vector3f(x, y, Float.MAX_VALUE));
	}

	/**
	 * Set an upper bound on the possible translation of the Transform
	 * 
	 * @param x
	 *            The maximum translation on the X axis
	 * @param y
	 *            The maximum translation on the Y axis
	 * @param z
	 *            The maximum translation on the Z axis
	 * @return This, for chainability
	 */
	public Transform setMaximumTranslation(final float x, final float y, final float z)
	{
		return setMaximumTranslation(new Vector3f(x, y, z));
	}

	/**
	 * Set an upper bound on the possible translation of the Transform. The Z component will be unbounded.
	 * 
	 * @param max
	 *            The maximum translation on the X and Y axes, as a {@link Vector2f}
	 * @return This, for chainability
	 */
	public Transform setMaximumTranslation(final Vector2f max)
	{
		return setMaximumTranslation(new Vector3f(max.x, max.y, Float.MAX_VALUE));
	}

	/**
	 * Set an upper bound on the possible translation of the Transform
	 * 
	 * @param max
	 *            The maximum translation on each axis as a {@link Vector3f}, or null to remove the bound
	 * @return This, for chainability
	 */
	public Transform setMaximumTranslation(final Vector3f max)
	{
		aMaximumVector = max;
		updated();
		return this;
	}

	/**
	 * Set a lower bound on the possible scale of the Transform. The same value will be applied to all axes.
	 * 
	 * @param min
	 *            The minimum scale on all axes
	 * @return This, for chainability
	 */
	public Transform setMinimumScale(final float min)
	{
		return setMinimumScale(new Vector3f(min, min, min));
	}

	/**
	 * Set a lower bound on the possible scale of the Transform
	 * 
	 * @param min
	 *            The minimum scale on each axis as a {@link Vector3f}, or null to remove the bound
	 * @return This, for chainability
	 */
	public Transform setMinimumScale(final Vector3f min)
	{
		aMinimumScale = min;
		updated();
		return this;
	}

	/**
	 * Set a lower bound on the possible translation of the Transform. The Z component will be unbounded.
	 * 
	 * @param x
	 *            The minimum translation on the X axis
	 * @param y
	 *            The minimum translation on the Y axis
	 * @return This, for chainability
	 */
	public Transform setMinimumTranslation(final float x, final float y)
	{
		return setMinimumTranslation(new Vector3f(x, y, Float.MIN_VALUE));
	}

	/**
	 * Set a lower bound on the possible translation of the Transform
	 * 
	 * @param x
	 *            The minimum translation on the X axis
	 * @param y
	 *            The minimum translation on the Y axis
	 * @param z
	 *            The minimum translation on the Z axis
	 * @return This, for chainability
	 */
	public Transform setMinimumTranslation(final float x, final float y, final float z)
	{
		return setMinimumTranslation(new Vector3f(x, y, z));
	}

	/**
	 * Set a lower bound on the possible translation of the Transform. The Z component will be unbounded.
	 * 
	 * @param min
	 *            The minimum translation on the X and Y axes, as a {@link Vector2f}
	 * @return This, for chainability
	 */
	public Transform setMinimumTranslation(final Vector2f min)
	{
		return setMinimumTranslation(new Vector3f(min.x, min.y, Float.MIN_VALUE));
	}

	/**
	 * Set a lower bound on the possible translation of the Transform
	 * 
	 * @param min
	 *            The minimum translation on each axis as a {@link Vector3f}, or null to remove the bound
	 * @return This, for chainability
	 */
	public Transform setMinimumTranslation(final Vector3f min)
	{
		aMinimumVector = min;
		updated();
		return this;
	}

	/**
	 * Set whether the {@link EverNode} should be notified every time this Transform changes. Wasteful during animation, useful
	 * for one-shot Transforms.
	 * 
	 * @param notify
	 *            Whether to notify the {@link EverNode} on every change or not
	 * @return This, for chainability
	 */
	protected Transform setNotifyOnChange(final boolean notify)
	{
		aNotifyOnChange = notify;
		return this;
	}

	/**
	 * Set the scale of the Transform
	 * 
	 * @param scale
	 *            The scale to use. Will be used on all axes.
	 * @return This, for chainability
	 */
	public Transform setScale(final double scale)
	{
		return setScale((float) scale);
	}

	/**
	 * Set the scale of the Transform
	 * 
	 * @param scale
	 *            The scale to use. Will be used on all axes.
	 * @return This, for chainability
	 */
	public Transform setScale(final float scale)
	{
		return setScale(scale, scale, scale);
	}

	/**
	 * Set the X and Y scale of the Transform. The Z component of the scale will be unchanged.
	 * 
	 * @param xScale
	 *            The X component of the scale to use
	 * @param yScale
	 *            The Y component of the scale to use
	 * @return This, for chainability
	 */
	public Transform setScale(final float xScale, final float yScale)
	{
		return setScale(xScale, yScale, aScale.z);
	}

	/**
	 * Set the 3 components of the scale of the Transform
	 * 
	 * @param xScale
	 *            The X component of the scale to use
	 * @param yScale
	 *            The Y component of the scale to use
	 * @param zScale
	 *            The Z component of the scale to use
	 * @return This, for chainability
	 */
	public Transform setScale(final float xScale, final float yScale, final float zScale)
	{
		aScale.set(Math.max(0, xScale), Math.max(0, yScale), Math.max(0, zScale));
		updated();
		return this;
	}

	/**
	 * Set the 3 components of the scale of the Transform
	 * 
	 * @param scale
	 *            The scale to use, as a {@link Vector3f}
	 * @return This, for chainability
	 */
	public Transform setScale(final Vector3f scale)
	{
		return setScale(scale.x, scale.y, scale.z);
	}

	/**
	 * Synchronize this Transform with another Transform
	 * 
	 * @param t
	 *            The Transform to synchronize with
	 * @return This, for chainability
	 */
	public Transform syncTo(final Transform t)
	{
		final boolean oldNotify = aNotifyOnChange;
		setNotifyOnChange(false);
		translate(t.getTranslation().clone());
		rotatePitchTo(t.getRotationPitch());
		setScale(t.getScaleAverage());
		setAlpha(t.getAlpha());
		setNotifyOnChange(oldNotify);
		updated();
		return this;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "(" + aVector + "; " + aRotation + "; " + aScale + "; " + aAlpha + ")";
	}

	/**
	 * Set the X and Y components of the translation of this Transform. The Z component will be left unchanged.
	 * 
	 * @param x
	 *            The X component of the translation
	 * @param y
	 *            The Y component of the translation
	 * @return This, for chainability
	 */
	public Transform translate(final double x, final double y)
	{
		return translate((float) x, (float) y);
	}

	/**
	 * Set the translation of this Transform
	 * 
	 * @param x
	 *            The X component of the translation
	 * @param y
	 *            The Y component of the translation
	 * @param z
	 *            The Z component of the translation
	 * @return This, for chainability
	 */
	public Transform translate(final double x, final double y, final double z)
	{
		return translate((float) x, (float) y, (float) z);
	}

	/**
	 * Set the X and Y components of the translation of this Transform. The Z component will be left unchanged.
	 * 
	 * @param x
	 *            The X component of the translation
	 * @param y
	 *            The Y component of the translation
	 * @return This, for chainability
	 */
	public Transform translate(final float x, final float y)
	{
		return translate(new Vector3f(x, y, aVector.z));
	}

	/**
	 * Set the translation of this Transform
	 * 
	 * @param x
	 *            The X component of the translation
	 * @param y
	 *            The Y component of the translation
	 * @param z
	 *            The Z component of the translation
	 * @return This, for chainability
	 */
	public Transform translate(final float x, final float y, final float z)
	{
		return translate(new Vector3f(x, y, z));
	}

	/**
	 * Set the X and Y components of the translation of this Transform. The Z component will be left unchanged.
	 * 
	 * @param offset
	 *            The translation to use, as a {@link Vector2f}
	 * @return This, for chainability
	 */
	public Transform translate(final Vector2f offset)
	{
		return translate(new Vector3f(offset.x, offset.y, aVector.z));
	}

	/**
	 * Set the translation of this Transform
	 * 
	 * @param offset
	 *            The translation to use, as a {@link Vector3f}
	 * @return This, for chainability
	 */
	public Transform translate(final Vector3f offset)
	{
		aVector.set(offset);
		updated();
		return this;
	}

	/**
	 * Called when this Transform has been updated on this frame. Has no effect if notifyOnChange is false, otherwise notifies
	 * TransformManager about the change
	 */
	protected void updated()
	{
		if (!aNotifyOnChange) {
			return;
		}
		if (aMinimumVector != null) {
			MathUtils.clampVector3fUpLocal(aMinimumVector, aVector);
		}
		if (aMaximumVector != null) {
			MathUtils.clampVector3fDownLocal(aMaximumVector, aVector);
		}
		if (aMinimumScale != null) {
			MathUtils.clampVector3fUpLocal(aMinimumScale, aScale);
		}
		if (aMaximumScale != null) {
			MathUtils.clampVector3fDownLocal(aMaximumScale, aScale);
		}
		if (!aVector.equals(aOldVector) || !aRotation.equals(aOldRotation) || !aScale.equals(aOldScale) || aAlpha != aOldAlpha) {
			aOldVector.set(aVector);
			aOldRotation = aRotation;
			aOldScale.set(aScale);
			aOldAlpha = aAlpha;
			TransformManager.needUpdate(aNode);
			if (aFirstUpdate) {
				commit();
				aFirstUpdate = false;
			}
		}
	}
}
