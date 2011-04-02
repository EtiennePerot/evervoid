package com.evervoid.client.sound;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.json.Json;
import com.evervoid.utils.MathUtils;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;

public class EVSoundEngine implements EVFrameObserver
{
	private static EVSoundEngine sInstance;
	public static final Logger sSoundEngineLog = Logger.getLogger(EVSoundEngine.class.getName());

	public static EVSoundEngine getInstance()
	{
		return sInstance;
	}

	public static void init(final AssetManager pAssetManager, final AudioRenderer pAudioRenderer)
	{
		if (sInstance == null) {
			sInstance = new EVSoundEngine(pAssetManager, pAudioRenderer);
		}
	}

	/**
	 * Play a sound effect that can be stacked and played simultaneously.
	 */
	private static void playEffect()
	{
		// Not implemented yet.
	}

	/**
	 * Play a sound effect that can not be played simultaneously.
	 */
	private static void playSound()
	{
		// Not implemented yet.
	}

	private final AudioRenderer aAudioRenderer;
	private final AssetManager aManager;
	private float aTimeLeft = 0;
	private AudioNode bgMusic;
	private final ArrayList<Song> songList = new ArrayList<Song>();

	private EVSoundEngine(final AssetManager pAssetManager, final AudioRenderer pAudioRenderer)
	{
		sSoundEngineLog.setLevel(Level.WARNING);
		// FIXME: The next line is commented out to stop sound from playing.
		// EVFrameManager.register(this);
		final Json musicInfo = Json.fromFile("res/snd/soundtracks.json");
		for (final String music : musicInfo.getAttributes()) {
			songList.add(new Song(music, musicInfo.getAttribute(music).getListItem(0).getInt()));
		}
		aManager = pAssetManager;
		aAudioRenderer = pAudioRenderer;
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		aTimeLeft -= f.aTpf;
		if (aTimeLeft <= 0) {
			final Song randomSong = (Song) MathUtils.getRandomElement(songList);
			bgMusic = new AudioNode(aManager, "snd/" + randomSong.getName(), true);
			aTimeLeft = randomSong.getLength();
			try {
				aAudioRenderer.playSource(bgMusic);
			}
			catch (final NullPointerException e) {
				aTimeLeft = 0;
				sSoundEngineLog.warning("Could not load \"" + randomSong.getName() + "\", this song will be disabled.");
				songList.remove(randomSong);
				if (songList.size() == 0) {
					sSoundEngineLog.severe("Could not load any songs, music will be disabled entirely.");
					EVFrameManager.deregister(this);
					sInstance = null;
				}
			}
		}
	}
}
