package com.evervoid.client.graphics;

import com.evervoid.client.EverVoidClient;
import com.jme3.math.Vector2f;

public class FrameUpdate
{
	public final float aTpf;

	public FrameUpdate(final float tpf)
	{
		aTpf = tpf;
	}

	public Vector2f getMousePosition()
	{
		return EverVoidClient.sCursorPosition;
	}
}
