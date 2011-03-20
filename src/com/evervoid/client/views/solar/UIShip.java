package com.evervoid.client.views.solar;

import java.util.List;

import com.evervoid.client.graphics.Colorable;
import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.Shade;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedTransform.DurationMode;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.ui.HorizontalCenteredControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.game.GameView;
import com.evervoid.state.EVContainer;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.data.TrailData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.observers.ShipObserver;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class UIShip extends UIShadedProp implements Colorable, ShipObserver
{
	private SpriteData aBaseSprite;
	private Sprite aColorableSprite;
	private final Ship aShip;
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

	/**
	 * Panel is structured in 3 columns Base | Stats | Abilities Base contains basic info (health, shields, rad) Stats contains
	 * stats (lasers, speed, etc) Abilities contains buttons for executing abilities
	 */
	@Override
	public UIControl buildPanelUI()
	{
		final UIControl root = new UIControl(BoxDirection.HORIZONTAL);
		final UIControl base = new UIControl(BoxDirection.VERTICAL);
		base.addUI(new HorizontalCenteredControl(new StaticTextControl(aShip.getData().getTitle(), ColorRGBA.White)));
		base.addUI(new HorizontalCenteredControl(new StaticTextControl("Health: " + aShip.getHealth() + "/"
				+ aShip.getMaxHealth(), ColorRGBA.Red)));
		final UIControl stats = new UIControl(BoxDirection.VERTICAL);
		final UIControl abilities = new UIControl(BoxDirection.VERTICAL);
		root.addUI(base);
		root.addFlexSpacer(1);
		root.addUI(stats);
		root.addFlexSpacer(1);
		root.addUI(abilities);
		return root;
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

	public boolean canShoot()
	{
		return aShip.canShoot() && aShip.getPlayer().equals(GameView.getPlayer());
	}

	@Override
	public void delFromGrid()
	{
		aShip.deregisterObserver(this);
		aTrail.removeFromParent();
		super.delFromGrid();
	}

	@Override
	protected void finishedMoving()
	{
		if (aSpriteReady) {
			aTrail.shipMoveEnd();
		}
	}

	public float getMovingSpeed()
	{
		return aGridTranslation.getMovingSpeed();
	}

	public Vector2f getTrailAttachPoint()
	{
		return MathUtils.getVector2fFromPoint(aShip.getData().getTrailOffset()).mult(aBaseSprite.scale);
	}

	@Override
	boolean isMovable()
	{
		return getPropState().equals(PropState.SELECTED) && aShip.getPlayer().equals(GameView.getPlayer());
	}

	@Override
	public void populateTransforms()
	{
		super.populateTransforms();
		if (aSpriteReady) {
			aTrail.shipMove();
		}
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
	public void shipBombed(final Ship ship, final GridLocation bombLocation)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipDestroyed(final Ship ship)
	{
		// TODO: Pretty death animation here
		aPropAlpha.setTargetAlpha(0).setDuration(0.5).start(new Runnable()
		{
			@Override
			public void run()
			{
				delFromGrid();
			}
		});
	}

	@Override
	public void shipJumped(final Ship ship, final EVContainer<Prop> oldContainer, final List<GridLocation> leavingMove,
			final EVContainer<Prop> newContainer, final Portal portal)
	{
		// Warning, hardcore animations ahead
		smoothMoveTo(leavingMove, new Runnable()
		{
			@Override
			public void run()
			{
				final UIProp portalUI = aSolarSystemGrid.getUIProp(portal);
				if (portalUI != null) {
					faceTowards(portalUI.getLocation(), new Runnable()
					{
						@Override
						public void run()
						{
							// The whole node is going to get destroyed at the end of this animation, so we can afford to
							// override the animation parameters here
							final Vector2f origin = getCellCenter();
							final Vector2f portalVec = portalUI.getCellCenter();
							final Vector2f multDelta = portalVec.subtract(origin).mult(10);
							aGridTranslation.setDuration(1);
							aPropAlpha.setDuration(0.35f);
							aGridTranslation.smoothMoveBy(multDelta).start(new Runnable()
							{
								@Override
								public void run()
								{
									delFromGrid();
								}
							});
							aPropAlpha.setTargetAlpha(0).start();
						}
					});
				}
			}
		});
	}

	@Override
	public void shipMoved(final Ship ship, final GridLocation oldLocation, final List<GridLocation> path)
	{
		smoothMoveTo(path, null);
	}

	@Override
	public void shipShot(final Ship ship, final GridLocation shootLocation)
	{
		getSolarSystemGrid().getTrailManager().addNode(
				new UIShipLaser(getCellCenter(), aGrid.getCellCenter(shootLocation), 0.4));
	}

	@Override
	public void shipTookDamage(final Ship ship, final int damageAmount)
	{
		refreshUI();
	}

	@Override
	public void smoothMoveTo(final List<GridLocation> moves, final Runnable callback)
	{
		super.smoothMoveTo(moves, callback);
		if (aSpriteReady) {
			aTrail.shipMoveStart();
		}
	}
}
