package com.evervoid.client.views.planet;

import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.ShipData;
import com.evervoid.utils.Pair;
import com.jme3.math.ColorRGBA;

public class SelectableBuildingControl extends UIControl
{
	public SelectableBuildingControl(final Building building)
	{
		super(BoxDirection.HORIZONTAL);
		addUI(new ImageControl("buildings/generic.png"));
		addSpacer(16, 1);
		final UIControl rest = new UIControl(BoxDirection.VERTICAL);
		rest.addUI(new StaticTextControl(building.getData().getTitle(), ColorRGBA.White));
		final Pair<ShipData, Integer> progress = building.getShipProgress();
		rest.addUI(new StaticTextControl("Status: "
				+ (progress == null ? "Idle." : "Building " + progress.getKey().getTitle() + "."), ColorRGBA.LightGray));
		addUI(rest, 1);
	}
}
