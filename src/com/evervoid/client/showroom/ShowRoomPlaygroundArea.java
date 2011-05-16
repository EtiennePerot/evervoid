package com.evervoid.client.showroom;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.client.ui.PlainRectangleControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.solar.UIShipSprite;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.data.TrailData;
import com.evervoid.state.data.WeaponData;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.utils.EVUtils;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * The UI area in which the ship actually moves.
 */
public class ShowRoomPlaygroundArea extends PlainRectangleControl implements EVFrameObserver
{
	/**
	 * Last recorded cursor position. This is necessary when the cursor is out of the rectangle's bounds and the user requests
	 * to shoot, because the cursor's position at that point is then irrelevant.
	 */
	private final Vector2f aLastCursorPosition = new Vector2f(0, 0);
	/**
	 * Distance at which the ship's shadow intensity will be the maximum. Will be initialized to the length of the diagonal of
	 * the playground rectangle divided by 2.
	 */
	private float aMaxShadeDistance = 0f;
	/**
	 * The sprite of the ship being toyed with
	 */
	private UIShipSprite aShip;
	/**
	 * Hue of the ship being toyed with
	 */
	private ColorRGBA aShipColor;
	/**
	 * Rotation animation of the ship being toyed with
	 */
	private AnimatedRotation aShipRotation;
	/**
	 * Translation animation of the ship being toyed with
	 */
	private AnimatedTranslation aShipTranslation;

	/**
	 * Constructor; needs the {@link RaceData} and {@link ShipData} to build the ship.
	 * 
	 * @param race
	 *            {@link RaceData} of the ship being toyed with
	 * @param ship
	 *            {@link ShipData} of the ship being toyed with
	 */
	public ShowRoomPlaygroundArea(final RaceData race, final ShipData ship)
	{
		super(ColorRGBA.Black);
		aShipColor = ColorRGBA.randomColor();
		updateData(race, ship);
		setDesiredDimension(new Dimension(600, 400));
		EVFrameManager.register(this);
	}

	@Override
	public boolean click(final Vector2f point)
	{
		if (inBounds(point)) {
			moveTo(new Vector2f(point.x - aComputedBounds.x, point.y - aComputedBounds.y), null);
			return true;
		}
		return false;
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		final Vector2f lightDelta = aLastCursorPosition.subtract(aShipTranslation.getTranslation2f());
		final Float angle = MathUtils.getAngleTowards(lightDelta);
		if (angle != null) { // Angle may be null if the cursor is exactly on the ship's origin
			aShip.setShadeAngle(angle - aShipRotation.getRotationPitch());
		}
		aShip.setShadePortion(lightDelta.length() / aMaxShadeDistance);
		if (aShipTranslation.isInProgress()) {
			aShip.shipMove();
		}
	}

	/**
	 * Move the ship to a location on screen
	 * 
	 * @param target
	 *            The location to move to
	 * @param callback
	 *            a {@link Runnable} to call when the ship is done moving
	 */
	private void moveTo(final Vector2f target, final Runnable callback)
	{
		final Vector2f boundedTarget = target.clone();
		final Vector2f halfDim = aShip.getDimensions().divide(2);
		MathUtils.clampVector2fLocal(halfDim, boundedTarget,
				new Vector2f(aComputedBounds.width, aComputedBounds.height).subtract(halfDim));
		aShip.shipMoveStart();
		rotateTowards(boundedTarget, new Runnable()
		{
			@Override
			public void run()
			{
				aShipTranslation.smoothMoveTo(boundedTarget).start(new Runnable()
				{
					@Override
					public void run()
					{
						aShip.shipMoveEnd();
						EVUtils.runCallback(callback);
					}
				});
			}
		});
	}

	@Override
	public boolean onMouseMove(final Vector2f point)
	{
		super.onMouseMove(point);
		if (inBounds(point)) {
			aLastCursorPosition.set(point.x - aComputedBounds.x, point.y - aComputedBounds.y);
		}
		return true;
	}

	/**
	 * Randomizes the ship's hue, and applies it to the ship instantly.
	 */
	void randomColor()
	{
		aShipColor = ColorRGBA.randomColor();
		aShip.setHue(aShipColor);
	}

	/**
	 * Rotates the ship towards a certain point.
	 * 
	 * @param point
	 *            The point that the ship should look at
	 * @param callback
	 *            A {@link Runnable} to call when the ship is done rotating
	 */
	private void rotateTowards(final Vector2f point, final Runnable callback)
	{
		aShipTranslation.stop();
		aShipRotation.setTargetPoint2D(point.subtract(aShipTranslation.getTranslation2f())).start(callback);
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

	/**
	 * Override the ship's {@link TrailData} with new information
	 * 
	 * @param data
	 *            The ship's new {@link TrailData}
	 */
	void setTrailData(final TrailData data)
	{
		aShip.setTrails(data, this);
	}

	/**
	 * Override the ship's {@link WeaponData} with new information
	 * 
	 * @param data
	 *            The ship's new {@link WeaponData}
	 */
	void setWeaponData(final WeaponData data)
	{
		aShip.setWeaponData(data);
	}

	/**
	 * Makes the ship rotate towards the latest recorded cursor position, then shoot at it.
	 */
	void shoot()
	{
		aShip.shipMoveStart();
		final Vector2f target = aLastCursorPosition.clone();
		final ShowRoomPlaygroundArea oldThis = this;
		rotateTowards(target, new Runnable()
		{
			@Override
			public void run()
			{
				aShip.shoot(oldThis, new Rectangle(target, 1, 1), null);
				aShip.shipMoveEnd();
			}
		});
	}

	/**
	 * Update the ship's {@link RaceData} and {@link ShipData} with new information
	 * 
	 * @param race
	 *            The ship's new {@link RaceData}
	 * @param ship
	 *            The ship's new {@link ShipData}
	 */
	void updateData(final RaceData race, final ShipData ship)
	{
		final Vector3f oldTranslation = new Vector3f(0, 0, 0);
		final Vector3f oldRotation = new Vector3f(0, 0, 0);
		if (aShipTranslation != null) {
			oldTranslation.set(aShipTranslation.getTranslation());
			aShipTranslation.delete();
		}
		if (aShipRotation != null) {
			oldRotation.set(aShipRotation.getRotation());
			aShipRotation.delete();
		}
		if (aShip != null) {
			aShip.removeFromParent();
		}
		aShip = new UIShipSprite(ship);
		aShipTranslation = aShip.getNewTranslationAnimation();
		aShipTranslation.translate(oldTranslation);
		aShipRotation = aShip.getNewRotationAnimation();
		aShipRotation.rotateTo(oldRotation);
		aShip.setAnimations(aShipTranslation, aShipRotation);
		setTrailData(race.getTrailData(race.getTrailTypes().iterator().next()));
		setWeaponData(race.getWeaponData(race.getWeaponTypes().iterator().next()));
		aShip.setShieldSprite(ship.getShieldSprite(null)); // FIXME
		aShip.setShadeColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1));
		aShip.setShields(1);
		aShip.setHue(aShipColor);
		addNode(aShip);
	}
}
