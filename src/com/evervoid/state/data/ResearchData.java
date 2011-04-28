package com.evervoid.state.data;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

/**
 * Represents a single research
 */
public class ResearchData implements Jsonable
{
	private boolean aAutoUnlock = false;
	private String aDescription = "";
	private final String aID;
	private String aNewTrail = null;
	private final Set<String> aPreconditions = new HashSet<String>();
	private float aSpeedMultiplier = 1f;
	private String aTitle = "";

	public ResearchData(final String researchID, final String researchTree, final String race, final Json j)
	{
		aDescription = j.getStringAttribute("description");
		aTitle = j.getStringAttribute("title");
		aID = researchID;
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

	public String getType()
	{
		return aID;
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
