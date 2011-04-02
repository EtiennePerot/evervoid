package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.ShadedSprite;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ClickObserver;
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
import com.evervoid.state.action.planet.PlanetAction;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.observers.PlanetObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.state.prop.Planet;
import com.evervoid.utils.Pair;
import com.jme3.math.ColorRGBA;

public class UIPlanet extends UIShadedProp implements PlanetObserver, ClickObserver, TurnListener
{
	private PlanetAction aActionToCommit;
	private final ButtonControl aCancelActionButton;
	private final Planet aPlanet;

	public UIPlanet(final SolarGrid grid, final Planet planet)
	{
		super(grid, planet.getLocation(), planet);
		aPlanet = planet;
		buildProp();
		aPlanet.registerObserver(this);
		GameView.registerTurnListener(this);
		// created cancel button
		aCancelActionButton = new ButtonControl("Cancel");
		aCancelActionButton.registerClickObserver(this);
	}

	@Override
	public void buildingsChanged(final Planet planet)
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected UIControl buildPanelUI()
	{
		// create all controls
		final UIControl root = new UIControl(BoxDirection.HORIZONTAL);
		final UIControl base = new UIControl(BoxDirection.VERTICAL);
		final UIControl stats = new UIControl(BoxDirection.VERTICAL);
		final UIControl action = new UIControl(BoxDirection.VERTICAL);
		// fill base control
		base.addUI(new RescalableControl(getPlanetSprite()), 1);
		base.addUI(new HorizontalCenteredControl(new StaticTextControl(aPlanet.getData().getTitle(), ColorRGBA.White)));
		base.addUI(new HorizontalCenteredControl(new StaticTextControl("Owned by " + aPlanet.getPlayer().getNickname(),
				GraphicsUtils.getColorRGBA(aPlanet.getPlayer().getColor()))));
		// fill stats control
		stats.addUI(new StaticTextControl("Health: " + aPlanet.getCurrentHealth() + "/" + aPlanet.getMaxHealth(), ColorRGBA.Red));
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
			action.addUI(new StaticTextControl("Current Action:", ColorRGBA.White));
			action.addUI(new StaticTextControl(aActionToCommit != null ? "  " + aActionToCommit.getDescription() : "  None",
					ColorRGBA.Red));
			aCancelActionButton.setEnabled(aActionToCommit != null);
			action.addUI((new UIControl(BoxDirection.HORIZONTAL)).addFlexSpacer(1).addUI(aCancelActionButton));
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

	@Override
	protected void buildSprite()
	{
		final ShadedSprite shade = new ShadedSprite(getPlanetSprite());
		addSprite(shade);
		setShade(shade);
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
	}

	void setAction(final PlanetAction action)
	{
		// Check if action being committed is the same as the one we already had
		if ((action == null && aActionToCommit == null)
				|| (action != null && aActionToCommit != null && action.equals(aActionToCommit))) {
			return;
		}
		if (action != null && !action.isValid()) {
			return; // Invalid action
		}
		// If it's not, then let's update the action
		if (aActionToCommit != null) {
			// If there was an action previously, remove it
			GameView.delAction(aActionToCommit);
			aActionToCommit = null;
		}
		// Now put the new action in place
		aActionToCommit = action;
		// show the action in the UIPanel
		refreshUI();
		if (aActionToCommit == null) {
			// Putting a null action -> do nothing
			return;
		}
		// Putting a non-null action -> Add it to GameView
		GameView.addAction(aActionToCommit);
	}

	@Override
	public void turnPlayedback()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void turnReceived(final TurnSynchronizer synchronizer)
	{
		// reset that held action
		setAction(null);
	}

	@Override
	public void turnSent()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void uiClicked(final UIControl clicked)
	{
		if (clicked.equals(aCancelActionButton)) {
			setAction(null);
		}
	}
}
