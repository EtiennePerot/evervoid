package client.views.solar;

import client.graphics.Grid;

import com.jme3.math.ColorRGBA;

public class SolarSystemGrid extends Grid
{
	public SolarSystemGrid()
	{
		super(48, 64, 64, 64, 1, new ColorRGBA(1f, 1f, 1f, 0.2f));
	}
}
