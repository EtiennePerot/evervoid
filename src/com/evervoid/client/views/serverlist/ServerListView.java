package com.evervoid.client.views.serverlist;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.jme3.math.Vector2f;

public class ServerListView extends EverView
{
	private final ServerBrowserControl aBrowser;

	public ServerListView()
	{
		aBrowser = new ServerBrowserControl();
		addNode(aBrowser);
		resolutionChanged();
	}

	@Override
	public void onFocus()
	{
		aBrowser.refresh();
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		aBrowser.onKeyPress(key);
		return true;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		aBrowser.onKeyRelease(key);
		return true;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		aBrowser.click(position);
		return true;
	}

	@Override
	public void resolutionChanged()
	{
		aBrowser.setBounds(new Bounds(32, 32, EverVoidClient.getWindowDimension().width - 64, EverVoidClient
				.getWindowDimension().height - 64));
	}
}
