package com.evervoid.client.graphics;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.AnimatedFloatingTranslation;
import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.client.graphics.geometry.AnimatedScaling;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.graphics.geometry.Transformable;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * General-purpose 3D node class. Used pretty much everywhere. Supports animations and recursion
 */
public class EverNode extends Node implements Transformable
{
	private float aFinalAlpha = 1f;
	private final Vector3f aFinalRotation = new Vector3f(0f, 0f, 0f);
	private final Vector3f aFinalScale = new Vector3f(1f, 1f, 1f);
	private final Vector3f aFinalTranslation = new Vector3f();
	/**
	 * Pointer to parent EverNode
	 */
	protected EverNode aParent = null;
	/**
	 * Pre-multiplied alpha value of the parent EverNodes
	 */
	private float aParentAlpha = 1f;
	/**
	 * Whether we should synchronize this object's transformation properties on the next frame or not (translation/rotation/etc)
	 */
	protected AtomicBoolean aRefreshTransform = new AtomicBoolean(false);
	/**
	 * Set of children nodes
	 */
	protected Set<EverNode> aSubnodes = new HashSet<EverNode>();
	/**
	 * Non-multiplied alpha value specific to this one EverNode, computed from Transforms
	 */
	private float aThisAlpha = 1f;
	/**
	 * Set of transformations to apply to this EverNode. Includes animated Transforms
	 */
	protected Set<Transform> aTransforms = new HashSet<Transform>();

	/**
	 * Default constructor
	 */
	public EverNode()
	{
		super();
	}

	/**
	 * Wrapper constructor: Builds an EverNode wrapping a pure jME3 Spatial
	 * 
	 * @param child
	 *            The Spatial object to wrap
	 */
	public EverNode(final Spatial child)
	{
		this();
		attachChild(child);
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
	 * Called when this node is destroyed. Node should unregister from whatever events it receives.
	 */
	protected void cleanUp()
	{
		for (final EverNode n : aSubnodes) {
			n.cleanUp();
		}
	}

	/**
	 * Recompute final transformation by iterating through all Transforms
	 */
	public void computeTransforms()
	{
		aFinalTranslation.set(0, 0, 0);
		aFinalRotation.set(0f, 0f, 0f);
		aFinalScale.set(1f, 1f, 1f);
		aFinalAlpha = 1f;
		for (final Transform t : aTransforms) {
			aFinalTranslation.addLocal(t.getTranslation());
			aFinalRotation.addLocal(t.getRotation());
			aFinalAlpha *= t.getAlpha();
			aFinalScale.multLocal(t.getScale());
		}
		populateTransforms();
	}

	/**
	 * Deletes all children nodes from this EverNode
	 */
	public void delAllNodes()
	{
		for (final EverNode n : aSubnodes) {
			delNode(n);
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
		if (node == null) {
			return;
		}
		node.cleanUp();
		if (aSubnodes.contains(node)) {
			aSubnodes.remove(node);
		}
		if (hasChild(node)) {
			detachChild(node);
		}
	}

	/**
	 * Create a new alpha animation Transform, and associates it with this EverNode
	 * 
	 * @return A new alpha animation Transform
	 */
	@Override
	public AnimatedAlpha getNewAlphaAnimation()
	{
		final AnimatedAlpha t = new AnimatedAlpha(this);
		aTransforms.add(t);
		return t;
	}

	/**
	 * Create a new floating translation animation Transform, and associates it with this EverNode
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
	 * Create a new rotation animation Transform, and associates it with this EverNode
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
	 * Create a new scaling animation Transform, and associates it with this EverNode
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
	 * Create a new translation animation Transform, and associates it with this EverNode
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
	 * Marks this node as needing a Geometry update
	 */
	public void needGeometricUpdate()
	{
		aRefreshTransform.set(true);
		for (final EverNode n : aSubnodes) {
			n.needGeometricUpdate();
		}
	}

	protected void populateTransforms()
	{
		setLocalTranslation(aFinalTranslation);
		setRotation(aFinalRotation);
		setLocalScale(aFinalScale);
		if (!MathUtils.near(aFinalAlpha, aThisAlpha)) {
			setInternalAlpha(aFinalAlpha);
		}
		for (final EverNode n : aSubnodes) {
			n.populateTransforms();
		}
	}

	/**
	 * Removes this node from the parent EverNode
	 */
	@Override
	public boolean removeFromParent()
	{
		if (aParent == null) {
			return false;
		}
		aParent.delNode(this);
		return true;
	}

	/**
	 * Called when the resolution is changed. Meant to be overridden by subclasses to perform all resolution-based size
	 * computations. Also recurses the resolution change to all subnodes.
	 */
	public void resolutionChanged()
	{
		for (final EverNode e : aSubnodes) {
			e.resolutionChanged();
		}
	}

	/**
	 * Called when this EverNode's alpha has changed. Meant to be overridden by subclasses
	 * 
	 * @param alpha
	 *            The final alpha value, computed from this EverNode's Transform and the alpha value of the EverNodes higher in
	 *            the tree
	 */
	protected void setAlpha(final float alpha)
	{
		// Overriden by subclasses by final alpha value
	}

	/**
	 * Sets the current node's internal (non-multiplied) alpha to a new value, notifies subclasses, and notifies children about
	 * the new value recursively. Also calls setAlpha on this EverNode with the final alpha value
	 * 
	 * @param alpha
	 */
	protected void setInternalAlpha(final float alpha)
	{
		aThisAlpha = alpha;
		setAlpha(aThisAlpha * aParentAlpha);
		for (final EverNode n : aSubnodes) {
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
	private void setRotation(final Vector3f rotation)
	{
		setLocalRotation(new Quaternion().fromAngles(rotation.x, rotation.y, rotation.z));
	}
}
