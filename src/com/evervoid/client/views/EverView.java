package com.evervoid.client.views;

import com.evervoid.client.EverNode;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.EverVoidClient.NodeType;
import com.evervoid.client.InputListener;
import com.jme3.math.Vector2f;

public abstract class EverView extends EverNode implements InputListener
{
	Bounds aBounds;

	protected EverView()
	{
		this(new Bounds(0, 0, EverVoidClient.getWindowDimension().width, EverVoidClient.getWindowDimension().height));
	}

	protected EverView(final Bounds pBound)
	{
		aBounds = pBound;
	}

	protected Bounds getBounds()
	{
		return aBounds;
	}

	protected int getBoundsHeight()
	{
		return aBounds.height;
	}

	protected int getBoundsWidth()
	{
		return aBounds.width;
	}

	public NodeType getNodeType()
	{
		return NodeType.TWODIMENSION;
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onMouseMove(final float tpf, final Vector2f position)
	{
		return false;
	}

	@Override
	public boolean onMouseRelease(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		return false;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		return false;
	}

	protected void setBounds(final Bounds pBounds)
	{
		aBounds = pBounds;
	}
}
