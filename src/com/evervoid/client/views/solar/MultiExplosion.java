package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.client.sound.EVSoundEngine;
import com.evervoid.client.sound.Sfx;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Vector2f;

public class MultiExplosion extends EverNode
{
	private final Rectangle aBounds;
	private final Runnable aCallback;
	private int aExplosionsLeft;

	public MultiExplosion(final EverNode animationNode, final float maxExplosions, final Rectangle bounds,
			final Runnable callback)
	{
		animationNode.addNode(this);
		aCallback = callback;
		aBounds = bounds;
		aExplosionsLeft = MathUtils.getRandomIntBetween(3, maxExplosions);
		EVSoundEngine.playEffect(Sfx.EXPLOSION);
		explosionStep();
	}

	private void explosionStep()
	{
		aExplosionsLeft--;
		final Vector2f target = aBounds.getRandomVector();
		final String type = aBounds.isCloseToMiddle(target) ? "medium_flower" : "small_flower";
		final Explosion current = new Explosion(this, target, type, null);
		if (aExplosionsLeft == 0) {
			// This was the last explosion
			current.setCallback(new Runnable()
			{
				@Override
				public void run()
				{
					if (aCallback != null) {
						aCallback.run();
					}
					removeFromParent();
					return;
				}
			});
		}
		else {
			// There's still some stuff that need some good ol' blowin' up
			final float duration = MathUtils.getRandomFloatBetween(Explosion.sExplosionFrameDuration * 2,
					current.getDurationLeft());
			new FrameTimer(new Runnable()
			{
				@Override
				public void run()
				{
					explosionStep();
				}
			}, duration, 1).start();
		}
	}
}
