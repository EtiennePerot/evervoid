package com.evervoid.state.geometry;

import java.util.Collection;
import java.util.Set;

import com.evervoid.client.views.solar.SolarGrid;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.prop.Prop;
import com.jme3.math.FastMath;

/**
 * GridLocation is a representation of the location and size of a {@link Prop} on a {@link SolarGrid}. The origin {@link Point}
 * is the bottom left of the Prop, the width and height represent the number x and y lengths respectively.
 */
public class GridLocation implements Cloneable, Jsonable
{
	/**
	 * The Dimension of this GridLocation.
	 */
	public Dimension dimension;
	/**
	 * The bottom left point of this GridLocation.
	 */
	public Point origin;

	/**
	 * Creates an GridLocation with dimension (1,1) and located at the point (x, y).
	 */
	public GridLocation(final int x, final int y)
	{
		this(new Point(x, y), new Dimension());
	}

	/**
	 * Creates a GridLocation at the Point (x, y) with the given dimension.
	 */
	public GridLocation(final int x, final int y, final Dimension dimension)
	{
		this(new Point(x, y), dimension);
	}

	/**
	 * Creates a GridLocation at the Point (x, y) with Dimension (width, height).
	 */
	public GridLocation(final int x, final int y, final int width, final int height)
	{
		this(new Point(x, y), new Dimension(width, height));
	}

	/**
	 * Creates a GridLocation from the contents of the Json.
	 */
	public GridLocation(final Json j)
	{
		this(new Point(j.getAttribute("origin")), new Dimension(j.getAttribute("dimension")));
	}

	/**
	 * Creates a GridLocation fixed at the parameter Point, and with dimension (1, 1).
	 */
	public GridLocation(final Point pPoint)
	{
		this(pPoint, new Dimension());
	}

	/**
	 * Creates a GridLocation fixed at the parameter Point and with the passed dimension.
	 */
	public GridLocation(final Point point, final Dimension dimension)
	{
		origin = point.clone();
		this.dimension = dimension.clone();
	}

	/**
	 * Creates a GridLocation fixed at the parameter Point and with dimension (width, height).
	 */
	public GridLocation(final Point point, final int width, final int height)
	{
		this(point, new Dimension(width, height));
	}

	/**
	 * Offsets the GridLocation's origin point by the x and y values. Positive x will move right and positive y will move up.
	 * 
	 * @param x
	 *            The x value to add.
	 * @param y
	 *            The y value to add.
	 * @return This modified GridLocation for chainability.
	 */
	public GridLocation add(final int x, final int y)
	{
		return add(new Point(x, y));
	}

	/**
	 * Offsets the GridLocation's origin point by the x and y values of the parameter Point. Positive x will move right and
	 * positive y will move up.
	 * 
	 * @param point
	 *            The Point to add.
	 * @return This modified GridLocation for chainability.
	 */
	public GridLocation add(final Point point)
	{
		return new GridLocation(origin.add(point), dimension);
	}

	@Override
	public GridLocation clone()
	{
		return new GridLocation(origin.clone(), dimension.clone());
	}

	/**
	 * @return Whether this GridLocation collides with the parameter GridLocation at any Point.
	 */
	public boolean collides(final GridLocation other)
	{
		return collidesX(other) && collidesY(other);
	}

	/**
	 * @return Whether this GridLocation contains the Point (x, y).
	 */
	public boolean collides(final int x, final int y)
	{
		return origin.x <= x && x < origin.x + dimension.width && origin.y <= y && y < origin.y + dimension.height;
	}

	/**
	 * @return Whether this GridLocation contains the parameter Point.
	 */
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

	/**
	 * Constrains a GridLocation to fit in the given boundary by shifting the origin point. The GridLocation's dimension is
	 * unchanged.
	 * 
	 * @param boundary
	 *            The boundary to which things must be constrained.
	 * @return The GridLocation, shifted if the constraints require it.
	 */
	public GridLocation constrain(final Dimension boundary)
	{
		return constrain(boundary.width, boundary.height);
	}

	/**
	 * Constrains a GridLocation to the rectangle (0, 0, width, height) by shifting the GridLocatoin's origin point. The
	 * dimension of the GridLocation remains unchanged.
	 * 
	 * @return The GridLocation, shifted as the constraints require it.
	 */
	public GridLocation constrain(final int width, final int height)
	{
		return constrain(0, 0, width, height);
	}

