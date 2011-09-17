package com.evervoid.client;

import com.evervoid.client.graphics.EverNode;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;

public abstract class EVjMonkeyApp extends Application
{
	/**
	 * The root two dimensional Node onto which every other node is attached is attached.
	 */
	private final EverNode aGuiNode = new EverNode();
	private ViewPort aGuiViewPort;
	/**
	 * The root three dimensional node onto which 3D nodes must be attached.
	 */
	protected Node aRootNode = new Node("Root Node");
	/**
	 * Whether settings should be shown on startup.
	 */
	protected boolean aShowSettings = false;

	/**
	 * Retrieves guiNode
	 * 
	 * @return guiNode Node object
	 */
	public Node getGuiNode()
	{
		return aGuiNode;
	}

	@Override
	public ViewPort getGuiViewPort()
	{
		return aGuiViewPort;
	}

	/**
	 * Retrieves rootNode
	 * 
	 * @return rootNode Node object
	 */
	public Node getRootNode()
	{
		return aRootNode;
	}

	/**
	 * @return The root view port
	 */
	public ViewPort getRootViewPort()
	{
		return viewPort;
	}

	@Override
	public void handleError(final String errMsg, final Throwable t)
	{
		if (errMsg.equals("Failed to create display")) {
			EverVoidClient.handleFailedDisplay(t);
		}
		else {
			super.handleError(errMsg, t);
		}
	}

	@Override
	public void initialize()
	{
		super.initialize();
		setPauseOnLostFocus(false);
		aRootNode.setCullHint(CullHint.Never);
		viewPort.attachScene(aRootNode);
		final int width = cam.getWidth();
		final int height = cam.getHeight();
		final Camera guiCamera = new Camera(width, height);
		aGuiViewPort = renderManager.createMainView("everGUI", guiCamera);
		aGuiViewPort.setClearEnabled(false);
		guiCamera.setParallelProjection(true);
		guiCamera.setFrustum(-Float.MAX_VALUE, Float.MAX_VALUE, -width / 2, width / 2, height / 2, -height / 2);
		guiCamera.setLocation(new Vector3f(width / 2, height / 2, 0));
		aGuiNode.setQueueBucket(Bucket.Gui);
		aGuiNode.setCullHint(CullHint.Never);
		aGuiViewPort.attachScene(aGuiNode);
		simpleInitApp();
	}

	public boolean isShowSettings()
	{
		return aShowSettings;
	}

	/**
	 * Toggles settings window to display at start-up
	 * 
	 * @param showSettings
	 *            Sets true/false
	 */
	public void setShowSettings(final boolean showSettings)
	{
		aShowSettings = showSettings;
	}

	/**
	 * Initializes the {@link Application}.
	 */
	public abstract void simpleInitApp();

	public void simpleRender(final RenderManager rm)
	{
	}

	public void simpleUpdate(final float tpf)
	{
	}

	@Override
	public void start()
	{
		// set some default settings in-case
		// settings dialog is not shown
		boolean loadSettings = false;
		if (settings == null) {
			setSettings(new AppSettings(true));
			loadSettings = true;
		}
		// show settings dialog
		if (aShowSettings) {
			if (!JmeSystem.showSettingsDialog(settings, loadSettings)) {
				return;
			}
		}
		// re-setting settings they can have been merged from the registry.
		setSettings(settings);
		// This just allows us to use a EVLwgjlDisplay
		if (context != null && context.isCreated()) {
			return;
		}
		if (settings == null) {
			settings = new AppSettings(true);
		}
		context = new EVLwjglDisplay();
		context.setSettings(settings);
		context.setSystemListener(this);
		((EVLwjglDisplay) context).create(false);
	}

	@Override
	public void update()
	{
		super.update(); // makes sure to execute AppTasks
		if (speed == 0 || paused) {
			return;
		}
		final float tpf = timer.getTimePerFrame() * speed;
		// update states
		stateManager.update(tpf);
		// TransformManager MUST tick before updateLogicalState / updateGeometricState
		simpleUpdate(tpf);
		aRootNode.updateLogicalState(tpf);
		aGuiNode.updateLogicalState(tpf);
		aRootNode.updateGeometricState();
		aGuiNode.updateGeometricState();
		// render states
		stateManager.render(renderManager);
		try {
			renderManager.render(tpf);
		}
		catch (final IllegalStateException e) {
			// Caught when video card can't keep up with bloom
		}
		simpleRender(renderManager);
		stateManager.postRender();
	}
}
