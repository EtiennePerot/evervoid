package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.materials.AlphaTextured;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.Point3D;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

public class UIBackgroundStar extends EverNode
{
	private final Spatial aGeometry;
	private AlphaTextured aMaterial;

	public UIBackgroundStar(final Point3D point, final String spriteInfo, final float size)
	{
		final Sphere sphere = new Sphere(30, 30, size);
		sphere.setTextureMode(Sphere.TextureMode.Projected);
		aGeometry = new Geometry("Solar System at " + point, sphere);
		try {
			aMaterial = new AlphaTextured(spriteInfo);
			aGeometry.setMaterial(aMaterial);
		}
		catch (final TextureException e) {
			System.err.println("Warning: Could not load texture! Info = " + spriteInfo);
		}
		attachChild(aGeometry);
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}
}
