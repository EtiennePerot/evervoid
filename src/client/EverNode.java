package client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import client.graphics.FrameUpdate;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class EverNode extends Node
{
	protected EverNode aParent = null;
	protected Vector2f aRotationAxisOffset = new Vector2f(0, 0);
	protected Set<EverNode> aSubnodes = new HashSet<EverNode>();
	protected List<Translation> aTranslations = new ArrayList<Translation>();

	public EverNode()
	{
		super();
		resolutionChanged();
	}

	public void addNode(final EverNode node)
	{
		node.setParent(this);
		aSubnodes.add(node);
		attachChild(node);
	}

	public void computeTranslation()
	{
		final Vector3f finalOffset = new Vector3f(0, 0, 0);
		for (final Translation t : aTranslations)
		{
			finalOffset.addLocal(t.get());
		}
		setLocalTranslation(finalOffset);
	}

	public void delNode(final EverNode node)
	{
		if (aSubnodes.contains(node))
		{
			aSubnodes.remove(node);
		}
		if (hasChild(node))
		{
			detachChild(node);
		}
	}

	public void faceTowards(final Vector2f point)
	{
		if (point == null)
		{
			setLocalRotation(new Quaternion());
		}
		float angle = 0f;
		if (point.equals(Vector2f.ZERO))
		{
			return; // Don't rotate at all
		}
		if (point.x == 0) // Avoid division by 0
		{
			if (point.y < 0)
			{
				angle = FastMath.PI / 2;
			}
			else
			{
				angle = -FastMath.PI / 2;
			}
		}
		else
		{
			angle = FastMath.PI + FastMath.atan(point.y / point.x);
			if (point.x < 0)
			{
				angle -= FastMath.PI;
			}
		}
		setRotation(angle);
	}

	public void frame(final FrameUpdate tpf)
	{
		// Do nothing; overridden by subclasses
	}

	public Translation getNewTranslation()
	{
		final Translation t = new Translation(this);
		aTranslations.add(t);
		return t;
	}

	public void recurse(final FrameUpdate f)
	{
		for (final EverNode e : aSubnodes)
		{
			e.recurse(f);
		}
		frame(f);
	}

	public void resolutionChanged()
	{
		for (final EverNode e : aSubnodes)
		{
			e.resolutionChanged();
		}
	}

	protected void setParent(final EverNode node)
	{
		aParent = node;
	}

	public void setRotation(final float angle)
	{
		setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Z));
	}
}
