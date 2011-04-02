package com.evervoid.client.views.planet;

import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.ShipData;
import com.evervoid.utils.Pair;
import com.jme3.math.ColorRGBA;

public class SelectableBuildingControl extends UIControl implements ClickObserver
{
	private final PlanetView aParent;
	private final int aSlot;

	public SelectableBuildingControl(final PlanetView parent, final int slot, final Building building)
	{
		super(BoxDirection.HORIZONTAL);
		aParent = parent;
		aSlot = slot;
		if (building == null) {
			addUI(new ImageControl(BuildingData.getBlankBuildingIcon()));
		}
		else {
			addUI(new ImageControl(building.getData().getIcon()));
		}
		addSpacer(16, 1);
		final UIControl rest = new UIControl(BoxDirection.VERTICAL);
		if (building == null) {
			rest.addUI(new StaticTextControl("(Empty slot)", ColorRGBA.Gray));
			rest.addUI(new StaticTextControl("Click to build a building.", ColorRGBA.DarkGray));
		}
		else {
			rest.addUI(new StaticTextControl(building.getData().getTitle(), ColorRGBA.White));
			final Pair<ShipData, Integer> progress = building.getShipProgress();
			rest.addUI(new StaticTextControl("Status: "
					+ (progress == null ? "Idle." : "Building " + progress.getKey().getTitle() + "."), ColorRGBA.LightGray));
		}
		addUI(rest, 1);
		setHoverSelectable(true);
		registerClickObserver(this);
	}

	@Override
	public void uiClicked(final UIControl clicked)
	{
		aParent.openSlot(aSlot);
	}
}
