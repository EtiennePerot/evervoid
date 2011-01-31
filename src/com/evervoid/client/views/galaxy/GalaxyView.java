package com.evervoid.client.views.galaxy;

import java.util.Set;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.GameView;
import com.evervoid.client.views.GameView.GameViewType;
import com.evervoid.state.Galaxy;
import com.evervoid.state.Point3D;
import com.evervoid.state.SolarSystem;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;

public class GalaxyView extends EverView implements FrameObserver
{
	/**
	 * The Galaxy this view represents
	 */
	private final Galaxy aGalaxy;
	/**
	 * The current scale of everything in the view.
	 */
	private final float scaleFactor;

	/**
	 * Default constructor. Takes in a galaxy to create a view based on that galaxy.
	 * 
	 * @param pGalaxy
	 *            The galaxy to create a view for.
	 */
	public GalaxyView(final Galaxy pGalaxy)
	{
		aGalaxy = pGalaxy;
		EVFrameManager.register(this);
		final Set<Point3D> pointSet = pGalaxy.getSolarPoints();
		final float camDimension = EverVoidClient.getCameraDimension();
		scaleFactor = camDimension / pGalaxy.getSize();
		for (final Point3D point : pointSet) {
			final UISolarSystem tempSS = new UISolarSystem(point, scaleFactor * aGalaxy.getSolarSystem(point).getRadius());
			tempSS.setTranslation(point.x * 10f / pGalaxy.getSize(), point.y * 10f / pGalaxy.getSize(),
					point.z * 10f / pGalaxy.getSize());
			addSolarNode(tempSS);
		}
	}

	/**
	 * attaches the UISolarSystem to this View.
	 * 
	 * @param pSolar
	 *            Solar system to add to the galaxy.
	 */
	public void addSolarNode(final UISolarSystem pSolar)
	{
		attachChild(pSolar);
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * Get the solar system under the specified vector.
	 * 
	 * @param position
	 *            A 2D vector representing a position on a plane.
	 * @return A solar system located under the given position.
	 */
	public SolarSystem getSolarSystemFromVector(final Vector2f position)
	{
		final CollisionResults results = new CollisionResults();
		final Ray ray = EverVoidClient.getRayFromVector(position);
		collideWith(ray, results);
		if (results.size() > 0) {
			final CollisionResult closest = results.getClosestCollision();
			final UISolarSystem tempSS = (UISolarSystem) closest.getGeometry();
			return aGalaxy.getSolarSystem(tempSS.getPoint());
		}
		else {
			// TODO - if selection system, de-select
		}
		// TODO - remove once our hack is fixed, this will create null pointers all over the place.
		return null;
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		GameView.changeView(GameViewType.SOLAR, getSolarSystemFromVector(position));
		return super.onMouseClick(position, tpf);
	}
}
