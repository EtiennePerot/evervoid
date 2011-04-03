package com.evervoid.client.views.planet;

import java.util.List;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.ScrollingControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.client.views.solar.UIPlanet;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.prop.Planet;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class BuildingView extends EverUIView
{
	private final Building aBuilding;
	private final PanelControl aPanel;
	private final ScrollingControl aPanelContents;
	private final AnimatedTranslation aSlideIn;
	private Vector2f aSlideOutOffset;
	private final int aSlot;

	public BuildingView(final PlanetView parent, final UIPlanet uiplanet, final int slot)
	{
		super(new UIControl());
		aSlot = slot;
		aSlideIn = getNewTranslationAnimation();
		final Planet planet = uiplanet.getPlanet();
		BuildingData builddata = uiplanet.getConstructingBuildingDataOnSlot(aSlot);
		aBuilding = planet.getBuildingAt(aSlot);
		aPanel = new PanelControl(aBuilding == null ? "(Empty slot #" + (slot + 1) + ")" : aBuilding.getData().getTitle());
		final UIControl rightMargin = new UIControl(BoxDirection.HORIZONTAL);
		aPanelContents = new ScrollingControl();
		aPanelContents.setAutomaticSpacer(4);
		aPanelContents.setDefaultTextColor(ColorRGBA.LightGray);
		if (aBuilding == null && builddata == null) {
			aPanelContents.addString("This building slot is empty.\nBuilding to build:");
			final RaceData race = planet.getPlayer().getRaceData();
			for (final String type : race.getBuildings()) {
				final BuildingData data = race.getBuildingData(type);
				aPanelContents.addUI(new ConstructibleBuildingControl(parent, uiplanet, slot, data));
			}
			// TODO: Show more building stats somewhere (probably panel) so that the player knows what the building is for
		}
		else if (aBuilding == null || !aBuilding.isBuildingComplete()) {
			// Currently building (if aBuilding == null, then the building process hasn't started state-side yet)
			final String percentage = aBuilding == null ? "0%" : aBuilding.getBuildingProgressPercentage();
			if (aBuilding == null) {
				if (builddata != null) {
					aPanel.getTitleBox().addUI(new ImageControl(builddata.getIcon()));
				}
			}
			else {
				aPanel.getTitleBox().addUI(new ImageControl(aBuilding.getData().getIcon()));
			}
			aPanelContents.addString("This building is under construction.");
			// TODO: Add progress bar here
			aPanelContents.addString("Progress: " + percentage);
		}
		else {
			// Ship is non-null and fully built
			aPanel.getTitleBox().addUI(new ImageControl(aBuilding.getData().getIcon()));
			ShipData beingBuilt = uiplanet.getConstructingShipDataOnSlot(aSlot);
			if (aBuilding.isBuildingShip() || beingBuilt != null) {
				// Building is currently building a ship (or planning to build one)
				if (beingBuilt == null) {
					beingBuilt = aBuilding.getShipCurrentlyBuilding();
				}
				aPanelContents.addString("This ship is under construction.");
				// TODO: Add progress bar here
				aPanelContents.addString("Progress: " + aBuilding.getShipConstructionPercentage());
				aPanelContents.addSpacer(1, 16);
				aPanelContents.addUI(new RescalableControl(beingBuilt.getBaseSprite()).setAllowScale(false, false));
			}
			else {
				// Building is idle, display buildable ships
				builddata = aBuilding.getData();
				final List<String> shipTypes = builddata.getAvailableShipTypes();
				if (shipTypes.isEmpty()) {
					aPanelContents.addUI(new StaticTextControl("This building cannot create ships.", ColorRGBA.Gray));
				}
				else {
					aPanelContents.addUI(new StaticTextControl("Ship to build:", ColorRGBA.LightGray));
					final RaceData race = planet.getPlayer().getRaceData();
					for (final String type : shipTypes) {
						aPanelContents.addUI(new ConstructibleShipControl(parent, uiplanet, aBuilding, race.getShipData(type)));
					}
					// TODO: Show more ship stats somewhere (probably panel) so that the player knows what the ship is for
				}
			}
		}
		rightMargin.addUI(aPanelContents, 1);
		rightMargin.addSpacer(4, 0);
		aPanel.addUI(rightMargin, 1);
		addUI(aPanel, 1);
	}

	public int getSlot()
	{
		return aSlot;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		if (aSlideIn != null) {
			aSlideOutOffset = new Vector2f(bounds.width, 0);
			aSlideIn.setTranslationNow(aSlideOutOffset);
			super.setBounds(new Bounds(bounds.x + aPanel.getRightMargin(), bounds.y, bounds.width, bounds.height));
		}
	}

	public void slideIn(final float duration)
	{
		aSlideIn.smoothMoveTo(new Vector2f(0, 0)).setDuration(duration).start();
	}

	public void slideOut(final float duration, final Runnable callback)
	{
		aSlideIn.smoothMoveTo(aSlideOutOffset).setDuration(duration).start(callback);
	}
}
