package client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import client.graphics.FrameUpdate;
import client.graphics.geometry.AnimatedRotation;
import client.graphics.geometry.AnimatedTranslation;
import client.graphics.geometry.Transform;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class EverNode extends Node
{
	protected EverNode aParent = null;
	protected AnimatedRotation aRotationAnimation = null;
	protected boolean aRotationAnimationEnabled = false;
	protected Vector2f aRotationAxisOffset = new Vector2f(0, 0);
	protected Set<EverNode> aSubnodes = new HashSet<EverNode>();
	protected List<Transform> aTransforms = new ArrayList<Transform>();
	protected AnimatedTranslation aTranslationAnimation = null;
	protected boolean aTranslationAnimationEnabled = false;

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
		if (aTranslationAnimation != null)
		{
			finalOffset.addLocal(aTranslationAnimation.getTranslation());
		}
		if (aRotationAnimation != null)
		{
			finalAngle += aRotationAnimation.getRotation();
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
		if (aTranslationAnimationEnabled)
		{
			aTranslationAnimation.frame(f);
		}
		if (aRotationAnimationEnabled)
		{
			aRotationAnimation.frame(f);
		}
	}

	public Transform getNewTransform()
	{
		final Transform t = new Transform(this);
		aTransforms.add(t);
		return t;
	}

	public AnimatedRotation getRotationAnimation()
	{
		if (aRotationAnimation == null)
		{
			aRotationAnimation = new AnimatedRotation(this);
		}
		return aRotationAnimation;
	}

	public AnimatedTranslation getTranslationAnimation()
	{
		if (aTranslationAnimation == null)
		{
			aTranslationAnimation = new AnimatedTranslation(this);
		}
		return aTranslationAnimation;
	}

	public void recurse(final FrameUpdate f)
	{
		frame(f);
		for (final EverNode e : aSubnodes)
		{
			e.recurse(f);
		}
	}

	public void registerRotationAnimation(final boolean subscribe)
	{
		aRotationAnimationEnabled = subscribe;
	}

	public void registerTranslationAnimation(final boolean subscribe)
	{
		aTranslationAnimationEnabled = subscribe;
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
}
