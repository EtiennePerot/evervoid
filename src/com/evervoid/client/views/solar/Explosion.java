package com.evervoid.client.views.solar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.utils.EVUtils;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class Explosion extends MultiSprite
{
	private static Map<String, List<String>> sAvailableExplosionSprites = null;
	public static float sExplosionFrameDuration = 0.075f;
	private static final String sExplosionSpritesPath = "explosions/";

	/**
	 * Detects available explosion types. Files must be named some_type_of_sequence_NUMBER.png
	 */
	private static void init()
	{
		// Init maps
		sAvailableExplosionSprites = new HashMap<String, List<String>>();
		final Map<String, Integer> frames = new HashMap<String, Integer>();
		// Find max number of frames for each sequence (I hate Java IO)
		final File[] available = new File(GraphicManager.getSpritePath(sExplosionSpritesPath)).listFiles();
		final Pattern pattern = Pattern.compile("^(.*?)_(\\d+)\\.png$", Pattern.CASE_INSENSITIVE);
		if (available != null) {
			for (final File f : available) {
				final Matcher result = pattern.matcher(f.getName());
				if (result.matches()) {
					final String type = result.group(1);
					final int frame = Integer.valueOf(result.group(2));
					if (!sAvailableExplosionSprites.containsKey(type)) {
						sAvailableExplosionSprites.put(type, new ArrayList<String>());
						frames.put(type, 0);
					}
					frames.put(type, Math.max(frame, frames.get(type)));
				}
			}
		}
		// Populate sprite map
		for (final String type : frames.keySet()) {
			for (int frame = 1; frame <= frames.get(type); frame++) {
				sAvailableExplosionSprites.get(type).add(sExplosionSpritesPath + type + "_" + frame + ".png");
			}
		}
	}

	private Runnable aCallback;
	private List<String> aFrames;
	private FrameTimer aTimer;

	public Explosion(final EverNode parent, final Vector2f origin, final String type, final Runnable callback)
	{
		if (sAvailableExplosionSprites == null) {
			init();
		}
		if (sAvailableExplosionSprites.containsKey(type)) {
			// Apply offset + Make sure we're on top of ships + Rescale randomly
			getNewTransform().translate(new Vector3f(origin.x, origin.y, 100))
					.setScale(MathUtils.getRandomFloatBetween(0.5, 1));
			parent.addNode(this); // Add self
			aCallback = callback;
			aTimer = new FrameTimer(new Runnable()
			{
				@Override
				public void run()
				{
					explosionStep();
				}
			}, sExplosionFrameDuration).start();
			// Make a copy, cause we'll be modifying it
			aFrames = new ArrayList<String>(sAvailableExplosionSprites.get(type));
			explosionStep(); // Display first frame right away
		}
	}

	private void explosionStep()
	{
		if (aFrames.isEmpty()) {
			// We're done, delete self
			removeFromParent();
			aTimer.stop();
			EVUtils.runCallback(aCallback);
			return;
		}
		delAllNodes(); // Clear self
		final String frame = aFrames.remove(0);
		addNode(new Sprite(frame)); // Add new frame
	}

	public float getDurationLeft()
	{
		return aFrames.size() * sExplosionFrameDuration;
	}

	public void setCallback(final Runnable callback)
	{
		aCallback = callback;
	}
}
