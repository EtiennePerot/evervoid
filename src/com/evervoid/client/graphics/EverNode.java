package com.evervoid.client.graphics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.AnimatedFloatingTranslation;
import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.client.graphics.geometry.AnimatedScaling;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.utils.EVUtils;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * General-purpose 3D node class. Used pretty much everywhere. Supports animations and composition
 */
public class EverNode extends Node
{
	/**
	 * All-computed, inherited+local alpha value to use. Alpha computation can be expensive due to the composite nature of
	 * nodes, so the alpha value is stored here instead of being recomputed.
	 */
	private float aFinalAlpha = 1f;
	/**
	 * All-computed, local-only rotation to use. Stored as a 3-float vector to store pitch (rotation around the x axis), yaw
	 * (rotation around the y axis), and roll (rotation around the z axis)
	 */
	private final Vector3f aFinalRotation = new Vector3f(0f, 0f, 0f);
	/**
	 * All-computed, local-only scale to use. Stored as a 3-float vector to store x scale, y scale, and z scale
	 */
	private final Vector3f aFinalScale = new Vector3f(1f, 1f, 1f);
	/**
	 * All-computed, local-only translation to use. Stored as a 3-float vector to store x offset, y offset, and z offset
	 */
	private final Vector3f aFinalTranslation = new Vector3f(0f, 0f, 0f);
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
	private final AtomicBoolean aRefreshTransform = new AtomicBoolean(false);
	/**
	 * Built-in alpha animation to smoothly appear/disappear
	 */
	private AnimatedAlpha aSmoothAppear = null;
	/**
	 * Set of children nodes
	 */
	private final Set<EverNode> aSubnodes = new HashSet<EverNode>();
	/**
	 * Non-multiplied alpha value specific to this one EverNode, computed from Transforms
	 */
	private float aThisAlpha = 1f;
	/**
	 * Set of transformations to apply to this EverNode. Includes animated Transforms
	 */
	private final Set<Transform> aTransforms = new HashSet<Transform>();

	/**
	 * Default constructor
	 */
	public EverNode()
	{
	}

	/**
	 * Wrapper constructor: Builds an EverNode wrapping another EverNode
	 * 
	 * @param child
	 *            The EverNode to wrap
	 */
	public EverNode(final EverNode child)
	{
		this();
		addNode(child);
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
		if (node == null || node == this) {
			return; // Nope
		}
		node.setParent(this);
		aSubnodes.add(node);
		attachChild(node);
		populateTransforms();
	}

