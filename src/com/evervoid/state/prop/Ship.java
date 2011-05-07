package com.evervoid.state.prop;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.data.TrailData;
import com.evervoid.state.data.WeaponData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.observers.ShipObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.utils.EVContainer;
import com.evervoid.utils.MathUtils;

public class Ship extends Prop implements EVContainer<Prop>
{
	/**
	 * The ShipData holding all information pertaining to this ship's type and stats.
	 */
	private final ShipData aData;
	/**
	 * The current number of HitPoints that this ship has.
	 * 
	 * @invariant In range [ 0, aData.getHealth(<current research>) ].
	 */
	private int aHealth;
	/**
	 * The set of all Objects currently observing this Ship.
	 */
	private final Set<ShipObserver> aObserverList;
	/**
	 * The current number of radiation held by this ship.
	 * 
	 * @invariant In range [ 0, aData.getRadiation(<current research>).
	 */
	private int aRadiation;
	/**
	 * The current number of shields on this ship
	 * 
	 * @invariant In range [ 0, aData.getRadiation(<current research>).
	 */
	private int aShields;
	/**
	 * The set of all Ships inside this Ship's cargo hold.
	 * 
	 * @invariant Sum of s.getDockingSize() for all s in cargo <= aData.getCargoCapacity(<current research>).
	 */
	private final Set<Ship> aShipCargo;

	/**
	 * Creates a new Ship based on the contents of the Json, pulling referenced information from the State.
	 * 
	 * @param j
	 *            The Json containing this Ship's information.
	 * @param state
	 *            The State to which the Ship belongs.
	 */
	public Ship(final Json j, final EVGameState state)
	{
		super(j, "ship", state);
		aData = aPlayer.getRaceData().getShipData(j.getStringAttribute("shiptype"));
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
		aObserverList = new HashSet<ShipObserver>();
		aHealth = j.getIntAttribute("health");
		aShields = j.getIntAttribute("shields");
		aRadiation = j.getIntAttribute("radiation");
		aShipCargo = new HashSet<Ship>();
		final List<Json> cargo = j.getListAttribute("shipCargo");
		for (final Json ship : cargo) {
			aShipCargo.add(new Ship(ship, aState));
		}
		aState.registerProp(this, aContainer);
	}

	/**
	 * Creates a new Ship with all the given parameters.
	 * 
	 * @param player
	 *            This Ship's owner.
	 * @param container
	 *            The Container in which this ship will spawn.
	 * @param location
	 *            The GridLocation this ship will spawn in (should be null unless container is an instance of SolarSystem).
	 * @param shipType
	 *            The shipType identifier that will be used in fetching this Ship's ShipData.
	 * @param state
	 *            The State to which this Ship will belong.
	 */
	public Ship(final Player player, final EVContainer<Prop> container, final GridLocation location, final String shipType,
			final EVGameState state)
	{
		super(state.getNextPropID(), player, location, "ship", state);
		aData = aPlayer.getRaceData().getShipData(shipType);
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
		aObserverList = new HashSet<ShipObserver>();
		aContainer = container;
		aHealth = aData.getHealth(player.getResearch());
		aShields = aData.getShields(player.getResearch());
		aRadiation = aData.getRadiation(player.getResearch());
		aShipCargo = new HashSet<Ship>();
	}

	@Override
	public boolean addElem(final Prop e)
	{
		if (!(e instanceof Ship)) {
			// though we are a Prop container, we only want Ships in the cargo
			return false;
		}
		final Ship s = (Ship) e;
		if (canHold(s) && s.getCurrentCargoSize() == 0) {
			// check that this ship won't put us over capacity
			// check that the ship isn't carrying anything; no recursion
			return aShipCargo.add(s);
		}
		// ship didn't meet criteria
		return false;
	}

	/**
	 * Adds the given number of health points to the Ship's current count; negative values subtract. The resulting amount is
	 * clamped between zero and max health as defined by the ShipData.
	 * 
	 * @param amount
	 *            The number of health points to be added.
	 */
	public void addHealth(final int amount)
	{
		aHealth = MathUtils.clampInt(0, aHealth + amount, getMaxHealth());
		for (final ShipObserver observer : aObserverList) {
			observer.shipHealthChanged(this, aHealth);
		}
		if (aHealth <= 0) { // ded; not beeg soorprize.
			die();
		}
	}

