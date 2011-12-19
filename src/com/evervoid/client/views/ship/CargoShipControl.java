package com.evervoid.client.views.ship;

import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.ship.LeaveCargo;
import com.evervoid.state.prop.Ship;
import com.jme3.math.ColorRGBA;

public class CargoShipControl extends UIControl implements ClickObserver
{
	private final UIControl aLoadButtonUI;
	private final ShipView aParent;
	private final Ship aShip;

	public CargoShipControl(final ShipView parent, final Ship ship)
	{
		super(BoxDirection.HORIZONTAL);
		aParent = parent;
		aShip = ship;
		addUI(new RescalableControl(aShip.getSprite()).setEnforcedDimension(64, 64));
		addSpacer(16, 1);
		addString(aShip.getData().getTitle(), ColorRGBA.White, BoxDirection.VERTICAL);
		addFlexSpacer(1);
		aLoadButtonUI = new UIControl(BoxDirection.HORIZONTAL);
		refreshLoadButton();
		addUI(new VerticalCenteredControl(aLoadButtonUI));
		setHoverSelectable(true);
		registerClickObserver(this);
	}

	private void refreshLoadButton()
	{
		aLoadButtonUI.delAllChildUIs();
		if (aParent.willUnload(aShip)) {
			aLoadButtonUI.addUI(new ButtonControl("Cancel unload"));
		}
		else {
			aLoadButtonUI.addUI(new ImageControl("icons/unload_ship.png"));
		}
	}

	@Override
	public boolean uiClicked(final UIControl clicked)
	{
		try {
			aParent.setAction(aShip, aParent.willUnload(aShip) ? null : new LeaveCargo(aShip));
			return true;
		}
		catch (final IllegalEVActionException e) {
			// Nope.avi
			return false;
		}
		finally {
			refreshLoadButton();
		}
	}
}
