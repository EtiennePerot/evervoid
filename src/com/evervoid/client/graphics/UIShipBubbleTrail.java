package com.evervoid.client.graphics;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.graphics.geometry.Transform;
import com.jme3.math.Vector2f;

public class UIShipBubbleTrail extends UIShipTrail
{
	private final float aBubbleDecay;
	private final float aBubbleDistanceInterval;
	private final Vector2f aLastBubbleLocation = new Vector2f();
	private final String aSpriteString;

	public UIShipBubbleTrail(final UIShip ship, final String sprite, final float distanceInterval, final float decay)
	{
		super(ship);
		ship.getSolarSystemGrid().getTrailManager().addNode(this);
		aSpriteString = sprite;
		aBubbleDecay = decay;
		aBubbleDistanceInterval = distanceInterval;
	}

	private Vector2f getAttachPoint()
	{
		return MathUtils.rotateVector(aShip.getTrailAttachPoint(), aShip.getFacingDirection()).add(aShip.getGridTranslation());
	}

	private void makeBubble(final Vector2f bubbleLocation)
	{
		aLastBubbleLocation.set(bubbleLocation);
		final Sprite spr = new Sprite(aSpriteString);
		final Transform bubbleTransform = spr.getNewTransform();
		bubbleTransform.translate(bubbleLocation).rotateTo(aShip.getFacingDirection()).commit();
		addNode(spr);
		spr.getNewAlphaAnimation().setTargetAlpha(0).setDuration(aBubbleDecay).start(new Runnable()
		{
			@Override
			public void run()
			{
				delNode(spr);
			}
		});
	}

	@Override
	public void shipMove()
	{
		final Vector2f newLocation = getAttachPoint();
		if (newLocation.distance(aLastBubbleLocation) > aBubbleDistanceInterval) {
			makeBubble(newLocation);
		}
	}

	@Override
	public void shipMoveStart()
	{
		makeBubble(getAttachPoint());
	}
}
