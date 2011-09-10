package com.evervoid.state.data;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.Research;

/**
 * ResearchData is all the data needed to serialize a {@link Research} Object. It contains information about it's cost, buil
 * time, effects, description...
 */
public class ResearchData implements Jsonable
{
	/**
	 * Whether this Research starts out unlocked.
	 */
	private boolean aAutoUnlock = false;
	/**
	 * The in game description of the Research.
	 */
	private String aDescription = "";
	/**
	 * The Trail this Research unlocks for all Ships owned by the Player.
	 */
	private String aNewTrail = null;
	/**
	 * The Set of conditions to be met before this research may be started.
	 */
	private final Set<String> aPreconditions = new HashSet<String>();
	/**
	 * The speed multiplier this Research grants to all Ship once complete.
	 */
	private float aSpeedMultiplier = 1f;
	/**
	 * The in game title of the Research.
	 */
	private String aTitle = "";
	/**
	 * The Research's type.
	 */
	private final String aType;

	/**
	 * Creates a new ResearchData object for the given race from the contents of the Json, and with the given type.
	 * 
	 * @param researchType
	 *            Type The type of the Research.
	 * @param race
	 *            The Race that can build this Research.
	 * @param j
	 *            The Json containing info on the Research.
	 */
	public ResearchData(final String researchType, final String race, final Json j)
	{
		aDescription = j.getStringAttribute("description");
		aTitle = j.getStringAttribute("title");
		aType = researchType;
		for (final String s : j.getStringListAttribute("preconditions")) {
			aPreconditions.add(s);
		}
		if (j.hasAttribute("autounlock")) {
			aAutoUnlock = j.getBooleanAttribute("autounlock");
		}
		if (j.hasAttribute("speedmultiplier")) {
			aSpeedMultiplier = j.getFloatAttribute("speedmultiplier");
		}
		if (j.hasAttribute("newtrail")) {
			aNewTrail = j.getStringAttribute("newtrail");
		}
	}

	/**
	 * @return The type of the Research.
	 */
	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json().setAttribute("title", aTitle).setAttribute("description", aDescription)
				.setListAttribute("preconditions", aPreconditions);
		if (aAutoUnlock) {
			j.setAttribute("autounlock", true);
		}
		if (aSpeedMultiplier != 1f) {
			j.setAttribute("speedmultiplier", aSpeedMultiplier);
		}
		if (aNewTrail != null) {
			j.setAttribute("newtrail", aNewTrail);
		}
		return j;
	}
}
