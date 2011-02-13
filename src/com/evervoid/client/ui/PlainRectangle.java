package com.evervoid.client.ui;

import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.materials.PlainColor;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class PlainRectangle extends EverNode
{
	private final Geometry aRectangle;

	public PlainRectangle(final Vector3f origin, final float width, final float height, final ColorRGBA fill)
	{
		getNewTransform().translate(origin);
		final Quad q = new Quad(width, height);
		aRectangle = new Geometry("Quad-" + hashCode(), q);
		aRectangle.setMaterial(new PlainColor(fill));
		attachChild(aRectangle);
	}

	public void setColor(final ColorRGBA newColor)
	{
		aRectangle.setMaterial(new PlainColor(newColor));
	}
}
