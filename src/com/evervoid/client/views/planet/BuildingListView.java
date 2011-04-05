package com.evervoid.client.views.planet;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.ScrollingControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.client.views.solar.UIPlanet;
import com.evervoid.state.building.Building;
import com.evervoid.state.prop.Planet;
import com.jme3.math.Vector2f;

public class BuildingListView extends EverUIView
{
	private final ScrollingControl aBuildingList;
	private final PanelControl aPanel;
	private final PlanetView aParent;
	private final Planet aPlanet;
	private final AnimatedTranslation aSlideIn;
	private Vector2f aSlideOutOffset;
	private final UIPlanet aUIPlanet;

	public BuildingListView(final PlanetView parent, final UIPlanet uiplanet)
	{
		super(new UIControl());
		aParent = parent;
		aSlideIn = getNewTranslationAnimation();
		aPanel = new PanelControl("Buildings");
		final UIControl leftMargin = new UIControl(BoxDirection.HORIZONTAL);
		leftMargin.addSpacer(4, 0);
		aBuildingList = new ScrollingControl();
		aBuildingList.setAutomaticSpacer(4);
		aUIPlanet = uiplanet;
		aPlanet = aUIPlanet.getPlanet();
		refreshUI();
		leftMargin.addUI(aBuildingList, 1);
		aPanel.addUI(leftMargin, 1);
		addUI(aPanel, 1);
	}

	void refreshUI()
	{
		aBuildingList.delAllChildUIs();
		for (final int slot : aPlanet.getBuildings().keySet()) {
			final Building b = aPlanet.getBuildingAt(slot);
			aBuildingList.addUI(new SelectableBuildingControl(aParent, aUIPlanet, slot, b)); // Handles null cases and all
		}
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		if (aSlideIn != null) {
			aSlideOutOffset = new Vector2f(-bounds.width, 0);
			aSlideIn.setTranslationNow(aSlideOutOffset);
			super.setBounds(new Bounds(bounds.x - aPanel.getLeftMargin(), bounds.y, bounds.width, bounds.height));
		}
	}

	public void slideIn(final float duration)
	{
		aSlideIn.smoothMoveTo(new Vector2f(0, 0)).setDuration(duration).start();
	}

	public void slideOut(final float duration)
	{
		aSlideIn.smoothMoveTo(aSlideOutOffset).setDuration(duration).start();
	}
}
