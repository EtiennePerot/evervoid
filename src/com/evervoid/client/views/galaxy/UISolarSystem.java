package com.evervoid.client.views.galaxy;

import com.evervoid.client.EverNode;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.materials.BaseTexture;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.Point3D;
import com.evervoid.state.SolarSystem;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class UISolarSystem extends EverNode
{
	private final Geometry aGeometry;
	private final Point3D aLocation;
	private Material aMaterial;

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
		String sprite = "";
		try {
			sprite = ss.getStar().getSprite().sprite;
			aMaterial = EverVoidClient.getNewMaterial("Common/MatDefs/Misc/SimpleTextured.j3md");
			final BaseTexture texture = GraphicManager.getTexture(sprite);
			aMaterial.setTexture("Star Texture", texture.getTexture());
			aGeometry.setMaterial(aMaterial);
		}
		catch (final TextureException e) {
			System.err.println("Warning: Could not load SphericalSprite! Info = " + sprite);
		}
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
		// aMaterial.setAlpha(alpha);
	}
}
