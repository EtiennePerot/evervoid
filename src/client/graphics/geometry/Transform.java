package client.graphics.geometry;

import client.EverNode;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class Transform
{
	protected float aAlpha = 1f;
	private Vector3f aMaximumVector = null;
	private Vector3f aMinimumVector = null;
	protected final EverNode aNode;
	private boolean aNotifyOnChange = true;
	protected float aOldAlpha = 1f;
	protected float aOldRotation = 0f;
	protected float aOldScale = 1f;
	protected final Vector3f aOldVector = new Vector3f(0, 0, 0);
	protected float aRotation = 0f;
	protected float aScale = 1f;
	protected final Vector3f aVector = new Vector3f(0, 0, 0);

	/**
	 * Warning: Do NOT call this directly; call parent.getNewTransform()
	 * instead!
	 * 
	 * @param parent
	 *            The EverNode that this transformation will affect
	 */
	public Transform(final EverNode parent)
	{
		aNode = parent;
	}

	public void faceTowards(final Vector2f point)
	{
		final Float angle = Geometry.getAngleTowards(point);
		if (angle != null)
		{
			rotateTo(angle);
		}
	}

	public float getAlpha()
	{
		return aAlpha;
	}

	/**
	 * Returns the EverNode that this Transform is attached to
	 * 
	 * @return The attached EverNoe
	 */
	public EverNode getNode()
	{
		return aNode;
	}

	public float getRotation()
	{
		return aRotation;
	}

	public float getScale()
	{
		return aScale;
	}

	public Vector3f getTranslation()
	{
		return aVector;
	}

	public Vector2f getTranslation2f()
	{
		return new Vector2f(aVector.x, aVector.y);
	}

	public void move(final float x, final float y)
	{
		move(new Vector3f(x, y, 0));
	}

	public void move(final float x, final float y, final float z)
	{
		move(new Vector3f(x, y, z));
	}

	public void move(final Vector2f offset)
	{
		move(new Vector3f(offset.x, offset.y, 0));
	}

	public void move(final Vector3f offset)
	{
		aVector.addLocal(offset);
		updated();
	}

	public void multScale(final float scale)
	{
		setScale(scale * aScale);
	}

	public void rotateBy(final float angle)
	{
		rotateTo(aRotation + angle);
	}

	public void rotateTo(final float angle)
	{
		aRotation = angle % FastMath.TWO_PI;
		updated();
	}

	public void setAlpha(final float alpha)
	{
		aAlpha = Geometry.clampFloat(0, alpha, 1);
		updated();
	}

	public void setMaximumConstraint(final float x, final float y)
	{
		setMaximumConstraint(new Vector3f(x, y, 0));
	}

	public void setMaximumConstraint(final float x, final float y, final float z)
	{
		setMaximumConstraint(new Vector3f(x, y, z));
	}

	public void setMaximumConstraint(final Vector2f max)
	{
		setMaximumConstraint(new Vector3f(max.x, max.y, 0));
	}

	public void setMaximumConstraint(final Vector3f max)
	{
		aMaximumVector = max;
		updated();
	}

	public void setMinimumConstraint(final float x, final float y)
	{
		setMinimumConstraint(new Vector3f(x, y, 0));
	}

	public void setMinimumConstraint(final float x, final float y, final float z)
	{
		setMinimumConstraint(new Vector3f(x, y, z));
	}

	public void setMinimumConstraint(final Vector2f max)
	{
		setMinimumConstraint(new Vector3f(max.x, max.y, 0));
	}

	public void setMinimumConstraint(final Vector3f min)
	{
		aMinimumVector = min;
		updated();
	}

	protected void setNotifyOnChange(final boolean notify)
	{
		aNotifyOnChange = notify;
	}

	public void setScale(final float scale)
	{
		aScale = Math.max(0, scale);
		updated();
	}

	public void translate(final float x, final float y)
	{
		translate(new Vector3f(x, y, 0));
	}

	public void translate(final float x, final float y, final float z)
	{
		translate(new Vector3f(x, y, z));
	}

	public void translate(final Vector2f offset)
	{
		translate(new Vector3f(offset.x, offset.y, 0));
	}

	public void translate(final Vector3f offset)
	{
		aVector.set(offset);
		updated();
	}

	protected void updated()
	{
		if (!aNotifyOnChange)
		{
			return;
		}
		if (aMinimumVector != null)
		{
			aVector.x = Math.max(aVector.x, aMinimumVector.x);
			aVector.y = Math.max(aVector.y, aMinimumVector.y);
			aVector.z = Math.max(aVector.z, aMinimumVector.z);
		}
		if (aMaximumVector != null)
		{
			aVector.x = Math.min(aVector.x, aMaximumVector.x);
			aVector.y = Math.min(aVector.y, aMaximumVector.y);
			aVector.z = Math.min(aVector.z, aMaximumVector.z);
		}
		if (!aVector.equals(aOldVector) || !Geometry.near(aOldRotation, aRotation) || !Geometry.near(aAlpha, aOldAlpha)
				|| !Geometry.near(aScale, aOldScale))
		{
			TransformManager.needUpdate(aNode);
			aOldVector.set(aVector);
			aOldRotation = aRotation;
			aOldScale = aScale;
			aOldAlpha = aAlpha;
		}
	}
}
