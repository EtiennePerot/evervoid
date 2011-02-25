package com.evervoid.client.views.serverlist;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.jme3.math.Vector2f;

public class ServerListView extends EverView
{
	private final UIControl aRootUI;

	public ServerListView()
	{
		aRootUI = new UIControl(BoxDirection.VERTICAL);
		addNode(aRootUI);
		aRootUI.addUI(new CenteredControl(new ServerListControl()), 1);
		resolutionChanged();
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		aRootUI.onKeyPress(key);
		return true;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		aRootUI.onKeyRelease(key);
		return true;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		aRootUI.click(position);
		return true;
	}

	@Override
	public void resolutionChanged()
	{
		aRootUI.setBounds(new Bounds(0, 0, EverVoidClient.getWindowDimension().width,
				EverVoidClient.getWindowDimension().height));
	}
}
