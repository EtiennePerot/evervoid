package com.evervoid.client.graphics;

import com.evervoid.client.graphics.geometry.AnimatedTransform.DurationMode;
import com.evervoid.client.graphics.geometry.Geometry.MovementDelta;
import com.evervoid.client.graphics.geometry.GridPoint;
import com.evervoid.client.views.solar.SolarSystemGrid;
import com.evervoid.engine.prop.Ship;
import com.jme3.math.ColorRGBA;

public class UIShip extends UIProp implements Colorable
{
	protected static enum ShipState
	{
		INACTIVE, MOVING, SELECTABLE, SELECTED;
	}

	private Sprite aColorableSprite;
	private MovementDelta aMovementDelta;
	private ShipState aState = ShipState.INACTIVE;
	private ShipTrail aTrail;

	public UIShip(final SolarSystemGrid grid, final GridPoint location)
	{
		super(grid, location);
		aGridTranslation.setDuration(1); // Set moving speed
		// Set rotation speed and mode:
		aFaceTowards.setSpeed(1.2f).setDurationMode(DurationMode.CONTINUOUS);
		setHue(ColorRGBA.randomColor());
	}

	public UIShip(final SolarSystemGrid grid, final Ship aShip)
	{
		this(grid, new GridPoint(aShip.getLocation()));
	}

	@Override
	protected void buildSprite()
	{
		final Sprite baseSprite = new Sprite("ships/square/scout_base.png");
		addSprite(baseSprite);
		aColorableSprite = new Sprite("ships/square/scout_color.png");
		addSprite(aColorableSprite);
		aTrail = new ShipTrail();
		addSprite(aTrail, Sprite.sSpriteScale * baseSprite.getWidth() + 2, 0);
		enableFloatingAnimation(1f, 2f);
	}

	@Override
	public void computeTransforms()
	{
		super.computeTransforms();
		if (aSpriteReady)
		{
			aTrail.setGradualState(aGridTranslation.getMovingSpeed());
		}
	}

	@Override
	public void faceTowards(final GridPoint target)
	{
		if (aState == ShipState.SELECTED)
		{
			super.faceTowards(target);
		}
	}

	@Override
	public void hasMoved()
	{
		super.hasMoved();
		if (aMovementDelta != null)
		{
			faceTowards(aMovementDelta.getAngle());
		}
		// FIXME: Should be "inactive" after moving
		// but selected for now because it's more convenient for testing
		aState = ShipState.SELECTED;
	}

	public void moveShip(final GridPoint destination)
	{
		if (aState == ShipState.SELECTED)
		{
			faceTowards(destination);
			aMovementDelta = MovementDelta.fromDelta(aGridLocation, destination);
			super.smoothMoveTo(destination);
			aState = ShipState.MOVING;
		}
	}

	public void moveShip(final int row, final int column)
	{
		moveShip(new GridPoint(column, row));
	}

	public void select()
	{
		aState = ShipState.SELECTED;
	}

	@Override
	public void setHue(final ColorRGBA hue)
	{
		aColorableSprite.setHue(hue);
	}

	@Override
	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		aColorableSprite.setHue(hue, multiplier);
	}
}
