package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.data.StarData;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.utils.MathUtils;

public class Star extends Prop
{
	/**
	 * Generates a random-type star
	 * 
	 * @param solarSystemDimension
	 *            Warning: This is the dimension of the ENTIRE solar system, not the dimension of the star. The Star will
	 *            automatically place itself in the middle of the solar system, hence the need to pass the total solar system
	 *            dimension
	 * @param state
	 *            The EverVoidGameState
	 * @return A star of a random type
	 */
	public static Star randomStar(final Dimension solarSystemDimension, final EVGameState state)
	{
		final String randomType = (String) MathUtils.getRandomElement(state.getStarTypes());
		final StarData data = state.getStarData(randomType);
		final int x = solarSystemDimension.width / 2 - data.getDimension().width / 2;
		final int y = solarSystemDimension.height / 2 - data.getDimension().height / 2;
		final GridLocation location = new GridLocation(x, y, data.getDimension());
		return new Star(state.getNextPropID(), location, state.getStarData(randomType), state);
	}

	private final StarData aData;

	public Star(final int id, final GridLocation location, final StarData data, final EVGameState state)
	{
		super(id, state.getNullPlayer(), location, "star", state);
		aData = data;
	}

	public Star(final Json j, final StarData data, final EVGameState state)
	{
		super(j, "star", state);
		aData = data;
	}

	public SpriteData getBorderSprite()
	{
		return aData.getBorderSprite();
	}

	public Color getGlowColor()
	{
		return aData.getGlowColor();
	}

	public float getRadiationLevel()
	{
		return aData.getRadiation();
	}

	public Color getShadowColor()
	{
		return aData.getShadowColor();
	}

	public SpriteData getSprite()
	{
		return aData.getSprite();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		return j.setStringAttribute("startype", aData.getType());
	}
}
