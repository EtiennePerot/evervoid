package com.evervoid.client.views.galaxy;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverNode;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.EverVoidClient.NodeType;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.geometry.AnimatedScaling;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.GameView;
import com.evervoid.client.views.GameView.PerspectiveType;
import com.evervoid.state.Galaxy;
import com.evervoid.state.Point3D;
import com.evervoid.state.SolarSystem;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GalaxyView extends EverView implements FrameObserver
{
	private final static float fCameraBounds = 10f;
	private final AnimatedScaling aAnimatedScale;
	/**
	 * The Galaxy this view represents
	 */
	private final Galaxy aGalaxy;
	/**
	 * The scale of camera to galaxy size.
	 */
	private final float aScale;
	/**
	 * A set Containing all the UISolarSystems in the view.
	 */
	private final Set<UISolarSystem> aSolarSet;
	private final EverNode aUISolarSystemContainer;

	/**
	 * Default constructor. Take galaxy to create a view based on that galaxy.
	 * 
	 * @param pGalaxy
	 *            The galaxy to create a view for.
	 */
	public GalaxyView(final Galaxy pGalaxy)
	{
		aGalaxy = pGalaxy;
		aSolarSet = new HashSet<UISolarSystem>();
		aUISolarSystemContainer = new EverNode();
		EVFrameManager.register(this);
		final Set<Point3D> pointSet = pGalaxy.getSolarPoints();
		final Vector3f minPoint = new Vector3f();
		final Vector3f maxPoint = new Vector3f();
		for (final Point3D point : pointSet) {
			final Vector3f temp = new Vector3f(point.x, point.y, point.z);
			minPoint.minLocal(temp);
			maxPoint.maxLocal(temp);
		}
		aScale = .8f * fCameraBounds / pGalaxy.getSize();
		for (final Point3D point : pointSet) {
			final SolarSystem ss = aGalaxy.getSolarSystemByPoint3D(point);
			final UISolarSystem tempSS = new UISolarSystem(ss, aScale * ss.getRadius());
			tempSS.getNewTransform().translate(point.x * aScale, point.y * aScale, point.z * aScale);
			addSolarNode(tempSS);
		}
		aAnimatedScale = getNewScalingAnimation();
		aAnimatedScale.setDuration(1f);
		// start at 60% of max
		aAnimatedScale.multTarget(.5f).start();
		addNode(new UIBackgroundStarfield(EverVoidClient.getWindowDimension().width / fCameraBounds, EverVoidClient
				.getWindowDimension().height
				/ fCameraBounds));
		addNode(aUISolarSystemContainer);
	}

	/**
	 * attaches the UISolarSystem to this View.
	 * 
	 * @param pSolar
	 *            Solar system to add to the galaxy.
	 */
	public void addSolarNode(final UISolarSystem pSolar)
	{
		aSolarSet.add(pSolar);
		aUISolarSystemContainer.addNode(pSolar);
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public NodeType getNodeType()
	{
		return NodeType.THREEDIMENSION;
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
		aUISolarSystemContainer.collideWith(ray, results);
		if (results.size() > 0) {
			final CollisionResult closest = results.getClosestCollision();
			final UISolarSystem tempSS = (UISolarSystem) closest.getGeometry().getParent();
			return aGalaxy.getSolarSystemByPoint3D(tempSS.getPoint());
		}
		else {
			// TODO - if selection system, de-select
			// No, no selection system; solar system info is shown on mouse hover
		}
		// TODO - remove once our hack is fixed, this will create null pointers all over the place.
		return null;
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		final SolarSystem picked = getSolarSystemFromVector(position);
		if (picked != null) {
			GameView.changePerspective(PerspectiveType.SOLAR, picked);
		}
		return super.onMouseClick(position, tpf);
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		rescale(-delta);
		return true;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		rescale(delta);
		return true;
	}

	/**
	 * scales the objects in the view by the given factor, number should be relative scale (ie. 1 represents no change).
	 * Negative number implies a decrease in size.
	 * 
	 * @param pFactor
	 *            The amount to scale all objects by
	 */
	private void rescale(final float pFactor)
	{
		final float scalingFactor = FastMath.pow(1.2f, FastMath.sign(pFactor));
		if (aAnimatedScale.getTargetScaleAverage() * scalingFactor <= 1
				&& aAnimatedScale.getTargetScaleAverage() * scalingFactor >= .2) {
			aAnimatedScale.multTarget(scalingFactor).start();
		}
	}
}
