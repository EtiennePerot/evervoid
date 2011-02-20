package com.evervoid.state;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.Ship;

public class PathfindingManager
{
	private final ArrayList<PathNode> closed = new ArrayList<PathNode>();
	private final ArrayList<PathNode> open = new ArrayList<PathNode>();
	private final int avoidPropDistance;
	private final int avoidPropPenalty;
	
	/**
	 * Pathfinding Manager constructor using default 
	 * prop avoidance and penalty values.
	 */
	public PathfindingManager(){
		this(2);
	}
	
	/**
	 * Pathfinding Manager using a specified
	 * prop avoidance distance.
	 * @param pAvoidPropDistance The preferred distance to avoid props.
	 */
	public PathfindingManager(int pAvoidPropDistance)
	{
		this(pAvoidPropDistance, 2);
	}
	
	/**
	 * Pathfinding Manager using specified prop avoidance and penalty values.
	 * @param pAvoidPropDistance 
	 * 				The preferred distance to avoid props.
	 * @param pAvoidPropPenalty 
	 * 				The penalty cost associated with being close to a prop.
	 */
	public PathfindingManager(int pAvoidPropDistance, int pAvoidPropPenalty)
	{
		avoidPropDistance = pAvoidPropDistance;
		avoidPropPenalty = pAvoidPropPenalty;
	}

	/**
	 * Computes the standard Euclidian distance between two points (floor).
	 * 
	 * @param pOrig
	 *            Point of origin.
	 * @param pDest
	 *            Point of destination.
	 * @return An integer representing the distance between two points.
	 */
	private int computeHeuristic(final Point pOrig, final Point pDest)
	{
		// Straight line distance
		int a = (pDest.y - pOrig.y);
		int b = (pDest.x - pOrig.x);
		a *= a;
		b *= b;
		return (int) Math.floor(Math.sqrt(a + b));
	}

	/**
	 * Returns an optimal path from a point to a goal. Assumes ship is in a solar system.
	 * 
	 * @param pShip
	 *            The ship that needs to move.
	 * @param pDestination
	 *            Point the ship wants to move to.
	 * @return An ArrayList of GridLocations containing the GridLocations along the optimal path or null if the goal wasn't
	 *         found.
	 */
	public ArrayList<GridLocation> findPath(final Ship pShip, final GridLocation pDestination)
	{
		Point destinationPoint = pDestination.origin; 
		GridLocation currentLocation = null, inflatedCurrentLocation = null;
		//cleanup
		open.clear();
		closed.clear();
		
		//useful variables
		int tentativeCostSoFar = 0;
		boolean tentativeIsBetter = false;
		PathNode current = null, neighbour = null;
		
		//Grab the data we need from the ship.
		final SolarSystem shipSolarSystem = (SolarSystem) pShip.getContainer();
		final Dimension solarSystemDimension = new Dimension(shipSolarSystem.getWidth(), shipSolarSystem.getHeight());
		final Point shipOrigin = pShip.getLocation().origin;
		final Dimension shipDimension = pShip.getLocation().dimension;
		
		//Create an internal representation of the grid.
		final PathNode[][] nodes = new PathNode[shipSolarSystem.getWidth()][shipSolarSystem.getHeight()];
		for (int i = 0; i < shipSolarSystem.getWidth(); i++) {
			for (int j = 0; j < shipSolarSystem.getHeight(); j++) {
				nodes[i][j] = new PathNode(new Point(i, j));
			}
		}
		//Grab the origin node from the internal grid representation.
		final PathNode originNode = nodes[shipOrigin.x][shipOrigin.y];
		originNode.costSoFar = 0;
		originNode.goalHeuristic = 0;

		//Add the origin to the open list of nodes to consider.
		open.add(originNode);
		
		//Start main pathfinding loop.
		while (open.size() != 0) {
			// Grab the element with the lowest total cost from the open list.
			current = grabLowest(open);
			open.remove(current);
			
			if (current.getCoord().equals(destinationPoint)) {
				// Found the goal, reconstruct the path from it.
				ArrayList<PathNode> tempResults = reconstructPath(current);
				// PRUNE!!
				prunePath(tempResults, pShip);
				
				// Stupid conversion to GridLocations.
				final ArrayList<GridLocation> finalResults = new ArrayList<GridLocation>();
				for (final PathNode r : tempResults) {
					finalResults.add(new GridLocation(r.getCoord().x, r.getCoord().y, shipDimension));
				}
				finalResults.remove(pShip.getLocation());
				return finalResults;
			}
			//Add the current element to the closed list.
			closed.add(current);
			
			for (final Point p : getNeighbours(shipSolarSystem, current.getCoord())) {
				currentLocation = new GridLocation(p, shipDimension);
				if (!currentLocation.fitsIn(solarSystemDimension) || !isLocationClear(pShip,currentLocation)){
					//Point doesn't fit in solar system or is occupied, discard.
					continue;
				}
				neighbour = nodes[p.x][p.y];
				if (closed.contains(neighbour)) {
					continue; //We don't consider nodes in the closed list.
				}
				tentativeCostSoFar = current.costSoFar + 1;
				
				// Induce a penalty if location is close to a prop.
				inflatedCurrentLocation = new GridLocation(p.x - avoidPropDistance, p.y - avoidPropDistance, 
												shipDimension.getWidth() + 2*avoidPropDistance, 
												shipDimension.getWidth() + 2*avoidPropDistance);
				if(!isLocationClear(pShip,inflatedCurrentLocation)){
					tentativeCostSoFar += avoidPropPenalty;
				}
				
				if (!(open.contains(neighbour))) {
					open.add(neighbour);
					tentativeIsBetter = true;
				}
				else if (tentativeCostSoFar < neighbour.costSoFar) {
					tentativeIsBetter = true;
				}
				else {
					tentativeIsBetter = false;
				}
				if (tentativeIsBetter) {
					neighbour.parent = current;
					neighbour.costSoFar = tentativeCostSoFar;
					neighbour.goalHeuristic = computeHeuristic(neighbour.getCoord(), destinationPoint);
					neighbour.totalCost = neighbour.costSoFar + neighbour.goalHeuristic;
				}
			}
		}
		return null;
	}

