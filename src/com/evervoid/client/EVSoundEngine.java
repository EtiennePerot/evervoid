package com.evervoid.client;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.math.Vector3f;

public class EVSoundEngine
{
	private final AudioRenderer aAudioRenderer;
	private final AssetManager aManager;
	private AudioNode bgMusic;

	public EVSoundEngine(final AssetManager pAssetManager, final AudioRenderer pAudioRenderer)
	{
		aManager = pAssetManager;
		aAudioRenderer = pAudioRenderer;
	}

	public void initAudio()
	{
		/* Background music - keeps playing in a loop. */
		bgMusic = new AudioNode(aManager, "snd/music/terran3.ogg", true);
		bgMusic.setLooping(false);
		bgMusic.setPositional(true);
		bgMusic.setLocalTranslation(Vector3f.ZERO.clone());
		bgMusic.setVolume(3);
		bgMusic.updateGeometricState();
		aAudioRenderer.playSource(bgMusic); // play continuously!
	}
}
