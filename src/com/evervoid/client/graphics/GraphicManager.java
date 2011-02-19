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
import com.jme3.font.BitmapFont;
import com.jme3.texture.Texture2D;

public class GraphicManager
{
	private static AssetManager gAssets = null;
	private static Map<String, BitmapFont> sFonts = new HashMap<String, BitmapFont>();
	private static Map<String, Dimension> sTextureDimensions = new HashMap<String, Dimension>();
	private static Map<String, BaseTexture> sTextures = new HashMap<String, BaseTexture>();

	public static AssetManager getAssetManager()
	{
		return GraphicManager.gAssets;
	}

	public static BitmapFont getFont(String font)
	{
		font = "Interface/Fonts/Default.fnt"; // FIXME: Hack; we need custom fonts
		if (!sFonts.containsKey(font)) {
			sFonts.put(font, gAssets.loadFont(font));
		}
		return sFonts.get(font);
	}

	public static BaseTexture getTexture(final String name) throws TextureException
	{
		if (!sTextures.containsKey(name)) {
			final BaseTexture texture = new BaseTexture((Texture2D) gAssets.loadTexture("gfx/" + name));
			texture.setSpriteFilters();
			sTextures.put(name, texture);
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
		gAssets = pManager;
		gAssets.registerLocator(new File(".").getAbsolutePath() + File.separator + "res", FileLocator.class);
		final Json textureInfo = Json.fromFile("res/gfx/textures.json");
		for (final String texture : textureInfo.getAttributes()) {
			final int width = textureInfo.getAttribute(texture).getListItem(0).getInt();
			final int height = textureInfo.getAttribute(texture).getListItem(1).getInt();
			sTextureDimensions.put(texture, new Dimension(width, height));
		}
	}
}
