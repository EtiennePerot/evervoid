package com.evervoid.state.action.ship;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.state.EVContainer;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.Pathfinder;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;

public class MoveShip extends ShipAction
{
	private final GridLocation aDestination;
	/**
	 * null path means not computed yet
	 */
	private List<GridLocation> aPath = new ArrayList<GridLocation>();

	public MoveShip(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		for (final Json step : j.getListAttribute("path")) {
			aPath.add(new GridLocation(step));
		}
		aDestination = new GridLocation(j.getAttribute("destination"));
	}

	public MoveShip(final Ship ship, final Point destination, final EVGameState state) throws IllegalEVActionException
	{
		super("MoveShip", ship, state);
		aDestination = new GridLocation(destination, ship.getData().getDimension());
	}

	private void computePath()
	{
		if (aPath == null || aPath.isEmpty()) {
			aPath = new Pathfinder().findPath(getShip(), aDestination);
		}
	}

	@Override
	public void execute()
	{
		getShip().move(aPath);
	}

	public List<GridLocation> getPath()
	{
		computePath();
		return aPath;
	}

	@Override
	public boolean isValidShipAction()
	{
		final EVContainer<Prop> container = getShip().getContainer();
		if (!(container instanceof SolarSystem)) {
			// Ship not in solar system
			return false;
		}
		final boolean valid = getShip().getValidDestinations().contains(aDestination);
		if (valid) {
			// If it's valid, it's worth computing the path right now
			computePath();
		}
		return valid;
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setListAttribute("path", aPath).setAttribute("destination", aDestination);
	}
}
