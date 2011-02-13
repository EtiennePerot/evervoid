package com.evervoid.client.graphics;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverNode;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.graphics.materials.SphericalMapped;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.gamedata.SpriteInfo;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class SphericalSprite extends EverNode implements FrameObserver
{
	private SphericalMapped aMaterial;
	private float aRotationTime = Float.MAX_VALUE;
	private final SpriteInfo aSpriteInfo;
	private Transform aSpriteTransform;
	private boolean aValidSprite = true;

	public SphericalSprite(final SpriteInfo sprite)
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
			aSpriteTransform.translate(-aMaterial.getHeight() * Sprite.sSpriteScale / 2 + sprite.x,
					-aMaterial.getHeight() * Sprite.sSpriteScale / 2 + sprite.y).commit();
			aSpriteTransform.setScale(Sprite.sSpriteScale);
		}
		catch (final TextureException e) {
			// Do nothing; just a blank node
			aValidSprite = false;
			System.err.println("Warning: Could not load SphericalSprite! Info = " + sprite);
		}
	}

	public SphericalSprite(final String sprite)
	{
		this(new SpriteInfo(sprite));
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		if (aMaterial != null) {
			aMaterial.addOffset(f.aTpf / aRotationTime);
		}
	}

	@Override
	public void setAlpha(final float alpha)
	{
		if (aMaterial != null) {
			aMaterial.setAlpha(alpha);
		}
	}

	public SphericalSprite setRotationTime(final float time)
	{
		aRotationTime = time;
		EVFrameManager.register(this);
		return this;
	}
}
