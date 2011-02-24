package com.evervoid.client.views.lobby;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.UIFocusable;

public class SelectableColorButton extends ImageControl implements UIFocusable
{
	private final Transform aAlphaTransform;
	private final String aColorName;
	private final ColorSelectionControl aListener;

	public SelectableColorButton(final String colorname, final ColorSelectionControl listener)
	{
		super("icons/colors/" + colorname + "_s.png");
		aListener = listener;
		aColorName = colorname;
		aAlphaTransform = getNewTransform().setAlpha(0.2f);
	}

	@Override
	public void defocus()
	{
		setFocusedNode(null);
	}

	@Override
	public void focus()
	{
		aListener.setColor(aColorName, true);
		aAlphaTransform.setAlpha(1f);
	}

	public void setActive(final boolean active)
	{
		aAlphaTransform.setAlpha(active ? 1f : 0.2f);
	}
}
