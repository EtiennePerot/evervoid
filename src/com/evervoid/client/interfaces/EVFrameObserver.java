package com.evervoid.client.interfaces;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.graphics.FrameUpdate;

/**
 * Classes implementing the EVFrameObserver interface can receive a frame() call on every frame. They must register to the
 * {@link EVFrameManager} in order to receive these updates.
 */
public interface EVFrameObserver
{
	/**
	 * Called on every frame with {@link EVFrameManager}.
	 * 
	 * @param f
	 *            The {@link FrameUpdate} containing information about the current frame
	 */
	public void frame(final FrameUpdate f);
}
