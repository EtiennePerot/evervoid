package com.evervoid.client.views.solar;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.Colorable;
import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.Shade;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedTransform.DurationMode;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.graphics.geometry.MathUtils.MovementDelta;
import com.evervoid.state.EVContainer;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.data.TrailData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.observers.ShipObserver;
import com.evervoid.state.prop.Ship;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class UIShip extends UIShadedProp implements Colorable, ShipObserver
{
	protected static enum ShipState
	{
		INACTIVE, MOVING, SELECTABLE, SELECTED;
	}

	private SpriteData aBaseSprite;
	private Sprite aColorableSprite;
	private MovementDelta aMovementDelta;
	private final Ship aShip;
	private ShipState aState = ShipState.INACTIVE;
	/**
	 * Trail of the ship. The trail auto-attaches to the ship (the method for that depends on the trail type), so no need to
	 * attach it manually in UIShip
	 */
	private UIShipTrail aTrail;

	public UIShip(final SolarGrid grid, final Ship ship)
	{
		super(grid, ship.getLocation(), ship);
		aShip = ship;
		buildProp();
		aGridTranslation.setDuration(ship.getData().getMovingTime());
		// Set rotation speed and mode:
		aFaceTowards.setSpeed(ship.getData().getRotationSpeed()).setDurationMode(DurationMode.CONTINUOUS);
		setHue(GraphicsUtils.getColorRGBA(ship.getColor()));
		ship.registerObserver(this);
	}

	@Override
	protected void buildSprite()
	{
		aBaseSprite = aShip.getData().getBaseSprite();
		final Sprite baseSprite = new Sprite(aBaseSprite);
		addSprite(baseSprite);
		aColorableSprite = new Sprite(aShip.getData().getColorOverlay());
		addSprite(aColorableSprite);
		final TrailData trailInfo = aShip.getTrailData();
		switch (trailInfo.trailKind) {
			case BUBBLE:
				aTrail = new UIShipBubbleTrail(this, trailInfo.baseSprite, trailInfo.distanceInterval, trailInfo.decayTime);
				break;
			case GRADUAL:
				aTrail = new UIShipLinearTrail(this, trailInfo.trailSprites);
				break;
		}
		final Point engineOffset = aShip.getData().getEngineOffset();
		addSprite(new Sprite(trailInfo.engineSprite, engineOffset.x, engineOffset.y));
		final Shade shade = new Shade(aShip.getData().getBaseSprite());
		shade.setGradientPortion(0.6f);
		addSprite(shade);
		setShade(shade);
		enableFloatingAnimation(1f, 2f);
	}

	@Override
	public void finishedMoving()
	{
		if (aMovementDelta != null) {
			faceTowards(aMovementDelta.getAngle());
		}
		if (aSpriteReady) {
			aTrail.shipMoveEnd();
		}
		// FIXME: Should be "inactive" after moving
		// but selected for now because it's more convenient for testing
		// aState = ShipState.SELECTED;
	}

	public float getMovingSpeed()
	{
		return aGridTranslation.getMovingSpeed();
	}

	public Vector2f getTrailAttachPoint()
	{
		return MathUtils.getVector2fFromPoint(aShip.getData().getTrailOffset()).mult(aBaseSprite.scale);
	}

	public void moveShip(final List<GridLocation> path)
	{
		final List<GridLocation> newPath = new ArrayList<GridLocation>(path);
		final GridLocation first = newPath.remove(0);
		if (newPath.isEmpty()) {
			smoothMoveTo(first);
		}
		else {
			smoothMoveTo(first, new Runnable()
			{
				@Override
				public void run()
				{
					moveShip(newPath);
				}
			});
		}
	}

	@Override
	public void populateTransforms()
	{
		super.populateTransforms();
		if (aSpriteReady && aState.equals(ShipState.MOVING)) {
			aTrail.shipMove();
		}
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

	@Override
	public void shipBombed(final GridLocation bombLocation)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipDestroyed(final Ship ship)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipJumped(final EVContainer<Ship> newContainer)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipMoved(final Ship ship, final GridLocation oldLocation, final List<GridLocation> path)
	{
		moveShip(path);
	}

	@Override
	public void shipShot(final GridLocation shootLocation)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipTookDamage(final int damageAmount)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void smoothMoveTo(final GridLocation destination, final Runnable callback)
	{
		faceTowards(destination);
		aMovementDelta = MovementDelta.fromDelta(aGridLocation, destination);
		// Moving must be done AFTER faceTowards, otherwise facing location is updated too soon
		super.smoothMoveTo(destination, callback);
		aState = ShipState.MOVING;
		if (aSpriteReady) {
			aTrail.shipMoveStart();
		}
	}
}
