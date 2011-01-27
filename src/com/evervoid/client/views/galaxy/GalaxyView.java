package com.evervoid.client.views.galaxy;

import java.util.Set;

import com.evervoid.client.ClientView;
import com.evervoid.client.EverNode;
import com.evervoid.client.FrameManager;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.state.Galaxy;
import com.evervoid.state.Point3D;
import com.evervoid.state.SolarSystem;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class GalaxyView extends ClientView implements FrameObserver
{
	// private final GalaxyGrid aGrid;
	public GalaxyView(final Galaxy galaxy)
	{
		FrameManager.register(this);
		// aGrid = new GalaxyGrid(this);
		// addNode(aGrid);
		final Set<Point3D> pointSet = galaxy.getSolarPoints();
		final int i = 0;
		final float scaleFactor = galaxy.getSize();
		for (final Point3D point : pointSet) {
			final SolarSystem ss = galaxy.getSolarSystem(point);
			final Sphere s1 = new Sphere(20, 20, ss.getSize() / scaleFactor);
			final Geometry blue = new Geometry("Sphere", s1);
			final Material mat1 = new Material(GraphicManager.gAssets, "Common/MatDefs/Misc/SolidColor.j3md");
			mat1.setColor("m_Color", ColorRGBA.Blue);
			blue.setLocalTranslation(point.x / 15, point.y / 15, point.z / 15);
			blue.setMaterial(mat1);
			final EverNode pivot = new EverNode();
			attachChild(pivot);
			// attach the two boxes to the *pivot* node!
			pivot.attachChild(blue);
			// rotate pivot node: Both boxes have rotated!
			pivot.rotate(0.4f, 0.4f, 0.0f);
		}
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		// TODO Auto-generated method stub
	}
}
