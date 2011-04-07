package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.data.SpriteData;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class UIShipBubbleTrail extends UIShipTrail
{
	private final float aBubbleDecay;
	private final float aBubbleDistanceInterval;
	private ColorRGBA aHueColor = null;
	private Float aHueMultiplier = null;
	private final Vector2f aLastBubbleLocation = new Vector2f();
	private final SpriteData aSprite;

	public UIShipBubbleTrail(final UIShip ship, final SpriteData sprite, final float distanceInterval, final float decay)
	{
		super(ship);
		ship.getGridAnimationNode().addNode(this);
		aSprite = sprite;
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
		final Sprite spr = new Sprite(aSprite);
		if (aHueColor != null) {
			if (aHueMultiplier != null) {
				spr.setHue(aHueColor, aHueMultiplier);
			}
			else {
				spr.setHue(aHueColor);
			}
		}
		final Transform bubbleTransform = spr.getNewTransform();
		bubbleTransform.translate(bubbleLocation).rotatePitchTo(aShip.getFacingDirection()).commit();
		addNode(spr);
		spr.smoothDisappear(aBubbleDecay);
	}

	@Override
	public void setHue(final ColorRGBA hue)
	{
		aHueColor = hue;
		aHueMultiplier = null;
	}

	@Override
	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		aHueColor = hue;
		aHueMultiplier = multiplier;
	}

	@Override
	void shipMove()
	{
		final Vector2f newLocation = getAttachPoint();
		if (newLocation.distance(aLastBubbleLocation) > aBubbleDistanceInterval) {
			makeBubble(newLocation);
		}
	}

	@Override
	void shipMoveStart()
	{
		makeBubble(getAttachPoint());
	}
}
