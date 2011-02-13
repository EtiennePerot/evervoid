package com.evervoid.client.ui;

import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.materials.PlainColor;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

public class PlainLine extends EverNode
{
	private final Geometry aLine;

	public PlainLine(final Vector3f start, final Vector3f end, final float width, final ColorRGBA color)
	{
		final Line l = new Line(start, end);
		l.setLineWidth(width);
		aLine = new Geometry("Line-" + hashCode() + " (From " + start + " to " + end + ")", l);
		aLine.setMaterial(new PlainColor(color));
		attachChild(aLine);
	}

	public void setColor(final ColorRGBA newColor)
	{
		aLine.setMaterial(new PlainColor(newColor));
	}
}
