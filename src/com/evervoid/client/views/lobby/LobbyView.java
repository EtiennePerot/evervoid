package com.evervoid.client.views.lobby;

import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.Sizer.SizerDirection;
import com.evervoid.client.views.Bounds;
import com.jme3.app.SimpleApplication;

public class LobbyView extends SimpleApplication
{
	public static void main(final String[] args)
	{
		new LobbyView().start();
	}

	@Override
	public void simpleInitApp()
	{
		GraphicManager.setAssetManager(assetManager);
		guiNode.attachChild(new BoxControl(new Bounds(0, 0, 50, 50), SizerDirection.HORIZONTAL));
		guiNode.attachChild(new Sprite("ui/menubox/right_corner_bottom.png"));
		System.out.println(new BoxControl(new Bounds(0, 0, 50, 50), SizerDirection.HORIZONTAL));
	}
}
