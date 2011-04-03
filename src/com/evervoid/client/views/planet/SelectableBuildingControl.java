package com.evervoid.client.views.planet;

import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.solar.UIPlanet;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.ShipData;
import com.evervoid.utils.Pair;
import com.jme3.math.ColorRGBA;

public class SelectableBuildingControl extends UIControl implements ClickObserver
{
	private final PlanetView aParent;
	private final int aSlot;

	public SelectableBuildingControl(final PlanetView parent, final UIPlanet uiplanet, final int slot, final Building building)
	{
		super(BoxDirection.HORIZONTAL);
		aParent = parent;
		aSlot = slot;
		BuildingData builddata = null;
		if (building == null) {
			builddata = uiplanet.getConstructingBuildingDataOnSlot(aSlot);
		}
		else {
			builddata = building.getData();
		}
		if (builddata == null) {
			addUI(new ImageControl(BuildingData.getBlankBuildingIcon()));
		}
		else {
			addUI(new ImageControl(builddata.getIcon()));
		}
		addSpacer(16, 1);
		final UIControl rest = new UIControl(BoxDirection.VERTICAL);
		if (builddata == null) {
			rest.addUI(new StaticTextControl("(Empty slot #" + (slot + 1) + ")", ColorRGBA.Gray));
			rest.addUI(new StaticTextControl("Click to build a building.", ColorRGBA.Gray));
		}
		else {
			rest.addUI(new StaticTextControl(builddata.getTitle(), ColorRGBA.White));
			if (building != null && building.isBuildingComplete()) {
				final Pair<ShipData, Integer> progress = building.getShipProgress();
				rest.addUI(new StaticTextControl("Status: "
						+ (progress == null ? "Idle." : "Building " + progress.getKey().getTitle() + "."), ColorRGBA.LightGray));
			}
			else {
				rest.addUI(new StaticTextControl("Under construction...", ColorRGBA.LightGray));
			}
		}
		addUI(rest, 1);
		setHoverSelectable(true);
		registerClickObserver(this);
	}

	@Override
	public void uiClicked(final UIControl clicked)
	{
		aParent.openSlot(aSlot, false);
	}
}
