package com.evervoid.client.views.galaxy;

import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.materials.PlainColor;
import com.evervoid.state.Color;
import com.evervoid.state.Point3D;
import com.evervoid.state.SolarSystem;
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
	public UISolarSystem(final SolarSystem ss, final float size)
	{
		super();
		aSphere = new Geometry("Solar System at " + ss.getPoint3D(), new Sphere(20, 20, size));
		final Color sunColor = ss.getSunGlowColor();
		final ColorRGBA colorRGBA = new ColorRGBA(sunColor.red, sunColor.green, sunColor.blue, sunColor.alpha);
		aMaterial = new PlainColor(colorRGBA);
		aSphere.setMaterial(aMaterial);
		attachChild(aSphere);
		aLocation = ss.getPoint3D();
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
