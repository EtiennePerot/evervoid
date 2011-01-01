package client.graphics;

import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture2D;

public class BaseTexture
{
	private final Texture2D aTexture;

	public BaseTexture(final Texture2D texture)
	{
		aTexture = texture;
	}

	public Texture2D getTexture()
	{
		return aTexture;
	}

	public void setSpriteFilters()
	{
		aTexture.setMagFilter(MagFilter.Nearest);
		aTexture.setMinFilter(MinFilter.NearestNoMipMaps);
	}
}
