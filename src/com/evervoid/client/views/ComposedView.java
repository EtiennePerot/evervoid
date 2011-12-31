package com.evervoid.client.views;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.KeyboardKey;
import com.jme3.math.Vector2f;

/**
 * A ComposedView is an {@link EverView} that contains multiple {@link EverView}. {@link EverView}-ception.
 */
public abstract class ComposedView extends EverView
{
	/**
	 * List of sub-views of this ComposedView. Ordered in the order that events are propagated.
	 */
	List<EverView> aViews = new ArrayList<EverView>();

	/**
	 * Constructor; uses screen-wide {@link Bounds}.
	 */
	protected ComposedView()
	{
	}

	/**
	 * Constructor
	 * 
	 * @param pBounds
	 *            The {@link Bounds} to use.
	 */
	protected ComposedView(final Bounds pBounds)
	{
		super(pBounds);
	}

	/**
	 * Add an {@link EverView} to this {@link ComposedView}. The new {@link EverView} will be added on the bottom of the view
	 * stack.
	 * 
	 * @param view
	 *            The {@link EverView} to add.
	 */
	protected void addView(final EverView view)
	{
		aViews.add(view);
		addNode(view);
	}

	/**
	 * @return The list of {@link EverView}s contained in this {@link ComposedView}.
	 */
	protected List<EverView> getChildrenViews()
	{
		return aViews;
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

	/**
	 * Remote an {@link EverView} from the {@link ComposedView}.
	 * 
	 * @param view
	 *            The {@link EverView} to remove.
	 */
	protected void removeView(final EverView view)
	{
		if (view != null && aViews.remove(view)) {
			view.onDefocus();
			view.removeFromParent();
		}
	}

	@Override
	public abstract void setBounds(Bounds bounds);
}
