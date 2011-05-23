package com.evervoid.client.graphics;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.graphics.materials.AlphaTextured;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.data.SpriteData;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 * Base class from which sprites are defined.
 */
public abstract class BaseSprite extends EverNode implements Sizable, Colorable
{
	/**
	 * The material used for the sprite. Must be AlphaTextured on a subclass of AlphaTextured.
	 */
	private AlphaTextured aMaterial;
	/**
	 * Information about the sprite (image, offset, scale, etc.)
	 */
	private final SpriteData aSpriteInfo;
	/**
	 * Transform applied to the sprite to comply with SpriteData's offset, and to determine which point corresponds to the
	 * origin of the sprite. By default, (0, 0) corresponds to the middle of the sprite.
	 */
	protected Transform aSpriteTransform;
	/**
	 * Whether the sprite successfully loaded or not (might fail if the image file doesn't exist, etc)
	 */
	private boolean aValidSprite = true;

	/**
	 * Main constructor; builds a Sprite from a {@link SpriteData} object
	 * 
	 * @param sprite
	 *            The sprite information to build from
	 */
	public BaseSprite(final SpriteData sprite)
	{
		aSpriteInfo = sprite;
		try {
			aMaterial = buildMaterial(sprite);
			final Quad q = new Quad(aMaterial.getWidth(), aMaterial.getHeight());
			final Geometry g = new Geometry("Sprite-" + sprite.sprite + " @ " + hashCode(), q);
			g.setMaterial(aMaterial);
			final EverNode image = new EverNode(g);
			addNode(image);
			// Offset image so that the origin is around the center of the image
			aSpriteTransform = image.getNewTransform();
			aSpriteTransform.translate(-aMaterial.getWidth() * sprite.scale / 2 + sprite.x,
					-aMaterial.getHeight() * sprite.scale / 2 + sprite.y).commit();
			aSpriteTransform.setScale(sprite.scale).commit();
		}
		catch (final TextureException e) {
			// Do nothing; just a blank node
			aValidSprite = false;
			System.err.println("Warning: Could not load Sprite! Info = " + sprite);
		}
	}

	/**
	 * Extra offset constructor; takes a SpriteData object, adds a specified offset to it, and builds the resulting sprite
	 * 
	 * @param offSprite
	 *            The sprite information to build from
	 * @param x
	 *            X offset to add
	 * @param y
	 *            Y offset to add
	 */
	public BaseSprite(final SpriteData offSprite, final int x, final int y)
	{
		this(offSprite.add(x, y));
	}

	/**
	 * Convenience constructor; builds a sprite from an image file name
	 * 
	 * @param image
	 *            The image to load
	 */
	public BaseSprite(final String image)
	{
		this(new SpriteData(image));
	}

	/**
	 * Convenience constructor; builds a sprite from an image filename and an offset
	 * 
	 * @param sprite
	 *            The image to load
	 * @param x
	 *            X offset to add
	 * @param y
	 *            Y offset to add
	 */
	public BaseSprite(final String sprite, final int x, final int y)
	{
		this(new SpriteData(sprite, x, y));
	}

	/**
	 * Cancels the centering offset on this Sprite. The (0, 0) point will now correspond to the bottom-left corner of the
	 * sprite. This affects the sprite object itself.
	 * 
	 * @return This for chainability
	 */
	public BaseSprite bottomLeftAsOrigin()
	{
		aSpriteTransform.translate(0, 0);
		return this;
	}

	/**
	 * Abstract method to build the material to use for the sprite. Subclasses may use any custom material, as long as it
	 * extends AlphaTextured (or is AlphaTextured)
	 * 
	 * @param sprite
	 *            The sprite information being loaded
	 * @return The material object to use for it.
	 * @throws TextureException
	 *             Thrown when the sprite is invalid
	 */
	protected abstract AlphaTextured buildMaterial(SpriteData sprite) throws TextureException;

	@Override
	public Vector2f getDimensions()
	{
		return new Vector2f(getWidth(), getHeight());
	}

	@Override
	public float getHeight()
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to get height of invalid sprite: " + this);
			return 0;
		}
		return aMaterial.getHeight() * aSpriteTransform.getScaleY();
	}

	/**
	 * For colored sprites, returns the hue of the sprite
	 * 
	 * @return The hue of the sprite
	 */
	public ColorRGBA getHue()
	{
		return aMaterial.getHue();
	}

	/**
	 * @return The material used on this sprite
	 */
	public AlphaTextured getMaterial()
	{
		return aMaterial;
	}

	/**
	 * @return The offset used on the sprite object.
	 */
	public Vector2f getSpriteOffset()
	{
		return aSpriteTransform.getTranslation2f();
	}

	@Override
	public float getWidth()
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to get width of invalid sprite: " + this);
			return 0;
		}
		return aMaterial.getWidth() * aSpriteTransform.getScaleX();
	}

	/**
	 * @return Whether the BaseSprite loaded correctly or not.
	 */
	public boolean isValidSprite()
	{
		return aValidSprite;
	}

	@Override
	protected void setAlpha(final float alpha)
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to set alpha of invalid sprite: " + this);
			return;
		}
		aMaterial.setAlpha(alpha);
	}

	@Override
	public void setHue(final ColorRGBA hue)
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to set hue of invalid sprite: " + this);
			return;
		}
		aMaterial.setHue(hue);
	}

	@Override
	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to set hue of invalid sprite: " + this);
			return;
		}
		aMaterial.setHue(hue, multiplier);
	}

	@Override
	public void setHueMultiplier(final float multiplier)
	{
		if (!aValidSprite) {
			System.err.println("Warning: Trying to set hue multiplier of invalid sprite: " + this);
			return;
		}
		aMaterial.setHueMultiplier(multiplier);
	}

	@Override
	public String toString()
	{
		String s = "Sprite " + aSpriteInfo.sprite;
		if (aSpriteInfo.x != 0 || aSpriteInfo.y != 0) {
			s += " @ " + aSpriteInfo.x + "; " + aSpriteInfo.y;
		}
		if (!aValidSprite) {
			s = "INVALID " + s;
		}
		return s;
	}
}