	/**
	 * Adds the given amount to the Ship's current radiation count; negative number subtract instead. The resulting amount is
	 * clamped between zero and the max radiation as defined by the ShipData.
	 * 
	 * @param amount
	 *            The radiation amount to add to the count.
	 */
	public void addRadiation(final int amount)
	{
		aRadiation = MathUtils.clampInt(0, aRadiation + amount, getMaxRadiation());
	}

	/**
	 * Adds the given amount to this Ship's current shields count; negative number subtract. The resulting amount is clamped
	 * between 0 and max radiation hold as defined by the ShipData.
	 * 
	 * @param amount
	 */
	public void addShields(final int amount)
	{
		aShields = MathUtils.clampInt(0, aShields + amount, getMaxShields());
		for (final ShipObserver observer : aObserverList) {
			observer.shipShieldsChanged(this, aShields);
		}
	}

	/**
	 * @return whether the parameter Ship can legally dock in this Ship's cargo hold.
	 */
	public boolean canHold(final Ship ship)
	{
		return ship.getDockingSize() + getCurrentCargoSize() <= getCargoCapacity();
	}

	/**
	 * @return whether the ship has enough radiation to execute a jump.
	 */
	public boolean canJump()
	{
		return aRadiation >= aState.getJumpCost();
	}

	/**
	 * @return whether the Ship has the ability to shoot.
	 */
	public boolean canShoot()
	{
		// If we have some kind of research which can make a ship shoot, this should be handled here
		return aData.canShoot();
	}

	/**
	 * @return whether the ship is capable of shooting the parameter location from it's current location.
	 */
	public boolean canShoot(final GridLocation location)
	{
		if (!canShoot()) {
			return false;
		}
		// TODO check that the line isn't obstructed by something (like another prop)
		return distanceTo(location) <= getSpeed();
	}

	/**
	 * @return whether the parameter Prop is a legal target for this ship.
	 */
	public boolean canShoot(final Prop prop)
	{
		return !getPlayer().equals(prop.getPlayer()) && canShoot(prop.getLocation());
	}

	/**
	 * Captures the target planet by changing its owner to be the same as the Ship's owner.
	 * 
	 * @param targetPlanet
	 *            The Planet to capture.
	 * @param underlyingMove
	 *            The move necessary for the ship to capture the Planet.
	 */
	public void capturePlanet(final Planet targetPlanet, final ShipPath underlyingMove)
	{
		targetPlanet.changeOwner(getPlayer());
		for (final ShipObserver observer : aObserverList) {
			observer.shipCapturedPlanet(this, targetPlanet, underlyingMove);
		}
	}

	@Override
	public boolean containsElem(final Prop e)
	{
		return aShipCargo.contains(e);
	}

	public void deregisterObserver(final ShipObserver observer)
	{
		aObserverList.remove(observer);
	}

	/**
	 * Warns all observers that this ship is dead, then de-registers the Prop from its State.
	 */
	public void die()
	{
		aHealth = 0;
		// warn all observers
		for (final ShipObserver observer : aObserverList) {
			observer.shipDestroyed(this);
		}
		// Note, you do not need to remove yourself from your container.; that is the container's job
		aState.deregisterProp(aID);
	}

	/**
	 * @return The euclidian distance between this Prop and the parameter location.
	 */
	public float distanceTo(final GridLocation location)
	{
		final GridLocation myLocation = getLocation();
		return myLocation.distanceTo(location);
	}

	/**
	 * @return The euclidian distance between this Prop and the parameter Prop.
	 */
	public float distanceTo(final Prop prop)
	{
		return distanceTo(prop.getLocation());
	}

	@Override
	public Iterable<? extends Prop> elemIterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Makes this ship enter the parameter Ship's cargo if that is legal.
	 * 
	 * @param containerShip
	 *            The ship to enter.
	 * @param shipPath
	 *            The path this ship must follow to get to the container Ship.
	 */
	public void enterCargo(final Ship containerShip, final ShipPath shipPath)
	{
		leaveContainer();
		enterContainer(containerShip);
		for (final ShipObserver observer : aObserverList) {
			observer.shipEnteredCargo(this, containerShip, shipPath);
		}
		// Also notify observers of the target ship that we've entered it
		for (final ShipObserver observer2 : containerShip.aObserverList) {
			observer2.shipEnteredThis(containerShip, this);
		}
	}

	@Override
	public boolean enterContainer(final EVContainer<Prop> container)
	{
		if (super.enterContainer(container)) {
			if (container instanceof ShipObserver) {
				// if the container cares, register it
				aObserverList.add((ShipObserver) container);
			}
			return true;
		}
		// failed to enter container
		return false;
	}

