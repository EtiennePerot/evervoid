package client.graphics;

import com.jme3.math.Vector2f;
import com.jme3.texture.Image;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture2D;

public class BaseTexture
{
	private final Vector2f aDimension;
	private final Texture2D aTexture;

	public BaseTexture(final Texture2D texture)
	{
		aTexture = texture;
		final Image img = aTexture.getImage();
		aDimension = new Vector2f(img.getWidth(), img.getHeight());
		;
	}

	public Vector2f getDimension()
	{
		return aDimension;
	}

	public float getHeight()
	{
		return aDimension.y;
	}

	public Texture2D getTexture()
	{
		return aTexture;
	}

	public float getWidth()
	{
		return aDimension.x;
	}

	public void setSpriteFilters()
	{
		aTexture.setMagFilter(MagFilter.Nearest);
		aTexture.setMinFilter(MinFilter.NearestNoMipMaps);
	}
}
