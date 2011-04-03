package com.evervoid.client.ui;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.Sizeable;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

public class RescalableControl extends UIControl
{
	private boolean aCanDownscale = true;
	private boolean aCanUpscale = true;
	private Dimension aMaximumDimension = null;
	private Dimension aMinimumDimension = null;
	private EverNode aNode;
	private Transform aResizing;
	private final Sizeable aSizeable;

	public RescalableControl(final Sizeable node)
	{
		aSizeable = node;
		if (node instanceof EverNode) {
			aNode = (EverNode) node;
			aResizing = aNode.getNewTransform();
			addNode(aNode);
		}
	}

	public RescalableControl(final SpriteData sprite)
	{
		this(new Sprite(sprite));
	}

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

	public void setAllowDownscale(final boolean allow)
	{
		aCanDownscale = allow;
		refreshDesiredDimensions();
		recomputeAllBounds();
	}

	public void setAllowScale(final boolean allowUpscale, final boolean allowDownscale)
	{
		aCanUpscale = allowUpscale;
		aCanDownscale = allowDownscale;
		refreshDesiredDimensions();
		recomputeAllBounds();
	}

	public void setAllowUpscale(final boolean allow)
	{
		aCanUpscale = allow;
		recomputeAllBounds();
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

	public void setEnforcedDimension(final Dimension enforced)
	{
		aCanUpscale = false;
		aCanDownscale = false;
		aMinimumDimension = enforced;
		aMaximumDimension = enforced;
		refreshDesiredDimensions();
		recomputeAllBounds();
	}

	public void setEnforcedDimension(final int enforcedWidth, final int enforcedHeight)
	{
		setEnforcedDimension(new Dimension(enforcedWidth, enforcedHeight));
	}

	public void setMaximumDimension(final Dimension dimension)
	{
		aMaximumDimension = dimension;
		recomputeAllBounds();
	}

	public void setMaximumDimension(final int maxWidth, final int maxHeight)
	{
		setMaximumDimension(new Dimension(maxWidth, maxHeight));
	}

	public void setMinimumDimension(final Dimension dimension)
	{
		aMinimumDimension = dimension;
		refreshDesiredDimensions();
		recomputeAllBounds();
	}

	public void setMinimumDimension(final int minWidth, final int minHeight)
	{
		setMinimumDimension(new Dimension(minWidth, minHeight));
	}
}
