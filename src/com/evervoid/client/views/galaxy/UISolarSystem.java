package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.state.Point3D;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class UISolarSystem extends Geometry
{
	private final Point3D aLocation;

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
		super("Sphere", new Sphere(20, 20, size));
		final Material mat1 = new Material(GraphicManager.gAssets, "Common/MatDefs/Misc/SolidColor.j3md");
		mat1.setColor("m_Color", ColorRGBA.Blue);
		setMaterial(mat1);
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

	/**
	 * Sets the translation of this object in 3D space.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setTranslation(final float x, final float y, final float z)
	{
		setLocalTranslation(x, y, z);
	}
}
