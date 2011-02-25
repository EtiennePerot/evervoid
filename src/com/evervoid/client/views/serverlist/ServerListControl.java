package com.evervoid.client.views.serverlist;

import com.evervoid.client.ui.UIControl;

public class ServerListControl extends UIControl
{
	public ServerListControl()
	{
		super(BoxDirection.VERTICAL);
		addUI(new DirectIPConnectControl());
	}
}
