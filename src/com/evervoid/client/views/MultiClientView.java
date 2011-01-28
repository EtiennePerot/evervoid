package com.evervoid.client.views;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.ClientView;
import com.jme3.math.Vector2f;

public abstract class MultiClientView extends ClientView
{
	/**
	 * List of sub-views of this MultiClientView. Ordered in the order that events are propagated.
	 */
	List<ClientView> aViews = new ArrayList<ClientView>();

	protected void addView(final ClientView view)
	{
		aViews.add(view);
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		for (final ClientView c : aViews) {
			if (c.onMouseClick(position, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseMove(final float tpf, final Vector2f position)
	{
		for (final ClientView c : aViews) {
			if (c.onMouseMove(tpf, position)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseRelease(final Vector2f position, final float tpf)
	{
		for (final ClientView c : aViews) {
			if (c.onMouseRelease(position, tpf)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		for (final ClientView c : aViews) {
			if (c.onMouseWheelDown(delta, tpf, position)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		for (final ClientView c : aViews) {
			if (c.onMouseWheelUp(delta, tpf, position)) {
				return true;
			}
		}
		return false;
	}
}
