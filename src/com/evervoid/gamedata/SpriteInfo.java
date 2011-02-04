package com.evervoid.gamedata;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class SpriteInfo implements Jsonable
{
	public static SpriteInfo fromJson(final Json j)
	{
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

	@Override
	public Json toJson()
	{
		return new Json().setIntAttribute("x", x).setIntAttribute("y", y).setStringAttribute("sprite", sprite);
	}
}