	private Set<Point> getNeighbours(final SolarSystem pSolarSystem, final Point p)
	{
		final Set<Point> directNeighbours = new HashSet<Point>();
		final Point east = new Point(p.x + 1, p.y);
		final Point west = new Point(p.x - 1, p.y);
		final Point north = new Point(p.x, p.y + 1);
		final Point south = new Point(p.x, p.y - 1);
		if (p.y - 1 >= 0) {
			directNeighbours.add(south);
		}
		if (p.y + 1 < pSolarSystem.getHeight()) {
			directNeighbours.add(north);
		}
		if (p.x - 1 >= 0) {
			directNeighbours.add(west);
		}
		if (p.x + 1 < pSolarSystem.getWidth()) {
			directNeighbours.add(east);
		}
		return directNeighbours;
	}

	/**
	 * Returns a list of points where a ship can move to.
	 * 
	 * @param pShip
	 *            A ship located in a solarSystem.
	 * @return A list of points where the specified ship can move within the solar system.
	 */
	public Set<GridLocation> getValidDestinations(final Ship pShip)
	{
		
		final Point shipOrigin = pShip.getLocation().origin;
		final Dimension shipDimension = pShip.getLocation().dimension;
		
		final SolarSystem shipSolarSystem = (SolarSystem) pShip.getContainer();
		final Dimension solarDimension = new Dimension(shipSolarSystem.getWidth(), shipSolarSystem.getHeight());
		final Set<Point> graphFrontier = new HashSet<Point>();
		final Set<Point> newFrontier = new HashSet<Point>();
		final Set<Point> validDestinations = new HashSet<Point>();
		GridLocation currentLocation = null;
		graphFrontier.addAll(getNeighbours(shipSolarSystem, shipOrigin));
		
		// Implementation of a limited-depth breadth-first search.
		for (int i = 0; i < pShip.getSpeed(); i++) {
			// Traverse all the points contained in the frontier.
			for (Point p : graphFrontier) {
				currentLocation = new GridLocation(p, shipDimension);
				if (currentLocation.fitsIn(solarDimension)){
					if (isLocationClear(pShip,currentLocation)) {
						// Point is not occupied nor already known as valid.
						validDestinations.add(p);
						// Add the neighbours to the new frontier.
						newFrontier.addAll(getNeighbours(shipSolarSystem, p));
					}
				}
				
			}
			/*
			 * Remove all already known points from the new frontier, clear the old frontier and replace with new frontier.
			 */
			newFrontier.removeAll(validDestinations);
			graphFrontier.clear();
			graphFrontier.addAll(newFrontier);
			newFrontier.clear();
		}
		
		Set<GridLocation> tempResults = new HashSet<GridLocation>();
		for (Point p:validDestinations){
			tempResults.add(new GridLocation(p, shipDimension));
		}
		return tempResults;
	}

