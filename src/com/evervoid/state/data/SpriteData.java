package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class SpriteData implements Jsonable
{
	private static final float sDefaultSpriteScale = 2;
	public final float scale;
	public final String sprite;
	public final int x;
	public final int y;

	public SpriteData(final Json j)
	{
		this(j.getStringAttribute("sprite"), j.getIntAttribute("x"), j.getIntAttribute("y"));
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
		this.scale = scale;
	}

	public SpriteData add(final int x, final int y)
	{
		return new SpriteData(sprite, this.x + x, this.y + y);
	}

	@Override
	public Json toJson()
	{
		if (x == 0 && y == 0) {
			return new Json(sprite);
		}
		return new Json().setIntAttribute("x", x).setIntAttribute("y", y).setStringAttribute("sprite", sprite);
	}

	@Override
	public String toString()
	{
		return "SpriteInfo(Image: " + sprite + "; x: " + x + "; y: " + y + ")";
	}
}
