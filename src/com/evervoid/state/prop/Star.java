package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
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
		final String randomType = MathUtils.getRandomElement(state.getStarTypes());
		final StarData data = state.getStarData(randomType);
		final int x = solarSystemDimension.width / 2 - data.getDimension().width / 2;
		final int y = solarSystemDimension.height / 2 - data.getDimension().height / 2;
		final GridLocation location = new GridLocation(x, y, data.getDimension());
		return new Star(location, state.getStarData(randomType), state);
	}

	private final StarData aData;

	/**
	 * Creates a new Star with the passed parameters.
	 * 
	 * @param location
	 *            The location the star will occupy.
	 * @param data
	 *            The data associated with this Star.
	 * @param state
	 *            The state to which this Star will belong.
	 */
	public Star(final GridLocation location, final StarData data, final EVGameState state)
	{
		super(state.getNextPropID(), state.getNullPlayer(), location, "star", state);
		aData = data;
		// make sure to enter your container, otherwise you'll never show up
		aState.registerProp(this, aContainer);
	}

	/**
	 * Creates a star based on the data within this Json.
	 * 
	 * @param j
	 *            The Json containing the representation of this Star.
	 * @param state
	 *            The state to which this Star will belong.
	 */
	public Star(final Json j, final EVGameState state)
	{
		super(j, "star", state);
		aData = aState.getStarData(j.getStringAttribute("startype"));
	}

	/**
	 * @return The SpriteData of the border halo surrounding the Star.
	 */
	public SpriteData getBorderSprite()
	{
		return aData.getBorderSprite();
	}

	/**
	 * @return The glow color associated with this star.
	 */
	public Color getGlowColor()
	{
		return aData.getGlowColor();
	}

	/**
	 * @return The radiation strength of this Star.
	 */
	public float getRadiationLevel()
	{
		return aData.getRadiation();
	}

	/**
	 * @return The color of the shadow cast by this star.
	 */
	public Color getShadowColor()
	{
		return aData.getShadowColor();
	}

	/**
	 * @return The SolarSystem this Star belongs to, this is just a recasted version of getContainer().
	 */
	public SolarSystem getSolarSystem()
	{
		return (SolarSystem) aContainer;
	}

	/**
	 * @return The SpriteData associated with this star.
	 */
	public SpriteData getSprite()
	{
		return aData.getSprite();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		return j.setAttribute("startype", aData.getType());
	}
}
