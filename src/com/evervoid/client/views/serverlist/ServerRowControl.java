package com.evervoid.client.views.serverlist;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.discovery.ServerData;
import com.evervoid.client.ui.RowControl;
import com.evervoid.client.ui.UIInputListener;
import com.jme3.math.ColorRGBA;

public class ServerRowControl extends RowControl implements UIInputListener
{
	private final String aHostname;

	public ServerRowControl(final ServerData server)
	{
		aHostname = server.hostName;
		addString(server.serverName + " @ " + server.hostName, ColorRGBA.Black, "redensek", 24);
		addFlexSpacer(1);
		addString(server.players + " player" + (server.players == 1 ? "" : "s"), ColorRGBA.Black, "redensek", 24);
		addFlexSpacer(1);
		addString(Math.round(server.ping / 1000000) + " ms", ColorRGBA.Black, "redensek", 24);
	}

	@Override
	public void onClick()
	{
		if (!isEnabled()) {
			return;
		}
		setFocusedNode(null);
		EVViewManager.switchTo(ViewType.LOADING);
		EVClientEngine.connect(aHostname);
	}

	@Override
	public void onDefocus()
	{
		setFocusedNode(null);
	}
}
