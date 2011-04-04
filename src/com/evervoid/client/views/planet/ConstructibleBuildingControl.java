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
import com.evervoid.state.action.building.IncrementBuildingConstruction;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.data.SpriteData;
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
		final MultiSprite iconSprite = new MultiSprite();
		iconSprite.addSprite(data.getIcon());
		iconSprite.addSprite(new SpriteData("ui/plus.png", (int) (iconSprite.getWidth() / 2 - 8), (int) (8 - iconSprite
				.getHeight() / 2)));
		addUI(new VerticalCenteredControl(new RescalableControl(iconSprite).setAllowScale(false, false)));
		addSpacer(16, 1);
		final UIControl rest = new UIControl(BoxDirection.VERTICAL);
		rest.addFlexSpacer(1);
		rest.addString(aData.getTitle(), ColorRGBA.White);
		final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
		row.setDefaultTextColor(ColorRGBA.White);
		row.addString("Cost:", BoxDirection.VERTICAL);
		final ResourceAmount amount = aData.getCost();
		for (final String resName : amount.getNames()) {
			if (amount.getValue(resName) <= 0) {
				continue;
			}
			row.addSpacer(12, 1);
			final ResourceData resData = aPlanet.getPlanet().getState().getResourceData(resName);
			row.addUI(new VerticalCenteredControl(new ImageControl(resData.getIcon())));
			row.addSpacer(2, 1);
			row.addString(amount.getFormattedValue(resName), BoxDirection.VERTICAL);
		}
		row.addFlexSpacer(1);
		row.addUI(new VerticalCenteredControl(new ImageControl("icons/resources/time.png")));
		row.addSpacer(4, 1);
		row.addUI(new VerticalCenteredControl(new StaticTextControl(aData.getBuildTime() + " turns", ColorRGBA.White)));
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
