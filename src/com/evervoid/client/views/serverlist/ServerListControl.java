package com.evervoid.client.views.serverlist;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.discovery.ServerData;
import com.evervoid.client.discovery.ServerDiscoveryObserver;
import com.evervoid.client.discovery.ServerDiscoveryService;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.jme3.math.ColorRGBA;

public class ServerListControl extends PanelControl implements ButtonListener, ServerDiscoveryObserver
{
	private final ButtonControl aBackButton;
	private final ButtonControl aRefreshButton;
	private final List<ServerRowControl> aServerRows = new ArrayList<ServerRowControl>();
	private final UIControl aServerTable;
	private final StaticTextControl aStatus;

	public ServerListControl()
	{
		super("everVoid servers");
		ServerDiscoveryService.addObserver(this);
		aBackButton = new ButtonControl("Main menu");
		aBackButton.addButtonListener(this);
		getTitleBox().addUI(aBackButton);
		aServerTable = new UIControl(BoxDirection.VERTICAL);
		addUI(aServerTable);
		addFlexSpacer(1);
		final UIControl bottomBar = new UIControl(BoxDirection.HORIZONTAL);
		aStatus = new StaticTextControl("Refreshing...", new ColorRGBA(0.65f, 0.65f, 0.75f, 1f), "redensek", 24);
		aStatus.setKeepBoundsOnChange(false);
		bottomBar.addUI(aStatus);
		bottomBar.addFlexSpacer(1);
		aRefreshButton = new ButtonControl("Refresh");
		aRefreshButton.addButtonListener(this);
		bottomBar.addUI(aRefreshButton);
		addUI(bottomBar);
	}

	@Override
	public void buttonClicked(final ButtonControl button)
	{
		if (button.equals(aBackButton)) {
			EVViewManager.switchTo(ViewType.MAINMENU);
		}
		else if (button.equals(aRefreshButton)) {
			refresh();
		}
	}

	@Override
	public void noServersFound()
	{
		aStatus.setText("0 servers found.");
	}

	void refresh()
	{
		ServerDiscoveryService.refresh();
		aStatus.setText("Refreshing...");
	}

	@Override
	public void resetFoundServers()
	{
		aServerTable.delAllChildUIs();
		aServerRows.clear();
	}

	@Override
	public void serverFound(final ServerData server)
	{
		final ServerRowControl newRow = new ServerRowControl(server);
		aServerRows.add(newRow);
		aServerTable.addUI(newRow);
		final int rows = aServerRows.size();
		for (int i = 0; i < rows; i++) {
			aServerRows.get(i).updateRowSprites(i == 0, i == rows - 1);
		}
		aStatus.setText(aServerTable.getNumChildrenUIs() + " server" + (rows == 1 ? "" : " ") + " found.");
	}
}
