package client.graphics;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.texture.Texture2D;

public class GraphicManager
{
	private static AssetManager gAssets = null;
	private static Map<String, BaseTexture> sTextures = new HashMap<String, BaseTexture>();

	public static AssetManager getAssetManager()
	{
		return GraphicManager.gAssets;
	}

	public static void setAssetManager(final AssetManager pManager)
	{
		GraphicManager.gAssets = pManager;
		GraphicManager.gAssets.registerLocator(new File(".").getAbsolutePath() + File.separator + "res",
				FileLocator.class);
	}

	public BaseTexture getTexture(final String name)
	{
		if (!GraphicManager.sTextures.containsKey(name))
		{
			final BaseTexture texture = new BaseTexture((Texture2D) GraphicManager.gAssets.loadTexture(name));
			texture.setSpriteFilters();
			GraphicManager.sTextures.put(name, texture);
			return texture;
		}
		return GraphicManager.sTextures.get(name);
	}
}
