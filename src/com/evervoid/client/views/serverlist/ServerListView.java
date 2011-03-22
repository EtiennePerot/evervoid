package com.evervoid.client.views.serverlist;

import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;

public class ServerListView extends EverUIView
{
	private final ServerBrowserControl aBrowser;

	public ServerListView()
	{
		super(new UIControl());
		aBrowser = new ServerBrowserControl();
		addUI(aBrowser, 1);
		setBounds(Bounds.getWholeScreenBounds(32));
	}

	@Override
	public void onFocus()
	{
		aBrowser.refresh();
	}
}
