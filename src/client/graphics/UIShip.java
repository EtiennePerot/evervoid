package client.graphics;

import java.awt.Point;

import client.views.solar.SolarSystemGrid;

import com.jme3.math.ColorRGBA;

public class UIShip extends Prop implements Colorable
{
	private Sprite aColorableSprite;

	public UIShip(final SolarSystemGrid grid, final int row, final int column)
	{
		this(grid, new Point(column, row));
	}

	public UIShip(final SolarSystemGrid grid, final Point location)
	{
		super(grid, location);
	}

	@Override
	protected void buildSprite()
	{
		addSprite("ships/square/scout_base.png");
		aColorableSprite = addSprite("ships/square/scout_color.png");
	}

	@Override
	public void setHue(final ColorRGBA hue)
	{
		aColorableSprite.setHue(hue);
	}

	@Override
	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		aColorableSprite.setHue(hue, multiplier);
	}
}
