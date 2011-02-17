package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Dimension;

public class PlanetData implements Jsonable
{
	public enum PlanetType implements Jsonable
	{
		GAS, METAL, ROCK;
		public static PlanetType fromJson(final Json j)
		{
			return PlanetType.valueOf(j.getString().toUpperCase());
		}

		@Override
		public Json toJson()
		{
			return new Json(toString());
		}

		@Override
		public String toString()
		{
			switch (this) {
				case GAS:
					return "gas";
				case METAL:
					return "metal";
				case ROCK:
					return "rock";
			}
			return null;
		}
	}

	private final SpriteData aBaseSprite;
	private final Dimension aDimension;
	private float aGasIncome = 0f;
	private final PlanetType aPlanetType;
	private final String aType;

	PlanetData(final String type, final Json j)
	{
		aType = type;
		aPlanetType = PlanetType.fromJson(j.getAttribute("planettype"));
		aDimension = Dimension.fromJson(j.getAttribute("dimension"));
		aBaseSprite = new SpriteData("planets/" + aPlanetType + "/" + aType + ".png");
		if (j.hasAttribute("gasincome")) {
			aGasIncome = j.getFloatAttribute("gasincome");
		}
	}

	public SpriteData getBaseSprite()
	{
		return aBaseSprite;
	}

	public Dimension getDimension()
	{
		return aDimension;
	}

	public float getGasIncome()
	{
		return aGasIncome;
	}

	public PlanetType getPlanetType()
	{
		return aPlanetType;
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json().setAttribute("dimension", aDimension).setAttribute("planettype", aPlanetType);
		if (aGasIncome != 0) {
			j.setFloatAttribute("gasincome", aGasIncome);
		}
		return j;
	}
}
