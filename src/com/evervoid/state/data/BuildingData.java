package com.evervoid.state.data;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.ResourceAmount;

public class BuildingData implements Jsonable
{
	public static String getBlankBuildingIcon()
	{
		return "buildings/_empty.png";
	}

	private final int aBuildTime;
	private final ResourceAmount aCost;
	private final ResourceAmount aIncome;
	private final int aRegenShields;
	private final List<String> aShipTypes;
	private final String aTitle;
	private final String aType;

	public BuildingData(final String type, final Json j)
	{
		aType = type;
		aTitle = j.getStringAttribute("title");
		aCost = new ResourceAmount(j.getAttribute("cost"));
		aBuildTime = j.getIntAttribute("buildTime");
		if (j.hasAttribute("canbuild")) {
			aShipTypes = j.getStringListAttribute("canbuild");
		}
		else {
			aShipTypes = new ArrayList<String>();
		}
		if (j.hasAttribute("regen")) {
			aRegenShields = j.getIntAttribute("regen");
		}
		else {
			aRegenShields = 0;
		}
		if (j.hasAttribute("income") && !j.getAttribute("income").isNull()) {
			aIncome = new ResourceAmount(j.getAttribute("income"));
		}
		else {
			aIncome = null;
		}
	}

	public List<String> getAvailableShipTypes()
	{
		return aShipTypes;
	}

	public int getBuildTime()
	{
		return aBuildTime;
	}

	public ResourceAmount getCost()
	{
		return aCost;
	}

	public String getIcon()
	{
		// TODO: Get sprites for buildings
		return "buildings/generic.png";
	}

	public ResourceAmount getIncome()
	{
		return aIncome;
	}

	public int getShieldRegen()
	{
		return aRegenShields;
	}

	public String getTitle()
	{
		return aTitle;
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setStringAttribute("title", aTitle);
		j.setAttribute("cost", aCost);
		j.setIntAttribute("buildTime", aBuildTime);
		j.setStringListAttribute("canbuild", aShipTypes);
		j.setAttribute("income", aIncome);
		j.setIntAttribute("regen", aRegenShields);
		return j;
	}
}
