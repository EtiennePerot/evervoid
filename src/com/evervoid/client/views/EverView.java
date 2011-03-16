package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.EverVoidClient.NodeType;
import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.interfaces.EVInputListener;
import com.jme3.math.Vector2f;

public abstract class EverView extends EverNode implements EVInputListener
{
	private Bounds aBounds;

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

	public void onDefocus()
	{
	}

	public void onFocus()
	{
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onLeftRelease(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
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

	@Override
	public boolean onRightClick(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onRightRelease(final Vector2f position, final float tpf)
	{
		return false;
	}

	public void setBounds(final Bounds pBounds)
	{
		aBounds = pBounds.clone();
	}
}
