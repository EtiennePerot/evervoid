package com.evervoid.client.views.serverlist;

import com.evervoid.client.ui.UIControl;

public class ServerBrowserControl extends UIControl
{
	private final ServerListControl aServerList;

	public ServerBrowserControl()
	{
		super(BoxDirection.VERTICAL);
		aServerList = new ServerListControl();
		addUI(aServerList, 1);
		addUI(new DirectIPConnectControl());
	}

	public void refresh()
	{
		aServerList.refresh();
	}
}
