package com.evervoid.gamedata;

public class OffsetSprite
{
	public final String sprite;
	public final int x;
	public final int y;

	public OffsetSprite(final String sprite)
	{
		this(sprite, 0, 0);
	}

	public OffsetSprite(final String sprite, final int x, final int y)
	{
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
}
