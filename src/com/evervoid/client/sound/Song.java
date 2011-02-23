package com.evervoid.client.sound;

public class Song
{
	private final int aSongLength;
	private final String aSongName;

	public Song(final String pSongName, final int pSongLength)
	{
		aSongName = pSongName;
		aSongLength = pSongLength;
	}

	public int getLength()
	{
		return aSongLength;
	}

	public String getName()
	{
		return aSongName;
	}
}
