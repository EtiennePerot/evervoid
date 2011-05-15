package com.evervoid.client.graphics;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.jme3.math.Vector2f;

/**
 * Data structure sent by {@link EVFrameManager} to all {@link EVFrameObserver} on every frame.
 */
public class FrameUpdate
{
	/**
	 * Time-per-frame: Time (in seconds) since last FrameUpdate.
	 */
	public final float aTpf;

	/**
	 * Constructor; initializes time-per-frame
	 * 
	 * @param tpf
	 *            Time-per-frame: Time (in seconds) since last FrameUpdate.
	 */
	public FrameUpdate(final float tpf)
	{
		aTpf = tpf;
	}

	/**
	 * Convenience method to get the mouse position at the moment of the FrameUpdate
	 * 
	 * @return Position of the mouse, in screen coordinates
	 */
	public Vector2f getMousePosition()
	{
		return EverVoidClient.sCursorPosition;
	}
}
