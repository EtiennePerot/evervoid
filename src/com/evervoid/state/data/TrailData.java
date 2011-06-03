package com.evervoid.state.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class TrailData implements Jsonable
{
	/**
	 * Defines how the trail will expand over time.
	 */
	public enum TrailKind implements Jsonable
	{
		/**
		 * The Trail leaves "bubbles" at regular intervals, which slowly fade away.
		 */
		BUBBLE,
		/**
		 * The Trail fades in slowly and then fades out slowly.
		 */
		GRADUAL;
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

	/**
	 * The base sprite for the Trail.
	 */
	public final SpriteData aBaseSprite;
	/**
	 * The Trail's type.
	 */
	private final String aType;
	/**
	 * The fade time of the Trail.
	 */
	public final float decayTime;
	/**
	 * The distance between each bubble if the Trail is of the BUBBLE variant.
	 */
	public final float distanceInterval;
	/**
	 * The Sprite for the engine.
	 */
	public final SpriteData engineSprite;
	/**
	 * This type of trail: Can be "gradual" (square ships) or "bubble" (round ships)
	 */
	public final TrailKind trailKind;
	public final List<SpriteData> trailSprites = new ArrayList<SpriteData>();

	/**
	 * Creates a new Trail Data with the given parameters.
	 * 
	 * @param type
	 *            The type of trail.
	 * @param race
	 *            The race of the ships with this type of trail.
	 * @param j
	 *            The Json containing the info pertinent to the trail.
	 */
	TrailData(final String type, final String race, final Json j)
	{
		aType = type;
		if (j.getStringAttribute("kind").equalsIgnoreCase("bubble")) {
			trailKind = TrailKind.BUBBLE;
			engineSprite = new SpriteData("ships/" + race + "/" + type + ".png");
			aBaseSprite = new SpriteData("ships/" + race + "/" + type + "_trail.png");
		}
		else {
			trailKind = TrailKind.GRADUAL;
			engineSprite = new SpriteData("ships/" + race + "/" + type + ".png");
			aBaseSprite = null;
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

	/**
	 * @return The type of the trail.
	 */
	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json().setAttribute("kind", trailKind);
		if (decayTime != 0) {
			j.setAttribute("decay", decayTime);
		}
		if (distanceInterval != 1) {
			j.setAttribute("distance", distanceInterval);
		}
		return j;
	}
}
