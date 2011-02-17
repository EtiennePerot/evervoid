package com.evervoid.client;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.system.JmeSystem;
import com.jme3.util.BufferUtils;

public abstract class EverJMEApp extends Application
{
	private class AppActionListener implements ActionListener
	{
		@Override
		public void onAction(final String name, final boolean value, final float tpf)
		{
			if (!value) {
				return;
			}
			if (name.equals("SIMPLEAPP_Exit")) {
				stop();
			}
			else if (name.equals("SIMPLEAPP_CameraPos")) {
				if (cam != null) {
					final Vector3f loc = cam.getLocation();
					final Quaternion rot = cam.getRotation();
					System.out.println("Camera Position: (" + loc.x + ", " + loc.y + ", " + loc.z + ")");
					System.out.println("Camera Rotation: " + rot);
					System.out.println("Camera Direction: " + cam.getDirection());
				}
			}
			else if (name.equals("SIMPLEAPP_Memory")) {
				BufferUtils.printCurrentDirectMemory(null);
			}
		}
	}

	private final AppActionListener actionListener = new AppActionListener();
	protected BitmapFont guiFont;
	protected Node guiNode = new Node("Gui Node");
	protected Node rootNode = new Node("Root Node");
	protected boolean showSettings = true;

	/**
	 * Retrieves guiNode
	 * 
	 * @return guiNode Node object
	 */
	public Node getGuiNode()
	{
		return guiNode;
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

	@Override
	public void initialize()
	{
		super.initialize();
		guiNode.setQueueBucket(Bucket.Gui);
		guiNode.setCullHint(CullHint.Never);
		rootNode.setCullHint(CullHint.Never);
		viewPort.attachScene(rootNode);
		guiViewPort.attachScene(guiNode);
		if (inputManager != null) {
			if (context.getType() == Type.Display) {
				inputManager.addMapping("SIMPLEAPP_Exit", new KeyTrigger(KeyInput.KEY_ESCAPE));
			}
			inputManager.addMapping("SIMPLEAPP_CameraPos", new KeyTrigger(KeyInput.KEY_C));
			inputManager.addMapping("SIMPLEAPP_Memory", new KeyTrigger(KeyInput.KEY_M));
			inputManager.addListener(actionListener, "SIMPLEAPP_Exit", "SIMPLEAPP_CameraPos", "SIMPLEAPP_Memory");
		}
		// call user code
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
		guiNode.updateLogicalState(tpf);
		rootNode.updateGeometricState();
		guiNode.updateGeometricState();
		// render states
		stateManager.render(renderManager);
		renderManager.render(tpf);
		simpleRender(renderManager);
		stateManager.postRender();
	}
}
