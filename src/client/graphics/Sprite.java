package client.graphics;

import client.EverNode;
import client.graphics.materials.AlphaTextured;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class Sprite extends EverNode
{
	private static final int sSpriteScale = 2;
	private final AlphaTextured aMaterial;

	public Sprite(final String image)
	{
		super();
		aMaterial = new AlphaTextured(image);
		final Quad q = new Quad(aMaterial.getWidth() * Sprite.sSpriteScale, aMaterial.getHeight() * Sprite.sSpriteScale);
		final Geometry g = new Geometry("Ship-" + hashCode(), q);
		g.setMaterial(aMaterial);
		attachChild(g);
		getNewTranslation().translate(-aMaterial.getWidth() * Sprite.sSpriteScale / 2,
				-aMaterial.getHeight() * Sprite.sSpriteScale / 2);
	}

	public void setHue(final ColorRGBA hue)
	{
		aMaterial.setHue(hue);
	}

	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		aMaterial.setHue(hue, multiplier);
	}
}
