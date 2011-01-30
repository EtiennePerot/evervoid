package com.evervoid.client.graphics;

import com.evervoid.state.Point3D;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class UISolarSystem extends Geometry
{
	private final Point3D aLocation;

	public UISolarSystem(final Point3D pPoint, final float scale)
	{
		super("Sphere", new Sphere(20, 20, scale));
		aLocation = pPoint;
		final Material mat1 = new Material(GraphicManager.gAssets, "Common/MatDefs/Misc/SolidColor.j3md");
		mat1.setColor("m_Color", ColorRGBA.Blue);
		setMaterial(mat1);
	}

	public Point3D getPoint()
	{
		return aLocation;
	}

	public void setTranslation(final float x, final float y, final float z)
	{
		setLocalTranslation(x, y, z);
	}
}
