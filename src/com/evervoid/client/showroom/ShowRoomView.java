package com.evervoid.client.showroom;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.state.data.BadJsonInitialization;
import com.evervoid.state.data.GameData;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.utils.Pair;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The {@link EverUIView} implementing the showroom feature.
 */
public class ShowRoomView extends EverUIView implements ButtonListener
{
	/**
	 * "Back" button (to go back to the main menu)
	 */
	private final ButtonControl aBackButton;
	/**
	 * Reference to the {@link GameData} to use.
	 */
	private GameData aData = null;
	/**
	 * Contains the list of clickable ship controls to launch the playground view
	 */
	private final UIControl aDataControl;
	/**
	 * If a {@link ShowRoomPlayground} is open, this contains a reference to it; otherwise, null.
	 */
	private ShowRoomPlayground aOpenPlayground = null;
	/**
	 * "Refresh" button to reload data from gamedata.json
	 */
	private final ButtonControl aRefreshButton;
	/**
	 * Contains the status text, listing the total number of ships in the game data
	 */
	private final UIControl aTotalControl;

	/**
	 * Constructor; needs nothing.
	 */
	public ShowRoomView()
	{
		super(new UIControl(BoxDirection.HORIZONTAL));
		final PanelControl panel = new PanelControl("Showroom");
		aBackButton = new ButtonControl("Back");
		aBackButton.addButtonListener(this);
		panel.getTitleBox().addUI(aBackButton);
		aDataControl = new UIControl();
		panel.addUI(aDataControl, 1);
		addUI(new CenteredControl(panel), 1);
		panel.addSpacer(1, 6);
		final UIControl bottomRow = new UIControl(BoxDirection.HORIZONTAL);
		aRefreshButton = new ButtonControl("Reload data");
		aRefreshButton.addButtonListener(this);
		bottomRow.addUI(aRefreshButton);
		bottomRow.addSpacer(8, 1);
		bottomRow.addFlexSpacer(1);
		aTotalControl = new UIControl(BoxDirection.HORIZONTAL);
		bottomRow.addUI(aTotalControl, 1);
		panel.addUI(bottomRow);
		refreshData();
		setBounds(Bounds.getWholeScreenBounds());
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aBackButton)) {
			EVViewManager.deregisterView(ViewType.SHOWROOM, new Runnable()
			{
				@Override
				public void run()
				{
					EVViewManager.switchTo(ViewType.MAINMENU);
				}
			});
		}
		else if (button.equals(aRefreshButton)) {
			refreshData();
		}
	}

	/**
	 * Close the playground, if it is open.
	 */
	void closePlayground()
	{
		deleteUI();
		aOpenPlayground = null;
	}

	/**
	 * Reloads {@link ShipData} and {@link RaceData} from the stored {@link GameData}.
	 * 
	 * @param race
	 *            The {@link RaceData} to reload
	 * @param ship
	 *            The {@link ShipData} to reload
	 * @return A {@link Pair} containing the new {@link RaceData} and {@link ShipData}
	 */
	Pair<RaceData, ShipData> getNewData(final RaceData race, final ShipData ship)
	{
		refreshData();
		if (aData == null) {
			return null;
		}
		final RaceData newRace = aData.getRaceData(race.getType());
		if (newRace == null) {
			return null;
		}
		final ShipData newShip = newRace.getShipData(ship.getType());
		if (newShip == null) {
			return null;
		}
		return new Pair<RaceData, ShipData>(newRace, newShip);
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (key.equals(KeyboardKey.SPACE) && aOpenPlayground != null) {
			aOpenPlayground.shoot();
			return true;
		}
		return super.onKeyPress(key, tpf);
	}

	@Override
	public boolean onRightClick(final Vector2f position, final float tpf)
	{
		if (aOpenPlayground != null) {
			aOpenPlayground.shoot();
			return true;
		}
		return super.onRightClick(position, tpf);
	}

	/**
	 * Open the showroom playground with a given {@link ShipData} and {@link RaceData}
	 * 
	 * @param race
	 *            The {@link RaceData} to use
	 * @param ship
	 *            The {@link ShipData} to use
	 */
	void openPlayground(final RaceData race, final ShipData ship)
	{
		aOpenPlayground = new ShowRoomPlayground(this, race, ship);
		pushUI(aOpenPlayground);
	}

	/**
	 * Refresh the {@link GameData} from gamedata.json
	 */
	private void refreshData()
	{
		aDataControl.delAllChildUIs();
		aTotalControl.delAllChildUIs();
		aData = null;
		try {
			aData = new GameData();
			aDataControl.addUI(new ShowRoomPanel(this, aData), 1);
			int races = 0;
			int ships = 0;
			for (final String r : aData.getRaceTypes()) {
				races++;
				ships += aData.getRaceData(r).getShipTypes().size();
			}
			aTotalControl.addString(races + " races; " + ships + " ship types.");
		}
		catch (final BadJsonInitialization e) {
			aDataControl.addString("Error while reading gamedata.json.", ColorRGBA.Red);
		}
	}
}
