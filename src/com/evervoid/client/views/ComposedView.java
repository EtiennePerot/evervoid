package com.evervoid.client.views;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.state.Dimension;
import com.jme3.math.Vector2f;

public abstract class ComposedView extends EverView
{
	/**
	 * List of sub-views of this ComposedView. Ordered in the order that events are propagated.
	 */
	List<EverView> aViews = new ArrayList<EverView>();

	protected ComposedView()
	{
		super();
	}

	protected ComposedView(final Dimension dimension)
	{
		super(dimension);
	}

	protected void addView(final EverView view)
	{
		aViews.add(view);
		addNode(view);
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		for (final EverView c : aViews) {
			if (c.onMouseClick(position, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseMove(final float tpf, final Vector2f position)
	{
		for (final EverView c : aViews) {
			if (c.onMouseMove(tpf, position)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseRelease(final Vector2f position, final float tpf)
	{
		for (final EverView c : aViews) {
			if (c.onMouseRelease(position, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		for (final EverView c : aViews) {
			if (c.onMouseWheelDown(delta, tpf, position)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		for (final EverView c : aViews) {
			if (c.onMouseWheelUp(delta, tpf, position)) {
				return true;
			}
		}
		return false;
	}

	protected void removeView(final EverView view)
	{
		if (aViews.remove(view)) {
			view.removeFromParent();
		}
	}
}
