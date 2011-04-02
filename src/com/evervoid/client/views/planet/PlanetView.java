package com.evervoid.client.views.planet;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.ComposedView;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.solar.SolarView;
import com.evervoid.state.prop.Planet;
import com.jme3.math.Vector2f;

public class PlanetView extends ComposedView
{
	private static final float sInnerHeightPercentage = 0.8f;
	private final PlanetBuildingList aBuildings;
	private PlanetBuildingView aBuildingView = null;
	private Bounds aLastBounds = null;
	private float aLastDuration = 0f;
	private final Planet aPlanet;
	private final SolarView aSolarView;

	public PlanetView(final SolarView parent, final Planet planet)
	{
		aSolarView = parent;
		aPlanet = planet;
		aBuildings = new PlanetBuildingList(this, aPlanet);
		addView(aBuildings);
	}

	public void close()
	{
		aSolarView.planetViewClose();
	}

	private Bounds getBuildingListBounds()
	{
		return new Bounds(aLastBounds.x, aLastBounds.y, aLastBounds.width / 3, aLastBounds.height);
	}

	private Bounds getBuildingViewBounds()
	{
		return new Bounds(aLastBounds.x + aLastBounds.width * 2 / 3, aLastBounds.y, aLastBounds.width / 3, aLastBounds.height);
	}

	private Bounds getPartialHeightBounds(final Bounds original)
	{
		final float newY = original.y + original.height * (1f - sInnerHeightPercentage) / 2f;
		final float newHeight = original.height * sInnerHeightPercentage;
		return new Bounds(original.x, newY, original.width, newHeight);
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (key.equals(KeyboardKey.ESCAPE)) {
			close();
			return true;
		}
		return false;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (super.onLeftClick(position, tpf)) {
			return true;
		}
		// Otherwise, check if click was outside of all subviews bounds
		for (final EverView view : getChildrenViews()) {
			if (view.getBounds().contains(position.x, position.y)) {
				return true; // Still inside
			}
		}
		// Outside of all subviews; close the planet view.
		close();
		return false;
	}

	void openSlot(final int slot)
	{
		final PlanetView oldThis = this; // Silly, but necessary
		// Need to schedule this later to avoid a concurrent modification to the view list, since we're inside a click event
		// handler here (which iterates over the views).
		EVViewManager.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (aBuildingView != null && slot == aBuildingView.getSlot()) {
					return;
				}
				final PlanetBuildingView oldView = aBuildingView;
				aBuildingView = new PlanetBuildingView(oldThis, aPlanet, slot);
				addView(aBuildingView);
				aBuildingView.setBounds(getBuildingViewBounds());
				if (oldView != null) {
					oldView.slideOut(aLastDuration, new Runnable()
					{
						@Override
						public void run()
						{
							removeView(oldView);
							aBuildingView.slideIn(aLastDuration);
						}
					});
				}
				else {
					aBuildingView.slideIn(aLastDuration);
				}
			}
		});
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		aLastBounds = getPartialHeightBounds(bounds);
		aBuildings.setBounds(getBuildingListBounds());
		if (aBuildingView != null) {
			aBuildingView.setBounds(getBuildingViewBounds());
		}
	}

	public void slideIn(final float duration)
	{
		aLastDuration = duration;
		aBuildings.slideIn(aLastDuration);
	}

	public void slideOut(final float duration, final Runnable callback)
	{
		aLastDuration = duration;
		aBuildings.slideOut(aLastDuration);
		if (aBuildingView != null) {
			aBuildingView.slideOut(aLastDuration, null);
		}
		if (callback != null) {
			new FrameTimer(callback, aLastDuration, 1).start();
		}
	}
}
