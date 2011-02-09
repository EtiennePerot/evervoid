package com.evervoid.gamedata;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class TrailData implements Jsonable
{
	public enum TrailKind
	{
		BUBBLE, GRADUAL;
	}

	private final String aType;
	public final SpriteInfo baseSprite;
	public final float decayTime;
	public final float distanceInterval;
	public final SpriteInfo engineSprite;
	/**
	 * This type of trail: Can be "gradual" (square ships) or "bubble" (round ships)
	 */
	public final TrailKind trailKind;
	public final List<String> trailSprites = new ArrayList<String>();

	TrailData(final String type, final Json j)
	{
		aType = type;
		if (j.getStringAttribute("kind").equalsIgnoreCase("bubble")) {
			trailKind = TrailKind.BUBBLE;
		}
		else {
			trailKind = TrailKind.GRADUAL;
		}
		if (j.hasAttribute("basesprite")) {
			baseSprite = SpriteInfo.fromJson(j.getAttribute("basesprite"));
		}
		else {
			baseSprite = null;
		}
		if (j.hasAttribute("decay")) {
			decayTime = j.getFloatAttribute("decay");
		}
		else {
			decayTime = 0;
		}
		if (j.hasAttribute("distance")) {
			distanceInterval = j.getFloatAttribute("distance");
		}
		else {
			distanceInterval = 1;
		}
		if (j.hasAttribute("enginesprite")) {
			engineSprite = SpriteInfo.fromJson(j.getAttribute("enginesprite"));
		}
		else {
			engineSprite = null;
		}
		if (j.hasAttribute("trailsprites")) {
			trailSprites.addAll(j.getStringListAttribute("trailsprites"));
		}
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json().setStringAttribute("type", aType);
		if (baseSprite != null) {
			j.setAttribute("basesprite", baseSprite);
		}
		if (engineSprite != null) {
			j.setAttribute("enginesprite", engineSprite);
		}
		if (!trailSprites.isEmpty()) {
			j.setStringListAttribute("trailsprites", trailSprites);
		}
		if (decayTime != 0) {
			j.setFloatAttribute("decay", decayTime);
		}
		if (distanceInterval != 0) {
			j.setFloatAttribute("distance", distanceInterval);
		}
		return j;
	}
}
