package com.evervoid.client.ui;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.Sizable;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

/**
 * A UIControl that automatically scales its contents and/or scales to its contents. Used to contain flexible-size elements that
 * may not be part of the UI library.
 */
public class RescalableControl extends UIControl
{
	/**
	 * Whether this RescalableControl is allowed to downscale its contents
	 */
	private boolean aCanDownscale = true;
	/**
	 * Whether this RescalableControl is allowed to upscale its contents
	 */
	private boolean aCanUpscale = true;
	/**
	 * The maximum dimension that this RescalableControl is allowed to have (null for no limit)
	 */
	private Dimension aMaximumDimension = null;
	/**
	 * The minimum dimension that this RescalableControl is allowed to have (null for no limit)
	 */
	private Dimension aMinimumDimension = null;
	/**
	 * The {@link EverNode} contained within this RescalableControl
	 */
	private EverNode aNode;
	/**
	 * The {@link Transform} used to resize the node
	 */
	private Transform aResizing;
	/**
	 * The {@link Sizable} interface that the {@link EverNode} must implement.
	 */
	private final Sizable aSizeable;

	/**
	 * Constructor; Build a RescalableControl from any {@link Sizable} node. Note that this object must be a subclass of
	 * {@link EverNode}.
	 * 
	 * @param node
	 *            The {@link Sizable} {@link EverNode} to put inside this RescalableControl
	 */
	public RescalableControl(final Sizable node)
	{
		aSizeable = node;
		if (node instanceof EverNode) {
			aNode = (EverNode) node;
			aResizing = aNode.getNewTransform();
			addNode(aNode);
		}
	}

	/**
	 * Constructor; Build a RescalableControl from a {@link SpriteData}. This will automatically initialize a {@link Sprite}
	 * with the appropriate dimensions.
	 * 
	 * @param sprite
	 *            The {@link SpriteData} to build a {@link Sprite} out of
	 */
	public RescalableControl(final SpriteData sprite)
	{
		this(new Sprite(sprite));
	}

	/**
	 * Called whenever there is a need to refresh the desired dimension within the parent UI components
	 */
	private void refreshDesiredDimensions()
	{
		if (aMinimumDimension != null) {
			setDesiredDimension(aMinimumDimension);
		}
		else if (!aCanDownscale) {
			final Vector2f dim = aSizeable.getDimensions();
			setDesiredDimension(new Dimension(dim.x, dim.y));
		}
		else {
			setDesiredDimension(null);
		}
	}

	/**
	 * Set whether this RescalableControl should allow downscaling of its contents or not
	 * 
	 * @param allow
	 *            Whether to allow downscaling (true) or not (false)
	 * @return This, for chainability
	 */
	public RescalableControl setAllowDownscale(final boolean allow)
	{
		aCanDownscale = allow;
		refreshDesiredDimensions();
		recomputeAllBounds();
		return this;
	}

	/**
	 * Set whether this RescalableControl should allow upscaling or downscaling of its contents or not
	 * 
	 * @param allowUpscale
	 *            Whether to allow upscaling (true) or not (false)
	 * @param allowDownscale
	 *            Whether to allow downscaling (true) or not (false)
	 * @return This, for chainability
	 */
	public RescalableControl setAllowScale(final boolean allowUpscale, final boolean allowDownscale)
	{
		aCanUpscale = allowUpscale;
		aCanDownscale = allowDownscale;
		refreshDesiredDimensions();
		recomputeAllBounds();
		return this;
	}

