package com.evervoid.client.views.serverlist;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.discovery.ServerData;
import com.evervoid.client.ui.RowControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIFocusable;
import com.jme3.math.ColorRGBA;

public class ServerRowControl extends RowControl implements UIFocusable
{
	private final String aHostname;

	public ServerRowControl(final ServerData server)
	{
		aHostname = server.hostName;
		addUI(new StaticTextControl(server.serverName + " @ " + server.hostName, ColorRGBA.Black, "redensek", 24));
		addFlexSpacer(1);
		addUI(new StaticTextControl(server.players + " player" + (server.players == 1 ? "" : " "), ColorRGBA.Black, "redensek",
				24));
		addFlexSpacer(1);
		addUI(new StaticTextControl(Math.round(server.ping / 1000000) + " ms", ColorRGBA.Black, "redensek", 24));
	}

	@Override
	public void defocus()
	{
		setFocusedNode(null);
	}

	@Override
	public void focus()
	{
		setFocusedNode(null);
		EVViewManager.switchTo(ViewType.LOADING);
		EVClientEngine.connect(aHostname);
	}
}
