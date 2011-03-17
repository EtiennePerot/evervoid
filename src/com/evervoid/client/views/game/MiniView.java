package com.evervoid.client.views.game;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.jme3.math.Vector2f;

/**
 * Represents a mini view in the bottom left corner. As it is non-interactive, it discards all input events except left click,
 * and prevents subclasses from overriding event handlers.
 */
public abstract class MiniView extends EverView
{
	private final GameView aGameView;

	public MiniView(final GameView gameview)
	{
		aGameView = gameview;
	}

	@Override
	public final boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		return false;
	}

	@Override
	public final boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		return false;
	}

	@Override
	public final boolean onLeftClick(final Vector2f position, final float tpf)
	{
		final Bounds b = getBounds();
		if (b.x <= position.x && b.y <= position.y && position.x < b.x + b.width && position.y < b.y + b.height) {
			aGameView.backPerspective();
			return true;
		}
		return false;
	}

	@Override
	public final boolean onLeftRelease(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public final boolean onMouseMove(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public final boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		return false;
	}

	@Override
	public final boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		return false;
	}

	@Override
	public final boolean onRightClick(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public final boolean onRightRelease(final Vector2f position, final float tpf)
	{
		return false;
	}
}