	/**
	 * Returns the PathNode with the lowest totalCost. Prevents the use of a priority queue to improve efficiency.
	 * 
	 * @param pOpen
	 *            An ArrayList of PathNodes that are in the open list.
	 * @return The PathNode with the lowest totalCost.
	 */
	private PathNode grabLowest(final ArrayList<PathNode> pOpen)
	{
		PathNode lowestNode = pOpen.get(0);
		for (final PathNode p : pOpen) {
			if (p.totalCost < lowestNode.totalCost) {
				lowestNode = p;
			}
		}
		return lowestNode;
	}

	/**
	 * Determines if any props are located on the direct route between the origin 
	 * and the destination. This is based on the Bresenham line drawing algorithm.
	 * 
	 * @param pOrigin
	 *            The point of origin.
	 * @param pDestination
	 *            The destination point.
	 * @param pSolarSystem
	 *            The solarSystem this path is in.
	 * @return True if route is clear of props, false otherwise.
	 */
	private boolean isDirectRouteClear(final Point pOrigin, final Point pDestination, final Ship pShip)
	{
		GridLocation currentGridLocation = null;
		int steepx, steepy, error, error2;
		int x0 = pOrigin.x;
		int y0 = pOrigin.y;
		
		final int x1 = pDestination.x;
		final int y1 = pDestination.y;
		
		final int deltax = Math.abs(x1 - x0);
		final int deltay = Math.abs(y1 - y0);

		if (x0 < x1) {
			steepx = 1;
		}
		else {
			steepx = -1;
		}
		if (y0 < y1) {
			steepy = 1;
		}
		else {
			steepy = -1;
		}
		error = deltax - deltay;
		
		while (true) {
			currentGridLocation = new GridLocation(new Point(x0, y0),pShip.getLocation().dimension);
			
			if (!isLocationClear(pShip, currentGridLocation)){
				return false;
			}
			if ((x0 == x1) && (y0 == y1)){
				break;
			}
			error2 = 2 * error;
			if (error2 > -deltay) {
				error = error - deltay;
				x0 = x0 + steepx;
			}
			if (error2 < deltax) {
				error = error + deltax;
				y0 = y0 + steepy;
			}
			
			
		}

		return true;
	}

	/**
	 * Takes in a path and tries to keep only useful "elbow" points.
	 * @param pLongPath An ArrayList of PathNodes that needs to be pruned.
	 * @param pShip The ship that will traverse this given path.
	 * @return A pruned ArrayList of PathNodes.
	 */
	private void prunePath(ArrayList<PathNode> pLongPath, final Ship pShip)
	{
		final ArrayList<PathNode> nodesToPrune = new ArrayList<PathNode>();
		PathNode current = pLongPath.get(0);
		PathNode previous = null;
		
		
		for (final PathNode p : pLongPath) {
			if (isDirectRouteClear(current.getCoord(), p.getCoord(), pShip)) {
				nodesToPrune.add(previous);
			}
			else{
				current = previous;
			}
			
			previous = p;
		}
		pLongPath.removeAll(nodesToPrune);
	}

	/**
	 * Reconstruct the optimal path starting from the goal.
	 * 
	 * @param pCurrentNode
	 *            Node currently being evaluated.
	 * @return ArrayList of PathNodes containing the optimal path.
	 */
	private ArrayList<PathNode> reconstructPath(final PathNode pCurrentNode)
	{
		if (pCurrentNode.parent != null) {
			final ArrayList<PathNode> p = reconstructPath(pCurrentNode.parent);
			p.add(pCurrentNode);
			return p;
		}
		else {
			final ArrayList<PathNode> path = new ArrayList<PathNode>();
			path.add(pCurrentNode);
			return path;
		}
	}
	
	private boolean isLocationClear(final Ship pShip, final GridLocation pLocation){
		final SolarSystem shipSolarSystem = (SolarSystem) pShip.getContainer();
		if (!shipSolarSystem.getPropsAt(pLocation).isEmpty()) {
			if (!(shipSolarSystem.getPropsAt(pLocation).size() == 1 && shipSolarSystem.getFirstPropAt(pLocation).equals(pShip))){
				return false;
			}
		}
		return true;
	}
}
