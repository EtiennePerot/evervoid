package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class SpriteInfo implements Jsonable
{
	public static SpriteInfo fromJson(final Json j)
	{
		// SpriteInfo objects can either be plain strings if offset is (0, 0)
		if (j.isString()) {
			return new SpriteInfo(j.getString());
		}
		// Or they can be full objects otherwise
		return new SpriteInfo(j.getStringAttribute("sprite"), j.getIntAttribute("x"), j.getIntAttribute("y"));
	}

	public final String sprite;
	public final int x;
	public final int y;

	/**
	 * Constructor using origin as position.
	 * 
	 * @param sprite
	 *            Sprite to use.
	 */
	public SpriteInfo(final String sprite)
	{
		this(sprite, 0, 0);
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
	 */
	public SpriteInfo(final String sprite, final int x, final int y)
	{
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}

	public SpriteInfo add(final int x, final int y)
	{
		return new SpriteInfo(sprite, this.x + x, this.y + y);
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
