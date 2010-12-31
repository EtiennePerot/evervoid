package client;

import java.awt.Dimension;
import java.awt.Toolkit;

import client.graphics.FrameUpdate;
import client.graphics.GraphicManager;
import client.views.solar.SolarSystemView;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

public class everVoidClient extends SimpleApplication implements AnalogListener
{
	private static everVoidClient sClient;
	public static Vector2f sCursorPosition = new Vector2f();
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
	public void onAnalog(final String name, final float isPressed, final float tpf)
	{
		// Forward mouse movements to game view
		everVoidClient.sCursorPosition = inputManager.getCursorPosition();
		aGameView.onMouseMove(name, isPressed, tpf, everVoidClient.sCursorPosition);
	}

	void sampleGame()
	{
		inputManager.addMapping("Mouse move", new MouseAxisTrigger(MouseInput.AXIS_X, false), new MouseAxisTrigger(
				MouseInput.AXIS_X, true), new MouseAxisTrigger(MouseInput.AXIS_Y, false), new MouseAxisTrigger(
				MouseInput.AXIS_Y, true));
		inputManager.addListener(this, "Mouse move");
		aGameView = new SolarSystemView();
		everVoidClient.addNode(aGameView);
	}

	@Override
	public void simpleInitApp()
	{
		flyCam.setEnabled(false);
		GraphicManager.setAssetManager(assetManager);
		everVoidClient.sScreenHeight = cam.getHeight();
		everVoidClient.sScreenWidth = cam.getWidth();
		sampleGame();
	}

	@Override
	public void simpleUpdate(final float tpf)
	{
		aGameView.recurse(new FrameUpdate(tpf));
	}
}
