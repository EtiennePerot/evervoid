package com.evervoid.client.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.json.Json;
import com.evervoid.utils.MathUtils;

public class EVSoundEngine implements EVFrameObserver
{
	private static MP3 sBGMusic;
	private static EVSoundEngine sInstance;
	private final static ArrayList<AudioInputStream> sSFXList = new ArrayList<AudioInputStream>();
	public static final Logger sSoundEngineLog = Logger.getLogger(EVSoundEngine.class.getName());

	public static void cleanup()
	{
		if (sBGMusic != null) {
			sBGMusic.close();
		}
	}

	public static EVSoundEngine getInstance()
	{
		return sInstance;
	}

	public static void init()
	{
		if (sInstance == null) {
			sInstance = new EVSoundEngine();
		}
	}

	/**
	 * Play a sound effect that can be stacked and played simultaneously.
	 */
	public static void playEffect(final int sfxNumber)
	{
		if (EverVoidClient.getSettings().getSfx()) {
			final AudioInputStream stream = sSFXList.get(sfxNumber);
			if (stream != null) {
				final Thread loadingThread = new Thread()
				{
					@Override
					public void run()
					{
						sInstance.playStream(stream);
					}
				};
				loadingThread.setDaemon(true);
				loadingThread.setName("LoadSound: " + sfxNumber);
				loadingThread.start();
			}
		}
	}

	public static void stopSound()
	{
		if (sBGMusic != null) {
			sBGMusic.close();
			sInstance.aTimeLeft = 0;
		}
	}

	private float aTimeLeft = 0;
	private final ArrayList<String> songList = new ArrayList<String>();

	private EVSoundEngine()
	{
		sSoundEngineLog.setLevel(Level.WARNING);
		EVFrameManager.register(this);
		final Json musicInfo = Json.fromFile("res/snd/music/soundtracks.json");
		for (final String music : musicInfo.getAttributes()) {
			songList.add("res" + File.separator + "snd" + File.separator + "music" + File.separator + music);
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
			final String randomSong = (String) MathUtils.getRandomElement(songList);
			sBGMusic = new MP3(randomSong);
			try {
				sBGMusic.play();
			}
			catch (final Exception e) {
				aTimeLeft = 0;
				sSoundEngineLog.warning("Could not load \"" + randomSong + "\", this song will be disabled.");
				songList.remove(randomSong);
				if (songList.size() == 0) {
					sSoundEngineLog.severe("Could not load any songs, music will be disabled entirely.");
					EVFrameManager.deregister(this);
					sInstance = null;
				}
			}
			// TODO: You know...
			aTimeLeft = 521;
		}
	}

	private void loadSounds()
	{
		// Load the sound effects in memory.
		final Json sfxInfo = Json.fromFile("res/snd/sfx/soundeffects.json");
		for (final String sound : sfxInfo.getAttributes()) {
			try {
				final File file = new File("res" + File.separator + "snd" + File.separator + "sfx" + File.separator + sound);
				final AudioInputStream tempSteam = AudioSystem.getAudioInputStream(file);
				sSFXList.add(tempSteam);
			}
			catch (final IOException e) {
				e.printStackTrace();
			}
			catch (final UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		}
		// Simplify the constants naming
		Collections.reverse(sSFXList);
	}

	private void playStream(final AudioInputStream stream)
	{
		try {
			final Clip tempClip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, stream.getFormat()));
			tempClip.open(stream);
			if (tempClip.isRunning()) {
				tempClip.stop();
			}
			tempClip.setFramePosition(0);
			tempClip.start();
		}
		catch (final Exception e) {
			// Nothing
		}
	}
}
