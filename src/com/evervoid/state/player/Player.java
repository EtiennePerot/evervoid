package com.evervoid.state.player;

public class Player
{
	private static Player sNullPlayer = null;

	public static Player getNullPlayer()
	{
		if (sNullPlayer == null) {
			sNullPlayer = new Player("Neutral");
			sNullPlayer.setColor(new PlayerColor(0, 0, 0, 0));
		}
		return sNullPlayer;
	}

	private PlayerColor aColor;
	private final String aName;

	public Player(final String name)
	{
		aName = name;
		aColor = PlayerColor.random(); // FIXME: Let the player choose his color
	}

	public PlayerColor getColor()
	{
		return aColor;
	}

	public void setColor(final PlayerColor color)
	{
		aColor = color;
	}
}
