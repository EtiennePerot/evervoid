package client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.graphics.FrameUpdate;
import client.graphics.GraphicManager;
import client.views.solar.SolarSystemView;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

public class everVoidClient extends SimpleApplication implements ActionListener, AnalogListener
{
	private static everVoidClient sClient;
	public static Vector2f sCursorPosition = new Vector2f();
	private static ClientInput sInputManager = new ClientInput();
	public static int sScreenHeight = 0;
	public static int sScreenWidth = 0;

	public static void addNode(final Spatial node)
	{
		everVoidClient.sClient.guiNode.attachChild(node);
	}

	public static void main(final String[] args)
	{
		everVoidClient.sClient = new everVoidClient();
		everVoidClient.sClient.setShowSettings(false);
		final AppSettings options = new AppSettings(true);
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		options.setResolution((int) (screenSize.width * .8), (int) (screenSize.height * .8));
		options.setFullscreen(false);
		options.setSamples(4);
		options.setVSync(true);
		everVoidClient.sClient.setSettings(options);
		everVoidClient.sClient.start();
	}

	private ClientView aGameView;

	public everVoidClient()
	{
		super();
		everVoidClient.sClient = this;
	}

	@Override
	public void onAction(final String name, final boolean isPressed, final float tpf)
	{
		everVoidClient.sInputManager.onAction(aGameView, name, isPressed, tpf, everVoidClient.sCursorPosition);
	}

	@Override
	public void onAnalog(final String name, final float isPressed, final float tpf)
	{
		everVoidClient.sCursorPosition = inputManager.getCursorPosition();
		everVoidClient.sInputManager.onAnalog(aGameView, name, isPressed, tpf, everVoidClient.sCursorPosition);
	}

	/**
	 * Temporary; delete once engine is done
	 */
	void sampleGame()
	{
		inputManager.addMapping("Mouse move", new MouseAxisTrigger(MouseInput.AXIS_X, false), new MouseAxisTrigger(
				MouseInput.AXIS_X, true), new MouseAxisTrigger(MouseInput.AXIS_Y, false), new MouseAxisTrigger(
				MouseInput.AXIS_Y, true));
		inputManager.addListener(this, "Mouse move");
		inputManager.addMapping("Mouse click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, "Mouse click");
		aGameView = new SolarSystemView();
		everVoidClient.addNode(aGameView);
		((SolarSystemView) aGameView).sampleGame();
	}

	@Override
	public void simpleInitApp()
	{
		Logger.getLogger("").setLevel(Level.SEVERE);
		flyCam.setEnabled(false);
		GraphicManager.setAssetManager(assetManager);
		everVoidClient.sScreenHeight = cam.getHeight();
		everVoidClient.sScreenWidth = cam.getWidth();
		guiNode.detachAllChildren();
		sampleGame();
	}

	@Override
	public void simpleUpdate(final float tpf)
	{
		aGameView.recurse(new FrameUpdate(tpf));
	}
}
