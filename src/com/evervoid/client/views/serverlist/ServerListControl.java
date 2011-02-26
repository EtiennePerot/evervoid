package com.evervoid.client.views.serverlist;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.discovery.EVDiscoveryService;
import com.evervoid.client.discovery.ServerData;
import com.evervoid.client.discovery.ServerDiscoveryObserver;
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
	private final UIControl aServerRows;
	private final StaticTextControl aStatus;

	public ServerListControl()
	{
		super("everVoid servers");
		// EVDiscoveryService.addObserver(this);
		aBackButton = new ButtonControl("Main menu");
		aBackButton.addButtonListener(this);
		getTitleBox().addUI(aBackButton);
		aServerRows = new UIControl(BoxDirection.VERTICAL);
		addUI(aServerRows, 1);
		final UIControl bottomBar = new UIControl(BoxDirection.HORIZONTAL);
		aStatus = new StaticTextControl("Refreshing...", new ColorRGBA(0.65f, 0.65f, 0.75f, 1f), "redensek", 24);
		bottomBar.addUI(aStatus);
		bottomBar.addFlexSpacer(1);
		aRefreshButton = new ButtonControl("Refresh");
		aRefreshButton.addButtonListener(this);
		bottomBar.addUI(aRefreshButton);
		addUI(bottomBar);
		// EVDiscoveryService.refresh();
	}

	@Override
	public void buttonClicked(final ButtonControl button)
	{
		if (button.equals(aBackButton)) {
			EVViewManager.switchTo(ViewType.MAINMENU);
		}
		else if (button.equals(aRefreshButton)) {
			EVDiscoveryService.refresh();
		}
	}

	@Override
	public void serverFound(final ServerData server)
	{
		System.out.println("Got server: " + server.hostName + " / " + server.ping);
	}
}
