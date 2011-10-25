package com.evervoid.client.sound;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.json.Json;
import com.evervoid.utils.LoggerUtils;
import com.evervoid.utils.MathUtils;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;

/**
 * This class is used to play sound effects as well as background music.
 */
public class EVSoundEngine implements EVFrameObserver
{
	/**
	 * The sound engine singleton instance
	 */
	private static EVSoundEngine sInstance;

	/**
	 * Close the background music stream in order to correctly terminate the program.
	 */
	public static void cleanup()
	{
		if (sInstance.sBGMusic != null) {
			sInstance.sBGMusic.stop();
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
	 * @param sfx
	 *            A sound effect representing a sound effect in the sfx list.
	 */
	public static void playEffect(final Sfx.SOUND_EFFECT sfx)
	{
		// Check if sound effects are enabled
		if (!EverVoidClient.getSettings().shouldPlaySfx()) {
			return;
		}
		// Get the sound effects from the list
		final AudioNode effect = sInstance.sSFXList.get(sfx.getIndex());
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

	/**
	 * Stop the currently playing background music, if any.
	 */
	public static void stopSound()
	{
		if (sInstance.sBGMusic != null) {
			// sBGMusic.close();
			sInstance.sBGMusic.stop();
			sInstance.aTimeLeft = 0;
		}
	}

	/**
	 * Time left of the currently playing background music.
	 */
	private float aTimeLeft = 0;
	/**
	 * This contains the currently playing background music
	 */
	private AudioNode sBGMusic;
	/**
	 * List of the available background musics.
	 */
	private final Map<AudioNode, Integer> songList = new HashMap<AudioNode, Integer>();
	/**
	 * This contains the list of sound effects
	 */
	// TODO: use an hashmap/set instead.
	private final Map<Integer, AudioNode> sSFXList = new HashMap<Integer, AudioNode>();

	/**
	 * EVSoundEngine constructor. Loads the music files into the appropriate list using the music files specified in a JSON
	 * file.
	 */
	private EVSoundEngine()
	{
		// Sounds don't report when they finish, we use frames to get a best guess as to when a clip is done
		EVFrameManager.register(this);
		// do all sound loading in a daemon thread, otherwise game takes too long to boot up
		final Thread loadAllSounds = new Thread()
		{
			@Override
			public void run()
			{
				loadSounds();
			}
		};
		loadAllSounds.setDaemon(true);
		loadAllSounds.setName("SoundLoader");
		loadAllSounds.start();
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		// count down to end of song
		aTimeLeft -= f.aTpf;
		if (aTimeLeft <= 0 && EverVoidClient.getSettings().shouldPlayMusic()) {
			// no song currently playing, there should be one
			if (sBGMusic != null) {
				sBGMusic.stop();
			}
			if (songList == null || songList.size() == 0) {
				// we haven't had time to load songs yet, try again later
				return;
			}
			// find a random song to play
			sBGMusic = MathUtils.getRandomElement(songList.keySet());
			// play it
			try {
				sBGMusic.play();
			}
			catch (final Exception e) {
				aTimeLeft = 0;
				LoggerUtils.warning("Could not load \"" + "\", this song will be disabled.");
				songList.remove(sBGMusic);
				if (songList.size() == 0) {
					LoggerUtils.severe("Could not load any songs, music will be disabled entirely.");
					EVFrameManager.deregister(this);
					sInstance = null;
				}
			}
			aTimeLeft = songList.get(sBGMusic);
		}
	}

	/**
	 * Load the sound effects in memory asynchronously.
	 */
	private void loadSounds()
	{
		// Load the sound effects in memory.
		final AssetManager manager = EverVoidClient.getAssetManger();
		// load sound effects
		final Json sfxInfo = Json.fromFile("snd/sfx/soundeffects.json");
		for (final String sound : sfxInfo.getAttributes()) {
			final AudioNode node = new AudioNode(manager, "snd" + File.separator + "sfx" + File.separator + sound + ".ogg",
					false);
			node.setVolume(6);
			sSFXList.put(sfxInfo.getIntAttribute(sound), node);
		}
		// load the background songs
		final Json musicInfo = Json.fromFile("snd/music/soundtracks.json");
		for (final String music : musicInfo.getAttributes()) {
			final AudioNode node = new AudioNode(manager, "snd" + File.separator + "music" + File.separator + music + ".ogg",
					true);
			node.setVolume(6);
			songList.put(node, musicInfo.getIntAttribute(music));
		}
	}
}