	/**
	 * Makes this ship enter the parameter container.
	 * 
	 * @param container
	 *            The container to enter.
	 * @param destination
	 *            The GridLocation this ship will enter in if the container is an intance of SolarSystem.
	 */
	public void enterContainer(final EVContainer<Prop> container, final GridLocation destination)
	{
		aLocation = destination;
		enterContainer(container);
	}

	/**
	 * @return The set of all ships currently in this Sip's cargo hold.
	 */
	public Set<Ship> getCargo()
	{
		return aShipCargo;
	}

	/**
	 * @return This Ship's maximum cargo capacity.
	 */
	public int getCargoCapacity()
	{
		return aData.getCargoCapacity(aPlayer.getResearch());
	}

	/**
	 * @return The overlay Color associated with this Sip's owner.
	 */
	public Color getColor()
	{
		return aPlayer.getColor();
	}

	/**
	 * @return The cost of building this Ship.
	 */
	public ResourceAmount getCost()
	{
		return aData.getBaseCost();
	}

	/**
	 * @return The size of all Ships currently in the cargo hold.
	 */
	public int getCurrentCargoSize()
	{
		int count = 0;
		for (final Ship s : aShipCargo) {
			count += s.getDockingSize();
		}
		return count;
	}

	/**
	 * @return The ShipData associated with this Ship.
	 */
	public ShipData getData()
	{
		return aData;
	}

	/**
	 * @return This Ship's docking size.
	 */
	private int getDockingSize()
	{
		return aData.getDockingSize();
	}

	/**
	 * @return This Ship's current health.
	 */
	public int getHealth()
	{
		return aHealth;
	}

	/**
	 * @return The current health regeneration rate.
	 */
	public int getHealthRegenRate()
	{
		return aData.getHealthRegenRate(aPlayer.getResearch());
	}

	/**
	 * @return The current max damage.
	 */
	public int getMaxDamage()
	{
		return aData.getDamage(aPlayer.getResearch());
	}

	/**
	 * @return The current max health.
	 */
	public int getMaxHealth()
	{
		return aData.getHealth(aPlayer.getResearch());
	}

	/**
	 * @return The current max radiation.
	 */
	public int getMaxRadiation()
	{
		return aData.getRadiation(aPlayer.getResearch());
	}

	/**
	 * @return The current max shields.
	 */
	public int getMaxShields()
	{
		return aData.getShields(aPlayer.getResearch());
	}

	/**
	 * @return The current radiation.
	 */
	public int getRadiation()
	{
		return aRadiation;
	}

	/**
	 * @return The current radiation regeneration rate.
	 */
	public int getRadiationRate()
	{
		if (!(getContainer() instanceof SolarSystem)) {
			return 0;
		}
		final SolarSystem ss = (SolarSystem) getContainer();
		final Star star = ss.getStar();
		final float distance = getLocation().distanceTo(star.getLocation());
		return (int) (10 * (star.getRadiationLevel() / distance));
		// FIXME - do correctly. Logarithmically based on distance (with a cap)
	}

	/**
	 * @return The current shield regeneration rate.
	 */
	public int getShieldRegenRate()
	{
		return aData.getShieldRegenRate(aPlayer.getResearch());
	}

	/**
	 * @return The current shields.
	 */
	public int getShields()
	{
		return aShields;
	}

	/**
	 * @return The current percentage of the shields.
	 */
	public float getShieldsFloat()
	{
		if (getMaxShields() == 0) {
			return 0; // Prevent silent division by 0
		}
		return (float) getShields() / (float) getMaxShields();
	}

	/**
	 * @return The Sprite associated with the Shield.
	 */
	public SpriteData getShieldSprite()
	{
		return aData.getShieldSprite(aPlayer.getResearch());
	}

	/**
	 * @return The identifier of this Ship's type.
	 */
	public String getShipType()
	{
		return aData.getTitle();
	}

	/**
	 * @return The Ship's current speed.
	 */
	public int getSpeed()
	{
		// TODO: Get speed multiplier from research
		return aData.getSpeed(aPlayer.getResearch());
	}

	/**
	 * @return This Ship's SpriteData.
	 */
	public SpriteData getSprite()
	{
		return aData.getBaseSprite();
	}

	/**
	 * @return This Ship's TrailData.
	 */
	public TrailData getTrailData()
	{
		// TODO: Make this depend on research
		return aPlayer.getRaceData().getTrailData("engine_0");
	}

	/**
	 * @return The set of all GridLocation to which this Ship can currently move.
	 */
	public Set<GridLocation> getValidDestinations()
	{
		return new Pathfinder().getValidDestinations(this);
	}

