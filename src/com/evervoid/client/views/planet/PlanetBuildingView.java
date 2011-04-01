package com.evervoid.client.views.planet;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.HorizontalCenteredControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.ScrollingControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
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
	private Vector2f aSlideOutOffset;

	public PlanetBuildingView(final PlanetView parent, final Planet planet)
	{
		super(new UIControl());
		aSlideIn = getNewTranslationAnimation();
		aPanel = new PanelControl("Buildings");
		final UIControl leftMargin = new UIControl(BoxDirection.HORIZONTAL);
		leftMargin.addSpacer(4, 0);
		aBuildingList = new ScrollingControl();
		if (planet.getBuildings().isEmpty()) {
			aBuildingList.addUI(aBuildingList.addUI(new HorizontalCenteredControl(new StaticTextControl("(No buildings)",
					new ColorRGBA(0.6f, 0.6f, 0.6f, 1f)))));
		}
		else {
			for (final int slot : planet.getBuildings().keySet()) {
				final Building b = planet.getBuildingAt(slot);
				aBuildingList.addUI(new SelectableBuildingControl(b)); // Handles null case
			}
		}
		leftMargin.addUI(aBuildingList, 1);
		aPanel.addUI(leftMargin, 1);
		addUI(aPanel, 1);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		if (aSlideIn != null) {
			aSlideOutOffset = new Vector2f(-bounds.width, 0);
			aSlideIn.setTranslationNow(aSlideOutOffset);
			super.setBounds(new Bounds(bounds.x - aPanel.getLeftMargin(), bounds.y, bounds.width, bounds.height));
		}
		System.out.println(aPanel);
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
