package com.evervoid.client.views.galaxy;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.UISolarSystem;
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
	private final Set<UISolarSystem> aSolarSet;
	private final float scaleFactor;

	/**
	 * Default constructor. Takes in a galaxy to create a view based on that galaxy.
	 * 
	 * @param pGalaxy
	 *            The galaxy to create a view for.
	 */
	public GalaxyView(final Galaxy pGalaxy)
	{
		EVFrameManager.register(this);
		aSolarSet = new HashSet<UISolarSystem>();
		final Set<Point3D> pointSet = pGalaxy.getSolarPoints();
		final float camDimension = EverVoidClient.getCameraDimension();
		final float screenSize = EverVoidClient.getWindowDimension().getWidthFloat();
		scaleFactor = camDimension / pGalaxy.getSize();
		for (final Point3D point : pointSet) {
			final UISolarSystem tempSS = new UISolarSystem(pGalaxy.getSolarSystem(point), scaleFactor);
			tempSS.setLocation(point.x * 10 / pGalaxy.getSize(), point.y * 10 / pGalaxy.getSize(),
					point.z * 10 / pGalaxy.getSize());
			addSolarNode(tempSS);
			System.out.println("SS at: " + point.x * 10 / pGalaxy.getSize() + " " + point.y * 10 / pGalaxy.getSize());
		}
	}

	/**
	 * @param pSolar
	 *            Solar system to add to the galaxy.
	 */
	public void addSolarNode(final UISolarSystem pSolar)
	{
		aSolarSet.add(pSolar);
		addNode(pSolar);
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
		// 1. Reset results list.
		final CollisionResults results = new CollisionResults();
		// 2. Aim the ray from cam loc to cam direction.
		System.out.println(position);
		final Ray ray = EverVoidClient.getRayFromVector(position);
		System.out.println(ray);
		// 3. Collect intersections between Ray and Shootables in results list.
		collideWith(ray, results);
		// TODO - deal with rotations
		if (results.size() > 0) {
			final CollisionResult closest = results.getClosestCollision();
			System.out.println("Hit " + closest);
		}
		else {
			System.out.println("Nope");
		}
		return null;
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		GameView.changeView(GameViewType.SOLAR, getSolarSystemFromVector(position));
		return super.onMouseClick(position, tpf);
	}
}
