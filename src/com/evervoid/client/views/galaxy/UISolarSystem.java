package com.evervoid.client.views.galaxy;

import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.materials.PlainColor;
import com.evervoid.state.Point3D;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class UISolarSystem extends EverNode
{
	private final Point3D aLocation;
	private final PlainColor aMaterial;
	private final Geometry aSphere;

	/**
	 * Create a UI representation of the solarSystem associated with the given point.
	 * 
	 * @param point
	 *            The point of the solar system this object represents.
	 * @param size
	 *            The size to scale the representation to.
	 */
	public UISolarSystem(final Point3D point, final float size)
	{
		super();
		aSphere = new Geometry("Solar System at " + point, new Sphere(20, 20, size));
		aMaterial = new PlainColor(ColorRGBA.Blue);
		aSphere.setMaterial(aMaterial);
		attachChild(aSphere);
		aLocation = point;
	}

	/**
	 * Gets the points associated with the solar System this UISolarSystem represents.
	 * 
	 * @return The SolarSystem's point.
	 */
	public Point3D getPoint()
	{
		return aLocation;
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}
}
