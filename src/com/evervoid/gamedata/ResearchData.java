package com.evervoid.gamedata;

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
		final Json j = new Json().setStringAttribute("title", aTitle).setStringAttribute("description", aDescription)
				.setStringListAttribute("preconditions", aPreconditions);
		if (aAutoUnlock) {
			j.setBooleanAttribute("autounlock", true);
		}
		if (aSpeedMultiplier != 1f) {
			j.setFloatAttribute("speedmultiplier", aSpeedMultiplier);
		}
		if (aNewTrail != null) {
			j.setStringAttribute("newtrail", aNewTrail);
		}
		return j;
	}
}
