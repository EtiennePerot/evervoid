package com.evervoid.client.ui;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.materials.PlainColor;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

public class PlainLine extends EverNode
{
	private final Geometry aLine;
	private final PlainColor aMaterial;

	public PlainLine(final Vector2f start, final Vector2f end, final float width, final ColorRGBA color)
	{
		this(new Vector3f(start.x, start.y, 0), new Vector3f(end.x, end.y, 0), width, color);
	}

	public PlainLine(final Vector3f start, final Vector3f end, final float width, final ColorRGBA color)
	{
		final Line l = new Line(start, end);
		l.setLineWidth(width);
		aLine = new Geometry("PlainLine-" + hashCode() + " (From " + start + " to " + end + ")", l);
		aMaterial = new PlainColor(color);
		aLine.setMaterial(aMaterial);
		attachChild(aLine);
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}

	public void setColor(final ColorRGBA newColor)
	{
		aLine.setMaterial(new PlainColor(newColor));
	}
}
