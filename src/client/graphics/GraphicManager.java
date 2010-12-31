package client.graphics;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;

public class GraphicManager
{
	private static AssetManager gAssets = null;
	private static Map<ColorRGBA, Material> gPlainMaterials = new HashMap<ColorRGBA, Material>();

	public static Material getMaterial(final String name)
	{
		return new Material(GraphicManager.gAssets, "mat" + File.separator + name + ".j3md");
	}

	public static Material getPlainMaterial(final ColorRGBA color)
	{
		if (!GraphicManager.gPlainMaterials.containsKey(color))
		{
			final Material m = GraphicManager.getMaterial("PlainColor");
			m.setColor("m_Color", color);
			m.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			GraphicManager.gPlainMaterials.put(color, m);
		}
		return GraphicManager.gPlainMaterials.get(color);
	}

	public static void setAssetManager(final AssetManager pManager)
	{
		GraphicManager.gAssets = pManager;
		GraphicManager.gAssets.registerLocator(new File(".").getAbsolutePath() + File.separator + "res",
				FileLocator.class);
	}
}
