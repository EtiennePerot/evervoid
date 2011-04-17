package com.evervoid.client.showroom;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.utils.Pair;

public class ShowRoomPlayground extends PanelControl implements ButtonListener
{
	private final ButtonControl aCloseButton;
	private final ShowRoomPlaygroundArea aPlayground;
	private final RaceData aRace;
	private final ButtonControl aRandomColorButton;
	private final ButtonControl aRefreshButton;
	private final ShipData aShip;
	private final ShowRoomView aView;

	public ShowRoomPlayground(final ShowRoomView view, final RaceData race, final ShipData ship)
	{
		super("Showroom: " + ship.getTitle());
		aView = view;
		aRace = race;
		aShip = ship;
		aCloseButton = new ButtonControl("Close");
		aCloseButton.addButtonListener(this);
		getTitleBox().addUI(aCloseButton);
		aPlayground = new ShowRoomPlaygroundArea(aRace, aShip);
		addUI(aPlayground, 1);
		addSpacer(1, 4);
		final UIControl buttonRow = new UIControl(BoxDirection.HORIZONTAL);
		aRefreshButton = new ButtonControl("Refresh");
		aRefreshButton.addButtonListener(this);
		buttonRow.addUI(aRefreshButton);
		buttonRow.addSpacer(8, 1);
		aRandomColorButton = new ButtonControl("Change color");
		aRandomColorButton.addButtonListener(this);
		buttonRow.addUI(aRandomColorButton);
		addUI(buttonRow);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aCloseButton)) {
			aView.closePlayground();
		}
		else if (button.equals(aRefreshButton)) {
			final Pair<RaceData, ShipData> newData = aView.getNewData(aRace, aShip);
			if (newData == null) {
				EVViewManager.displayError("Error while reloading gamedata.json.");
			}
			else {
				aPlayground.updateData(newData.getKey(), newData.getValue());
			}
		}
		else if (button.equals(aRandomColorButton)) {
			aPlayground.randomColor();
		}
	}

	void shoot()
	{
		aPlayground.shoot();
	}
}
