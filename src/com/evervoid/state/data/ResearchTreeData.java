package com.evervoid.state.data;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

/**
 * ResearchTreeData is a {@link ResearchData} container representing the whole research tree that a race can attain.
 */
public class ResearchTreeData implements Jsonable
{
	/**
	 * All Research the race can attain, mapped to their types.
	 */
	private final Map<String, ResearchData> aResearches = new HashMap<String, ResearchData>();
	/**
	 * The in game title of the research tree.
	 */
	private final String aTitle;
	/**
	 * The Research's type.
	 */
	private final String aType;

	/**
	 * Creates a ResearchTree that is associated with the given race from the contents of the Json, with the given title.
	 * 
	 * @param type
	 *            The Research's type.
	 * @param race
	 *            The Race that can research this tree.
	 * @param j
	 *            The contents of the tree.
	 */
	public ResearchTreeData(final String type, final String race, final Json j)
	{
		aType = type;
		aTitle = j.getStringAttribute("title");
		final Json upgrades = j.getAttribute("upgrades");
		for (final String research : upgrades.getAttributes()) {
			aResearches.put(research, new ResearchData(research, race, upgrades.getAttribute(research)));
		}
	}

	/**
	 * @return The type of the ResearchTree.
	 */
	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("title", aTitle).setMapAttribute("upgrades", aResearches);
	}
}
