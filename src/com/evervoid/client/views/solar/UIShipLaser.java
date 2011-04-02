package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.Smoothing;
import com.evervoid.state.data.SpriteData;
import com.jme3.math.Vector2f;

public class UIShipLaser extends MultiSprite
{
	public UIShipLaser(final EverNode animationNode, final Vector2f origin, final Vector2f target, final double duration,
			final Runnable callback)
	{
		animationNode.addNode(this); // Add self
		addSprite(new SpriteData("ships/round/projectile_0.png"));
		final AnimatedAlpha alpha = getNewAlphaAnimation();
		alpha.setAlpha(0);
		final AnimatedTranslation translation = getNewTranslationAnimation();
		translation.setSmoothing(Smoothing.LINEAR).faceTowards(target.subtract(origin)).translate(origin);
		translation.smoothMoveTo(target).setDuration(duration).start(new Runnable()
		{
			@Override
			public void run()
			{
				new Explosion(animationNode, target, "small_round", new Runnable()
				{
					@Override
					public void run()
					{
						if (callback != null) {
							callback.run();
						}
					}
				});
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
