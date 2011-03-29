package com.evervoid.client.views.lobby;

import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.UIInputListener;

public class SelectableColorButton extends ImageControl implements UIInputListener
{
	private final AnimatedAlpha aAlphaTransform;
	private final String aColorName;
	private final ColorSelectionControl aListener;

	public SelectableColorButton(final String colorname, final ColorSelectionControl listener)
	{
		super("icons/colors/" + colorname + "_s.png");
		aListener = listener;
		aColorName = colorname;
		aAlphaTransform = getNewAlphaAnimation();
		aAlphaTransform.setDuration(0.4).setAlpha(0.2);
	}

	@Override
	public void onClick()
	{
		if (!isEnabled()) {
			return;
		}
		aListener.setColor(aColorName, true);
	}

	@Override
	public void onDefocus()
	{
		setFocusedNode(null);
	}

	public void setActive(final boolean active)
	{
		aAlphaTransform.setTargetAlpha(active ? 1f : 0.2f).start();
	}
}
