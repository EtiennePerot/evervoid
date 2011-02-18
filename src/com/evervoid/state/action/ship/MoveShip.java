package com.evervoid.state.action.ship;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Ship;

public class MoveShip extends ShipAction
{
	private final List<GridLocation> aPath;

	public MoveShip(final Json j, final EVGameState state)
	{
		super(j, state);
		aPath = new ArrayList<GridLocation>();
		aPath.add(new GridLocation(j.getAttribute("destination")));
	}

	public MoveShip(final Player player, final Ship ship, final List<GridLocation> destinations)
	{
		super(player, "Move", ship);
		aPath = destinations;
	}

	@Override
	public void execute()
	{
		aShip.move(aPath);
	}

	@Override
	public boolean isValid()
	{
		final SolarSystem ss = (SolarSystem) aShip.getContainer();
		for (int i = 0; i < aPath.size(); i++) {
			if (ss.isOccupied(aPath.get(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("destination", new Json(aPath));
		return j;
	}
}