	/**
	 * Constrains a GridLocation to the rectangle (minX, minY, maxX, maxY) by shifting the GridLocatoin's origin point. The
	 * dimension of the GridLocation remains unchanged.
	 * 
	 * @return
	 */
	public GridLocation constrain(final int minX, final int minY, final int maxX, final int maxY)
	{
		return new GridLocation(origin.constrain(minX, minY, maxX - dimension.width, maxY - dimension.height), dimension);
	}

	/**
	 * @return The Manhattan distance between the center of the two GridLocations.
	 */
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

	/**
	 * @return True if and only if this GridLocation fits within the passed dimension.
	 */
	public boolean fitsIn(final Dimension dimension)
	{
		return getX() + getWidth() <= dimension.width && getY() + getHeight() <= dimension.height;
	}

	/**
	 * @return True if and only if this GridLocation is strictly within the bounds of the passed container.
	 */
	public boolean fitsIn(final GridLocation container)
	{
		// this.origin is greater than container.origin
		// this.rightmost is smaller than container.rightmost
		// this.topmost is smaller than contaienr.topmost
		return getX() >= container.getX() && getY() >= container.getY()
				&& getX() + getWidth() <= container.getX() + container.getWidth()
				&& getY() + getHeight() <= container.getY() + container.getHeight();
	}

	/**
	 * @return The center of the GridLocation, rounded to be close to the origin if necessary
	 */
	public Point getCenter()
	{
		// use in truncation to round down the center point
		return origin.add(dimension.width / 2, dimension.height / 2);
	}

	/**
	 * Returns the GridLocation from the passed collection that is closest to this GridLocation ad determined by the Euclidian
	 * distance. If two GridLocations in the list are equal distances away, the one closest to the bias Location is picked.
	 * 
	 * @param locations
	 *            The GridLocations from which to pick.
	 * @param bias
	 *            The bias GridLocation to determine winners in case of ties.
	 * @return The GridLocation closest to this one.
	 */
	public GridLocation getClosest(final Iterable<GridLocation> locations, final GridLocation bias)
	{
		float minDistance = Float.MAX_VALUE;
		float minBiasDistance = Float.MAX_VALUE;
		GridLocation min = null;
		for (final GridLocation loc : locations) {
			final float distance = squareDistanceTo(loc);
			if (distance < minDistance) {
				minDistance = distance;
				minBiasDistance = loc.squareDistanceTo(bias);
				min = loc;
			}
			else if (distance == minDistance) {
				final float biasDistance = loc.squareDistanceTo(bias);
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
	 * @param collection
	 *            The set of Points to search through
	 * @return The GridLocation with the closest origin
	 */
	public GridLocation getClosestOrigin(final Collection<Point> collection)
	{
		return new GridLocation(origin.getClosestTo(collection), dimension);
	}

	/**
	 * @return This GridLocation's height.
	 */
	public int getHeight()
	{
		return dimension.height;
	}

	/**
	 * TODO - wtf... there's already a Manhattan distance from centers. This one's a weird Manhattan from origins?
	 */
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

	/**
	 * @return The width of this GridLocation.
	 */
	public int getWidth()
	{
		return dimension.width;
	}

	/**
	 * @return The x coordinate of this GridLocation's origin.
	 */
	public int getX()
	{
		return origin.x;
	}

	/**
	 * @return The y coordinate of this GridLocaiton's origin.
	 */
	public int getY()
	{
		return origin.y;
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	/**
	 * @return The Manhattan distance between the centers of the two GridLocations.
	 */
	public Point manhattanDistance(final GridLocation other)
	{
		return getCenter().subtract(other.getCenter());
	}

	/**
	 * @return The square of the Euclidian distance between the centers of the two GridLocations.
	 */
	public float squareDistanceTo(final GridLocation other)
	{
		final float centerX = origin.x + (dimension.width) / 2f;
		final float centerY = origin.y + (dimension.height) / 2f;
		final float otherCenterX = other.origin.x + (other.dimension.width) / 2f;
		final float otherCenterY = other.origin.y + (other.dimension.height) / 2f;
		return FastMath.sqr(centerX - otherCenterX) + FastMath.sqr(centerY - otherCenterY);
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
