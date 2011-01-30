package com.evervoid.client;

import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.views.GameView;
import com.evervoid.network.connection.ServerConnection;
import com.evervoid.network.server.EverVoidServer;
import com.evervoid.state.Dimension;
import com.evervoid.state.EverVoidGameState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 * everVoid game client providing the user with a user interface to play the game.
 */
public class EverVoidClient extends SimpleApplication implements ActionListener, AnalogListener
{
	public enum NodeType
	{
		THREEDIMENSION, TWODIMENSION;
		public Node getNode(final EverVoidClient client)
		{
			switch (this) {
				case TWODIMENSION:
					return client.guiNode;
				case THREEDIMENSION:
					return client.rootNode;
			}
			return null;
		}
	}

	/**
	 * Instance of the everVoidClient
	 */
	private static EverVoidClient sClient;
	public static Vector2f sCursorPosition = new Vector2f();
	protected static EverVoidGameState sGameState;
	private static final EVInputManager sInputManager = new EVInputManager();
	private static int sScreenHeight = 0;
	private static int sScreenWidth = 0;

	/**
	 * Add a root node to the main window
	 * 
	 * @param type
	 *            Type of node to add
	 * @param node
	 *            The node to add
	 */
	public static void addRootNode(final NodeType type, final EverNode node)
	{
		type.getNode(sClient).attachChild(node);
	}

	/**
	 * Delete a root node from the main window
	 * 
	 * @param node
	 *            The node to delete (type doesn't matter)
	 */
	public static void delRootNode(final EverNode node)
	{
		// Try detaching from both; no side-effects
		sClient.guiNode.detachChild(node);
		sClient.rootNode.detachChild(node);
	}

	public static float getCameraDimension()
	{
		return sClient.cam.getViewPortTop();
	}

	public static Ray getRayFromVector(final Vector2f vector)
	{
		final Vector3f worldCoordinates = sClient.cam.getWorldCoordinates(vector, 0);
		final Vector3f worldCoordinates2 = new Vector3f(worldCoordinates.x, worldCoordinates.y, 999);
		return new Ray(sClient.cam.getDirection(), worldCoordinates.subtract(worldCoordinates2));
	}

	/**
	 * 
	 */
	public static Dimension getWindowDimension()
	{
		return new Dimension(sScreenWidth, sScreenHeight);
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
		final Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		options.setResolution((int) (screenSize.width * .8), (int) (screenSize.height * .8));
		options.setFullscreen(false);
		options.setSamples(4);
		options.setVSync(true);
		options.setTitle("everVoid");
		sClient.setSettings(options);
		sClient.start();
	}

	/**
	 * Set the game state to a particular game state.
	 * 
	 * @param pState
	 *            EverVoid Game State to set the new state to.
	 */
	public static void setGameState(final EverVoidGameState pState)
	{
		sGameState = pState.clone();
	}

	private EverVoidServer aTestServer;

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
		sInputManager.onAction(name, isPressed, tpf, sCursorPosition);
	}

	@Override
	public void onAnalog(final String name, final float delta, final float tpf)
	{
		sCursorPosition = inputManager.getCursorPosition();
		sInputManager.onAnalog(name, delta, tpf, sCursorPosition);
	}

	@Override
	public void requestClose(final boolean esc)
	{
		super.requestClose(esc);
		aTestServer.stop();
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
		inputManager.addMapping("Click g", new KeyTrigger(KeyInput.KEY_G));
		inputManager.addListener(this, "Click g");
		inputManager.addMapping("Click s", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addListener(this, "Click s");
		sGameState = new EverVoidGameState();
		final GameView gameView = new GameView(sGameState);
		EVViewManager.registerView(ViewType.GAME, gameView);
		EVViewManager.switchTo(ViewType.GAME);
	}

	@Override
	public void simpleInitApp()
	{
		Logger.getLogger("").setLevel(Level.SEVERE);
		flyCam.setEnabled(false);
		GraphicManager.setAssetManager(assetManager);
		sScreenHeight = cam.getHeight();
		sScreenWidth = cam.getWidth();
		// Network connection test START
		aTestServer = new EverVoidServer();
		final ServerConnection testConnecton = new ServerConnection("localhost");
		aTestServer.start();
		testConnecton.start();
		// Network connection test END
		sampleGame();
	}

	@Override
	public void simpleUpdate(final float tpf)
	{
		EVFrameManager.tick(new FrameUpdate(tpf));
	}
}
