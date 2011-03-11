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

public abstract class EverJMEApp extends Application
{
	private final EverNode aGuiNode = new EverNode();
	private ViewPort aGuiViewPort;
	protected Node rootNode = new Node("Root Node");
	protected boolean showSettings = true;

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
		return rootNode;
	}

	public ViewPort getRootViewPort()
	{
		return viewPort;
	}

	@Override
	public void initialize()
	{
		super.initialize();
		setPauseOnLostFocus(false);
		rootNode.setCullHint(CullHint.Never);
		viewPort.attachScene(rootNode);
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
		return showSettings;
	}

	/**
	 * Toggles settings window to display at start-up
	 * 
	 * @param showSettings
	 *            Sets true/false
	 */
	public void setShowSettings(final boolean showSettings)
	{
		this.showSettings = showSettings;
	}

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
		if (showSettings) {
			if (!JmeSystem.showSettingsDialog(settings, loadSettings)) {
				return;
			}
		}
		// re-setting settings they can have been merged from the registry.
		setSettings(settings);
		super.start();
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
		// simple update and root node
		simpleUpdate(tpf);
		rootNode.updateLogicalState(tpf);
		aGuiNode.updateLogicalState(tpf);
		rootNode.updateGeometricState();
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
