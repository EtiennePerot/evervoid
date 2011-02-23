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

	@Override
	public boolean equals(final Object other)
	{
		if (super.equals(other)) {
			return true;
		}
		if (other == null || !other.getClass().equals(getClass())) {
			return false;
		}
		final Song o = (Song) other;
		return aSongName.equals(o.getName()) && aSongLength == o.getLength();
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
