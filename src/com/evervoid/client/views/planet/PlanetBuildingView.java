package com.evervoid.client.views.planet;

import java.util.List;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.PanelControl;
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
import com.evervoid.state.prop.Planet;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class PlanetBuildingView extends EverUIView
{
	private final Building aBuilding;
	private final PanelControl aPanel;
	private final ScrollingControl aPanelContents;
	private final AnimatedTranslation aSlideIn;
	private Vector2f aSlideOutOffset;
	private final int aSlot;

	public PlanetBuildingView(final PlanetView parent, final UIPlanet uiplanet, final int slot)
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
		if (aBuilding == null && builddata == null) {
			aPanelContents
					.addUI(new StaticTextControl("This building slot is empty.\nBuilding to build:", ColorRGBA.LightGray));
			final RaceData race = planet.getPlayer().getRaceData();
			for (final String type : race.getBuildings()) {
				final BuildingData data = race.getBuildingData(type);
				aPanelContents.addUI(new ConstructibleBuildingControl(parent, uiplanet, slot, data));
			}
			// TODO: Show more building stats somewhere (probably panel) so that the player knows what the building is for
		}
		else if (aBuilding == null || !aBuilding.isComplete()) {
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
			aPanelContents.addUI(new StaticTextControl("This building is under construction.", ColorRGBA.LightGray));
			// TODO: Add progress bar here
			aPanelContents.addUI(new StaticTextControl("Progress: " + percentage, ColorRGBA.LightGray));
		}
		else {
			// Building is completely built
			// TODO: Check if ship is being built
			builddata = aBuilding.getData();
			final List<String> shipTypes = builddata.getAvailableShipTypes();
			if (shipTypes.isEmpty()) {
				aPanelContents.addUI(new StaticTextControl("This building cannot create ships.", ColorRGBA.Gray));
			}
			else {
				aPanelContents.addUI(new StaticTextControl("Ship to build:", ColorRGBA.LightGray));
				final RaceData race = planet.getPlayer().getRaceData();
				for (final String type : shipTypes) {
					aPanelContents.addUI(new ConstructibleShipControl(parent, uiplanet, slot, race.getShipData(type)));
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
