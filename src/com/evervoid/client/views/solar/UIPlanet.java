package com.evervoid.client.views.solar;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.ShadedSprite;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.ui.HorizontalCenteredControl;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.turn.TurnListener;
import com.evervoid.client.views.game.turn.TurnSynchronizer;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.building.IncrementBuildingConstruction;
import com.evervoid.state.action.building.IncrementShipConstruction;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.observers.PlanetObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.state.prop.Planet;
import com.evervoid.utils.Pair;
import com.jme3.math.ColorRGBA;

public class UIPlanet extends UIShadedProp implements PlanetObserver, TurnListener
{
	private final Map<Integer, Action> aBuildingSlotActions = new HashMap<Integer, Action>();
	private Sprite aColorGlowSprite;
	private final Planet aPlanet;

	public UIPlanet(final SolarGrid grid, final Planet planet)
	{
		super(grid, planet.getLocation(), planet);
		aPlanet = planet;
		buildProp();
		aPlanet.registerObserver(this);
		GameView.registerTurnListener(this);
	}

	@Override
	public void buildingsChanged(final Planet planet)
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void buildSprite()
	{
		final ShadedSprite shade = new ShadedSprite(getPlanetSprite());
		addSprite(shade);
		setShade(shade);
		aColorGlowSprite = new Sprite(aPlanet.getData().getGlowSprite());
		refreshGlowColor();
	}

	@Override
	public void delFromGrid()
	{
		aPlanet.deregisterObserver(this);
	}

	@Override
	protected void finishedMoving()
	{
		// Nothing
	}

	/**
	 * @param slot
	 *            A building slot
	 * @return The BuildingData that the UIPlanet is currently building on the specified slot, or null if the UIPlanet isn't
	 *         building a building on this slot
	 */
	public BuildingData getConstructingBuildingDataOnSlot(final int slot)
	{
		final Action act = aBuildingSlotActions.get(slot);
		if (act == null || !(act instanceof IncrementBuildingConstruction)) {
			return null;
		}
		return ((IncrementBuildingConstruction) act).getBuildingData();
	}

	/**
	 * @param slot
	 *            A building slot
	 * @return The ShipData that the UIPlanet is currently building on the specified slot, or null if the UIPlanet isn't
	 *         building a ship on this slot
	 */
	public ShipData getConstructingShipDataOnSlot(final int slot)
	{
		final Action act = aBuildingSlotActions.get(slot);
		if (act == null || !(act instanceof IncrementShipConstruction)) {
			return null;
		}
		return ((IncrementShipConstruction) act).getShipData();
	}

