package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.MiniView;
import com.evervoid.state.Galaxy;

class MiniGalaxyView extends MiniView
{
	private final Galaxy2D aMiniGalaxy;
	private final Transform aTransform;

	MiniGalaxyView(final GameView gameview, final Galaxy galaxy)
	{
		super(gameview);
		aTransform = getNewTransform();
		aMiniGalaxy = new Galaxy2D(galaxy);
		addNode(aMiniGalaxy);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aMiniGalaxy.setBounds(bounds);
		aTransform.translate(bounds.x, bounds.y);
	}
}
