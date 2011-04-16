package com.evervoid.client.showroom;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
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

public class ShowRoomView extends EverUIView implements ButtonListener
{
	private final ButtonControl aBackButton;
	private GameData aData = null;
	private final UIControl aDataControl;
	private ShowRoomPlayground aOpenPlayground = null;
	private final ButtonControl aRefreshButton;
	private final UIControl aTotalControl;

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

	void closePlayground()
	{
		deleteUI();
		aOpenPlayground = null;
	}

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

	void openPlayground(final RaceData race, final ShipData ship)
	{
		aOpenPlayground = new ShowRoomPlayground(this, race, ship);
		pushUI(aOpenPlayground);
	}

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
