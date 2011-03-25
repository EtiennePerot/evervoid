package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.ShadedSprite;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.building.Building;
import com.evervoid.state.observers.PlanetObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

public class UIPlanet extends UIShadedProp implements PlanetObserver
{
	private final Planet aPlanet;

	public UIPlanet(final SolarGrid grid, final Planet planet)
	{
		super(grid, planet.getLocation(), planet);
		aPlanet = planet;
		buildProp();
		aPlanet.registerObserver(this);
	}

	@Override
	public void buildingConstructed(final Building building, final int progress)
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected UIControl buildPanelUI()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void buildSprite()
	{
		final ShadedSprite shade = new ShadedSprite(aPlanet.getData().getBaseSprite());
		addSprite(shade);
		setShade(shade);
	}

	@Override
	public void captured(final Player player)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void delFromGrid()
	{
		aPlanet.deregisterObserver(this);
	}

	@Override
	protected void finishedMoving()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipConstructed(final Ship ship, final int progress)
	{
		// TODO Auto-generated method stub
	}
}
