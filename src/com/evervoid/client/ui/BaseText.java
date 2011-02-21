package com.evervoid.client.ui;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.Sizeable;
import com.evervoid.client.graphics.geometry.Transform;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class BaseText extends EverNode implements Sizeable
{
	private final Transform aBottomLeftOffset;
	private final ColorRGBA aColor;
	private final BitmapText aText;

	public BaseText(final String text, final ColorRGBA color)
	{
		aColor = color;
		aText = new BitmapText(GraphicManager.getFont("FIXME"));
		aText.setColor(aColor);
		attachChild(aText);
		aBottomLeftOffset = getNewTransform();
		setText(text);
	}

	@Override
	public Vector2f getDimensions()
	{
		return new Vector2f(getWidth(), getHeight());
	}

	@Override
	public float getHeight()
	{
		return aText.getLineHeight();
	}

	@Override
	public float getWidth()
	{
		return aText.getLineWidth();
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aText.setColor(new ColorRGBA(aColor.r, aColor.g, aColor.b, aColor.a * alpha));
	}

	public void setText(final String text)
	{
		aText.setText(text);
		System.out.println("Translating by " + getHeight());
		aBottomLeftOffset.translate(0, getHeight());
	}
}
