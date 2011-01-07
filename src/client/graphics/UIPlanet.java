package client.graphics;

import java.awt.Dimension;

import client.graphics.geometry.GridPoint;

public class UIPlanet extends UIProp
{
	public UIPlanet(final Grid grid, final GridPoint location, final Dimension size)
	{
		super(grid, location, new Dimension(2, 2));
		System.out.println("Offset: " + aSpriteOffset.getTranslation2f());
	}

	@Override
	protected void buildSprite()
	{
		addSprite("planets/gas/planet_gas_1.png");
	}
}
