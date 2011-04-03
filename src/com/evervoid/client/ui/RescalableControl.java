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

	public void setAllowDownscale(final boolean allow)
	{
		aCanDownscale = allow;
		setBounds(getComputedBounds());
	}

	public void setAllowScale(final boolean allowUpscale, final boolean allowDownscale)
	{
		aCanUpscale = allowUpscale;
		aCanDownscale = allowDownscale;
		setBounds(getComputedBounds());
	}

	public void setAllowUpscale(final boolean allow)
	{
		aCanUpscale = allow;
		setBounds(getComputedBounds());
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		if (bounds == null || aResizing == null) {
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
		aResizing.setScale(scale).translate((float) bounds.width / 2, (float) bounds.height / 2);
	}

	public void setMaximumDimension(final Dimension dimension)
	{
		aMaximumDimension = dimension;
		setBounds(getComputedBounds());
	}

	public void setMaximumDimension(final int maxWidth, final int maxHeight)
	{
		setMaximumDimension(new Dimension(maxWidth, maxHeight));
	}
}
