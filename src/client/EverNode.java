package client;

import java.util.HashSet;
import java.util.Set;

import client.graphics.FrameUpdate;
import client.graphics.geometry.AnimatedFloatingTranslation;
import client.graphics.geometry.AnimatedRotation;
import client.graphics.geometry.AnimatedScaling;
import client.graphics.geometry.AnimatedTransform;
import client.graphics.geometry.AnimatedTranslation;
import client.graphics.geometry.Geometry;
import client.graphics.geometry.Transform;
import client.graphics.geometry.Transformable;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * General-purpose 3D node class. Used pretty much everywhere. Supports
 * animations and recursion
 */
public class EverNode extends Node implements Transformable
{
	/**
	 * Set of undergoing animations
	 */
	protected Set<AnimatedTransform> aAnimations = new HashSet<AnimatedTransform>();
	/**
	 * Set of animations that finished during the last frame. Used for cleaning
	 * up the animation queue
	 */
	protected Set<AnimatedTransform> aFinishedAnimations = new HashSet<AnimatedTransform>();
	/**
	 * Pointer to parent EverNode
	 */
	protected EverNode aParent = null;
	/**
	 * Pre-multiplied alpha value of the parent EverNodes
	 */
	private float aParentAlpha = 1f;
	/**
	 * Set of children nodes
	 */
	protected Set<EverNode> aSubnodes = new HashSet<EverNode>();
	/**
	 * Non-multiplied alpha value specific to this one EverNode, computed from
	 * Transforms
	 */
	private float aThisAlpha = 1f;
	/**
	 * Set of transformations to apply to this EverNode. Includes animated
	 * Transforms
	 */
	protected Set<Transform> aTransforms = new HashSet<Transform>();

	/**
	 * Default constructor
	 */
	public EverNode()
	{
		super();
		resolutionChanged();
	}

	/**
	 * Add a child EverNode
	 * 
	 * @param node
	 *            The node to add
	 */
	public void addNode(final EverNode node)
	{
		node.setParent(this);
		aSubnodes.add(node);
		attachChild(node);
	}

	/**
	 * Recompute final transformation by iterating through all Transforms
	 */
	public void computeTransforms()
	{
		final Vector3f finalOffset = new Vector3f(0, 0, 0);
		float finalAngle = 0f;
		float finalAlpha = 1f;
		float finalScale = 1f;
		for (final Transform t : aTransforms)
		{
			finalOffset.addLocal(t.getTranslation());
			finalAngle += t.getRotation();
			finalAlpha *= t.getAlpha();
			finalScale *= t.getScale();
		}
		setLocalTranslation(finalOffset);
		setRotation(finalAngle);
		setLocalScale(finalScale);
		if (!Geometry.near(finalAlpha, aThisAlpha))
		{
			setInternalAlpha(finalAlpha);
		}
	}

	/**
	 * Delete a child EverNode
	 * 
	 * @param node
	 *            The child to delete
	 */
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

	/**
	 * Called on each game frame. The default behavior is to take care of
	 * animations by notifying active animation Transforms
	 * 
	 * @param f
	 *            This frame's FrameUpdate object
	 */
	public void frame(final FrameUpdate f)
	{
		boolean recompute = false;
		for (final AnimatedTransform t : aAnimations)
		{
			recompute = t.frame(f) || recompute;
		}
		if (!aFinishedAnimations.isEmpty()) // Clean up finished animations
		{
			for (final AnimatedTransform t : aFinishedAnimations)
			{
				aAnimations.remove(t);
			}
			aFinishedAnimations.clear();
		}
		if (recompute)
		{
			computeTransforms();
		}
	}

	/**
	 * Create a new floating translation animation Transform, and associates it
	 * with this EverNode
	 * 
	 * @return A new floating translation animation Transform
	 */
	@Override
	public AnimatedFloatingTranslation getNewFloatingTranslationAnimation()
	{
		final AnimatedFloatingTranslation t = new AnimatedFloatingTranslation(this);
		aTransforms.add(t);
		return t;
	}

