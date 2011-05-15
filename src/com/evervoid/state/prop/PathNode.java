package com.evervoid.state.prop;

import com.evervoid.state.geometry.Point;

/**
 * Data structure used for the A* pathfinding algorithm.
 */
class PathNode implements Comparable<PathNode>
{
	/**
	 * The cost of the path previous to this PathNode.
	 */
	public int costSoFar; // Equivalent to g
	/**
	 * The Point on the grid that this PathNode represent.
	 */
	private final Point fCoord;
	/**
	 * The estimated remaining cost to the goal.
	 */
	public int goalHeuristic; // Equivalent to h
	/**
	 * The previous Node in the Path this Node belongs to.
	 */
	public PathNode parent;
	/**
	 * The total estimated for the Path this Node belongs to.
	 */
	public int totalCost; // Equivalent to f (f = g + h)

	/**
	 * Default PathNode constructor.
	 * 
	 * @param pParent
	 *            Parent of this PathNode.
	 * @param pCostSoFar
	 *            Cost to reach this node from the origin.
	 * @param pCoord
	 *            Point associated with this node's coordinates.
	 */
	public PathNode(final PathNode pParent, final int pCostSoFar, final Point pCoord)
	{
		costSoFar = pCostSoFar;
		goalHeuristic = 0;
		parent = pParent;
		fCoord = pCoord;
	}

	/**
	 * Constructor used to initialise nodes without path knowledge has a null parent and 0 costSoFar.
	 * 
	 * @param pCoord
	 *            Point associated with this node's coordinates.
	 */
	public PathNode(final Point pCoord)
	{
		this(null, 0, pCoord);
	}

	@Override
	public int compareTo(final PathNode o)
	{
		if (o.totalCost < totalCost) {
			return 1;
		}
		else if (o.totalCost == totalCost) {
			return 0;
		}
		else {
			return -1;
		}
	}

	@Override
	public boolean equals(final Object other)
	{
		if (super.equals(other)) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!other.getClass().equals(getClass())) {
			return false;
		}
		final PathNode l = (PathNode) other;
		return fCoord.equals(l.getCoord());
	}

	/**
	 * @return The Point representing the coordinates of this PathNode.
	 */
	public Point getCoord()
	{
		return fCoord;
	}
}
