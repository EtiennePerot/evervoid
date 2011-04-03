package com.evervoid.client.views.planet;

import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.client.views.solar.UIPlanet;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.building.IncrementShipConstruction;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.player.ResourceAmount;
import com.jme3.math.ColorRGBA;

public class ConstructibleShipControl extends UIControl implements ClickObserver
{
	private final Building aBuilding;
	private final ShipData aData;
	private final PlanetView aParent;
	private final UIPlanet aPlanet;

	public ConstructibleShipControl(final PlanetView parent, final UIPlanet uiplanet, final Building building,
			final ShipData data)
	{
		super(BoxDirection.HORIZONTAL);
		aParent = parent;
		aPlanet = uiplanet;
		aBuilding = building;
		aData = data;
		final SpriteData baseSpriteData = aData.getIconSprite();
		final MultiSprite shipSprite = new MultiSprite();
		shipSprite.addSprite(baseSpriteData);
		shipSprite.addSprite(new SpriteData("ui/plus.png", (int) (shipSprite.getWidth() / 2 - 8), (int) (8 - shipSprite
				.getHeight() / 2)));
		final RescalableControl baseSprite = new RescalableControl(shipSprite);
		baseSprite.setEnforcedDimension((int) (32 * baseSpriteData.scale), (int) (32 * baseSpriteData.scale));
		addUI(new VerticalCenteredControl(baseSprite));
		addSpacer(16, 1);
		final UIControl rest = new UIControl(BoxDirection.VERTICAL);
		rest.addFlexSpacer(1);
		rest.addUI(new StaticTextControl(aData.getTitle(), ColorRGBA.White));
		final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
		row.addUI(new VerticalCenteredControl(new StaticTextControl("Cost:", ColorRGBA.White)));
		final ResourceAmount amount = aData.getBaseCost();
		for (final String resName : amount.getNames()) {
			if (amount.getValue(resName) <= 0) {
				continue;
			}
			row.addSpacer(12, 1);
			final ResourceData resData = aPlanet.getPlanet().getState().getResourceData(resName);
			row.addUI(new VerticalCenteredControl(new ImageControl(resData.getIcon())));
			row.addSpacer(2, 1);
			row.addUI(new VerticalCenteredControl(new StaticTextControl(amount.getFormattedValue(resName), ColorRGBA.White)));
		}
		row.addFlexSpacer(1);
		row.addUI(new VerticalCenteredControl(new ImageControl("ui/time.png")));
		row.addSpacer(4, 1);
		row.addUI(new VerticalCenteredControl(new StaticTextControl(aData.getBaseBuildTime() + " turns", ColorRGBA.White)));
		rest.addUI(row);
		rest.addFlexSpacer(1);
		addUI(rest, 1);
		setHoverSelectable(true);
		registerClickObserver(this);
	}

	@Override
	public void uiClicked(final UIControl clicked)
	{
		try {
			aPlanet.setAction(aBuilding.getSlot(), new IncrementShipConstruction(aPlanet.getPlanet().getState(), aBuilding,
					aData));
		}
		catch (final IllegalEVActionException e) {
			// Notify player maybe, but this shouldn't happen at all if the UI has been built correctly
		}
		aParent.refreshSlots(aBuilding.getSlot());
	}
}