	/**
	 * Called when this node is destroyed. Node should unregister from whatever events it receives.
	 */
	protected void cleanUp()
	{
		for (final EverNode n : getEffectiveChildren()) {
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
		// Use a temporary list to prevent concurrent modification
		final List<EverNode> tempList = new ArrayList<EverNode>(getEffectiveChildren());
		for (final EverNode n : tempList) {
			n.removeFromParent();
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
		node.aParent = null;
		if (aSubnodes.contains(node)) {
			aSubnodes.remove(node);
		}
		if (hasChild(node)) {
			detachChild(node);
		}
	}

	/**
	 * Returns the current, all-multiplied alpha value of this EverNode. Materials and objects requiring manual re-painting
	 * (other than through setAlpha) should use this to get the correct alpha value to use
	 * 
	 * @return The final alpha value of this EverNode
	 */
	protected float getComputedAlpha()
	{
		return aThisAlpha * aParentAlpha;
	}

	/**
	 * @return All EverNodes that operations on this EverNode should also affect
	 */
	protected Set<EverNode> getEffectiveChildren()
	{
		return aSubnodes;
	}

	/**
	 * @return The final rotation of this EverNode
	 */
	protected Vector3f getFinalRotation()
	{
		return aFinalRotation;
	}

	/**
	 * @return The final scale of this EverNode
	 */
	protected Vector3f getFinalScale()
	{
		return aFinalScale;
	}

	/**
	 * @return The final translation of this EverNode
	 */
	protected Vector3f getFinalTranslation()
	{
		return aFinalTranslation;
	}

	/**
	 * Create a new alpha animation Transform, and associates it with this EverNode
	 * 
	 * @return A new alpha animation Transform
	 */
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

	/**
	 * Called when Transforms have been changed; applies the final-computed transformation properties to the EverNode, and
	 * propagates alpha to all children.
	 */
	protected void populateTransforms()
	{
		setLocalTranslation(aFinalTranslation);
		setRotation(aFinalRotation);
		setLocalScale(aFinalScale);
		if (!MathUtils.near(aFinalAlpha, aThisAlpha)) {
			setInternalAlpha(aFinalAlpha);
		}
		for (final EverNode n : getEffectiveChildren()) {
			n.populateTransforms();
		}
	}

	/**
	 * Removes this node from the parent EverNode
	 * 
	 * @return Whether this node had a parent or not
	 */
	@Override
	public boolean removeFromParent()
	{
		if (aParent == null) {
			EverVoidClient.delRootNode(this);
			return false;
		}
		aParent.delNode(this);
		return true;
	}

	/**
	 * Do NOT call this to delete a Transform! Call transform.delete() instead.
	 * 
	 * @param transform
	 *            The transform to delete.
	 */
	public void removeTransform(final Transform transform)
	{
		if (equals(transform.getNode())) {
			aTransforms.remove(transform);
		}
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
	 * Called when this EverNode's alpha has changed. Meant to be overridden by subclasses. DO NOT CALL THIS DIRECTLY; use
	 * Transforms instead.
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
	 *            The alpha to use
	 */
	protected void setInternalAlpha(final float alpha)
	{
		aThisAlpha = alpha;
		setAlpha(aThisAlpha * aParentAlpha);
		for (final EverNode n : getEffectiveChildren()) {
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
	 * @param rotation
	 *            The 3-float vector to use as rotation (x = pitch, y = yaw, z = roll)
	 */
	private void setRotation(final Vector3f rotation)
	{
		setLocalRotation(new Quaternion().fromAngles(rotation.x, rotation.y, rotation.z));
	}

	/**
	 * Smoothly fades this EverNode in.
	 * 
	 * @param duration
	 *            The duration of the fade.
	 * @return this for chainability
	 */
	public EverNode smoothAppear(final float duration)
	{
		return smoothAppear(duration, null);
	}

	/**
	 * Smoothly fades this EverNode in.
	 * 
	 * @param duration
	 *            The duration of the fade.
	 * @param callback
	 *            A callback to call when the animation is done
	 * @return this for chainability
	 */
	public EverNode smoothAppear(final float duration, final Runnable callback)
	{
		if (aSmoothAppear == null) {
			aSmoothAppear = getNewAlphaAnimation();
			aSmoothAppear.setAlpha(0);
		}
		aSmoothAppear.setTargetAlpha(1).setDuration(duration).start(callback);
		return this;
	}

	/**
	 * Smoothly deletes this EverNode.
	 * 
	 * @param duration
	 *            The duration of the fade.
	 * @return this for chainability
	 */
	public EverNode smoothDisappear(final float duration)
	{
		return smoothDisappear(duration, null);
	}

	/**
	 * Smoothly deletes this EverNode.
	 * 
	 * @param duration
	 *            The duration of the fade.
	 * @param callback
	 *            A callback to call when the animation is done
	 * @return this for chainability
	 */
	public EverNode smoothDisappear(final float duration, final Runnable callback)
	{
		if (aSmoothAppear == null) {
			aSmoothAppear = getNewAlphaAnimation();
			aSmoothAppear.setAlpha(1);
		}
		aSmoothAppear.setTargetAlpha(0).setDuration(duration).start(new Runnable()
		{
			@Override
			public void run()
			{
				removeFromParent();
				EVUtils.runCallback(callback);
			}
		});
		return this;
	}

	@Override
	public String toString()
	{
		String str = getClass().getSimpleName() + "-EverNode @ " + hashCode();
		if (getName() != null) {
			str += " [Named " + getName() + "]";
		}
		return str;
	}
}
