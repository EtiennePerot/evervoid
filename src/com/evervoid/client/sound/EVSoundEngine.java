package com.evervoid.client.sound;

import java.util.ArrayList;
import java.util.Collections;
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
	private static AudioRenderer aAudioRenderer;
	private final static ArrayList<AudioNode> sfxList = new ArrayList<AudioNode>();
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
	public static void playEffect(final int sfxNumber)
	{
		// Because an audioNode can only be playing once, we play a copy instead so that
		// we can play more than one simultaneously
		final AudioNode soundEffect = (AudioNode) sfxList.get(sfxNumber).clone();
		aAudioRenderer.playSource(soundEffect);
	}

	/**
	 * Play a sound effect that can not be played simultaneously.
	 */
	public static void playSound(final int sfxNumber)
	{
		aAudioRenderer.playSource(sfxList.get(sfxNumber));
	}

	private final AssetManager aManager;
	private float aTimeLeft = 0;
	private AudioNode bgMusic;
	private final ArrayList<Sound> songList = new ArrayList<Sound>();

	private EVSoundEngine(final AssetManager pAssetManager, final AudioRenderer pAudioRenderer)
	{
		aManager = pAssetManager;
		aAudioRenderer = pAudioRenderer;
		sSoundEngineLog.setLevel(Level.WARNING);
		// FIXME: The next line is commented out to stop sound from playing.
		EVFrameManager.register(this);
		final Json musicInfo = Json.fromFile("res/snd/music/soundtracks.json");
		for (final String music : musicInfo.getAttributes()) {
			songList.add(new Sound(music, musicInfo.getAttribute(music).getListItem(0).getInt()));
		}
		// Load the sound effects in memory.
		final Json sfxInfo = Json.fromFile("res/snd/sfx/soundeffects.json");
		for (final String sound : sfxInfo.getAttributes()) {
			sfxList.add(new AudioNode(aManager, "snd/sfx/" + sound, false));
		}
		// Simplify the constants naming
		Collections.reverse(sfxList);
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		aTimeLeft -= f.aTpf;
		if (aTimeLeft <= 0) {
			final Sound randomSong = (Sound) MathUtils.getRandomElement(songList);
			bgMusic = new AudioNode(aManager, "snd/music/" + randomSong.getName(), true);
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
