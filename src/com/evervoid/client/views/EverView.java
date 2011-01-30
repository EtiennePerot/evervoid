package com.evervoid.client.views;

import com.evervoid.client.EverNode;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.EverVoidClient.NodeType;
import com.evervoid.client.InputListener;
import com.evervoid.state.Dimension;
import com.jme3.math.Vector2f;

public abstract class EverView extends EverNode implements InputListener
{
	Dimension aDimension;

	protected EverView()
	{
		this(EverVoidClient.getWindowDimension());
	}

	protected EverView(final Dimension pDimension)
	{
		aDimension = pDimension;
	}

	protected int getHeight()
	{
		return aDimension.getHeight();
	}

	public NodeType getNodeType()
	{
		return NodeType.TWODIMENSION;
	}

	protected int getWidth()
	{
		return aDimension.getWidth();
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

	protected void setDimmension(final Dimension pDimmension)
	{
		aDimension = pDimmension;
	}
}
