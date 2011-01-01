package client;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class Translation
{
	private Vector3f maximum = null;
	private Vector3f minimum = null;
	private final EverNode node;
	private final Vector3f oldVector = new Vector3f(0, 0, 0);
	private final Vector3f vector = new Vector3f(0, 0, 0);

	/**
	 * Warning: Do NOT call this directly; call parent.getNewTranslation()
	 * instead!
	 * 
	 * @param parent
	 *            The EverNode that this translation will affect
	 */
	protected Translation(final EverNode parent)
	{
		node = parent;
	}

	public Vector3f get()
	{
		return vector;
	}

	public Vector2f get2f()
	{
		return new Vector2f(vector.x, vector.y);
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
		vector.addLocal(offset);
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
		maximum = max;
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
		minimum = min;
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
		vector.set(offset);
		updated();
	}

	protected void updated()
	{
		if (minimum != null)
		{
			vector.x = Math.max(vector.x, minimum.x);
			vector.y = Math.max(vector.y, minimum.y);
			vector.z = Math.max(vector.z, minimum.z);
		}
		if (maximum != null)
		{
			vector.x = Math.min(vector.x, maximum.x);
			vector.y = Math.min(vector.y, maximum.y);
			vector.z = Math.min(vector.z, maximum.z);
		}
		if (!vector.equals(oldVector))
		{
			node.computeTranslation();
			oldVector.set(vector);
		}
	}
}
