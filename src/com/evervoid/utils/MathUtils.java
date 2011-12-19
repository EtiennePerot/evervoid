package com.evervoid.utils;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.geometry.Point3D;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * A Static class containing everVoid related math utility functions.
 */
public class MathUtils
{
	public static enum AxisDelta
	{
		DOWN, UP;
		public int getDirection()
		{
			switch (this) {
				case UP:
					return 1;
				case DOWN:
					return -1;
			}
			return 0;
		}
	}

	public static enum Border
	{
		DOWN, LEFT, RIGHT, UP;
		public float getXDirection()
		{
			switch (this) {
				case LEFT:
					return -1;
				case RIGHT:
					return 1;
			}
			return 0;
		}

		public float getYDirection()
		{
			switch (this) {
				case DOWN:
					return -1;
				case UP:
					return 1;
			}
			return 0;
		}
	}

	public static enum MovementDirection
	{
		DOWNWARD, LEFTWARD, RIGHTWARD, UPWARD;
		public static MovementDirection fromAngle(float angle)
		{
			angle = angle % FastMath.TWO_PI;
			if (angle < 0) {
				angle += FastMath.TWO_PI;
			}
			if (MathUtils.near(angle, FastMath.HALF_PI * 0.5)) {
				if (FastMath.rand.nextBoolean()) {
					return LEFTWARD;
				}
				return UPWARD;
			}
			if (MathUtils.near(angle, FastMath.HALF_PI * 1.5)) {
				if (FastMath.rand.nextBoolean()) {
					return UPWARD;
				}
				return RIGHTWARD;
			}
			if (MathUtils.near(angle, FastMath.HALF_PI * 2.5)) {
				if (FastMath.rand.nextBoolean()) {
					return RIGHTWARD;
				}
				return DOWNWARD;
			}
			if (MathUtils.near(angle, FastMath.HALF_PI * 3.5)) {
				if (FastMath.rand.nextBoolean()) {
					return LEFTWARD;
				}
				return DOWNWARD;
			}
			if (angle < FastMath.HALF_PI * 0.5 || angle > FastMath.HALF_PI * 3.5) {
				return LEFTWARD;
			}
			if (angle < FastMath.HALF_PI * 1.5) {
				return UPWARD;
			}
			if (angle < FastMath.HALF_PI * 2.5) {
				return RIGHTWARD;
			}
			return DOWNWARD;
		}

		/**
		 * Calculates the Direction associate with moving from origin to destination.
		 * 
		 * @param origin
		 *            The origin of the move.
		 * @param destination
		 *            The destination of the move.
		 * @return The direction of the move.
		 */
		public static MovementDirection fromDelta(final GridLocation origin, final GridLocation destination)
		{
			return MovementDirection.fromDelta(new Vector2f(origin.getX(), origin.getY()), new Vector2f(destination.getX(),
					destination.getY()));
		}

		/**
		 * Calculates the direction to the grid's origin (bottom left) of the grid.
		 * 
		 * @param loc
		 *            The location from which the delta will be calculated.
		 * @return The direction pointing to the grid's origin.
		 */
		public static MovementDirection fromDelta(final Point loc)
		{
			return MovementDirection.fromDelta(new Vector2f(loc.x, loc.y));
		}

		/**
		 * @param delta
		 *            the vector representing the move.
		 * @return The direction in which the move was made.
		 */
		public static MovementDirection fromDelta(final Vector2f delta)
		{
			final Float angle = MathUtils.getAngleTowards(delta);
			if (angle == null) {
				return LEFTWARD; // Default
			}
			return MovementDirection.fromAngle(angle);
		}

		/**
		 * Determines the direction of a move
		 * 
		 * @param origin
		 *            The original location
		 * @param destination
		 *            The destination location
		 * @return The direction in which the move was made
		 */
		public static MovementDirection fromDelta(final Vector2f origin, final Vector2f destination)
		{
			return MovementDirection.fromDelta(destination.subtract(origin));
		}

