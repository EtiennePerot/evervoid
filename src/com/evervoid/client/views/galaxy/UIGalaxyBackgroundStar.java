package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.materials.BaseMaterial;
import com.evervoid.state.geometry.Point3D;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

public class UIGalaxyBackgroundStar extends EverNode
{
	private final Spatial aGeometry;
	private final BaseMaterial aMaterial;

	public UIGalaxyBackgroundStar(final Point3D point, final float size)
	{
		final Sphere sphere = new Sphere(30, 30, size);
		sphere.setTextureMode(Sphere.TextureMode.Projected);
		aGeometry = new Geometry("Solar System at " + point, sphere);
		aMaterial = new BaseMaterial("PlainColor");
		aMaterial.setColor("m_Color", ColorRGBA.randomColor());
		aGeometry.setMaterial(aMaterial);
		attachChild(aGeometry);
	}

	@Override
	public void setAlpha(final float alpha)
	{
		// aMaterial.setAlpha(alpha);
	}
}
