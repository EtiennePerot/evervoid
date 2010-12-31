package client;

import client.graphics.GraphicManager;
import client.views.solar.SolarSystemView;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;

public class everVoidClient extends SimpleApplication
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
		new everVoidClient().start();
	}

	private ClientView aGameView;

	public everVoidClient()
	{
		super();
		everVoidClient.sClient = this;
	}

	void sampleGame()
	{
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
		everVoidClient.sCursorPosition = inputManager.getCursorPosition();
		aGameView.recurse(tpf);
	}
}