	@Override
	protected UIControl getPanelUI()
	{
		// create all controls
		final UIControl root = new UIControl(BoxDirection.HORIZONTAL);
		final UIControl base = new UIControl(BoxDirection.VERTICAL);
		final UIControl stats = new UIControl(BoxDirection.VERTICAL);
		final UIControl action = new UIControl(BoxDirection.VERTICAL);
		// fill base control
		base.addUI(new RescalableControl(getPlanetSprite()), 1);
		base.addUI(new HorizontalCenteredControl(new StaticTextControl(aPlanet.getData().getTitle(), ColorRGBA.White)));
		final Player owner = aPlanet.getPlayer();
		if (owner.isNullPlayer()) {
			base.addUI(new HorizontalCenteredControl(new StaticTextControl("Neutral", ColorRGBA.LightGray)));
		}
		else {
			base.addUI(new HorizontalCenteredControl(new StaticTextControl("Owned by " + owner.getNickname(), GraphicsUtils
					.getColorRGBA(aPlanet.getPlayer().getColor()))));
		}
		// fill stats control
		stats.addUI(new StaticTextControl("Health: " + aPlanet.getCurrentHealth() + "/" + aPlanet.getMaxHealth(), ColorRGBA.Red));
		if (aPlanet.getMaxShields() != 0) {
			stats.addUI(new StaticTextControl("Shields: " + aPlanet.getCurrentShields() + "/" + aPlanet.getMaxShields(),
					ColorRGBA.Red));
		}
		stats.addFlexSpacer(1);
		stats.addUI(new StaticTextControl("Resources:", ColorRGBA.White));
		final ResourceAmount amount = aPlanet.getResourceRate();
		for (final String resName : amount.getNames()) {
			if (amount.getValue(resName) <= 0) {
				continue;
			}
			final ResourceData data = aPlanet.getState().getResourceData(resName);
			final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
			row.addUI(new VerticalCenteredControl(new ImageControl(data.getIcon())));
			row.addSpacer(4, 1);
			row.addUI(new VerticalCenteredControl(new StaticTextControl(data.getTitle() + ": "
					+ amount.getFormattedValue(resName), ColorRGBA.White)));
			stats.addUI(row);
		}
		stats.addFlexSpacer(1);
		if (aPlanet.getPlayer().equals(GameView.getLocalPlayer())) {
			// this is player sensitive information, only display it if the prop belongs to local player
			// TODO maybe add an isGameOver clause to the above
			for (final int slot : aPlanet.getBuildings().keySet()) {
				final Building building = aPlanet.getBuildingAt(slot);
				if (building == null) {
					continue;
				}
				final Pair<ShipData, Integer> shipProgress = building.getShipProgress();
				if (shipProgress == null) {
					stats.addUI(new VerticalCenteredControl(new StaticTextControl("Not buidling a ship", ColorRGBA.Red)));
				}
				else {
					stats.addUI(new VerticalCenteredControl(
							new StaticTextControl("Building ship " + shipProgress.getKey().getTitle() + ", "
									+ shipProgress.getValue() + " turns left ", ColorRGBA.Red)));
				}
			}
			stats.addFlexSpacer(1);
			// build action subsection
			boolean idle = true;
			for (int slot = 0; slot < aPlanet.getData().getNumOfBuildingSlots(); slot++) {
				if (aBuildingSlotActions.get(slot) != null) {
					idle = false;
					break;
				}
			}
			action.addUI(new StaticTextControl("Status:\n" + (idle ? "Idle" : "Building"), ColorRGBA.White));
			action.addFlexSpacer(1);
			// add them all to the root
		}
		root.addUI(base);
		root.addFlexSpacer(1);
		root.addUI(stats);
		root.addFlexSpacer(1);
		root.addUI(action);
		return root;
	}

	public Planet getPlanet()
	{
		return aPlanet;
	}

	private SpriteData getPlanetSprite()
	{
		return aPlanet.getData().getBaseSprite();
	}

	@Override
	boolean isSelectable()
	{
		return true;
	}

	@Override
	public void planetCaptured(final Planet planet, final Player player)
	{
		refreshUI();
		refreshGlowColor();
	}

	private void refreshGlowColor()
	{
		if (aPlanet.getPlayer().isNullPlayer()) {
			delSprite(aColorGlowSprite);
		}
		else {
			aColorGlowSprite.setHue(GraphicsUtils.getColorRGBA(aPlanet.getPlayer().getColor()));
			addSprite(aColorGlowSprite);
		}
	}

	public void setAction(final int slot, final Action action)
	{
		if (!getPlanet().hasSlot(slot)) {
			return; // Invalid slot
		}
		// Check if action being committed is the same as the one we already had
		final Action previous = aBuildingSlotActions.get(slot);
		if ((previous == null && action == null) || (previous != null && previous.equals(action))) {
			return; // No change
		}
		if (previous != null) {
			GameView.delAction(previous);
		}
		if (action != null && action.isValid()) {
			aBuildingSlotActions.put(slot, action);
		}
		else {
			aBuildingSlotActions.put(slot, null);
		}
		GameView.addAction(action);
		refreshUI();
	}

	@Override
	public void turnPlayedback()
	{
		final int totalSlots = getPlanet().getData().getNumOfBuildingSlots();
		for (int slot = 0; slot < totalSlots; slot++) {
			final Action act = aBuildingSlotActions.get(slot);
			if (act instanceof IncrementBuildingConstruction) {
				final IncrementBuildingConstruction inc = (IncrementBuildingConstruction) act;
				if (inc.shouldContinueBuilding()) {
					aBuildingSlotActions.put(slot, inc.clone());
				}
				else {
					aBuildingSlotActions.remove(slot);
				}
			}
			else if (act instanceof IncrementShipConstruction) {
				// TODO: Do it; make sure to put null if construction is done
				aBuildingSlotActions.remove(slot);
			}
			else {
				aBuildingSlotActions.remove(slot);
			}
			if (aBuildingSlotActions.get(slot) != null) {
				GameView.addAction(aBuildingSlotActions.get(slot));
			}
		}
		refreshUI();
	}

	@Override
	public void turnReceived(final TurnSynchronizer synchronizer)
	{
		// Nothing
	}

	@Override
	public void turnSent()
	{
		// Nothing
	}
}
