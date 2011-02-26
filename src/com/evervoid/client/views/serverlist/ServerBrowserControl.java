package com.evervoid.client.views.serverlist;

import com.evervoid.client.ui.UIControl;

public class ServerBrowserControl extends UIControl
{
	public ServerBrowserControl()
	{
		super(BoxDirection.VERTICAL);
		addUI(new ServerListControl(), 1);
		addUI(new DirectIPConnectControl());
	}
}
