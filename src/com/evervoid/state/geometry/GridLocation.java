package com.evervoid.state.geometry;

import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class GridLocation implements Cloneable, Jsonable
{
	public Dimension dimension;
	public Point origin;

	public GridLocation(final int x, final int y)
	{
		this(new Point(x, y), new Dimension());
	}

	public GridLocation(final int x, final int y, final Dimension dimension)
	{
		this(new Point(x, y), dimension);
	}

	public GridLocation(final int x, final int y, final int width, final int height)
	{
		this(new Point(x, y), new Dimension(width, height));
	}

	public GridLocation(final Json j)
	{
		this(new Point(j.getAttribute("origin")), new Dimension(j.getAttribute("dimension")));
	}

	public GridLocation(final Point origin)
	{
		this(origin, new Dimension());
	}

	public GridLocation(final Point origin, final Dimension dimension)
	{
		this.origin = origin.clone();
		this.dimension = dimension.clone();
	}

	public GridLocation(final Point point, final int width, final int height)
	{
		this(point, new Dimension(width, height));
	}

	public GridLocation add(final int x, final int y)
	{
		return add(new Point(x, y));
	}

	public GridLocation add(final Point point)
	{
		return new GridLocation(origin.add(point), dimension);
	}

	@Override
	public GridLocation clone()
	{
		return new GridLocation(origin.clone(), dimension.clone());
	}

	public boolean collides(final GridLocation other)
	{
		return collidesX(other) && collidesY(other);
	}

	public boolean collides(final int x, final int y)
	{
		return origin.x <= x && x < origin.x + dimension.width && origin.y <= y && y < origin.y + dimension.height;
	}

	public boolean collides(final Point point)
	{
		return collides(point.x, point.y);
	}

	/**
	 * @param other
	 *            Another GridLocation
	 * @return Whether this GridLocation overlaps the specified one if they were projected on a one-dimensional X axis
	 */
	public boolean collidesX(final GridLocation other)
	{
		final int y = other.origin.y;
		for (int x = origin.x; x < origin.x + dimension.width; x++) {
			if (other.collides(x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param other
	 *            Another GridLocation
	 * @return Whether this GridLocation overlaps the specified one if they were projected on a one-dimensional Y axis
	 */
	public boolean collidesY(final GridLocation other)
	{
		final int x = other.origin.x;
		for (int y = origin.y; y < origin.y + dimension.height; y++) {
			if (other.collides(x, y)) {
				return true;
			}
		}
		return false;
	}

	public GridLocation constrain(final Dimension boundary)
	{
		return constrain(boundary.width, boundary.height);
	}

	public GridLocation constrain(final int width, final int height)
	{
		return constrain(0, 0, width, height);
	}

	public GridLocation constrain(final int minX, final int minY, final int maxX, final int maxY)
	{
		return new GridLocation(origin.constrain(minX, minY, maxX - dimension.width, maxY - dimension.height), dimension);
	}

	public Point delta(final GridLocation other)
	{
		return getCenter().subtract(other.getCenter());
	}

	public float distanceTo(final GridLocation other)
	{
		return getCenter().distanceTo(other.getCenter());
	}

	/**
	 * @param other
	 *            Other Object to compare to.
	 * @return True if the other object is a GridLocation of the same size, false otherwise.
	 */
	@Override
	public boolean equals(final Object other)
	{
		if (super.equals(other)) {
			return true;
		}
		if (other == null || !other.getClass().equals(getClass())) {
			return false;
		}
		final GridLocation l = (GridLocation) other;
		return origin.equals(l.origin) && dimension.equals(l.dimension);
	}

	public boolean fitsIn(final Dimension dimension)
	{
		return getX() + getWidth() <= dimension.width && getY() + getHeight() <= dimension.height;
	}

	public Point getCenter()
	{
		return origin.add(dimension.width / 2, dimension.height / 2);
	}

	public GridLocation getClosest(final Iterable<GridLocation> locations, final GridLocation bias)
	{
		int minDistance = Integer.MAX_VALUE;
		int minBiasDistance = Integer.MAX_VALUE;
		GridLocation min = null;
		for (final GridLocation loc : locations) {
			final int distance = getManhattanDistance(loc);
			if (distance < minDistance) {
				minDistance = distance;
				minBiasDistance = loc.getManhattanDistance(bias);
				min = loc;
			}
			else if (distance == minDistance) {
				final int biasDistance = loc.getManhattanDistance(bias);
				if (biasDistance < minBiasDistance) {
					minBiasDistance = biasDistance;
					min = loc;
				}
			}
		}
		return min;
	}

	/**
	 * Finds the closest GridLocation with an origin in the specified set of Points. Does NOT take dimension into account!
	 * 
	 * @param points
	 *            The set of Points to search through
	 * @return The GridLocation with the closest origin
	 */
	public GridLocation getClosestOrigin(final Iterable<Point> points)
	{
		return new GridLocation(origin.getClosestTo(points), dimension);
	}

	public int getHeight()
	{
		return dimension.height;
	}

	public int getManhattanDistance(final GridLocation other)
	{
		if (other == null) {
			return 0;
		}
		int dX = 0;
		int dY = 0;
		if (!collidesX(other)) {
			final GridLocation minX = other.origin.x > origin.x ? this : other;
			final GridLocation maxX = other.origin.x > origin.x ? other : this;
			dX = maxX.origin.x - minX.origin.x - minX.dimension.width;
		}
		if (!collidesY(other)) {
			final GridLocation minY = other.origin.y > origin.y ? this : other;
			final GridLocation maxY = other.origin.y > origin.y ? other : this;
			dY = maxY.origin.y - minY.origin.y - minY.dimension.height;
		}
		return dX + dY;
	}

	/**
	 * @return The Set of Points spanned by this GridLocation
	 */
	public Set<Point> getPoints()
	{
		return dimension.getPoints(origin);
	}

	public int getWidth()
	{
		return dimension.width;
	}

	public int getX()
	{
		return origin.x;
	}

	public int getY()
	{
		return origin.y;
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("origin", origin).setAttribute("dimension", dimension);
	}

	@Override
	public String toString()
	{
		return "Loc[" + origin + " @ " + dimension + "]";
	}
}
