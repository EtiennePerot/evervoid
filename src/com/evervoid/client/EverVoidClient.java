package com.evervoid.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.views.solar.SolarSystemView;
import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

/**
 * everVoid game client providing the user with a user interface to play the
 * game.
 */
public class EverVoidClient extends SimpleApplication implements ActionListener, AnalogListener
{
	/**
	 * Instance of the everVoidClient
	 */
	private static EverVoidClient sClient;
	public static Vector2f sCursorPosition = new Vector2f();
	private static ClientInput sInputManager = new ClientInput();
	public static int sScreenHeight = 0;
	public static int sScreenWidth = 0;

	/**
	 * Attaches the passed Spatial node to the guiNode, which becomes the node's
	 * new parent.
	 * 
	 * @param node
	 *            The node to attach to guiNode
	 * @see Spatial
	 */
	public static void addRootNode(final Spatial node)
	{
		sClient.guiNode.attachChild(node);
	}

	/**
	 * everVoid Client program
	 * 
	 * @param args
	 *            Arguments passed to the program.
	 */
	public static void main(final String[] args)
	{
		sClient = new EverVoidClient();
		sClient.setShowSettings(false);
		final AppSettings options = new AppSettings(true);
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		options.setResolution((int) (screenSize.width * .8), (int) (screenSize.height * .8));
		options.setFullscreen(false);
		options.setSamples(4);
		// options.setVSync(true);
		sClient.setSettings(options);
		sClient.start();
	}

	/**
	 * Instance of a specific game view, such as solar system view, galaxy view,
	 * etc.
	 */
	private ClientView aGameView;

	/**
	 * Private constructor for the everVoidClient
	 */
	private EverVoidClient()
	{
		super();
		sClient = this;
	}

	@Override
	public void onAction(final String name, final boolean isPressed, final float tpf)
	{
		sInputManager.onAction(aGameView, name, isPressed, tpf, sCursorPosition);
	}

	@Override
	public void onAnalog(final String name, final float delta, final float tpf)
	{
		sCursorPosition = inputManager.getCursorPosition();
		sInputManager.onAnalog(aGameView, name, delta, tpf, sCursorPosition);
	}

	/**
	 * Temporary; delete once engine is done.
	 */
	void sampleGame()
	{
		inputManager.addMapping("Mouse move", new MouseAxisTrigger(MouseInput.AXIS_X, false), new MouseAxisTrigger(
				MouseInput.AXIS_X, true), new MouseAxisTrigger(MouseInput.AXIS_Y, false), new MouseAxisTrigger(
				MouseInput.AXIS_Y, true));
		inputManager.addMapping("Mouse wheel up", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping("Mouse wheel down", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addListener(this, "Mouse move");
		inputManager.addListener(this, "Mouse wheel up");
		inputManager.addListener(this, "Mouse wheel down");
		inputManager.addMapping("Mouse click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, "Mouse click");
		aGameView = new SolarSystemView();
		addRootNode(aGameView);
		((SolarSystemView) aGameView).sampleGame();
	}

	@Override
	public void simpleInitApp()
	{
		Logger.getLogger("").setLevel(Level.SEVERE);
		flyCam.setEnabled(false);
		GraphicManager.setAssetManager(assetManager);
		sScreenHeight = cam.getHeight();
		sScreenWidth = cam.getWidth();
		// guiNode.detachAllChildren();
		sampleGame();
	}

	@Override
	public void simpleUpdate(final float tpf)
	{
		FrameManager.tick(new FrameUpdate(tpf));
	}
}