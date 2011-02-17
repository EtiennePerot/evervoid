package com.evervoid.state;

import java.util.ArrayList;

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
	public ArrayList<Point> getValidDestinations(SolarSystem pSolarSystem, Ship pShip){
		Point shipOrigin = pShip.getLocation().origin;
		int shipRange = 5; //TODO: Pull the range from the shipdata
		ArrayList<Point> graphFrontier = new ArrayList<Point>();
		ArrayList<Point> newFrontier = new ArrayList<Point>();
		ArrayList<Point> validDestinations = new ArrayList<Point>();
		
		graphFrontier.add(shipOrigin);
		
		//Implementation of a limited-depth breadth-first search.
		for(int i = 0; i < shipRange; i++){
			//Traverse all the points contained in the frontier.
			for (Point p : graphFrontier){
				if (pSolarSystem.getPropAt(p) == null && !validDestinations.contains(p)){
					//Point is not occupied nor already known as valid.
					
					validDestinations.add(p);
					
					//Add the neighbours to the new frontier.
					Point east = new Point(p.x+1, p.y);
					Point west = new Point(p.x-1, p.y);
					Point north = new Point(p.x, p.y+1);
					Point south = new Point(p.x, p.y-1);
					newFrontier.add(east);
					newFrontier.add(west);
					newFrontier.add(north);
					newFrontier.add(south);
				}
			}
			/* Remove all already known points from the new frontier,
			 * clear the old frontier and replace with new frontier.*/
			newFrontier.removeAll(validDestinations);
			graphFrontier.clear();
			graphFrontier.addAll(newFrontier);
			newFrontier.clear();
		}
		
		return validDestinations;
	}

}
