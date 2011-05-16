package com.evervoid.client.showroom;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.ScrollingControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.state.data.GameData;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.utils.Pair;

/**
 * Panel containing all clickable ships in the showroom menu
 */
class ShowRoomPanel extends ScrollingControl implements ClickObserver
{
	/**
	 * Maps clickable controls to the ({@link RaceData}, {@link ShipData}) pair to load when they are clicked
	 */
	private final Map<UIControl, Pair<RaceData, ShipData>> aShipControls = new HashMap<UIControl, Pair<RaceData, ShipData>>();
	/**
	 * Reference to the parent {@link ShowRoomView}
	 */
	private final ShowRoomView aView;

	/**
	 * Constructor; just needs the {@link GameData} and the parent view
	 * 
	 * @param view
	 *            The parent {@link ShowRoomView}
	 * @param data
	 *            The {@link GameData} to use
	 */
	public ShowRoomPanel(final ShowRoomView view, final GameData data)
	{
		super(600, 400);
		aView = view;
		setAutomaticSpacer(6);
		for (final String raceType : data.getRaceTypes()) {
			final RaceData race = data.getRaceData(raceType);
			final UIControl raceRow = new UIControl(BoxDirection.HORIZONTAL);
			raceRow.addUI(new VerticalCenteredControl(new ImageControl(race.getRaceIcon("small"))));
			raceRow.addSpacer(4, 1);
			raceRow.addString(race.getTitle(), BoxDirection.VERTICAL);
			addUI(raceRow);
			for (final String shipType : race.getShipTypes()) {
				final ShipData ship = race.getShipData(shipType);
				final UIControl shipRow = new UIControl(BoxDirection.HORIZONTAL);
				shipRow.addSpacer(32, 1); // Indent slightly
				shipRow.addUI(new VerticalCenteredControl(new RescalableControl(ship.getBaseSprite()).setEnforcedDimension(32,
						32)));
				shipRow.addSpacer(4, 1);
				shipRow.addString(ship.getTitle(), BoxDirection.VERTICAL);
				shipRow.setHoverSelectable(true);
				aShipControls.put(shipRow, new Pair<RaceData, ShipData>(race, ship));
				shipRow.registerClickObserver(this);
				addUI(shipRow);
			}
			addSpacer(1, 8);
		}
	}

	@Override
	public void uiClicked(final UIControl clicked)
	{
		final Pair<RaceData, ShipData> clickedShip = aShipControls.get(clicked);
		aView.openPlayground(clickedShip.getKey(), clickedShip.getValue());
	}
}
