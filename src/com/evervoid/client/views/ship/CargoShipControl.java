package com.evervoid.client.views.ship;

import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.prop.Ship;
import com.jme3.math.ColorRGBA;

public class CargoShipControl extends UIControl implements ClickObserver
{
	private final ShipView aParent;
	private final Ship aShip;

	public CargoShipControl(final ShipView parent, final Ship ship)
	{
		super(BoxDirection.HORIZONTAL);
		aParent = parent;
		aShip = ship;
		final UIControl rest = new UIControl(BoxDirection.VERTICAL);
		rest.addFlexSpacer(1);
		final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
		row.addUI(new RescalableControl(aShip.getSprite()), 1);
		row.addUI(new StaticTextControl(aShip.getShipType(), ColorRGBA.White));
		rest.addUI(row);
		rest.addFlexSpacer(1);
		addUI(rest, 1);
		setHoverSelectable(true);
		registerClickObserver(this);
	}

	@Override
	public void uiClicked(final UIControl clicked)
	{
		aParent.setSelectedShip(new CargoShipView(aParent, aShip));
	}
}
