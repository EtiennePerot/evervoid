package com.evervoid.client.graphics;

import com.evervoid.client.EverNode;
import com.evervoid.state.SolarSystem;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class UISolarSystem extends EverNode
{
	final private Geometry aSolarGeo;

	public UISolarSystem(final SolarSystem pSolarSystem)
	{
		final Sphere s1 = new Sphere(20, 20, Math.max(pSolarSystem.getHeight(), pSolarSystem.getWidth()));
		aSolarGeo = new Geometry("Sphere", s1);
		final Material mat1 = new Material(GraphicManager.gAssets, "Common/MatDefs/Misc/SolidColor.j3md");
		mat1.setColor("m_Color", ColorRGBA.Blue);
		aSolarGeo.setMaterial(mat1);
		attachChild(aSolarGeo);
	}

	public Geometry getLocation()
	{
		return aSolarGeo;
	}

	public void setLocation(final float x, final float y, final float z)
	{
		aSolarGeo.setLocalTranslation(x, y, z);
	}
}
