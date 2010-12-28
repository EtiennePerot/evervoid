package client.graphics;

import java.io.File;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;

public class GraphicManager
{
	private static AssetManager gAssets;
	private static GraphicManager gManager = new GraphicManager();

	public static GraphicManager get()
	{
		return GraphicManager.gManager;
	}

	public static void setAssetManager(final AssetManager pManager)
	{
		GraphicManager.gAssets = pManager;
		GraphicManager.gAssets.registerLocator(new File(".").getAbsolutePath()
				+ File.separator + "res", FileLocator.class);
	}
}
