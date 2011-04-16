package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.Colorable;
import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.MultiSprite;
import com.jme3.math.Vector2f;

public abstract class UIShipTrail extends MultiSprite implements Colorable
{
	protected EverNode aAnimationNode;
	protected Vector2f aOffset;
	protected UIShipSprite aShip;

	public UIShipTrail(final UIShipSprite ship, final EverNode animationNode, final Vector2f trailOffset)
	{
		super();
		aShip = ship;
		aAnimationNode = animationNode;
		aOffset = trailOffset;
	}

	void shipMove()
	{
		// Overriden by subclasses
	}

	void shipMoveEnd()
	{
		// Overriden by subclasses
	}

	void shipMoveStart()
	{
		// Overriden by subclasses
	}
}
