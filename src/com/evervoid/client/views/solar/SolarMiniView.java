package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.MiniView;
import com.evervoid.state.SolarSystem;
import com.jme3.math.ColorRGBA;

public class SolarMiniView extends MiniView
{
	private final SolarGrid aGrid;
	private final Transform aGridTransform;

	public SolarMiniView(final GameView gameview, final SolarSystem ss)
	{
		super(gameview);
		aGrid = new SolarGrid(null, ss);
		aGrid.setLineColor(new ColorRGBA(1, 1, 1, 0.025f));
		aGridTransform = aGrid.getNewTransform();
		addNode(aGrid);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		final float boundsRatio = (float) bounds.width / (float) bounds.height;
		final float gridRatio = aGrid.getTotalWidth() / aGrid.getTotalHeight();
		if (boundsRatio > gridRatio) {
			// Bounds is longer than grid, so grid height = bounds height, and grid width is not fully occupying space
			final float scale = bounds.height / aGrid.getTotalHeight();
			aGridTransform.setScale(scale);
			aGridTransform.translate(bounds.x + (float) bounds.width / 2 - aGrid.getTotalWidth() * scale / 2, bounds.y);
		}
		else {
			// Bounds is taller than grid, so grid width = bounds width, and grid height is not fully occupying space final
			final float scale = bounds.width / aGrid.getTotalWidth();
			aGridTransform.setScale(scale);
			aGridTransform.translate(bounds.x, bounds.y + (float) bounds.height / 2 - aGrid.getTotalHeight() * scale / 2);
		}
	}
}
