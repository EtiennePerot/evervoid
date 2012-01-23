package com.evervoid.client.graphics.materials;

/**
 * Raised when a texture is not found or cannot be loaded.
 */
public class TextureException extends Exception
{
	/**
	 * ID for serialization
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The name of the texture for which the exception was raised
	 */
	private final String aTextureName;

	/**
	 * Constructor
	 * 
	 * @param textureName
	 *            The name of the texture for which the exception was raised
	 */
	public TextureException(final String textureName)
	{
		aTextureName = textureName;
	}

	@Override
	public String toString()
	{
		return "Invalid texture: " + aTextureName;
	}
}
