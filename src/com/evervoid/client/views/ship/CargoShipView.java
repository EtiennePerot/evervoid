package com.evervoid.client.views.ship;

import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.client.views.game.GameView;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.ship.LeaveCargo;
import com.evervoid.state.action.ship.ShipAction;
import com.evervoid.state.prop.Ship;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class CargoShipView extends EverUIView implements ClickObserver
{
	private final ButtonControl aDeployButton;
	private final PanelControl aPanel;
	private final ShipView aParent;
	private final Ship aShip;
	private final AnimatedTranslation aSlideIn;
	private Vector2f aSlideOutOffset;
	UIControl rightMargin;

	public CargoShipView(final ShipView parent, final Ship ship)
	{
		super(new UIControl());
		aParent = parent;
		aShip = ship;
		aSlideIn = getNewTranslationAnimation();
		aDeployButton = new ButtonControl("Deploy");
		aDeployButton.registerClickObserver(this);
		aPanel = new PanelControl("I am a ship. Of type " + aShip.getShipType() + "!");
		refresh();
		addUI(aPanel, 1);
	}

	public void refresh()
	{
		if (rightMargin != null) {
			rightMargin.delAllChildUIs();
			aPanel.deleteChildUI(rightMargin);
		}
		rightMargin = new UIControl(BoxDirection.VERTICAL);
		// deploy button
		rightMargin.addUI(aDeployButton);
		// current action
		final ShipAction action = aParent.getAction(aShip);
		if (action != null) {
			rightMargin.addUI(new StaticTextControl("Current Action: " + action.getDescription(), ColorRGBA.Red));
		}
		// rightMargin.addFlexSpacer(1);
		aPanel.addUI(rightMargin, 1);
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

	@Override
	public void uiClicked(final UIControl clicked)
	{
		if (clicked.equals(aDeployButton)) {
			try {
				if (aParent.getAction(aShip) != null) {
					aParent.setAction(aShip, null);
				}
				else {
					aParent.setAction(aShip, new LeaveCargo(aShip, GameView.getGameState()));
				}
				refresh();
			}
			catch (final IllegalEVActionException e) {
				// no neighbors
			}
		}
		else {
			// what the hell did you click?
		}
	}
}