package client;

import client.graphics.GraphicManager;

import com.jme3.app.SimpleApplication;

public class everVoidClient extends SimpleApplication
{
	@Override
	public void simpleInitApp()
	{
		flyCam.setEnabled(false);
		GraphicManager.setAssetManager(assetManager);
	}
}
