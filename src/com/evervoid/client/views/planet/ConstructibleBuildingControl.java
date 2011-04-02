package com.evervoid.client.views.planet;

import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.client.views.solar.UIPlanet;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.building.IncrementBuildingConstruction;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.player.ResourceAmount;
import com.jme3.math.ColorRGBA;

public class ConstructibleBuildingControl extends UIControl implements ClickObserver
{
	private final BuildingData aData;
	private final PlanetView aParent;
	private final UIPlanet aPlanet;
	private final int aSlot;

	public ConstructibleBuildingControl(final PlanetView parent, final UIPlanet uiplanet, final int slot,
			final BuildingData data)
	{
		super(BoxDirection.HORIZONTAL);
		aParent = parent;
		aPlanet = uiplanet;
		aSlot = slot;
		aData = data;
		addUI(new VerticalCenteredControl(new ImageControl(data.getIcon())));
		addSpacer(16, 1);
		final UIControl rest = new UIControl(BoxDirection.VERTICAL);
		rest.addFlexSpacer(1);
		rest.addUI(new StaticTextControl(aData.getTitle(), ColorRGBA.White));
		final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
		row.addUI(new VerticalCenteredControl(new StaticTextControl("Cost:", ColorRGBA.White)));
		final ResourceAmount amount = aData.getCost();
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
			aPlanet.setAction(aSlot, new IncrementBuildingConstruction(aPlanet.getPlanet().getState(), aPlanet.getPlanet(),
					aSlot, aData.getType()));
		}
		catch (final IllegalEVActionException e) {
			// Notify player maybe, but this shouldn't happen at all if the UI has been built correctly
		}
		aParent.refreshSlots(aSlot);
	}
}