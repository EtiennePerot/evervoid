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
	public PathfindingManager(){

	}
	
	/**
	 * Returns a list of points where a ship can move to.
	 * @param pShip A ship located in a solarSystem.
	 * @param pSolarSystem The solar system containing the ship.
	 * @return A list of points where the specified ship can move within the solar system.
	 */
	public Set<Point> getValidDestinations(SolarSystem pSolarSystem, Ship pShip){
		Point shipOrigin = pShip.getLocation().origin;
		Dimension shipDimension = pShip.getLocation().dimension;
		Set<Point> graphFrontier = new HashSet<Point>();
		Set<Point> newFrontier = new HashSet<Point>();
		Set<Point> validDestinations = new HashSet<Point>();
		
		graphFrontier.addAll(getNeighbours(shipOrigin));
		
		//Implementation of a limited-depth breadth-first search.
		for(int i = 0; i < pShip.getSpeed(); i++){
			
			//Traverse all the points contained in the frontier.
			for (Point p : graphFrontier){
				if (!pSolarSystem.isOccupied(new GridLocation(p,shipDimension))){
					//Point is not occupied nor already known as valid.
					validDestinations.add(p);
					
					//Add the neighbours to the new frontier.
					newFrontier.addAll(getNeighbours(p));
				}
			}
			/* Remove all already known points from the new frontier,
			 * clear the old frontier and replace with new frontier.*/
			newFrontier.removeAll(validDestinations);
			graphFrontier.clear();
			graphFrontier.addAll(newFrontier);
			newFrontier.clear();
		}
		validDestinations.remove(shipOrigin);
		return validDestinations;
	}
	
	
	private Set<Point> getNeighbours(Point p){
		Set<Point> directNeighbours = new HashSet<Point>();
		
		Point east = new Point(p.x + 1, p.y);
		Point west = new Point(p.x - 1, p.y);
		Point north = new Point(p.x, p.y + 1);
		Point south = new Point(p.x, p.y - 1);
		directNeighbours.add(south);
		directNeighbours.add(north);
		directNeighbours.add(west);
		directNeighbours.add(east);

		return directNeighbours;
	}

}