		/**
		 * @return the angle associated with the direction. North is associated with pi/2 and South with 3pi/2
		 */
		public float getAngle()
		{
			switch (this) {
				case UPWARD:
					return FastMath.HALF_PI;
				case RIGHTWARD:
					return FastMath.PI;
				case DOWNWARD:
					return FastMath.HALF_PI * 3;
				default:
					return 0;
			}
		}
	}

	/**
	 * Clamps a double within the bounds [min, max] by setting it the the closest bound if it not within the range.
	 * 
	 * @param min
	 *            The minimum bound.
	 * @param value
	 *            The value to clamp.
	 * @param max
	 *            The maximum bound.
	 * @return The clamped double.
	 */
	public static double clampDouble(final double min, final double value, final double max)
	{
		return Math.min(max, Math.max(min, value));
	}

	/**
	 * Clamps a float within the bounds [min, max] by setting it the the closest bound if it not within the range.
	 * 
	 * @param min
	 *            The minimum bound.
	 * @param value
	 *            The value to clamp.
	 * @param max
	 *            The maximum bound.
	 * @return The clamped float.
	 */
	public static float clampFloat(final float min, final float value, final float max)
	{
		return Math.min(max, Math.max(min, value));
	}

	/**
	 * Clamps an integer within the bounds [min, max] by setting it the the closest bound if it not within the range.
	 * 
	 * @param min
	 *            The minimum bound.
	 * @param value
	 *            The value to clamp.
	 * @param max
	 *            The maximum bound.
	 * @return The clamped integer.
	 */
	public static int clampInt(final int min, final int value, final int max)
	{
		return Math.min(max, Math.max(min, value));
	}

	public static void clampVector2fDownLocal(final Vector2f vector, final Vector2f max)
	{
		vector.set(Math.min(max.x, vector.x), Math.min(max.y, vector.y));
	}

	public static void clampVector2fLocal(final Vector2f min, final Vector2f vector, final Vector2f max)
	{
		clampVector2fUpLocal(min, vector);
		clampVector2fDownLocal(vector, max);
	}

	public static void clampVector2fUpLocal(final Vector2f min, final Vector2f vector)
	{
		vector.set(Math.max(min.x, vector.x), Math.max(min.y, vector.y));
	}

	public static void clampVector3fDownLocal(final Vector3f vector, final Vector3f max)
	{
		vector.set(Math.min(max.x, vector.x), Math.min(max.y, vector.y), Math.min(max.z, vector.z));
	}

	public static void clampVector3fLocal(final Vector3f min, final Vector3f vector, final Vector3f max)
	{
		clampVector3fUpLocal(min, vector);
		clampVector3fDownLocal(vector, max);
	}

	public static void clampVector3fUpLocal(final Vector3f min, final Vector3f vector)
	{
		vector.set(Math.max(min.x, vector.x), Math.max(min.y, vector.y), Math.max(min.z, vector.z));
	}

	public static Float getAngleTowards(final Vector2f point)
	{
		if (point == null) {
			return 0f;
		}
		float angle = 0f;
		if (point.equals(Vector2f.ZERO)) {
			return null; // Don't rotate at all
		}
		if (point.x == 0) // Avoid division by 0
		{
			if (point.y < 0) {
				angle = FastMath.HALF_PI;
			}
			else {
				angle = -FastMath.HALF_PI;
			}
		}
		else {
			angle = FastMath.PI + FastMath.atan(point.y / point.x);
			if (point.x < 0) {
				angle -= FastMath.PI;
			}
		}
		return angle;
	}

