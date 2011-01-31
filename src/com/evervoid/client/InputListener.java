package com.evervoid.client;

import com.jme3.math.Vector2f;

public interface InputListener
{
	public boolean onMouseClick(final Vector2f position, final float tpf);

	public boolean onMouseMove(final float tpf, final Vector2f position);

	public boolean onMouseRelease(final Vector2f position, final float tpf);

	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position);

	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position);
}