	/**
	 * @return This Ship's WeaponData.
	 */
	public WeaponData getWeaponData()
	{
		// TODO: Make this depend on research
		return aPlayer.getRaceData().getWeaponData("weapon_0");
	}

	/**
	 * @return A List of Points on which weapons can be placed.
	 */
	public List<Point> getWeaponSlots()
	{
		return aData.getWeaponSlots();
	}

	/**
	 * @return A list of all weapon Sprites associated with this Ship.
	 */
	public List<SpriteData> getWeaponSprites()
	{
		return aData.getWeaponSprites(getWeaponData());
	}

	/**
	 * @return The speed at which this Ship goes through Wormholes.
	 */
	public Integer getWormholeSpeed()
	{
		// FIXME do something, use research
		return 1;
	}

	/**
	 * @return Whether this Ship is at max health.
	 */
	public boolean isAtMaxHealth()
	{
		return aHealth == getMaxHealth();
	}

	/**
	 * @return Whether this Ship is at max radiation.
	 */
	public boolean isAtMaxRadiation()
	{
		return aRadiation == getMaxRadiation();
	}

	/**
	 * @return Whether this Ship is at max shields.
	 */
	public boolean isAtMaxShields()
	{
		return aShields == getMaxShields();
	}

	/**
	 * @return Whether this ship is dead.
	 */
	public boolean isDead()
	{
		return aHealth <= 0;
	}

	/**
	 * @param ss
	 *            The Solar System to which the Ship is moving.
	 * @param jumpLocation
	 *            The GridLocation from which the Ship will be jumping.
	 * @param leavingMove
	 *            The move the Ship will take to leave the SolarSytem.
	 * @param destinationLocation
	 *            The location the Ship will occupy in the destination SolarSystem.
	 */
	public void jumpToSolarSystem(final SolarSystem ss, final GridLocation jumpLocation, final List<GridLocation> leavingMove,
			final GridLocation destinationLocation)
	{
		removeRadiation(aState.getJumpCost());
		final ShipPath path = new ShipPath(aLocation, jumpLocation, leavingMove, (SolarSystem) aContainer);
		for (final ShipObserver observer : aObserverList) {
			// FIXME jumping into a solar system should be done from wormholes, not solar systems
			observer.shipJumped(this, aContainer, path.clone(), ss);
		}
		leaveContainer();
		aLocation = destinationLocation;
		enterContainer(ss);
	}

	@Override
	public void leaveContainer()
	{
		leaveContainer(null);
	}

	/**
	 * The Ship exits its current container.
	 * 
	 * @param exitPath
	 *            The path the Ship is taking on its way out.
	 */
	public void leaveContainer(final ShipPath exitPath)
	{
		final EVContainer<Prop> oldContainer = aContainer;
		super.leaveContainer();
		// clone list so that observers may remove themselves
		final Set<ShipObserver> oldObservers = new HashSet<ShipObserver>(aObserverList);
		for (final ShipObserver observer : oldObservers) {
			observer.shipLeftContainer(this, oldContainer, exitPath);
		}
		aContainer = null;
	}

	/**
	 * Moves the Ship from its current location to the destination by moving it along the path.
	 * 
	 * @param destination
	 *            The final GridLocation the Ship will occupy.
	 * @param path
	 *            The path this Ship will take on its way to the destination.
	 */
	public void move(final GridLocation destination, final ShipPath path)
	{
		final GridLocation oldLocation = aLocation;
		aLocation = destination;
		for (final ShipObserver observer : aObserverList) {
			observer.shipMoved(this, oldLocation, path.clone());
		}
	}

	/**
	 * Registers a ShipObserver for message.
	 * 
	 * @param sObserver
	 *            The ShipObserver to register.
	 */
	public void registerObserver(final ShipObserver sObserver)
	{
		aObserverList.add(sObserver);
	}

	@Override
	public boolean removeElem(final Prop e)
	{
		return aShipCargo.remove(e);
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

	/**
	 * Damages first shields, overflow goes to health.
	 * 
	 * @param damage
	 *            The amount of damage to be dealt.
	 */
	public void takeDamage(final int damage)
	{
		removeHealth(Math.max(0, damage - aShields));
		removeShields(Math.min(aShields, damage));
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("health", aHealth);
		j.setAttribute("radiation", aRadiation);
		j.setAttribute("shields", aShields);
		j.setAttribute("shiptype", aData.getType());
		j.setListAttribute("shipCargo", aShipCargo);
		return j;
	}
}
