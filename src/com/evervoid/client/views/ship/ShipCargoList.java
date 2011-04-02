package com.evervoid.client.views.ship;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.ScrollingControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.client.views.solar.UIShip;
import com.evervoid.state.prop.Ship;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class ShipCargoList extends EverUIView
{
	private final ScrollingControl aCargo;
	private final PanelControl aPanel;
	private final ShipView aParent;
	private final AnimatedTranslation aSlideIn;
	private Vector2f aSlideOutOffset;
	private final UIShip aUIShip;

	public ShipCargoList(final ShipView parent, final UIShip ship)
	{
		super(new UIControl());
		aParent = parent;
		aSlideIn = getNewTranslationAnimation();
		aPanel = new PanelControl("Cargo");
		final UIControl leftMargin = new UIControl(BoxDirection.HORIZONTAL);
		leftMargin.addSpacer(4, 0);
		aCargo = new ScrollingControl();
		aCargo.setAutomaticSpacer(5);
		aUIShip = ship;
		refreshUI();
		leftMargin.addUI(aCargo, 1);
		aPanel.addUI(leftMargin, 1);
		addUI(aPanel, 1);
	}

	void refreshUI()
	{
		aCargo.delAllChildUIs();
		final Ship ship = aUIShip.getShip();
		for (final Ship s : ship.getCargo()) {
			final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
			row.addUI(new RescalableControl(s.getSprite()), 1);
			row.addUI(new StaticTextControl(s.getShipType(), ColorRGBA.White));
			aCargo.addUI(row);
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
