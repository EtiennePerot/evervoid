package com.evervoid.client.views;

import com.evervoid.client.InputListener;
import com.jme3.math.Vector2f;

public abstract class Perspective implements InputListener
{
	private EverView aContentNode = null;
	private EverView aPanelNode = null;

	public EverView getContentView()
	{
		return aContentNode;
	}

	public EverView getPanelView()
	{
		return aPanelNode;
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		if (aContentNode != null && aContentNode.onMouseClick(position, tpf)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onMouseClick(position, tpf)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseMove(final float tpf, final Vector2f position)
	{
		if (aContentNode != null && aContentNode.onMouseMove(tpf, position)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onMouseMove(tpf, position)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseRelease(final Vector2f position, final float tpf)
	{
		if (aContentNode != null && aContentNode.onMouseRelease(position, tpf)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onMouseRelease(position, tpf)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		if (aContentNode != null && aContentNode.onMouseWheelDown(delta, tpf, position)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onMouseWheelDown(delta, tpf, position)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		if (aContentNode != null && aContentNode.onMouseWheelUp(delta, tpf, position)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onMouseWheelUp(delta, tpf, position)) {
			return true;
		}
		return false;
	}

	protected void setComponents(final EverView content, final EverView panel)
	{
		aContentNode = content;
		aPanelNode = panel;
	}

	protected void setContent(final EverView content)
	{
		aContentNode = content;
	}

	protected void setPanel(final EverView panel)
	{
		aPanelNode = panel;
	}
}
