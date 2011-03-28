package com.evervoid.state.prop;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.Color;
import com.evervoid.state.EVContainer;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.data.TrailData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.observers.ShipObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;

public class Ship extends Prop
{
	private final ShipData aData;
	private int aHealth;
	private final Set<ShipObserver> aObserverList;
	private int aRadiation;
	private int aShields;

	public Ship(final int id, final Player player, final EVContainer<Prop> container, final GridLocation location,
			final String shipType, final EVGameState state)
	{
		super(id, player, location, "ship", state);
		aData = aPlayer.getRaceData().getShipData(shipType);
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
		aObserverList = new HashSet<ShipObserver>();
		aContainer = container;
		aHealth = aData.getHealth(player.getResearch());
		aShields = aData.getShields(player.getResearch());
		aRadiation = aData.getRadiation(player.getResearch());
	}

	public Ship(final Json j, final Player player, final EVGameState state)
	{
		super(j, player, "ship", state);
		aData = aPlayer.getRaceData().getShipData(j.getStringAttribute("shiptype"));
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
		aObserverList = new HashSet<ShipObserver>();
		aHealth = j.getIntAttribute("health");
		aShields = j.getIntAttribute("shields");
		aRadiation = j.getIntAttribute("radiation");
	}

	public void addHealth(final int amount)
	{
		if (amount == 0) {
			return;
		}
		aHealth = Math.max(0, aHealth + amount);
		// TODO - upper bound
		for (final ShipObserver observer : aObserverList) {
			observer.shipHealthChanged(this, aHealth);
		}
	}

	public void addRadiation(final int amount)
	{
		if (amount == 0) {
			return;
		}
		aRadiation = Math.min(aRadiation + amount, aData.getRadiation(aPlayer.getResearch()));
	}

	public void addShields(final int amount)
	{
		if (amount == 0) {
			return;
		}
		aShields = Math.max(0, aShields + amount);
		// TODO cap at max
	}

	public boolean canJump()
	{
		return aRadiation >= aState.getJumpCost();
	}

	public boolean canShoot()
	{
		// If we have some kind of research which can make a ship shoot, this should be handled here
		return aData.canShoot();
	}

	public void damage(final int damage)
	{
		removeShields(Math.min(aShields, damage));
		removeHealth(Math.max(0, damage - aShields));
	}

	public void deregisterObserver(final ShipObserver observer)
	{
		aObserverList.remove(observer);
	}

	public void die()
	{
		// warn all observers
		for (final ShipObserver observer : aObserverList) {
			observer.shipDestroyed(this);
		}
		// Note, you do not need to remove yourself from your container. The Container should observe the ship, and so it will
		// remove it
		aState.deregisterProp(aID);
	}

	public float distanceTo(final Ship other)
	{
		final GridLocation myLocation = getLocation();
		final GridLocation otherLocation = other.getLocation();
		return myLocation.distanceTo(otherLocation);
	}

	@Override
	public void enterContainer(final EVContainer<Prop> container)
	{
		super.enterContainer(container);
		aObserverList.add((ShipObserver) container);
	}

	public Color getColor()
	{
		return aPlayer.getColor();
	}

	public ResourceAmount getCost()
	{
		return aData.getBaseCost();
	}

	public ShipData getData()
	{
		return aData;
	}

	public int getHealth()
	{
		return aHealth;
	}

	public int getHealthRegenRate()
	{
		return aData.getHealthRegenRate(aPlayer.getResearch());
	}

	public int getMaxDamage()
	{
		return aData.getDamage(aPlayer.getResearch());
	}

	public int getMaxHealth()
	{
		return aData.getHealth(aPlayer.getResearch());
	}

	public int getMaxRadiation()
	{
		return aData.getRadiation(aPlayer.getResearch());
	}

	public int getMaxShields()
	{
		// TODO - multiply based on research
		return aData.getShields(aPlayer.getResearch());
	}

	public int getRadiation()
	{
		return aRadiation;
	}

	public int getShieldRegenRate()
	{
		return aData.getShieldRegenRate(aPlayer.getResearch());
	}

	public int getShields()
	{
		return aShields;
	}

	public int getSpeed()
	{
		// TODO: Get speed multiplier from research
		return aData.getBaseSpeed();
	}

	public TrailData getTrailData()
	{
		// TODO: Make this depend on research
		// FIXME: Haaaax
		return aPlayer.getRaceData().getTrailData("engine_0");
	}

	public Set<GridLocation> getValidDestinations()
	{
		return new Pathfinder().getValidDestinations(this);
	}

	public boolean isAtMaxHealth()
	{
		return aHealth == getMaxHealth();
	}

	public boolean isAtMaxRadiation()
	{
		return aRadiation == aData.getRadiation(aPlayer.getResearch());
	}

	public boolean isAtMaxShields()
	{
		return aShields == aData.getShields(aPlayer.getResearch());
	}

	public void jumpToSolarSystem(final SolarSystem ss, final GridLocation jumpLocation, final List<GridLocation> leavingMove,
			final GridLocation destinationLocation, final Portal portal)
	{
		removeRadiation(aState.getJumpCost());
		final ShipPath path = new ShipPath(aLocation, jumpLocation, leavingMove);
		for (final ShipObserver observer : aObserverList) {
			observer.shipJumped(this, aContainer, path.clone(), ss, portal);
		}
		leaveContainer();
		aLocation = destinationLocation;
		enterContainer(ss);
	}

	@Override
	public void leaveContainer()
	{
		super.leaveContainer();
		aObserverList.remove(aContainer);
		aContainer = null;
	}

	public void move(final GridLocation destination, final ShipPath path)
	{
		final GridLocation oldLocation = aLocation;
		aLocation = destination;
		for (final ShipObserver observer : aObserverList) {
			observer.shipMoved(this, oldLocation, path.clone());
		}
	}

	public void registerObserver(final ShipObserver sObserver)
	{
		aObserverList.add(sObserver);
	}

	private void removeHealth(final int amount)
	{
		addHealth(-amount);
	}

	private void removeRadiation(final int amount)
	{
		addRadiation(-amount);
	}

	private void removeShields(final int amount)
	{
		addShields(-amount);
	}

	/**
	 * Simple function needed to pass the event to all of the ship's observers
	 * 
	 * @param targetShip
	 */
	public void shoot(final Ship targetShip)
	{
		for (final ShipObserver observers : aObserverList) {
			observers.shipShot(this, targetShip.getLocation());
		}
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("health", aHealth);
		j.setIntAttribute("radiation", aRadiation);
		j.setIntAttribute("shields", aShields);
		j.setStringAttribute("shiptype", aData.getType());
		return j;
	}
}
