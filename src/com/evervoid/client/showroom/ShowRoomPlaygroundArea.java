package com.evervoid.client.showroom;

import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.PlainRectangleControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.solar.UIShipSprite;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class ShowRoomPlaygroundArea extends PlainRectangleControl
{
	private float aMaxShadeDistance = 0f;
	private final UIShipSprite aShip;
	private final AnimatedRotation aShipRotation;
	private final AnimatedTranslation aShipTranslation;

	public ShowRoomPlaygroundArea(final RaceData race, final ShipData ship)
	{
		super(ColorRGBA.Black);
		aShip = new UIShipSprite(ship);
		aShipTranslation = aShip.getNewTranslationAnimation();
		aShipTranslation.translate(0, 0, 1);
		aShipRotation = aShip.getNewRotationAnimation();
		aShip.setAnimations(aShipTranslation, aShipRotation);
		randomColor();
		addNode(aShip);
		setDesiredDimension(new Dimension(600, 400));
		aShip.setShadeColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1));
	}

	@Override
	public boolean onMouseMove(final Vector2f point)
	{
		super.onMouseMove(point);
		final Vector2f insidePoint = new Vector2f(point.x - aComputedBounds.x, point.y - aComputedBounds.y);
		final Vector2f lightDelta = insidePoint.subtract(aShipTranslation.getTranslation2f());
		final Float angle = MathUtils.getAngleTowards(lightDelta);
		if (angle != null) { // Angle may be null if the cursor is exactly tt the ship's origin
			aShip.setShadeAngle(angle - aShipRotation.getRotationPitch());
		}
		aShip.setShadePortion(lightDelta.length() / aMaxShadeDistance);
		return true;
	}

	public void randomColor()
	{
		aShip.setHue(ColorRGBA.randomColor());
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		final Bounds playBounds = getComputedBounds();
		// Half diagonal distance = full shading
		aMaxShadeDistance = FastMath.sqrt(FastMath.sqr(playBounds.width) + FastMath.sqr(playBounds.height)) / 2f;
		aShipTranslation.setTranslationNow(playBounds.width / 2, playBounds.height / 2);
	}
}
