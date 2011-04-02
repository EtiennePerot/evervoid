package com.evervoid.client.sound;

public class Sound
{
	private final int aSongLength;
	private final String aSongName;

	public Sound(final String pSoundName)
	{
		// Length is not needed for sound effects, so set it to 0.
		this(pSoundName, 0);
	}

	public Sound(final String pSongName, final int pSongLength)
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
		final Sound o = (Sound) other;
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
