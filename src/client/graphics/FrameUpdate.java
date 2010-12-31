package client.graphics;

import client.everVoidClient;

import com.jme3.math.Vector2f;

public class FrameUpdate
{
	private final float aTpf;

	public FrameUpdate(final float tpf)
	{
		aTpf = tpf;
	}

	public Vector2f getMousePosition()
	{
		return everVoidClient.sCursorPosition;
	}

	public float getTPF()
	{
		return aTpf;
	}
}
