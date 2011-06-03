package com.evervoid.state.data;

import com.evervoid.client.graphics.Sprite;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

/**
 * SpriteData is an immutable representation of the data necessary to serialize a {@link Sprite}. The image may have an offset
 * (represented by the x and y coordinates), and may be stretched (represented by the scale).
 */
public class SpriteData implements Jsonable
{
	/**
	 * The default scale by which Stars are stretched. This can overwritten on a Star by calling the scaled constructor or using
	 * getRescaled().
	 */
	public static final float sDefaultSpriteScale = 2;
	/**
	 * The Scale by which the sprite is stretched.
	 */
	public final float scale;
	/**
	 * The sprite's url.
	 */
	public final String sprite;
	/**
	 * TODO
	 */
	public final int x;
	/**
	 * TODO
	 */
	public final int y;

	/**
	 * Json constructor of SpriteData; can either be just a string ("icons/stuff.png") or an object {sprite: "icons/stuff.png",
	 * x: 10, y: 12, scale: 1.5}
	 * 
	 * @param j
	 *            The Json representation
	 */
	public SpriteData(final Json j)
	{
		if (j.isString()) {
			sprite = j.getString();
			x = 0;
			y = 0;
			scale = sDefaultSpriteScale;
		}
		else {
			sprite = j.getStringAttribute("sprite");
			x = j.getIntAttribute("x");
			y = j.getIntAttribute("y");
			scale = j.getFloatAttribute("scale");
		}
	}

	/**
	 * Constructor using origin as position.
	 * 
	 * @param sprite
	 *            Sprite to use.
	 */
	public SpriteData(final String sprite)
	{
		this(sprite, 0, 0);
	}

	/**
	 * Constructor using the default scale factor.
	 * 
	 * @param sprite
	 *            The sprite to use.
	 * @param x
	 *            The x of the point of origin.
	 * @param y
	 *            The y of the point of orign.
	 */
	public SpriteData(final String sprite, final int x, final int y)
	{
		this(sprite, x, y, sDefaultSpriteScale);
	}

	/**
	 * Constructor using specified position.
	 * 
	 * @param sprite
	 *            Sprite to use.
	 * @param x
	 *            Horizontal coordinate
	 * @param y
	 *            Vertical coordinate.
	 * @param scale
	 *            The scale of the sprite
	 */
	public SpriteData(final String sprite, final int x, final int y, final float scale)
	{
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		this.scale = Math.max(0, scale);
	}

	/**
	 * @return A new Sprite with the same image as the existing sprite, but translated by the x and y values.
	 */
	public SpriteData add(final int x, final int y)
	{
		return new SpriteData(sprite, this.x + x, this.y + y);
	}

	/**
	 * @return A new sprite located at the same place as the previous, and with the same image, but scaled by the scale factor.
	 */
	public SpriteData getRescaled(final float scaleFactor)
	{
		return new SpriteData(sprite, x, y, scale * scaleFactor);
	}

	@Override
	public Json toJson()
	{
		if (x == 0 && y == 0 && scale == sDefaultSpriteScale) {
			return new Json(sprite);
		}
		return new Json().setAttribute("x", x).setAttribute("y", y).setAttribute("sprite", sprite).setAttribute("scale", scale);
	}

	@Override
	public String toString()
	{
		return "SpriteInfo(Image: " + sprite + "; x: " + x + "; y: " + y + "; scale: " + scale + ")";
	}
}
