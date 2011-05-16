package com.evervoid.client.showroom;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.utils.Pair;

/**
 * UI for the panel of the showroom's "playground" mode where the user can move his ships around.
 */
public class ShowRoomPlayground extends PanelControl implements ButtonListener
{
	/**
	 * Close button
	 */
	private final ButtonControl aCloseButton;
	/**
	 * The actual playground area
	 */
	private final ShowRoomPlaygroundArea aPlayground;
	/**
	 * The {@link RaceData} of the ship being toyed with
	 */
	private final RaceData aRace;
	/**
	 * "Random color" button
	 */
	private final ButtonControl aRandomColorButton;
	/**
	 * "Refresh" button
	 */
	private final ButtonControl aRefreshButton;
	/**
	 * {@link ShipData} of the ship being toyed with
	 */
	private final ShipData aShip;
	/**
	 * Reference to the parent {@link ShowRoomView}
	 */
	private final ShowRoomView aView;

	/**
	 * Constructor; builds the UI for a given {@link RaceData} and {@link ShipData}.
	 * 
	 * @param view
	 *            Reference to the parent {@link ShowRoomView}
	 * @param race
	 *            The {@link RaceData} of the ship being toyed with
	 * @param ship
	 *            The {@link ShipData} of the ship being toyed with
	 */
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

	/**
	 * Called when the user requests the ship to shoot.
	 */
	void shoot()
	{
		aPlayground.shoot();
	}
}
