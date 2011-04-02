package com.evervoid.client.views.ship;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.ScrollingControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.state.prop.Ship;
import com.jme3.math.Vector2f;

public class CargoShipView extends EverUIView
{
	private final PanelControl aPanel;
	private final ScrollingControl aPanelContents;
	private final ShipView aParent;
	private final Ship aShip;
	private final AnimatedTranslation aSlideIn;
	private Vector2f aSlideOutOffset;

	public CargoShipView(final ShipView parent, final Ship ship)
	{
		super(new UIControl());
		aParent = parent;
		aShip = ship;
		aSlideIn = getNewTranslationAnimation();
		final UIControl rightMargin = new UIControl(BoxDirection.HORIZONTAL);
		aPanel = new PanelControl("I am a ship. Of type " + aShip.getShipType() + "!");
		aPanelContents = new ScrollingControl();
		aPanelContents.setAutomaticSpacer(4);
		rightMargin.addUI(aPanelContents, 1);
		rightMargin.addSpacer(4, 0);
		aPanel.addUI(rightMargin, 1);
		addUI(aPanel, 1);
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
