package com.evervoid.state.geometry;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.math.FastMath;

/**
 * Point3D is a simple representation of a three dimensional point, storing each coordinate in a float. Point3D is an immutable
 * class, any modifications on a Point3D returns a new Point3D representing the result of applying the transformation on the
 * original point.
 */
public class Point3D implements Jsonable
{
	/**
	 * @param j
	 *            The serialized Json of the Point3D
	 * @return The Point3D represented by the Json.
	 */
	public static Point3D fromJson(final Json j)
	{
		return new Point3D(j.getFloatAttribute("x"), j.getFloatAttribute("y"), j.getFloatAttribute("z"));
	}

	/**
	 * @return the origin.
	 */
	public static Point3D originPoint()
	{
		return new Point3D(0, 0, 0);
	}

	/**
	 * The X value of the point.
	 */
	public final float x;
	/**
	 * The Y value of the point.
	 */
	public final float y;
	/**
	 * The z value of the point.
	 */
	public final float z;

	/**
	 * Creates a Point3D with the given values.
	 * 
	 * @param x
	 *            The X value of the point.
	 * @param y
	 *            The Y value of the point.
	 * @param z
	 *            The Z value of the point.
	 */
	public Point3D(final float x, final float y, final float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a Point3D with the given values.
	 * 
	 * @param x
	 *            The X value of the point.
	 * @param y
	 *            The Y value of the point.
	 * @param z
	 *            The Z value of the point.
	 */
	public Point3D(final int x, final int y, final int z)
	{
		this((float) x, (float) y, (float) z);
	}

	/**
	 * @param newX
	 *            The x value to add
	 * @param newY
	 *            The y value to add
	 * @param newZ
	 *            the z value to add
	 * @return A new Point3D resulting from adding the three parameters to this Point3D.
	 */
	public Point3D add(final float newX, final float newY, final float newZ)
	{
		return new Point3D(x + newX, y + newY, z + newZ);
	}

	/**
	 * @param other
	 *            The point to add
	 * @return A new Point3D resulting from adding the parameter Point to this Point3D. The Z coordinate will remain unchanged.
	 */
	public Point3D add(final Point other)
	{
		return add(other.x, other.y, 0);
	}

	/**
	 * @param other
	 *            The point to add
	 * @return A new Point3D resulting from adding the parameter Point3D to this one.
	 */
	public Point3D add(final Point3D other)
	{
		return add(other.x, other.y, other.z);
	}

	/**
	 * @param point
	 *            The Point
	 * @return The distance between this Point3D and the other one.
	 */
	public float distanceTo(final Point3D point)
	{
		final float deltaX = x - point.x;
		final float deltaY = y - point.y;
		final float deltaZ = z - point.z;
		final float sumSquares = FastMath.sqr(deltaX) + FastMath.sqr(deltaY) + FastMath.sqr(deltaZ);
		return FastMath.sqrt(sumSquares);
	}

	/**
	 * @return The distance between this Point3D and the origin.
	 */
	public float getDistanceToOrigin()
	{
		return distanceTo(new Point3D(0, 0, 0));
	}

	/**
	 * @param point
	 *            The Point to compare to
	 * @return Whether the two Point3D are the exact same point on the coordinate plane.
	 */
	public boolean sameAs(final Point3D point)
	{
		return x == point.x && y == point.y && z == point.z;
	}

	/**
	 * @param scaleFactor
	 *            The value to scale by
	 * @return A new Point3D resulting in scaling this one by the scaleFactor.
	 */
	public Point3D scale(final float scaleFactor)
	{
		return new Point3D(x * scaleFactor, y * scaleFactor, z * scaleFactor);
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("x", x).setAttribute("y", y).setAttribute("z", z);
	}

	@Override
	public String toString()
	{
		return "Point3D[" + x + "; " + y + ";" + z + "]";
	}
}
