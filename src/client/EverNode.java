package client;

import java.util.HashSet;
import java.util.Set;

import client.graphics.FrameUpdate;
import client.graphics.geometry.AnimatedRotation;
import client.graphics.geometry.AnimatedTransform;
import client.graphics.geometry.AnimatedTranslation;
import client.graphics.geometry.Transform;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class EverNode extends Node
{
	protected Set<AnimatedTransform> aAnimations = new HashSet<AnimatedTransform>();
	protected Set<AnimatedTransform> aFinishedAnimations = new HashSet<AnimatedTransform>();
	protected EverNode aParent = null;
	protected Vector2f aRotationAxisOffset = new Vector2f(0, 0);
	protected Set<EverNode> aSubnodes = new HashSet<EverNode>();
	protected Set<Transform> aTransforms = new HashSet<Transform>();

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

	public void computeTransforms()
	{
		final Vector3f finalOffset = new Vector3f(0, 0, 0);
		float finalAngle = 0f;
		for (final Transform t : aTransforms)
		{
			finalOffset.addLocal(t.getTranslation());
			finalAngle += t.getRotation();
		}
		setLocalTranslation(finalOffset);
		setRotation(finalAngle);
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

	public void frame(final FrameUpdate f)
	{
		aFinishedAnimations.clear();
		for (final AnimatedTransform t : aAnimations)
		{
			t.frame(f);
		}
		if (!aFinishedAnimations.isEmpty())
		{
			for (final AnimatedTransform t : aFinishedAnimations)
			{
				aAnimations.remove(t);
			}
			aFinishedAnimations.clear();
			computeTransforms();
		}
	}

	public AnimatedRotation getNewRotationAnimation()
	{
		final AnimatedRotation t = new AnimatedRotation(this);
		aTransforms.add(t);
		return t;
	}

	public Transform getNewTransform()
	{
		final Transform t = new Transform(this);
		aTransforms.add(t);
		return t;
	}

	public AnimatedTranslation getNewTranslationAnimation()
	{
		final AnimatedTranslation t = new AnimatedTranslation(this);
		aTransforms.add(t);
		return t;
	}

	public void recurse(final FrameUpdate f)
	{
		frame(f);
		for (final EverNode e : aSubnodes)
		{
			e.recurse(f);
		}
	}

	public void registerAnimation(final AnimatedTransform animation)
	{
		if (!aAnimations.contains(animation))
		{
			aAnimations.add(animation);
			computeTransforms();
		}
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

	protected void setRotation(final float angle)
	{
		setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Z));
	}

	public void unregisterAnimation(final AnimatedTransform animation)
	{
		if (aAnimations.contains(animation))
		{
			aFinishedAnimations.remove(animation);
		}
	}
}
