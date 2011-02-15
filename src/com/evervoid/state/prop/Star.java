package com.evervoid.state.prop;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.gamedata.SpriteInfo;
import com.evervoid.gamedata.StarData;
import com.evervoid.json.Json;
import com.evervoid.state.Color;
import com.evervoid.state.Dimension;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.GridLocation;

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
	public static Star randomStar(final Dimension solarSystemDimension, final EverVoidGameState state)
	{
		final String randomType = (String) MathUtils.getRandomElement(state.getStarTypes());
		final StarData data = state.getStarData(randomType);
		final int x = solarSystemDimension.width / 2 - data.getDimension().width / 2;
		final int y = solarSystemDimension.height / 2 - data.getDimension().height / 2;
		final GridLocation location = new GridLocation(x, y, data.getDimension());
		return new Star(location, randomType, state);
	}

	private final StarData aData;

	public Star(final GridLocation location, final String type, final EverVoidGameState state)
	{
		super(state.getNullPlayer(), location, state);
		aData = state.getStarData(type);
	}

	public Star(final Json j, final EverVoidGameState state)
	{
		super(j, state);
		aData = state.getStarData(j.getStringAttribute("startype"));
	}

	public SpriteInfo getBorderSprite()
	{
		return aData.getBorderSprite();
	}

	public Color getGlowColor()
	{
		return aData.getGlowColor();
	}

	@Override
	public String getPropType()
	{
		return "star";
	}

	public SpriteInfo getSprite()
	{
		return aData.getSprite();
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setStringAttribute("startype", aData.getType());
	}
}
