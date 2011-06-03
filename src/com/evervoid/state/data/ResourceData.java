package com.evervoid.state.data;

import javax.annotation.Resource;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

/**
 * ResourceData represents the core data needed to serialize {@link Resource} Object. It defines it's type and title.
 */
public class ResourceData implements Jsonable
{
	/**
	 * The Resource's in game title.
	 */
	private final String aTitle;
	/**
	 * The Resource's type.
	 */
	private final String aType;

	/**
	 * Creates a ResourceData from the contents of the Json, and with the given type.
	 * 
	 * @param type
	 *            The Type of the Resource.
	 * @param j
	 *            The Json containing the Resource's data.
	 */
	public ResourceData(final String type, final Json j)
	{
		aType = type;
		aTitle = j.getStringAttribute("title");
	}

	/**
	 * @return The URL of the Resour'ce icon Sprite.
	 */
	public String getIconSpriteURL()
	{
		return "icons/resources/" + aType + ".png";
	}

	/**
	 * @return The in game title of the Resource.
	 */
	public String getTitle()
	{
		return aTitle;
	}

	/**
	 * @return The resource type.
	 */
	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("title", aTitle);
	}
}
