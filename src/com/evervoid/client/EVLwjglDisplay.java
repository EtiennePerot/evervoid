package com.evervoid.client;

import java.util.logging.Logger;

import com.evervoid.utils.CatchableThread;
import com.evervoid.utils.EVUtils;
import com.evervoid.utils.ExceptionHandler;
import com.jme3.system.lwjgl.LwjglDisplay;

/**
 * An everVoid subclass of {@link LwjglDisplay} used to override create(bool) this is done simply to allow for error catching in
 * the thread launching the JME render.
 */
public class EVLwjglDisplay extends LwjglDisplay
{
	/**
	 * Default Lwjgl logger.
	 */
	private static final Logger logger = Logger.getLogger(LwjglDisplay.class.getName());
	/**
	 * The Exception handler for this Display.
	 */
	private final ExceptionHandler aExceptionHandler;

	/**
	 * Default constructor.
	 */
	public EVLwjglDisplay()
	{
		aExceptionHandler = EVUtils.sDefaultHandler;
	}

	/**
	 * @param pWaitFor
	 *            Whether to wait for the display to create.
	 */
	@Override
	public void create(final boolean pWaitFor)
	{
		if (created.get()) {
			logger.warning("create() called when display is already created!");
			return;
		}
		final CatchableThread thread = new CatchableThread(this, "LWJGL Renderer Thread", aExceptionHandler);
		thread.start();
		if (pWaitFor) {
			waitFor(true);
		}
	}
}
