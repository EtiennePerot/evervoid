package com.evervoid.client.views.solar;

import java.util.List;

import com.evervoid.client.graphics.Colorable;
import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.Shade;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedTransform.DurationMode;
import com.evervoid.client.ui.HorizontalCenteredControl;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.turn.TurnListener;
import com.evervoid.client.views.game.turn.TurnSynchronizer;
import com.evervoid.state.EVContainer;
import com.evervoid.state.action.ship.JumpShipIntoPortal;
import com.evervoid.state.action.ship.MoveShip;
import com.evervoid.state.action.ship.ShipAction;
import com.evervoid.state.action.ship.ShootShip;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.data.TrailData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.observers.ShipObserver;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.ShipPath;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class UIShip extends UIShadedProp implements Colorable, ShipObserver, TurnListener
{
	private static final float sActionUIIndicationDuration = 0.7f;
	private EverNode aActionNode = null;
	private ShipAction aActionToCommit = null;
	private final SpriteData aBaseSprite;
	private Sprite aColorableSprite;
	/**
	 * If true, this ship is frozen until we received a turn
	 */
	private boolean aFrozen = false;
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
		aBaseSprite = aShip.getData().getBaseSprite();
		buildProp();
		aGridTranslation.setDuration(ship.getData().getMovingTime());
		// Set rotation speed and mode:
		aFaceTowards.setSpeed(ship.getData().getRotationSpeed()).setDurationMode(DurationMode.CONTINUOUS);
		setHue(GraphicsUtils.getColorRGBA(ship.getColor()));
		ship.registerObserver(this);
		GameView.registerTurnListener(this);
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
		base.addUI(new RescalableControl(buildShipSprite(new MultiSprite(), false)), 1);
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

	/**
	 * Builds a ship sprite and attaches it to a MultiSprite.
	 * 
	 * @param base
	 *            The MultiSprite to build inside
	 * @param bindToInstance
	 *            Whether to attach the sprites created to the current UIShip instance
	 * @param bottomLeftAsOrigin
	 *            Whether to use the bottom left corner as origin or not
	 * @return
	 */
	private MultiSprite buildShipSprite(final MultiSprite base, final boolean bindToInstance)
	{
		final Sprite baseSprite = new Sprite(aBaseSprite);
		base.addSprite(baseSprite);
		final Sprite colorOverlay = new Sprite(aShip.getData().getColorOverlay());
		base.addSprite(colorOverlay);
		if (bindToInstance) {
			aColorableSprite = colorOverlay;
		}
		colorOverlay.setHue(aColorableSprite.getHue());
		final Point engineOffset = aShip.getData().getEngineOffset();
		final TrailData trailInfo = aShip.getTrailData();
		base.addSprite(new Sprite(trailInfo.engineSprite, engineOffset.x, engineOffset.y));
		return base;
	}

	@Override
	protected void buildSprite()
	{
		buildShipSprite(aSprite, true);
		final TrailData trailInfo = aShip.getTrailData();
		switch (trailInfo.trailKind) {
			case BUBBLE:
				aTrail = new UIShipBubbleTrail(this, trailInfo.baseSprite, trailInfo.distanceInterval, trailInfo.decayTime);
				break;
			case GRADUAL:
				aTrail = new UIShipLinearTrail(this, trailInfo.trailSprites);
				break;
		}
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
		GameView.deregisterTurnListener(this);
		aTrail.removeFromParent();
		if (aActionNode != null) {
			aActionNode.smoothDisappear(sActionUIIndicationDuration);
		}
		super.delFromGrid();
	}

	@Override
	protected void finishedMoving()
	{
		if (aSpriteReady) {
			aTrail.shipMoveEnd();
		}
	}

	public EverNode getGridAnimationNode()
	{
		return getSolarSystemGrid().getGridAnimationNode();
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
		return !aFrozen && getPropState().equals(PropState.SELECTED) && aShip.getPlayer().equals(GameView.getPlayer());
	}

	@Override
	public void populateTransforms()
	{
		super.populateTransforms();
		if (aSpriteReady) {
			aTrail.shipMove();
		}
	}

	void setAction(final ShipAction action)
	{
		// Check if action being committed is the same as the one we already had
		if ((action == null && aActionToCommit == null)
				|| (action != null && aActionToCommit != null && action.equals(aActionToCommit))) {
			return;
		}
		// If it's not, then let's update the action
		if (aActionToCommit != null) {
			// If there was an action previously, remove it
			GameView.delAction(aActionToCommit);
			aActionToCommit = null;
		}
		if (aActionNode != null) {
			// If there were UI indications of the action, delete them
			aActionNode.smoothDisappear(sActionUIIndicationDuration);
			aActionNode = null;
		}
		// Now put the new action in place
		aActionToCommit = action;
		if (aActionToCommit == null) {
			// Putting a null action -> do nothing
			return;
		}
		// Putting a non-null action -> Add it to GameView
		GameView.addAction(aActionToCommit);
		if (aActionToCommit instanceof MoveShip) {
			final List<GridLocation> path = ((MoveShip) aActionToCommit).getSamplePath();
			aActionNode = new ActionLine(aGrid, 1f, new ColorRGBA(.8f, .8f, 1f, 0.5f), aGridLocation, path);
			faceTowards(path.get(0));
		}
		else if (aActionToCommit instanceof JumpShipIntoPortal) {
			final GridLocation portal = ((JumpShipIntoPortal) aActionToCommit).getPortal().getLocation();
			final List<GridLocation> path = ((JumpShipIntoPortal) aActionToCommit).getUnderlyingMove().getSamplePath();
			aActionNode = new ActionLine(aGrid, 1f, new ColorRGBA(.8f, 1, .8f, .5f), aGridLocation, path, portal);
			faceTowards(path.get(0));
		}
		else if (aActionToCommit instanceof ShootShip) {
			final GridLocation enemy = ((ShootShip) aActionToCommit).getTarget().getLocation();
			aActionNode = new ActionLine(aGrid, 1f, new ColorRGBA(1, .8f, .8f, .5f), aGridLocation, enemy);
			faceTowards(enemy);
		}
		// TODO: Add more actions here
		if (aActionNode != null) {
			getGridAnimationNode().addNode(aActionNode);
			aActionNode.smoothAppear(sActionUIIndicationDuration);
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
		// FIXME: Using aPropAlpha doesn't work here. .start gets called twice for the same UIShip, which results in the
		// callback being run synchronously
		getNewAlphaAnimation().setTargetAlpha(0).setDuration(0.5).start(new Runnable()
		{
			@Override
			public void run()
			{
				delFromGrid();
			}
		}, true, false);
	}

	@Override
	public void shipJumped(final Ship ship, final EVContainer<Prop> oldContainer, final ShipPath leavingMove,
			final EVContainer<Prop> newContainer, final Portal portal)
	{
		// Warning, hardcore animations ahead
		smoothMoveTo(leavingMove.getPath(), new Runnable()
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
							aPropAlpha.setDuration(0.35);
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
	public void shipMoved(final Ship ship, final GridLocation oldLocation, final ShipPath path)
	{
		// Do nothing! The TurnSynchronizer will take care of the movement.
	}

	@Override
	public void shipShot(final Ship ship, final GridLocation shootLocation)
	{
		getSolarSystemGrid().getGridAnimationNode().addNode(
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

	@Override
	public void turnPlayedback()
	{
		aFrozen = false;
	}

	@Override
	public void turnReceived(final TurnSynchronizer synchronizer)
	{
		aActionToCommit = null;
		if (aActionNode != null) {
			aActionNode.smoothDisappear(sActionUIIndicationDuration);
			aActionNode = null;
		}
		synchronizer.registerShip(aShip, this);
	}

	@Override
	public void turnSent()
	{
		aFrozen = true;
	}
}
