package com.evervoid.client.views.galaxy;

import java.util.Map;

import com.evervoid.client.EverNode;
import com.evervoid.client.FrameManager;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.GameView;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.state.Point3D;
import com.evervoid.state.SolarSystem;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class GalaxyView extends GameView implements FrameObserver
{
	// private final GalaxyGrid aGrid;
	public GalaxyView(final Map<SolarSystem, Point3D> pGalaxy)
	{
		super();
		FrameManager.register(this);
		// aGrid = new GalaxyGrid(this);
		// addNode(aGrid);
		final Sphere s1 = new Sphere(16, 16, 1);
		final Geometry blue = new Geometry("Box", s1);
		final Material mat1 = new Material(GraphicManager.gAssets, "Common/MatDefs/Misc/SolidColor.j3md");
		mat1.setColor("m_Color", ColorRGBA.Blue);
		blue.setMaterial(mat1);
		// create a red box straight above the blue one at (1,3,1)
		final Box box2 = new Box(new Vector3f(1, 3, 1), 1, 1, 1);
		final Geometry red = new Geometry("Box", box2);
		final Material mat2 = new Material(GraphicManager.gAssets, "Common/MatDefs/Misc/SolidColor.j3md");
		mat2.setColor("m_Color", ColorRGBA.Red);
		red.setMaterial(mat2);
		// create a pivot node at (0,0,0) and attach it to root
		final EverNode pivot = new EverNode();
		attachChild(pivot);
		// attach the two boxes to the *pivot* node!
		pivot.attachChild(blue);
		pivot.attachChild(red);
		// rotate pivot node: Both boxes have rotated!
		pivot.rotate(0.4f, 0.4f, 0.0f);
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		// TODO Auto-generated method stub
	}
}
