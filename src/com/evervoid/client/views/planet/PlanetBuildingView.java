package com.evervoid.client.views.planet;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.ScrollingControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
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

	public PlanetBuildingView(final PlanetView parent, final Planet planet, final int slot)
	{
		super(new UIControl());
		aSlot = slot;
		aSlideIn = getNewTranslationAnimation();
		aBuilding = planet.getBuildingAt(aSlot);
		aPanel = new PanelControl(aBuilding == null ? "(Empty slot)" : aBuilding.getData().getTitle());
		final UIControl rightMargin = new UIControl(BoxDirection.HORIZONTAL);
		aPanelContents = new ScrollingControl();
		aPanelContents.setAutomaticSpacer(4);
		if (aBuilding == null) {
			aPanelContents
					.addUI(new StaticTextControl("This building slot is empty.\nBuilding to build:", ColorRGBA.LightGray));
			final RaceData race = planet.getPlayer().getRaceData();
			for (final String type : race.getBuildings()) {
				final BuildingData data = race.getBuildingData(type);
				aPanelContents.addUI(new BuildableBuildingControl(parent, planet, data));
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
