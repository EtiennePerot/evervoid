package com.evervoid.client;

import java.util.ArrayList;

import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.client.sound.Song;
import com.evervoid.json.Json;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;

public class EVSoundEngine implements EVFrameObserver
{
	private static EVSoundEngine sInstance;

	public static void init(final AssetManager pAssetManager, final AudioRenderer pAudioRenderer)
	{
		sInstance = new EVSoundEngine(pAssetManager, pAudioRenderer);
	}

	private final AudioRenderer aAudioRenderer;
	private final AssetManager aManager;
	private float aTimeLeft = 0;
	private AudioNode bgMusic;
	private final ArrayList<Song> songList = new ArrayList<Song>();

	private EVSoundEngine(final AssetManager pAssetManager, final AudioRenderer pAudioRenderer)
	{
		EVFrameManager.register(this);
		final Json musicInfo = Json.fromFile("res/snd/soundtracks.json");
		for (final String music : musicInfo.getAttributes()) {
			System.out.println(music);
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
			aAudioRenderer.playSource(bgMusic);
		}
	}
}