	/**
	 * Set whether this RescalableControl should allow upscaling of its contents or not
	 * 
	 * @param allow
	 *            Whether to allow upscaling (true) or not (false)
	 * @return This, for chainability
	 */
	public RescalableControl setAllowUpscale(final boolean allow)
	{
		aCanUpscale = allow;
		recomputeAllBounds();
		return this;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		if (bounds == null) {
			return;
		}
		super.setBounds(bounds);
		if (aResizing == null) {
			return;
		}
		final Vector2f nodeDim = aSizeable.getDimensions();
		float scale = Math.min(1, Math.min(bounds.width / nodeDim.x, bounds.height / nodeDim.y));
		if (!aCanDownscale) {
			scale = Math.max(1, scale);
		}
		if (!aCanUpscale) {
			scale = Math.min(1, scale);
		}
		if (aMaximumDimension != null) {
			final float rescaledW = nodeDim.x * scale;
			final float rescaledH = nodeDim.y * scale;
			if (rescaledW > aMaximumDimension.width || rescaledH > aMaximumDimension.height) {
				scale = Math.min(scale, Math.min(aMaximumDimension.width / nodeDim.x, aMaximumDimension.height / nodeDim.y));
			}
		}
		if (aMinimumDimension != null) {
			final float rescaledW = nodeDim.x * scale;
			final float rescaledH = nodeDim.y * scale;
			if (rescaledW < aMinimumDimension.width || rescaledH < aMinimumDimension.height) {
				scale = Math.max(scale, Math.max(aMinimumDimension.width / nodeDim.x, aMinimumDimension.height / nodeDim.y));
			}
		}
		aResizing.setScale(scale).translate((float) bounds.width / 2, (float) bounds.height / 2);
	}

	/**
	 * Force this RescalableControl to always have a certain {@link Dimension}
	 * 
	 * @param enforced
	 *            The {@link Dimension} to force on the RescalableControl
	 * @return This, for chainability
	 */
	public RescalableControl setEnforcedDimension(final Dimension enforced)
	{
		aCanUpscale = false;
		aCanDownscale = false;
		aMinimumDimension = enforced;
		aMaximumDimension = enforced;
		refreshDesiredDimensions();
		recomputeAllBounds();
		return this;
	}

	/**
	 * Force this RescalableControl to always have a certain dimension
	 * 
	 * @param enforcedWidth
	 *            The width of the {@link Dimension} to force on the RescalableControl
	 * @param enforcedHeight
	 *            The height of the {@link Dimension} to force on the RescalableControl
	 * @return This, for chainability
	 */
	public RescalableControl setEnforcedDimension(final int enforcedWidth, final int enforcedHeight)
	{
		return setEnforcedDimension(new Dimension(enforcedWidth, enforcedHeight));
	}

	/**
	 * Set the maximum dimension of this RescalableControl
	 * 
	 * @param dimension
	 *            The maximum dimension that this RescalableControl is allowed to have (null for no limit)
	 * @return This, for chainability
	 */
	public RescalableControl setMaximumDimension(final Dimension dimension)
	{
		aMaximumDimension = dimension;
		recomputeAllBounds();
		return this;
	}

	/**
	 * Set the maximum dimension of this RescalableControl
	 * 
	 * @param maxWidth
	 *            The width of the maximum dimension that this RescalableControl is allowed to have
	 * @param maxHeight
	 *            The height of the maximum dimension that this RescalableControl is allowed to have
	 * @return This, for chainability
	 */
	public RescalableControl setMaximumDimension(final int maxWidth, final int maxHeight)
	{
		return setMaximumDimension(new Dimension(maxWidth, maxHeight));
	}

	/**
	 * Set the minimum dimension of this RescalableControl
	 * 
	 * @param dimension
	 *            The minimum dimension that this RescalableControl is allowed to have (null for no limit)
	 * @return This, for chainability
	 */
	public RescalableControl setMinimumDimension(final Dimension dimension)
	{
		aMinimumDimension = dimension;
		refreshDesiredDimensions();
		recomputeAllBounds();
		return this;
	}

	/**
	 * Set the minimum dimension of this RescalableControl
	 * 
	 * @param minWidth
	 *            The width of the minimum dimension that this RescalableControl is allowed to have
	 * @param minHeight
	 *            The height of the minimum dimension that this RescalableControl is allowed to have
	 * @return This, for chainability
	 */
	public RescalableControl setMinimumDimension(final int minWidth, final int minHeight)
	{
		return setMinimumDimension(new Dimension(minWidth, minHeight));
	}
}
