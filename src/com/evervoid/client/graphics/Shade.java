package com.evervoid.client.graphics;

import com.evervoid.client.graphics.materials.AlphaShaded;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.data.SpriteData;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 * A base shadow that can be applied to a given sprite.
 */
public class Shade extends EverNode implements Sizable, Shadable
{
	/**
	 * The {@link AlphaShaded} material is used to generate shadows.
	 */
	private AlphaShaded aMaterial;

	/**
	 * Constructor; creates the shadow.
	 * 
	 * @param sprite
	 *            The sprite to shade; NOT the shadow map! The shadow map will automatically be detected if there is one; if
	 *            there isn't, the sprite itself will be used as its own shadow map.
	 */
	public Shade(final SpriteData sprite)
	{
		try {
			aMaterial = new AlphaShaded(sprite.sprite);
			final Quad q = new Quad(aMaterial.getWidth(), aMaterial.getHeight());
			final Geometry g = new Geometry("Shade of " + sprite.sprite + " @ " + hashCode(), q);
			g.setMaterial(aMaterial);
			attachChild(g);
			getNewTransform().translate(-aMaterial.getWidth() * sprite.scale / 2, -aMaterial.getHeight() * sprite.scale / 2)
					.move(sprite.x, sprite.y).setScale(sprite.scale);
		}
		catch (final TextureException e) {
			// Do nothing; just a blank node
			System.err.println("Warning: Could not load Shade! Info: " + sprite);
		}
	}

	/**
	 * Convenience constructor; creates the shadow
	 * 
	 * @param image
	 *            The file name of the sprite to shade; NOT the shadow map! The shadow map will automatically be detected if
	 *            there is one; if there isn't, the sprite itself will be used as its own shadow map.
	 */
	public Shade(final String image)
	{
		this(new SpriteData(image));
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
