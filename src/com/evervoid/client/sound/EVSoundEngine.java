package com.evervoid.client.sound;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.json.Json;
import com.evervoid.utils.LoggerUtils;
import com.evervoid.utils.MathUtils;

/**
 * This class is used to play sound effects as well as background music.
 */
public class EVSoundEngine implements EVFrameObserver
{
	/**
	 * This contains the currently playing background music
	 */
	private static MP3 sBGMusic;
	/**
	 * The sound engine singleton instance
	 */
	private static EVSoundEngine sInstance;
	/**
	 * This contains the list of sound effects
	 */
	// TODO: use an hashmap/set instead.
	private final static ArrayList<MP3> sSFXList = new ArrayList<MP3>();

	/**
	 * Close the background music stream in order to correctly terminate the program.
	 */
	public static void cleanup()
	{
		if (sBGMusic != null) {
			sBGMusic.close();
		}
	}

	/**
	 * @return The EVSoundEngine singleton instance
	 */
	public static EVSoundEngine getInstance()
	{
		return sInstance;
	}

	/**
	 * Create a singleton instance of the sound engine.
	 */
	public static void init()
	{
		if (sInstance == null) {
			sInstance = new EVSoundEngine();
		}
	}

	/**
	 * Play a sound effect that can be stacked and played simultaneously.
	 * 
	 * @param sfxNumber
	 *            A numebr representing a sound effect in the sfx list.
	 */
	public static void playEffect(final int sfxNumber)
	{
		// Check if sound effects are enabled
		if (EverVoidClient.getSettings().getSfx()) {
			// The sound effects from the list
			// TODO: Create a static enum lsit to use with a hashmap so it's easy for other classes to play sound effects
			final MP3 effect = sSFXList.get(sfxNumber);
			if (effect != null) {
				try {
					effect.play();
				}
				catch (final Exception e) {
					// Specified sound effect does not exist
					LoggerUtils.warning("The specified sound effect does not exist.");
					// Worst case, the effect didn't play.
				}
			}
		}
	}

	/**
	 * Stop the currently playing background music, if any.
	 */
	public static void stopSound()
	{
		if (sBGMusic != null) {
			sBGMusic.close();
			sInstance.aTimeLeft = 0;
		}
	}

	/**
	 * Time left of the currently playing background music.
	 */
	private float aTimeLeft = 0;
	/**
	 * List of the available background musics.
	 */
	// TODO: use a better data structure
	private final ArrayList<MP3> songList = new ArrayList<MP3>();

	/**
	 * EVSoundEngine constructor. Loads the music files into the appropriate list using the music files specified in a JSON
	 * file.
	 */
	private EVSoundEngine()
	{
		EVFrameManager.register(this);
		final Json musicInfo = Json.fromFile("snd/music/soundtracks.json");
		for (final String music : musicInfo.getAttributes()) {
			songList.add(new MP3("music" + File.separator + music, musicInfo.getAttribute(music).getInt()));
		}
		final Thread loadAllSounds = new Thread()
		{
			@Override
			public void run()
			{
				loadSounds();
			}
		};
		loadAllSounds.setDaemon(true);
		loadAllSounds.setName("Loading all sounds");
		loadAllSounds.start();
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		aTimeLeft -= f.aTpf;
		if (aTimeLeft <= 0 && EverVoidClient.getSettings().getSound()) {
			if (sBGMusic != null) {
				sBGMusic.close();
			}
			sBGMusic = (MathUtils.getRandomElement(songList)).clone();
			try {
				sBGMusic.play();
			}
			catch (final Exception e) {
				aTimeLeft = 0;
				LoggerUtils.warning("Could not load \"" + sBGMusic.getSoundName() + "\", this song will be disabled.");
				songList.remove(sBGMusic);
				if (songList.size() == 0) {
					LoggerUtils.severe("Could not load any songs, music will be disabled entirely.");
					EVFrameManager.deregister(this);
					sInstance = null;
				}
			}
			aTimeLeft = sBGMusic.getDuration();
		}
	}

	/**
	 * Load the sound effects in memory asynchronously.
	 */
	private void loadSounds()
	{
		// Load the sound effects in memory.
		final Json sfxInfo = Json.fromFile("snd/sfx/soundeffects.json");
		for (final String sound : sfxInfo.getAttributes()) {
			sSFXList.add(new MP3("sfx" + File.separator + sound));
		}
		// Simplify the constants naming
		Collections.reverse(sSFXList);
	}
}
