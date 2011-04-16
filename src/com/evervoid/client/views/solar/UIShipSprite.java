package com.evervoid.client.views.solar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.evervoid.client.graphics.Colorable;
import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.Shadable;
import com.evervoid.client.graphics.Shade;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.Animation;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.data.TrailData;
import com.evervoid.state.data.WeaponData;
import com.evervoid.state.geometry.Point;
import com.evervoid.utils.EVUtils;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class UIShipSprite extends MultiSprite implements Colorable, Shadable
{
	/**
	 * Constant that all ship shields alpha will be multiplied by, because full-opacity shields don't look good.
	 */
	private static final float sShieldFullAlpha = 0.35f;
	private static final float sZOffsetBase = 1;
	private static final float sZOffsetColor = 2;
	private static final float sZOffsetEngine = 4;
	private static final float sZOffsetLasers = 5;
	private static final float sZOffsetShade = 7;
	private static final float sZOffsetShield = 6;
	private static final float sZOffsetTrails = 0;
	private final SpriteData aBaseSprite;
	private final Sprite aColorableSprite;
	private final ShipData aData;
	private final Set<Sprite> aEngineSprites = new HashSet<Sprite>();
	private float aLastRecordedShields = 1f;
	private AnimatedRotation aRotation = null;
	private final Shade aShade;
	private Sprite aShield = null;
	private AnimatedAlpha aShieldAlpha = null;
	/**
	 * Trail of the ship. The trail auto-attaches to the ship (the method for that depends on the trail type), so no need to
	 * attach it manually in UIShip
	 */
	private final List<UIShipTrail> aTrail = new ArrayList<UIShipTrail>(1);
	private AnimatedTranslation aTranslation = null;
	private final Set<Sprite> aTurretSprites = new HashSet<Sprite>();
	private WeaponData aWeaponData = null;

	public UIShipSprite(final ShipData data)
	{
		aData = data;
		aBaseSprite = aData.getBaseSprite();
		final Sprite baseSprite = new Sprite(aBaseSprite);
		addSprite(baseSprite, sZOffsetBase);
		aColorableSprite = new Sprite(aData.getColorOverlay());
		addSprite(aColorableSprite, sZOffsetColor);
		aShade = new Shade(aData.getBaseSprite());
		aShade.setGradientPortion(0.6f);
		addSprite(aShade, sZOffsetShade);
	}

	public UIShipSprite(final UIShip uiship)
	{
		this(uiship.getShip().getData());
		setShieldSprite(uiship.getShip().getShieldSprite());
		setWeaponSprites(uiship.getShip().getWeaponSprites());
		setWeaponData(uiship.getShip().getWeaponData());
		setAnimations(uiship.getTranslationAnimation(), uiship.getRotationAnimation());
	}

	public Vector2f getAnimationNodeOffset()
	{
		if (aTranslation == null) {
			return new Vector2f(0f, 0f);
		}
		return aTranslation.getTranslation2f();
	}

	@Override
	protected Set<EverNode> getEffectiveChildren()
	{
		final Set<EverNode> children = super.getEffectiveChildren();
		if (aTrail.isEmpty()) {
			return children;
		}
		final Set<EverNode> withTrail = new HashSet<EverNode>(children.size() + aTrail.size());
		withTrail.addAll(children);
		withTrail.addAll(aTrail);
		return withTrail;
	}

	public float getFacingDirection()
	{
		if (aRotation == null) {
			return 0;
		}
		return aRotation.getRotationPitch();
	}

	public float getMovingSpeed()
	{
		if (aTranslation == null) {
			return 0;
		}
		return aTranslation.getMovingSpeed();
	}

	private Vector2f getRandomTargetVector(final Rectangle target)
	{
		Vector2f v = target.getRandomVector();
		while (!target.isCloseToMiddle(v)) {
			v = target.getRandomVector();
		}
		return v;
	}

	public UIShipSprite setAnimations(final AnimatedTranslation translation, final AnimatedRotation rotation)
	{
		aTranslation = translation;
		aRotation = rotation;
		return this;
	}

	@Override
	public Shadable setGradientPortion(final float gradientPortion)
	{
		return aShade.setGradientPortion(gradientPortion);
	}

	@Override
	public void setHue(final ColorRGBA hue)
	{
		aColorableSprite.setHue(hue);
		for (final UIShipTrail trail : aTrail) {
			trail.setHue(hue);
		}
	}

	@Override
	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		aColorableSprite.setHue(hue, multiplier);
		for (final UIShipTrail trail : aTrail) {
			trail.setHue(hue, multiplier);
		}
	}

	@Override
	public Shadable setShadeAngle(final float shadeAngle)
	{
		return aShade.setShadeAngle(shadeAngle);
	}

	@Override
	public Shadable setShadeColor(final ColorRGBA glowColor)
	{
		return aShade.setShadeColor(glowColor);
	}

	@Override
	public Shadable setShadePortion(final float shadePortion)
	{
		return aShade.setShadePortion(shadePortion);
	}

	public void setShields(final float shieldsFloat)
	{
		aLastRecordedShields = shieldsFloat;
		aShieldAlpha.setTargetAlpha(sShieldFullAlpha * aLastRecordedShields).start();
	}

	public void setShieldSprite(final SpriteData shieldSprite)
	{
		if (aShield != null) {
			aShieldAlpha.delete();
			delSprite(aShield);
		}
		aShield = new Sprite(shieldSprite);
		aShield.getNewTransform().setScale(aData.getShieldScale());
		aShieldAlpha = aShield.getNewAlphaAnimation();
		aShieldAlpha.setDuration(0.5).setAlpha(sShieldFullAlpha * aLastRecordedShields);
		addSprite(aShield, sZOffsetShield);
	}

	public UIShipSprite setTrails(final TrailData trails, final EverNode animationNode)
	{
		for (final Sprite s : aEngineSprites) {
			delSprite(s);
		}
		aEngineSprites.clear();
		for (final Point p : aData.getEngineOffsets()) {
			final Sprite engineSprite = new Sprite(trails.engineSprite, p.x, p.y);
			aEngineSprites.add(engineSprite);
			addSprite(engineSprite, sZOffsetEngine);
		}
		for (final UIShipTrail s : aTrail) {
			if (delSprite(s) == null) {
				s.removeFromParent();
			}
		}
		aTrail.clear();
		UIShipTrail trail = null;
		for (final Point offset : aData.getTrailOffsets()) {
			switch (trails.trailKind) {
				case BUBBLE:
					trail = new UIShipBubbleTrail(this, animationNode, new Vector2f(offset.x, offset.y), trails.baseSprite,
							trails.distanceInterval, trails.decayTime);
					break;
				case GRADUAL:
					trail = new UIShipLinearTrail(this, animationNode, new Vector2f(offset.x, offset.y), trails.trailSprites);
					break;
			}
			if (trail != null) {
				trail.getNewTransform().translate(0, 0, sZOffsetTrails);
				aTrail.add(trail);
			}
		}
		return this;
	}

	public UIShipSprite setWeaponData(final WeaponData data)
	{
		aWeaponData = data;
		return this;
	}

	public void setWeaponSprites(final List<SpriteData> weapons)
	{
		for (final Sprite s : aTurretSprites) {
			delSprite(s);
		}
		aTurretSprites.clear();
		for (final SpriteData turret : weapons) {
			final Sprite turretS = new Sprite(turret);
			aTurretSprites.add(turretS);
			addSprite(turretS, sZOffsetLasers);
		}
	}

	public void shipMove()
	{
		for (final UIShipTrail trail : aTrail) {
			trail.shipMove();
		}
	}

	public void shipMoveEnd()
	{
		for (final UIShipTrail trail : aTrail) {
			trail.shipMoveEnd();
		}
	}

	public void shipMoveStart()
	{
		for (final UIShipTrail trail : aTrail) {
			trail.shipMoveStart();
		}
	}

	public UIShipSprite shoot(final EverNode animationNode, final Rectangle target, final Runnable callback)
	{
		final List<Point> lasers = aData.getWeaponSlots();
		if (aWeaponData == null || lasers.isEmpty()) {
			// Ship has no visible weapons or unknown weapon data; just call callback directly, can't play animation
			EVUtils.runCallback(callback);
			return this;
		}
		// Else, it's animation time~
		final Animation animation = new Animation(callback);
		final Vector2f shipOffset = getAnimationNodeOffset();
		final float shots = aWeaponData.getShots();
		final float interval = aWeaponData.getInterval();
		final float duration = aWeaponData.getSpeed();
		final SpriteData laserSprite = aWeaponData.getLaserSprite();
		for (final Point laser : lasers) {
			final Vector2f offset = new Vector2f(laser.x, laser.y);
			offset.rotateAroundOrigin(-getFacingDirection(), true);
			final Vector2f targetVector = getRandomTargetVector(target);
			float delay = 0;
			for (int shot = 0; shot < shots; shot++) {
				final Vector2f randomShot = new Vector2f(MathUtils.getRandomFloatBetween(-4, 4),
						MathUtils.getRandomFloatBetween(-4, 4));
				animation.addStep(delay, new Runnable()
				{
					@Override
					public void run()
					{
						new UIShipLaser(animationNode, shipOffset.add(offset), targetVector.add(randomShot), duration,
								laserSprite);
					}
				});
				delay += interval;
			}
		}
		animation.start();
		return this;
	}
}