	/**
	 * @param coords
	 *            Set of vertices to get the bounds for
	 * @return The bounds of the rectangle containing all the points associated with the vectors passed
	 */
	public static Rectangle getBounds(final Iterable<Vector3f> coords)
	{
		float minX = 0f, minY = 0f, maxX = 0f, maxY = 0f;
		for (final Vector3f v : coords) {
			minX = Math.min(minX, v.x);
			minY = Math.min(minY, v.y);
			maxX = Math.max(maxX, v.x);
			maxY = Math.max(maxY, v.y);
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * @param coords
	 *            Set of vertices to get the bounds for
	 * @return The bounds of the rectangle containing all the points associated with the vectors passed
	 */
	public static Rectangle getBounds(final Vector3f... coords)
	{
		float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
		for (final Vector3f v : coords) {
			minX = Math.min(minX, v.x);
			minY = Math.min(minY, v.y);
			maxX = Math.max(maxX, v.x);
			maxY = Math.max(maxY, v.y);
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * @param <T>
	 *            Type of elements in the list
	 * @param collection
	 *            The collection of elements to search through
	 * @return A random element from list, or null if list is empty.
	 */
	public static <T> T getRandomElement(final Collection<T> collection)
	{
		if (collection.isEmpty()) {
			return null;
		}
		final int index = getRandomIntBetween(0, collection.size() - 1);
		final Iterator<T> iter = collection.iterator();
		int i = 0;
		T t = iter.next();
		while (i++ < index) {
			t = iter.next();
		}
		return t;
	}

	/**
	 * Same as the getRandomElement(Collection) method, but specialized on lists which provide arbitrary access, thus making it
	 * faster than getRandomElement(Collection).
	 * 
	 * @param <T>
	 *            Type of elements in the list
	 * @param list
	 *            The list of elements to search through
	 * @return A random element from list, or null if list is empty.
	 */
	public static <T> T getRandomElement(final List<T> list)
	{
		return list.isEmpty() ? null : list.get(getRandomIntBetween(0, list.size() - 1));
	}

	/**
	 * @param min
	 *            The minimum value of the number
	 * @param max
	 *            The maximum value of the number
	 * @return A random float in the range [int, max].
	 * @precondition, max > min
	 */
	public static float getRandomFloatBetween(final double min, final double max)
	{
		return (float) clampDouble(min, min + FastMath.rand.nextDouble() * (max - min), max);
	}

	/**
	 * @param min
	 *            The minimum value of the number
	 * @param max
	 *            The maximum value of the number
	 * @return A random float in the range [int, max].
	 * @precondition, max > min
	 * @warning this is utility wrapper for getRandomFloatBetwee(float, float). It simply casts and calls.
	 */
	public static float getRandomFloatBetween(final float min, final float max)
	{
		return getRandomFloatBetween((double) min, (double) max);
	}

	/**
	 * @param min
	 *            The minimum value of the number
	 * @param max
	 *            The maximum value of the number
	 * @return A random integer in the range [int, max].
	 * @precondition, max > min
	 * @warning this is utility wrapper for getRandomIntBetwee(float, float). It simply casts and calls.
	 */
	public static int getRandomIntBetween(final float min, final float max)
	{
		return MathUtils.getRandomIntBetween((int) min, (int) max);
	}

	/**
	 * @param min
	 *            The minimum value of the number
	 * @param max
	 *            The maximum value of the number
	 * @return A random integer in the range [int, max].
	 * @precondition, max > min
	 */
	public static int getRandomIntBetween(final int min, final int max)
	{
		return Math.round(getRandomFloatBetween(min, max));
	}

	/**
	 * @param xMin
	 *            The minimum x value of the vector
	 * @param xMax
	 *            The maximum x value of the vector
	 * @param yMin
	 *            The minimum y value of the vector
	 * @param yMax
	 *            The maximum y value of the vector
	 * @return A vector of the form (x, y) where xMin < x < xMax and yMin < y < yMax.
	 */
	public static Vector2f getRandomVector2fWithin(final float xMin, final float xMax, final float yMin, final float yMax)
	{
		return getRandomVector2fWithin(new Vector2f(xMin, yMin), new Vector2f(xMax, yMax));
	}

	/**
	 * @param min
	 *            The minimum values for the new vector
	 * @param max
	 *            The maximum values for the new vector
	 * @return A vector of the form (x, y) where min.x < x < max.x and min.y < y < max.y.
	 */
	public static Vector2f getRandomVector2fWithin(final Vector2f min, final Vector2f max)
	{
		return new Vector2f(getRandomFloatBetween(min.x, max.x), getRandomFloatBetween(min.y, max.y));
	}

	/**
	 * Given a vector and a 4-bounds rectangle, gives the relative offset of the vector within the rectangle (e.g. a vector
	 * corresponding to the top-right corner of the rectangle would have relative offset (1, 1)).
	 * 
	 * @param absolute
	 *            The vector
	 * @param referential
	 *            The rectangle
	 * @return The relative offset
	 */
	public static Vector2f getRelativeVector2f(final Vector2f absolute, final Rectangle referential)
	{
		return new Vector2f((absolute.x - referential.x) / referential.width, (absolute.y - referential.y) / referential.height);
	}

	/**
	 * Given a vector and a 4-bounds rectangle, gives the relative offset of the vector within the rectangle (e.g. a vector
	 * corresponding to the top-right corner of the rectangle would have relative offset (1, 1)).
	 * 
	 * @param absolute
	 *            The vector
	 * @param referential
	 *            The rectangle
	 * @return The relative offset
	 */
	public static Vector2f getRelativeVector2f(final Vector3f absolute, final Rectangle referential)
	{
		return getRelativeVector2f(new Vector2f(absolute.x, absolute.y), referential);
	}

	/**
	 * @param number
	 *            The number from which we start looking for the next power of two.
	 * @return The next greatest power of two (number does not need to be a power of two)
	 */
	public static int getUpperPowerOf2(final int number)
	{
		final int pow = FastMath.nearestPowerOfTwo(number);
		// nearest power is too low
		if (pow < number) {
			return pow * 2;
		}
		return pow;
	}

	/**
	 * @param point
	 *            The Point to convert.
	 * @return The 2d Vector joining (0,0) to point.
	 */
	public static Vector2f getVector2fFromPoint(final Point point)
	{
		return new Vector2f(point.x, point.y);
	}

	/**
	 * TODO - make sense of this function. I don't get it, someone else do the javadoc.
	 */
	public static Map<Border, Float> isInBorder(final Vector2f point, final Rectangle rectangle, final float borderScale)
	{
		final Map<Border, Float> borderRatios = new EnumMap<Border, Float>(Border.class);
		final float x = point.x;
		final float y = point.y;
		final float borderWidth = rectangle.width * borderScale;
		final float borderHeight = rectangle.height * borderScale;
		if (rectangle.x <= x && x <= rectangle.x + borderWidth) {
			borderRatios.put(Border.LEFT, 1 - (x - rectangle.x) / borderWidth);
		}
		else if (rectangle.x + rectangle.width - borderWidth <= x && x <= rectangle.x + rectangle.width) {
			borderRatios.put(Border.RIGHT, 1 - (rectangle.x + rectangle.width - x) / borderWidth);
		}
		if (rectangle.y <= y && y <= rectangle.y + borderHeight) {
			borderRatios.put(Border.DOWN, 1 - (y - rectangle.y) / borderHeight);
		}
		else if (rectangle.y + rectangle.height - borderHeight <= y && y <= rectangle.y + rectangle.height) {
			borderRatios.put(Border.UP, 1 - (rectangle.y + rectangle.height - y) / borderHeight);
		}
		return borderRatios;
	}

	/**
	 * @param number
	 *            The number to modulus
	 * @param mod
	 *            The value by which to modulus
	 * @return The value of number modulus mod (guaranteed to be within bounds [0,mod) ).
	 * @warning This is a utility wrapper for mod(float, float). It simply casts and calls.
	 */
	public static float mod(final double number, final double mod)
	{
		return mod((float) number, (float) mod);
	}

	/**
	 * @param number
	 *            The number to modulus
	 * @param mod
	 *            The value by which to modulus
	 * @return The value of number modulus mod (guaranteed to be within bounds [0,mod) ).
	 */
	public static float mod(float number, float mod)
	{
		if (mod == 0) {
			throw new IllegalArgumentException("mod argument cannot be 0");
		}
		mod = FastMath.abs(mod);
		while (number < 0) {
			number += mod;
		}
		return number % mod;
	}

	/**
	 * @param number
	 *            The number to modulus
	 * @param mod
	 *            The value by which to modulus
	 * @return The value of number modulus mod (guaranteed to be within bounds [0,mod) ).
	 */
	public static int mod(int number, int mod)
	{
		if (mod == 0) {
			throw new IllegalArgumentException("mod argument cannot be 0");
		}
		mod = Math.abs(mod);
		while (number < 0) {
			number += mod;
		}
		return number % mod;
	}

	/**
	 * @param vector
	 *            The vectors to modulus
	 * @param mod
	 *            the value by which to modulus
	 * @return A new vector in which every element x_i = vector_i % mod_i
	 */
	public static Vector2f moduloVector2f(final Vector2f vector, final Vector2f mod)
	{
		return new Vector2f(mod(vector.x, mod.x), mod(vector.y, mod.y));
	}

	/**
	 * @param x
	 *            The value to check
	 * @param y
	 *            The value to check against
	 * @return whether x is within an arbitrary distance of y.
	 */
	public static boolean near(final double x, final double y)
	{
		return nearZero(x - y);
	}

	/**
	 * @param x
	 *            The value to check
	 * @param y
	 *            The value to check against
	 * @return whether x is within an arbitrary distance of y.
	 */
	public static boolean near(final float x, final float y)
	{
		return nearZero(x - y);
	}

	/**
	 * Checks to see if x is within an arbitrary distance of zero. Used to overcome floating-point precision issues.
	 * 
	 * @param x
	 *            The value to be checked.
	 * @return Whether x is close to zero.
	 */
	public static boolean nearZero(final double x)
	{
		return Math.abs(x) < 0.00001;
	}

	/**
	 * @param x
	 *            The value to be checkd
	 * @return whether x is within an arbitrary distance of zero
	 * @warning This is a utility wrapper for nearZero(boolean), it will simply cast x to a double
	 */
	public static boolean nearZero(final float x)
	{
		return MathUtils.nearZero((double) x);
	}

	/**
	 * Creates a 2D vector joining (0,0) and the point made by chomping z from the parameter.
	 * 
	 * @param point
	 *            The point on which to base the vector.
	 * @return The new Vector.
	 */
	public static Vector2f point3DToVector2f(final Point3D point)
	{
		return new Vector2f(point.x, point.y);
	}

	/**
	 * Creates a 3D vector joining (0,0,0) and the parameter point.
	 * 
	 * @param point
	 *            The point on which to base the vector.
	 * @return The new Vector
	 */
	public static Vector3f point3DToVector3f(final Point3D point)
	{
		return new Vector3f(point.x, point.y, point.z);
	}

	/**
	 * Creates a Vector joining (0,0) and the point passed as a parameter.
	 * 
	 * @param point
	 *            The point on which to base the vector.
	 * @return The new Vector
	 */
	public static Vector2f pointToVector2f(final Point point)
	{
		return new Vector2f(point.x, point.y);
	}

	/**
	 * Creates a copy of the original Vector2f, but rotated by angle degrees.
	 * 
	 * @param vector
	 *            The Vector2F on which to based the rotation.
	 * @param degrees
	 *            The angle to rotate the vector by.
	 * @return new vector rotated by angle degrees.
	 */
	public static Vector2f rotateVector(final Vector2f vector, final float degrees)
	{
		final Vector2f copy = vector.clone();
		copy.rotateAroundOrigin(degrees, false);
		return copy;
	}
}
