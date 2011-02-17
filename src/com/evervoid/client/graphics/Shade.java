package com.evervoid.client.graphics;

import com.evervoid.client.graphics.materials.AlphaShaded;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.data.SpriteInfo;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class Shade extends EverNode implements Sizeable, Shadable
{
	private AlphaShaded aMaterial;

	public Shade(final SpriteInfo sprite)
	{
		super();
		try {
			aMaterial = new AlphaShaded(sprite.sprite);
			final Quad q = new Quad(aMaterial.getWidth(), aMaterial.getHeight());
			final Geometry g = new Geometry("Shade of " + sprite.sprite + " @ " + hashCode(), q);
			g.setMaterial(aMaterial);
			attachChild(g);
			getNewTransform()
					.translate(-aMaterial.getWidth() * Sprite.sSpriteScale / 2,
							-aMaterial.getHeight() * Sprite.sSpriteScale / 2).move(sprite.x, sprite.y)
					.setScale(Sprite.sSpriteScale);
		}
		catch (final TextureException e) {
			// Do nothing; just a blank node
			System.err.println("Warning: Could not load Shade! Info: " + sprite);
		}
	}

	public Shade(final String image)
	{
		this(new SpriteInfo(image));
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

	@Override
	public Shadable setGradientPortion(final float gradientPortion)
	{
		aMaterial.setGradientPortion(gradientPortion);
		return this;
	}

	@Override
	public Shadable setShadeAngle(final float shadeAngle)
	{
		aMaterial.setShadeAngle(shadeAngle);
		return this;
	}

	@Override
	public Shadable setShadeColor(final ColorRGBA glowColor)
	{
		aMaterial.setShadeColor(glowColor);
		return this;
	}

	@Override
	public Shadable setShadePortion(final float shadePortion)
	{
		aMaterial.setShadePortion(shadePortion);
		return this;
	}
}
