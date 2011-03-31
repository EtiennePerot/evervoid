package com.evervoid.client.views.planet;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.ScrollingControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.state.building.Building;
import com.evervoid.state.prop.Planet;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class PlanetBuildingView extends EverUIView
{
	private final ScrollingControl aBuildingList;
	private final PanelControl aPanel;
	private final AnimatedTranslation aSlideIn;

	public PlanetBuildingView(final PlanetView parent, final Planet planet)
	{
		super(new UIControl());
		aSlideIn = getNewTranslationAnimation();
		aPanel = new PanelControl("Buildings");
		aBuildingList = new ScrollingControl();
		for (final Building building : planet.getBuildings()) {
			// TODO: Make fancier
			aBuildingList.addUI(new StaticTextControl(building.getType(), ColorRGBA.White));
		}
		aPanel.addUI(aBuildingList, 1);
		addUI(aPanel, 1);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		if (aSlideIn != null) {
			aSlideIn.setTranslationNow(-bounds.width, 0, 0);
			super.setBounds(new Bounds(bounds.x - aPanel.getLeftMargin(), bounds.y, bounds.width, bounds.height));
		}
	}

	public void slideIn(final float duration)
	{
		aSlideIn.smoothMoveTo(new Vector2f(0, 0)).setDuration(duration).start();
	}
}
