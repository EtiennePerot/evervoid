package com.evervoid.client.views;

import com.evervoid.client.ui.UIConnector;
import com.evervoid.state.data.SpriteData;

public class LoadingView extends EverView
{
	public LoadingView()
	{
		final UIConnector loading = new UIConnector(new SpriteData("ui/loading.png", 0, 0, 1));
		addNode(loading);
	}
}
