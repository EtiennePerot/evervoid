package com.evervoid.client.graphics;

import com.evervoid.client.graphics.materials.PlainColor;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class PlainRectangle extends EverNode
{
	private final PlainColor aMaterial;
	private final Geometry aRectangle;

	public PlainRectangle(final Vector2f origin, final float width, final float height, final ColorRGBA fill)
	{
		this(new Vector3f(origin.x, origin.y, 0), width, height, fill);
	}

	public PlainRectangle(final Vector3f origin, final float width, final float height, final ColorRGBA fill)
	{
		getNewTransform().translate(origin);
		final Quad q = new Quad(width, height);
		aRectangle = new Geometry("Quad-" + hashCode(), q);
		aMaterial = new PlainColor(fill);
		aRectangle.setMaterial(aMaterial);
		attachChild(aRectangle);
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}

	public void setColor(final ColorRGBA newColor)
	{
		aRectangle.setMaterial(new PlainColor(newColor));
	}
}
