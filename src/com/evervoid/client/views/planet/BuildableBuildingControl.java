package com.evervoid.client.views.planet;

import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.state.prop.Planet;
import com.jme3.math.ColorRGBA;

public class BuildableBuildingControl extends UIControl implements ClickObserver
{
	private final PlanetView aParent;

	public BuildableBuildingControl(final PlanetView parent, final Planet planet, final BuildingData data)
	{
		super(BoxDirection.HORIZONTAL);
		aParent = parent;
		// FIXME: Use non-generic building icons when we have them
		addUI(new VerticalCenteredControl(new ImageControl("buildings/generic.png")));
		addSpacer(16, 1);
		final UIControl rest = new UIControl(BoxDirection.VERTICAL);
		rest.addFlexSpacer(1);
		rest.addUI(new StaticTextControl(data.getTitle(), ColorRGBA.White));
		final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
		row.addUI(new VerticalCenteredControl(new StaticTextControl("Cost:", ColorRGBA.White)));
		final ResourceAmount amount = data.getCost();
		for (final String resName : amount.getNames()) {
			if (amount.getValue(resName) <= 0) {
				continue;
			}
			row.addSpacer(12, 1);
			final ResourceData resData = planet.getState().getResourceData(resName);
			row.addUI(new VerticalCenteredControl(new ImageControl(resData.getIcon())));
			row.addSpacer(2, 1);
			row.addUI(new VerticalCenteredControl(new StaticTextControl(String.valueOf(amount.getValue(resName)),
					ColorRGBA.White)));
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
		// TODO Auto-generated method stub
	}
}
