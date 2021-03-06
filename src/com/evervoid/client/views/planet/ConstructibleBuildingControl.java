package com.evervoid.client.views.planet;

import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.client.views.solar.UIPlanet;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.building.IncrementBuildingConstruction;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.SpriteData;
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
		rest.addUI(new ResourceRow(uiplanet.getPlanet().getState(), null, aData.getCost(), aData.getBuildTime()));
		rest.addFlexSpacer(1);
		addUI(rest, 1);
		setHoverSelectable(true);
		registerClickObserver(this);
	}

	@Override
	public boolean uiClicked(final UIControl clicked)
	{
		try {
			aPlanet.setAction(aSlot, new IncrementBuildingConstruction(aPlanet.getPlanet().getState(), aPlanet.getPlanet(),
					aSlot, aData.getType()));
			return true;
		}
		catch (final IllegalEVActionException e) {
			// Notify player maybe, but this shouldn't happen at all if the UI has been built correctly
			return false;
		}
		finally {
			aParent.refreshSlots(aSlot);
		}
	}
}
