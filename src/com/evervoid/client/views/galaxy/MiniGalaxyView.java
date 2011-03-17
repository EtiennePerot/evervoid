package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.MiniView;
import com.evervoid.state.Galaxy;
import com.jme3.math.ColorRGBA;

class MiniGalaxyView extends MiniView
{
	private final Transform aTransform;

	MiniGalaxyView(final GameView gameview, final Galaxy galaxy)
	{
		super(gameview);
		aTransform = getNewTransform();
		// TODO: Pretty galaxy here instead of this silly text
		addNode(new StaticTextControl("Galaxiez", ColorRGBA.White));
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aTransform.translate(bounds.x, bounds.y);
		// TODO: Scale galaxy to fit in bounds
	}
}
