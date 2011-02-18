package com.evervoid.client.graphics;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.graphics.materials.BaseTexture;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.json.Json;
import com.evervoid.state.geometry.Dimension;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.texture.Texture2D;

public class GraphicManager
{
	private static AssetManager gAssets = null;
	private static Map<String, Dimension> sTextureDimensions = new HashMap<String, Dimension>();
	private static Map<String, BaseTexture> sTextures = new HashMap<String, BaseTexture>();

	public static AssetManager getAssetManager()
	{
		return GraphicManager.gAssets;
	}

	public static BaseTexture getTexture(final String name) throws TextureException
	{
		if (!GraphicManager.sTextures.containsKey(name)) {
			final BaseTexture texture = new BaseTexture((Texture2D) GraphicManager.gAssets.loadTexture("gfx/" + name));
			texture.setSpriteFilters();
			GraphicManager.sTextures.put(name, texture);
			if (sTextureDimensions.containsKey(name)) {
				final Dimension textureSize = sTextureDimensions.get(name);
				texture.setPortion(textureSize.width / texture.getWidth(), textureSize.height / texture.getHeight());
			}
			return texture;
		}
		return GraphicManager.sTextures.get(name);
	}

	public static void setAssetManager(final AssetManager pManager)
	{
		GraphicManager.gAssets = pManager;
		GraphicManager.gAssets.registerLocator(new File(".").getAbsolutePath() + File.separator + "res", FileLocator.class);
		final Json textureInfo = Json.fromFile("res/gfx/textures.json");
		for (final String texture : textureInfo.getAttributes()) {
			sTextureDimensions.put(texture, Dimension.fromJson(textureInfo.getAttribute(texture)));
		}
	}
}
