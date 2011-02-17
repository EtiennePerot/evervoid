package com.evervoid.client.interfaces;

import com.evervoid.client.KeyboardKey;
import com.jme3.math.Vector2f;

public interface EVInputListener
{
	public boolean onKeyPress(final KeyboardKey key, final float tpf);

	public boolean onKeyRelease(final KeyboardKey key, final float tpf);

	public boolean onMouseClick(final Vector2f position, final float tpf);

	public boolean onMouseMove(final Vector2f position, final float tpf);

	public boolean onMouseRelease(final Vector2f position, final float tpf);

	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position);

	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position);
}
