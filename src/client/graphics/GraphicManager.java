package client.graphics;

import java.io.File;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;

public class GraphicManager
{
	private static AssetManager gAssets = null;

	public static Material getMaterial(final String name)
	{
		return new Material(GraphicManager.gAssets, "mat" + File.separator + name + ".j3md");
	}

	public static void setAssetManager(final AssetManager pManager)
	{
		GraphicManager.gAssets = pManager;
		GraphicManager.gAssets.registerLocator(new File(".").getAbsolutePath() + File.separator + "res",
				FileLocator.class);
	}
}
