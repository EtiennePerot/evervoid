package com.evervoid.client.views;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.state.data.SpriteData;

public class LogoView extends EverUIView
{
	public LogoView()
	{
		super(new UIControl(BoxDirection.VERTICAL));
		addUI(new RescalableControl(new Sprite(new SpriteData("logo.png", 0, 0, 1))), 1);
		setBounds(Bounds.getWholeScreenBounds());
		final AnimatedAlpha alpha = getNewAlphaAnimation();
		alpha.setDuration(3).setAlpha(0);
		alpha.setTargetAlpha(1).start(new Runnable()
		{
			@Override
			public void run()
			{
				EVViewManager.prepareViews();
				new FrameTimer(new Runnable()
				{
					@Override
					public void run()
					{
						alpha.setTargetAlpha(0).setDuration(1.5).start(new Runnable()
						{
							@Override
							public void run()
							{
								EVViewManager.switchTo(ViewType.MAINMENU);
							}
						});
					}
				}, 1f, 1).start();
			}
		});
	}
}
