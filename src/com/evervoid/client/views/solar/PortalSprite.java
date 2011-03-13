package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.BaseSprite;
import com.evervoid.client.graphics.materials.AlphaTextured;
import com.evervoid.client.graphics.materials.RotatedAlphaTextured;
import com.evervoid.client.graphics.materials.TextureException;
import com.evervoid.state.data.SpriteData;
import com.jme3.texture.Texture.MinFilter;

public class PortalSprite extends BaseSprite
{
	private RotatedAlphaTextured aMaterial;

	public PortalSprite()
	{
		super("space/wormhole.png");
	}

	@Override
	protected AlphaTextured buildMaterial(final SpriteData sprite) throws TextureException
	{
		aMaterial = new RotatedAlphaTextured(sprite.sprite);
		// Keep pixely look with scaling
		aMaterial.getTexture().setMinFilter(MinFilter.NearestNearestMipMap);
		return aMaterial;
	}

	public void setRotationAngle(final float angle)
	{
		aMaterial.setRotationAngle(angle);
	}
}
