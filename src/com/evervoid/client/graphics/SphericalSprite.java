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

/**
 * A sprite with its texture warped in a sphere-like manner.
 */
public class SphericalSprite extends EverNode implements EVFrameObserver, Sizable, Spherical
{
	/**
	 * Spere mapping is done with the {@link SphericalMapped} material.
	 */
	private SphericalMapped aMaterial;
	/**
	 * If rotation is enabled (off by default), this value holds the amount of time necessary (in seconds) for a full
	 * revolution.
	 */
	private float aRotationTime = Float.MAX_VALUE;
	/**
	 * Holds information about the sprite in use.
	 */
	private final SpriteData aSpriteInfo;
	/**
	 * Transform applied to the sprite to comply with SpriteData's offset, and to determine which point corresponds to the
	 * origin of the sprite. By default, (0, 0) corresponds to the middle of the sprite.
	 */
	private Transform aSpriteTransform;
	/**
	 * Whether the sprite successfully loaded or not (might fail if the image file doesn't exist, etc)
	 */
	private boolean aValidSprite = true;

	/**
	 * Main constructor; builds a SphericalSprite from a {@link SpriteData} object
	 * 
	 * @param sprite
	 *            The sprite information to build from
	 */
	public SphericalSprite(final SpriteData sprite)
	{
		aSpriteInfo = sprite;
		try {
			aMaterial = new SphericalMapped(aSpriteInfo.sprite);
			// Calling two times getHeight() because it's a sphere so it doesn't matter.
			// Width of texture is twice as long so it's faster to get the height than to get the width and divide by 2
			final Quad q = new Quad(aMaterial.getHeight(), aMaterial.getHeight());
			final Geometry g = new Geometry("SphericalSprite-" + aSpriteInfo.sprite + " @ " + hashCode(), q);
			g.setMaterial(aMaterial);
			final EverNode image = new EverNode(g);
			addNode(image);
			// Offset image so that the origin is around the center of the image
			aSpriteTransform = image.getNewTransform();
			aSpriteTransform.translate(-aMaterial.getHeight() * aSpriteInfo.scale / 2 + aSpriteInfo.x,
					-aMaterial.getHeight() * aSpriteInfo.scale / 2 + aSpriteInfo.y).commit();
			aSpriteTransform.setScale(aSpriteInfo.scale);
		}
		catch (final TextureException e) {
			// Do nothing; just a blank node
			aValidSprite = false;
			System.err.println("Warning: Could not load SphericalSprite! Info = " + aSpriteInfo);
		}
	}

	/**
	 * Convenience constructor; builds a sprite from an image file name
	 * 
	 * @param image
	 *            The image to load
	 */
	public SphericalSprite(final String image)
	{
		this(new SpriteData(image));
	}

	/**
	 * Cancels the centering offset on this SphericalSprite
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

	/**
	 * @return Whether the SphericalSprite loaded correctly or not.
	 */
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

	@Override
	public void setClipPixels(final int pixels)
	{
		if (aMaterial != null) {
			aMaterial.setClipPixels(pixels);
		}
	}

	@Override
	public void setClipRadius(final float radius)
	{
		if (aMaterial != null) {
			aMaterial.setClipRadius(radius);
		}
	}

	@Override
	public void setRotationTime(final float time)
	{
		aRotationTime = time;
		EVFrameManager.register(this);
	}
}
