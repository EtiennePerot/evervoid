package com.evervoid.client.graphics;

import com.evervoid.client.EverNode;
import com.evervoid.client.graphics.materials.AlphaTextured;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class Sprite extends EverNode implements Sizeable
{
	public static final float sSpriteScale = 2f;
	private final AlphaTextured aMaterial;

	public Sprite(final String image)
	{
		super();
		aMaterial = new AlphaTextured(image);
		final Quad q = new Quad(aMaterial.getWidth() * Sprite.sSpriteScale, aMaterial.getHeight() * Sprite.sSpriteScale);
		final Geometry g = new Geometry("Sprite-" + image + " @ " + hashCode(), q);
		g.setMaterial(aMaterial);
		attachChild(g);
		getNewTransform().translate(-aMaterial.getWidth() * Sprite.sSpriteScale / 2,
				-aMaterial.getHeight() * Sprite.sSpriteScale / 2);
	}

	@Override
	public Vector2f getDimensions()
	{
		return aMaterial.getDimensions();
	}

	@Override
	public float getHeight()
	{
		return aMaterial.getHeight();
	}

	@Override
	public float getWidth()
	{
		return aMaterial.getWidth();
	}

	@Override
	protected void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}

	public void setHue(final ColorRGBA hue)
	{
		aMaterial.setHue(hue);
	}

	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		aMaterial.setHue(hue, multiplier);
	}

	@Override
	public void setInternalAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}
}
