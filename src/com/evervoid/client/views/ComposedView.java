package com.evervoid.client.views;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.KeyboardKey;
import com.jme3.math.Vector2f;

public abstract class ComposedView extends EverView
{
	/**
	 * List of sub-views of this ComposedView. Ordered in the order that events are propagated.
	 */
	List<EverView> aViews = new ArrayList<EverView>();

	protected ComposedView()
	{
	}

	protected ComposedView(final Bounds pBounds)
	{
		super(pBounds);
	}

	protected void addView(final EverView view)
	{
		aViews.add(view);
		addNode(view);
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		for (final EverView c : aViews) {
			if (c != null && c.onKeyPress(key, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		for (final EverView c : aViews) {
			if (c != null && c.onKeyRelease(key, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		for (final EverView c : aViews) {
			if (c != null && c.onLeftClick(position, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onLeftRelease(final Vector2f position, final float tpf)
	{
		for (final EverView c : aViews) {
			if (c != null && c.onLeftRelease(position, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		for (final EverView c : aViews) {
			if (c != null && c.onMouseMove(position, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		for (final EverView c : aViews) {
			if (c != null && c.onMouseWheelDown(delta, tpf, position)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		for (final EverView c : aViews) {
			if (c != null && c.onMouseWheelUp(delta, tpf, position)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onRightClick(final Vector2f position, final float tpf)
	{
		for (final EverView c : aViews) {
			if (c != null && c.onRightClick(position, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onRightRelease(final Vector2f position, final float tpf)
	{
		for (final EverView c : aViews) {
			if (c != null && c.onRightRelease(position, tpf)) {
				return true;
			}
		}
		return false;
	}

	protected void removeView(final EverView view)
	{
		if (view != null && aViews.remove(view)) {
			view.removeFromParent();
		}
	}
}
