package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.player.Research;
import com.evervoid.state.player.ResourceAmount;

public class PlanetData implements Jsonable
{
	/**
	 * The starting health for Planets of this type.
	 */
	private final int aBaseHealth;
	/**
	 * The starting health regeneration rate for Planets of this type.
	 */
	private final int aBaseHealthRegen;
	/**
	 * The starting sprite for Planets of this type.
	 */
	private final SpriteData aBaseSprite;
	/**
	 * The starting number of building slots on Planets of this type.
	 */
	private final int aBuildingSlots;
	/**
	 * The sprite on which the Player color is projected.
	 */
	private final SpriteData aColorGlowSprite;
	/**
	 * The dimension of Planets of this type.
	 */
	private final Dimension aDimension;
	/**
	 * The planet type.
	 */
	private final String aPlanetType;
	/**
	 * The starting resource rate for Planets of this type.
	 */
	private final ResourceAmount aResourceRates;
	/**
	 * The in game title for Planets of this type.
	 */
	private final String aTitle;
	/**
	 * The type of this PlanetData.
	 */
	private final String aType;

	PlanetData(final String type, final Json j)
	{
		aType = type;
		aPlanetType = j.getStringAttribute("planettype");
		aTitle = j.getStringAttribute("title");
		aDimension = new Dimension(j.getAttribute("dimension"));
		aBaseSprite = new SpriteData("planets/" + aPlanetType + "/" + aType + ".png");
		aColorGlowSprite = new SpriteData("planets/" + aPlanetType + "/" + aType + ".glow.png");
		aResourceRates = new ResourceAmount(j.getAttribute("resources"));
		aBuildingSlots = j.getIntAttribute("slots");
		aBaseHealth = j.getIntAttribute("health");
		aBaseHealthRegen = j.getIntAttribute("healthRegen");
	}

	/**
	 * @return The starting health for Planets of this type.
	 */
	public int getBaseHealth()
	{
		return aBaseHealth;
	}

	/**
	 * @return The base sprite for Planets of this type.
	 */
	public SpriteData getBaseSprite()
	{
		return aBaseSprite;
	}

	/**
	 * @return The dimension for Planets of this type.
	 */
	public Dimension getDimension()
	{
		return aDimension;
	}

	/**
	 * @return The sprite on which the Player color will be projected.
	 */
	public SpriteData getGlowSprite()
	{
		return aColorGlowSprite;
	}

	/**
	 * @return The health regeneration rate for Planets of this type at the given research level.
	 */
	public int getHealthRegenRate(final Research research)
	{
		// TODO deal with research
		return aBaseHealthRegen;
	}

	/**
	 * @return The number of building slots on Planets of this type.
	 */
	public int getNumOfBuildingSlots()
	{
		return aBuildingSlots;
	}

	/**
	 * @return The amount of resources produced by Planets of this type.
	 */
	public ResourceAmount getResourceRate()
	{
		return aResourceRates;
	}

	/**
	 * @return The in game name for this Planet type.
	 */
	public String getTitle()
	{
		return aTitle;
	}

	/**
	 * @return The planet type.
	 */
	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("dimension", aDimension);
		j.setAttribute("planettype", aPlanetType);
		j.setAttribute("title", aTitle);
		j.setAttribute("resources", aResourceRates);
		j.setAttribute("slots", aBuildingSlots);
		j.setAttribute("health", aBaseHealth);
		j.setAttribute("healthRegen", aBaseHealthRegen);
		return j;
	}
}
