package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.client.graphics.geometry.Smoothing;
import com.evervoid.client.sound.EVSoundEngine;
import com.evervoid.client.sound.Sfx;
import com.evervoid.state.data.SpriteData;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Vector2f;

public class UIShipBomb extends MultiSprite
{
	public UIShipBomb(final EverNode animationNode, final Vector2f origin, final Rectangle target, final double duration,
			final SpriteData bomb, final Runnable callback)
	{
		EVSoundEngine.playEffect(Sfx.SOUND_EFFECT.LASER); // Pew pew
		animationNode.addNode(this); // Add self
		addSprite(bomb);
		final AnimatedAlpha alpha = getNewAlphaAnimation();
		alpha.setAlpha(0);
		final AnimatedTranslation translation = getNewTranslationAnimation();
		translation.setSmoothing(Smoothing.LINEAR).faceTowards(target.getCenter2f()).translate(origin);
		translation.smoothMoveTo(target.getCenter2f()).setDuration(duration).start(new Runnable()
		{
			@Override
			public void run()
			{
				new MultiExplosion(animationNode, MathUtils.getRandomFloatBetween(4, 8), target, callback);
				removeFromParent();
			}
		});
		// Go to 0.999 so that we can do a dummy animation to alpha 1 until the last quarter of the animation
		alpha.setTargetAlpha(0.999).setDuration(duration / 4).start(new Runnable()
		{
			@Override
			public void run()
			{
				alpha.setTargetAlpha(1).setDuration(duration / 2).start(new Runnable()
				{
					@Override
					public void run()
					{
						alpha.setTargetAlpha(0).setDuration(duration / 4).start();
					}
				});
			}
		});
	}
}
