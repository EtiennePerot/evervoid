package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.prop.Star;

/**
 * StarData contains all the data necessary to serialize a {@link Star}. It determines the sprites, size... of the Star.
 */
public class StarData implements Jsonable
{
	/**
	 * The Sprite of the Star's surrounding halo.
	 */
	private final SpriteData aBorderSprite;
	/**
	 * The Star's dimension.
	 */
	private final Dimension aDimension;
	/**
	 * The Color with which the star glows.
	 */
	private final Color aGlowColor;
	/**
	 * The amount of radiation this Star emits.
	 */
	private final float aRadiation;
	/**
	 * The Color of the shadow this Star casts on other Props.
	 */
	private final Color aShadowColor;
	/**
	 * This Star's Sprite.
	 */
	private final SpriteData aSprite;
	/**
	 * This Star's type.
	 */
	private final String aType;

	/**
	 * Creates a Star from the contents of the Json.
	 * 
	 * @param type
	 * @param j
	 */
	public StarData(final String type, final Json j)
	{
		aType = type;
		aDimension = new Dimension(j.getAttribute("dimension"));
		aGlowColor = new Color(j.getAttribute("glow"));
		aShadowColor = new Color(j.getAttribute("shadow"));
		aRadiation = j.getFloatAttribute("radiation");
		aSprite = new SpriteData("stars/" + type + ".png");
		aBorderSprite = new SpriteData("stars/" + type + "_border.png");
	}

	/**
	 * @return The Sprite of the halo surrounding the Star.
	 */
	public SpriteData getBorderSprite()
	{
		return aBorderSprite;
	}

	/**
	 * @return The Star's dimmension.
	 */
	public Dimension getDimension()
	{
		return aDimension;
	}

	/**
	 * @return The Star's glow color.
	 */
	public Color getGlowColor()
	{
		return aGlowColor;
	}

	/**
	 * @return The Star's radiation output.
	 */
	public float getRadiation()
	{
		return aRadiation;
	}

	/**
	 * @return The color of the shadow the Star casts.
	 */
	public Color getShadowColor()
	{
		return aShadowColor;
	}

	/**
	 * @return The Star's sprite.
	 */
	public SpriteData getSprite()
	{
		return aSprite;
	}

	/**
	 * @return The Star's type.
	 */
	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("radiation", aRadiation);
		j.setAttribute("glow", aGlowColor);
		j.setAttribute("shadow", aShadowColor);
		j.setAttribute("dimension", aDimension);
		return j;
	}
}