	/**
	 * Create a new rotation animation Transform, and associates it with this
	 * EverNode
	 * 
	 * @return A new rotation animation Transform
	 */
	@Override
	public AnimatedRotation getNewRotationAnimation()
	{
		final AnimatedRotation t = new AnimatedRotation(this);
		aTransforms.add(t);
		return t;
	}

	/**
	 * Create a new scaling animation Transform, and associates it with this
	 * EverNode
	 * 
	 * @return A new scaling animation Transform
	 */
	@Override
	public AnimatedScaling getNewScalingAnimation()
	{
		final AnimatedScaling t = new AnimatedScaling(this);
		aTransforms.add(t);
		return t;
	}

	/**
	 * Create a new basic Transform and associates it with this EverNode
	 * 
	 * @return A new basic Transform
	 */
	@Override
	public Transform getNewTransform()
	{
		final Transform t = new Transform(this);
		aTransforms.add(t);
		return t;
	}

	/**
	 * Create a new translation animation Transform, and associates it with this
	 * EverNode
	 * 
	 * @return A new translation animation Transform
	 */
	@Override
	public AnimatedTranslation getNewTranslationAnimation()
	{
		final AnimatedTranslation t = new AnimatedTranslation(this);
		aTransforms.add(t);
		return t;
	}

	/**
	 * Pass the frame update to all children
	 * 
	 * @param f
	 *            The FrameUpdate object of the current frame
	 */
	public void recurse(final FrameUpdate f)
	{
		frame(f);
		for (final EverNode e : aSubnodes)
		{
			e.recurse(f);
		}
	}

	/**
	 * Called by animation Transforms when an animation is started. Registers
	 * the specified animation Transform to receive frame update event
	 * 
	 * @param animation
	 *            The animated Transform to register
	 */
	@Override
	public void registerAnimation(final AnimatedTransform animation)
	{
		if (!aAnimations.contains(animation))
		{
			aAnimations.add(animation);
			computeTransforms();
		}
	}

	/**
	 * Called when the resolution is changed AND when the EverNode is created.
	 * Meant to be overridden by subclasses to perform all resolution-based size
	 * calculations. Also recurses the resolution change to all subnodes.
	 */
	public void resolutionChanged()
	{
		for (final EverNode e : aSubnodes)
		{
			e.resolutionChanged();
		}
	}

	/**
	 * Called when this EverNode's alpha has changed. Meant to be overridden by
	 * subclasses
	 * 
	 * @param alpha
	 *            The final alpha value, computed from this EverNode's Transform
	 *            and the alpha value of the EverNodes higher in the tree
	 */
	protected void setAlpha(final float alpha)
	{
		// Overriden by subclasses by final alpha value
	}

	/**
	 * Sets the current node's internal (non-multiplied) alpha to a new value,
	 * notifies subclasses, and notifies children about the new value
	 * recursively. Also calls setAlpha on this EverNode with the final alpha
	 * value
	 * 
	 * @param alpha
	 */
	protected void setInternalAlpha(final float alpha)
	{
		aThisAlpha = alpha;
		setAlpha(aThisAlpha * aParentAlpha);
		for (final EverNode n : aSubnodes)
		{
			n.aParentAlpha = aParentAlpha * aThisAlpha;
			n.setInternalAlpha(n.aThisAlpha);
		}
	}

	/**
	 * Set the parent EverNode of this EverNode
	 * 
	 * @param node
	 *            The parent of this EverNode
	 */
	protected void setParent(final EverNode node)
	{
		aParent = node;
	}

	/**
	 * Compute Quaternion-based rotation from the requested angle of rotation
	 * 
	 * @param angle
	 *            The angle to rotate to
	 */
	private void setRotation(final float angle)
	{
		setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Z));
	}

	/**
	 * Unregister an animated Transform from this EverNode. Called by animated
	 * Trasforms when they are finished and no longer need to receive frame
	 * update events
	 * 
	 * @param animation
	 *            The animated Transform to unregister
	 */
	@Override
	public void unregisterAnimation(final AnimatedTransform animation)
	{
		if (aAnimations.contains(animation))
		{
			aFinishedAnimations.remove(animation);
		}
	}
}
