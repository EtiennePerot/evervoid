package com.evervoid.gamedata;

public class SpriteInfo
{
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
}
