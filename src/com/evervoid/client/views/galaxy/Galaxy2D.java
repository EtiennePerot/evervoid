package com.evervoid.client.views.galaxy;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.state.Galaxy;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.geometry.Point3D;

public class Galaxy2D extends EverView
{
	private final Map<UISolarSystem, Transform> aSolarSystems;
	private final int maxSize;

	protected Galaxy2D(final Galaxy galaxy)
	{
		aSolarSystems = new HashMap<UISolarSystem, Transform>();
		maxSize = galaxy.getSize();
		for (final SolarSystem ss : galaxy.getSolarSystems()) {
			add(new UISolarSystem(ss));
		}
	}

	private void add(final UISolarSystem ss)
	{
		// store the transform so it can be referred back to
		addNode(ss);
		aSolarSystems.put(ss, ss.getNewTransform());
	}

	public void rescale()
	{
		// calculate center of bounds and new scale
		final Bounds bounds = getBounds();
		final float scale = (bounds.width < bounds.height ? (float) bounds.width : (float) bounds.height) / maxSize;
		final Point center = new Point(bounds.width / 2, bounds.height / 2);
		// for each solar system
		for (final UISolarSystem ss : aSolarSystems.keySet()) {
			// translate and scale accordingly
			final Point3D point = ss.getPoint();
			final Transform t = aSolarSystems.get(ss);
			t.translate(point.x * scale + center.x, point.y * scale + center.y, 0);
			t.setScale(scale * ss.getSize());
		}
	}

	@Override
	public void setBounds(final Bounds pBounds)
	{
		super.setBounds(pBounds);
		rescale();
	}
}
