package com.evervoid.client.graphics;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.graphics.materials.SphericalMapped;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.state.data.SpriteData;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class SphericalSprite extends EverNode implements EVFrameObserver, Sizeable
{
	private SphericalMapped aMaterial;
	private float aRotationTime = Float.MAX_VALUE;
	private final SpriteData aSpriteInfo;
	private Transform aSpriteTransform;
	private boolean aValidSprite = true;

	public SphericalSprite(final SpriteData sprite)
	{
		aSpriteInfo = sprite;
		try {
			aMaterial = new SphericalMapped(sprite.sprite);
			// Calling two times getHeight() because it's a sphere so it doesn't matter.
			// Width of texture is twice as long so it's faster to get the height than to get the width and divide by 2
			final Quad q = new Quad(aMaterial.getHeight(), aMaterial.getHeight());
			final Geometry g = new Geometry("SphericalSprite-" + sprite.sprite + " @ " + hashCode(), q);
			g.setMaterial(aMaterial);
			final EverNode image = new EverNode(g);
			addNode(image);
			// Offset image so that the origin is around the center of the image
			aSpriteTransform = image.getNewTransform();
			aSpriteTransform.translate(-aMaterial.getHeight() * sprite.scale / 2 + sprite.x,
					-aMaterial.getHeight() * sprite.scale / 2 + sprite.y).commit();
			aSpriteTransform.setScale(sprite.scale);
		}
		catch (final TextureException e) {
			// Do nothing; just a blank node
			aValidSprite = false;
			System.err.println("Warning: Could not load SphericalSprite! Info = " + sprite);
		}
	}

	public SphericalSprite(final String sprite)
	{
		this(new SpriteData(sprite));
	}

	/**
	 * Cancels the centering offset on this Sprite
	 * 
	 * @return This
	 */
	public SphericalSprite bottomLeftAsOrigin()
	{
		aSpriteTransform.translate(0, 0);
		return this;
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		if (aMaterial != null) {
			aMaterial.addOffset(f.aTpf / aRotationTime);
		}
	}

	public SpriteData getData()
	{
		return aSpriteInfo;
	}

	@Override
	public Vector2f getDimensions()
	{
		return new Vector2f(getWidth(), getHeight());
	}

	@Override
	public float getHeight()
	{
		return aMaterial.getHeight() * aSpriteTransform.getScaleY();
	}

	@Override
	public float getWidth()
	{
		return aMaterial.getWidth() * aSpriteTransform.getScaleX();
	}

	public boolean isValidSprite()
	{
		return aValidSprite;
	}

	@Override
	public void setAlpha(final float alpha)
	{
		if (aMaterial != null) {
			aMaterial.setAlpha(alpha);
		}
	}

	/**
	 * Shaves a certain number of rendered pixels off the edge of this sphere
	 * 
	 * @param pixels
	 *            Number of pixels to shave
	 */
	public void setClipPixels(final int pixels)
	{
		if (aMaterial != null) {
			aMaterial.setClipPixels(pixels);
		}
	}

	/**
	 * Limits the rendered radius of the sphere
	 * 
	 * @param radius
	 *            The radius (from 0 to 1) to render
	 */
	public void setClipRadius(final float radius)
	{
		if (aMaterial != null) {
			aMaterial.setClipRadius(radius);
		}
	}

	public SphericalSprite setRotationTime(final float time)
	{
		aRotationTime = time;
		EVFrameManager.register(this);
		return this;
	}
}
