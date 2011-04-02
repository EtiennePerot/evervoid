package com.evervoid.client.views.ship;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.ComposedView;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.solar.SolarView;
import com.evervoid.client.views.solar.UIShip;
import com.jme3.math.Vector2f;

public class ShipView extends ComposedView
{
	private static final float sInnerHeightPercentage = 0.8f;
	private final ShipCargoList aCargo;
	private Bounds aLastBounds = null;
	private float aLastDuration = 0f;
	private final UIShip aShip;
	private final SolarView aSolarView;

	public ShipView(final SolarView parent, final UIShip uiShip)
	{
		aSolarView = parent;
		aShip = uiShip;
		aCargo = new ShipCargoList(this, aShip);
		addView(aCargo);
	}

	public void close()
	{
		aSolarView.shipViewClose();
	}

	private Bounds getBuildingListBounds()
	{
		return new Bounds(aLastBounds.x, aLastBounds.y, aLastBounds.width / 3, aLastBounds.height);
	}

	private Bounds getPartialHeightBounds(final Bounds original)
	{
		final float newY = original.y + original.height * (1f - sInnerHeightPercentage) / 2f;
		final float newHeight = original.height * sInnerHeightPercentage;
		return new Bounds(original.x, newY, original.width, newHeight);
	}

	private Bounds getShipViewBounds()
	{
		return new Bounds(aLastBounds.x + aLastBounds.width * 2 / 3, aLastBounds.y, aLastBounds.width / 3, aLastBounds.height);
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

	public void refreshSlots(final int slot)
	{
		aCargo.refreshUI();
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		aLastBounds = getPartialHeightBounds(bounds);
		aCargo.setBounds(getBuildingListBounds());
	}

	public void slideIn(final float duration)
	{
		aLastDuration = duration;
		aCargo.slideIn(aLastDuration);
	}

	public void slideOut(final float duration, final Runnable callback)
	{
		aLastDuration = duration;
		aCargo.slideOut(aLastDuration);
		if (callback != null) {
			new FrameTimer(callback, aLastDuration, 1).start();
		}
	}
}
