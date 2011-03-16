package com.evervoid.state.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class TrailData implements Jsonable
{
	public enum TrailKind implements Jsonable
	{
		BUBBLE, GRADUAL;
		@Override
		public Json toJson()
		{
			switch (this) {
				case BUBBLE:
					return new Json("bubble");
				default:
					return new Json("gradual");
			}
		}
	}

	private final String aType;
	public final SpriteData baseSprite;
	public final float decayTime;
	public final float distanceInterval;
	public final SpriteData engineSprite;
	/**
	 * This type of trail: Can be "gradual" (square ships) or "bubble" (round ships)
	 */
	public final TrailKind trailKind;
	public final List<SpriteData> trailSprites = new ArrayList<SpriteData>();

	TrailData(final String type, final String race, final Json j)
	{
		aType = type;
		if (j.getStringAttribute("kind").equalsIgnoreCase("bubble")) {
			trailKind = TrailKind.BUBBLE;
			engineSprite = new SpriteData("ships/" + race + "/" + type + ".png");
			baseSprite = new SpriteData("ships/" + race + "/" + type + "_trail.png");
		}
		else {
			trailKind = TrailKind.GRADUAL;
			engineSprite = new SpriteData("ships/" + race + "/" + type + ".png");
			baseSprite = null;
			int trails = 1;
			while (new File(GraphicManager.getSpritePath("ships/" + race + "/" + type + "_trail." + trails + ".png")).exists()) {
				trailSprites.add(new SpriteData("ships/" + race + "/" + type + "_trail." + trails + ".png"));
				trails++;
			}
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
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json().setAttribute("kind", trailKind);
		if (decayTime != 0) {
			j.setFloatAttribute("decay", decayTime);
		}
		if (distanceInterval != 1) {
			j.setFloatAttribute("distance", distanceInterval);
		}
		return j;
	}
}
