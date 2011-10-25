package com.evervoid.client.graphics;

import static com.evervoid.utils.ResourceUtils.getResourceDir;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.materials.BaseTexture;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.json.Json;
import com.evervoid.state.geometry.Dimension;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.texture.Texture2D;

/**
 * Graphic and general asset loader. You usually shouldn't have to use this directly, because graphic classes load textures for
 * you.
 */
public class GraphicManager
{
	/**
	 * Reference to the {@link AssetManager} to load from.
	 */
	private static AssetManager gAssets = null;
	/**
	 * Cache of loaded fonts.
	 */
	private static Map<String, BitmapFont> sFonts = new HashMap<String, BitmapFont>();
	/**
	 * Cache of texture sizes.
	 */
	private static Map<String, Dimension> sTextureDimensions = new HashMap<String, Dimension>();
	/**
	 * Cache of loaded textures.
	 */
	private static Map<String, BaseTexture> sTextures = new HashMap<String, BaseTexture>();

	/**
	 * @return A reference to the {@link AssetManager} from which assets are loaded.
	 */
	public static AssetManager getAssetManager()
	{
		return GraphicManager.gAssets;
	}

	/**
	 * Loads a font (if not already loaded) and returns it.
	 * 
	 * @param font
	 *            The font to load; file to load is expected to be in gfx/fonts/{font}_{size}.fnt
	 * @param size
	 *            The size variant of the font to load. Affects file path.
	 * @return The loaded {@link BitmapFont}.
	 */
	public static BitmapFont getFont(final String font, final int size)
	{
		// For reference, fonts have to be 256x256 bitmaps with 32-bit color depth and PNG texture compression
		// Can generate such textures with http://www.angelcode.com/products/bmfont/
		// Config file needs to be modified; see http://jmonkeyengine.org/groups/gui/forum/topic/nifty-custom-font/
		// To select a TTF font, go to Options -> Font settings -> Face
		if (!sFonts.containsKey(font + "_" + size)) {
			sFonts.put(font + "_" + size, gAssets.loadFont("gfx/fonts/" + font + "_" + size + ".fnt"));
		}
		return sFonts.get(font + "_" + size);
	}

	/**
	 * Convenience function to get the path of a sprite from its filename
	 * 
	 * @param sprite
	 *            The sprite to get the path of
	 * @return The final path to the sprite
	 */
	public static String getSpritePath(final String sprite)
	{
		return getResourceDir() + "/gfx/" + sprite;
	}

	/**
	 * Loads a texture (if not already loaded) and returns it.
	 * 
	 * @param name
	 *            The filename of the texture to load
	 * @return The loaded {@link BaseTexture}
	 * @throws TextureException
	 *             If the texture couldn't be loaded (file not found, etc.)
	 */
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

	/**
	 * Called by {@link EverVoidClient} during initialization; grabs the {@link AssetManager} and populates its locators.
	 * 
	 * @param pManager
	 *            The {@link AssetManager} to grab.
	 */
	public static void setAssetManager(final AssetManager pManager)
	{
		gAssets = pManager;
		final Json textureInfo = Json.fromFile("gfx/textures.json");
		for (final String texture : textureInfo.getAttributes()) {
			final int width = textureInfo.getAttribute(texture).getListItem(0).getInt();
			final int height = textureInfo.getAttribute(texture).getListItem(1).getInt();
			sTextureDimensions.put(texture, new Dimension(width, height));
		}
	}
}
