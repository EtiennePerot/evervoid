package com.evervoid.client.sound;

/**
 * Static class corresponding to the location of the files in the sfx list in {@link EVSoundEngine}.
 */
public final class Sfx
{
	/**
	 * Enumerates all the possible sound effects and keeps their index value in the sfx list.
	 */
	public static enum SOUND_EFFECT
	{
		/**
		 * Default beep sound.
		 */
		BEEP(2),
		/**
		 * Secondary beep (played when a user goes back to a previous menu).
		 */
		BEEP_BACK(3),
		/**
		 * Double beep sound.
		 */
		DOUBLE_BEEP(4),
		/**
		 * Sound played when a ship explodes.
		 */
		EXPLOSION(1),
		/**
		 * Sound played when a ship fires.
		 */
		LASER(0);
		/**
		 * The index of the files in the sfx list.
		 */
		private final int aIndex;

		/**
		 * Creates a new INDEX_VALUE
		 * 
		 * @param pIndex
		 *            the value of the index.
		 */
		private SOUND_EFFECT(final int pIndex)
		{
			aIndex = pIndex;
		}

		/**
		 * @return The index value.
		 */
		public int getIndex()
		{
			return aIndex;
		}
	}

	/**
	 * Makes the Sfx constructor private.
	 */
	private Sfx()
	{
	}
}