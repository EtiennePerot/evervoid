package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.graphics.materials.GlowTextured;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.Point3D;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class UISolarSystem extends EverNode
{
	private final Geometry aGeometry;
	private final Point3D aLocation;
	private GlowTextured aMaterial;

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
		final Sphere sphere = new Sphere(30, 30, size);
		sphere.setTextureMode(Sphere.TextureMode.Projected);
		aGeometry = new Geometry("Solar System at " + ss.getPoint3D(), sphere);
		final SpriteData spriteInfo = ss.getStar().getSprite();
		try {
			aMaterial = new GlowTextured(spriteInfo.sprite);
			aGeometry.setMaterial(aMaterial);
		}
		catch (final TextureException e) {
			System.err.println("Warning: Could not load texture! Info = " + spriteInfo);
		}
		ColorRGBA glowColor = GraphicsUtils.getColorRGBA(ss.getStar().getGlowColor());
		glowColor = new ColorRGBA(MathUtils.clampFloat(0, glowColor.r * 10, 1), MathUtils.clampFloat(0, glowColor.g * 10, 1),
				MathUtils.clampFloat(0, glowColor.b * 10, 1), glowColor.a);
		System.out.println("Making glow: " + glowColor);
		aMaterial.setGlow(glowColor);
		attachChild(aGeometry);
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
