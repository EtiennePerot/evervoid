package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.graphics.materials.PlainColor;
import com.evervoid.state.geometry.Point3D;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

public class UIGalaxyBackgroundStar extends EverNode
{
	private final Spatial aGeometry;
	private final PlainColor aMaterial;

	public UIGalaxyBackgroundStar(final Point3D point, final float size)
	{
		final Sphere sphere = new Sphere(10, 10, size);
		sphere.setTextureMode(Sphere.TextureMode.Projected);
		aGeometry = new Geometry("Solar System at " + point, sphere);
		aMaterial = new PlainColor(new ColorRGBA(MathUtils.getRandomFloatBetween(0.6, 1), MathUtils.getRandomFloatBetween(0.7,
				1), MathUtils.getRandomFloatBetween(0.8, 1), MathUtils.getRandomFloatBetween(0.8, 1)));
		aGeometry.setMaterial(aMaterial);
		attachChild(aGeometry);
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}
}
