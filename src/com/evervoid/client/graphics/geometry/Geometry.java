package com.evervoid.client.graphics.geometry;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.EnumMap;
import java.util.Map;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class Geometry
{
	public static enum AxisDelta
	{
		DOWN, UP;
		public int getDirection()
		{
			switch (this)
			{
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
			switch (this)
			{
				case LEFT:
					return -1;
				case RIGHT:
					return 1;
			}
			return 0;
		}

		public float getYDirection()
		{
			switch (this)
			{
				case DOWN:
					return -1;
				case UP:
					return 1;
			}
			return 0;
		}
	}

	public static enum MovementDelta
	{
		DOWNWARD, LEFTWARD, RIGHTWARD, UPWARD;
		public static MovementDelta fromAngle(float angle)
		{
			angle = angle % FastMath.TWO_PI;
			if (angle < 0)
			{
				angle += FastMath.TWO_PI;
			}
			if (Geometry.near(angle, FastMath.HALF_PI * 0.5))
			{
				if (FastMath.rand.nextBoolean())
				{
					return LEFTWARD;
				}
				return UPWARD;
			}
			if (Geometry.near(angle, FastMath.HALF_PI * 1.5))
			{
				if (FastMath.rand.nextBoolean())
				{
					return UPWARD;
				}
				return RIGHTWARD;
			}
			if (Geometry.near(angle, FastMath.HALF_PI * 2.5))
			{
				if (FastMath.rand.nextBoolean())
				{
					return RIGHTWARD;
				}
				return DOWNWARD;
			}
			if (Geometry.near(angle, FastMath.HALF_PI * 3.5))
			{
				if (FastMath.rand.nextBoolean())
				{
					return LEFTWARD;
				}
				return DOWNWARD;
			}
			if (angle < FastMath.HALF_PI * 0.5 || angle > FastMath.HALF_PI * 3.5)
			{
				return LEFTWARD;
			}
			if (angle < FastMath.HALF_PI * 1.5)
			{
				return UPWARD;
			}
			if (angle < FastMath.HALF_PI * 2.5)
			{
				return RIGHTWARD;
			}
			return DOWNWARD;
		}

		public static MovementDelta fromDelta(final GridPoint origin, final GridPoint destination)
		{
			return MovementDelta
					.fromDelta(new Vector2f(origin.x, origin.y), new Vector2f(destination.x, destination.y));
		}

		public static MovementDelta fromDelta(final Point delta)
		{
			return MovementDelta.fromDelta(new Vector2f(delta.x, delta.y));
		}

		public static MovementDelta fromDelta(final Vector2f delta)
		{
			final Float angle = Geometry.getAngleTowards(delta);
			if (angle == null)
			{
				return LEFTWARD; // Default
			}
			return MovementDelta.fromAngle(angle);
		}

		public static MovementDelta fromDelta(final Vector2f origin, final Vector2f destination)
		{
			return MovementDelta.fromDelta(destination.subtract(origin));
		}

		public float getAngle()
		{
			switch (this)
			{
				case UPWARD:
					return FastMath.HALF_PI;
				case RIGHTWARD:
					return FastMath.PI;
				case DOWNWARD:
					return FastMath.HALF_PI * 3;
			}
			return 0;
		}
	}

	public static float clampFloat(final float min, final float value, final float max)
	{
		return Math.min(max, Math.max(min, value));
	}

	public static int clampInt(final int min, final int value, final int max)
	{
		return Math.min(max, Math.max(min, value));
	}

	public static Float getAngleTowards(final Vector2f point)
	{
		if (point == null)
		{
			return 0f;
		}
		float angle = 0f;
		if (point.equals(Vector2f.ZERO))
		{
			return null; // Don't rotate at all
		}
		if (point.x == 0) // Avoid division by 0
		{
			if (point.y < 0)
			{
				angle = FastMath.HALF_PI;
			}
			else
			{
				angle = -FastMath.HALF_PI;
			}
		}
		else
		{
			angle = FastMath.PI + FastMath.atan(point.y / point.x);
			if (point.x < 0)
			{
				angle -= FastMath.PI;
			}
		}
		return angle;
	}

	public static float getRandomFloatBetween(final double min, final double max)
	{
		return (float) (min + FastMath.rand.nextDouble() * (max - min));
	}

	public static float getRandomFloatBetween(final float min, final float max)
	{
		return getRandomFloatBetween((double) min, (double) max);
	}

	public static Vector2f getRandomVector2fWithin(final float xMin, final float xMax, final float yMin,
			final float yMax)
	{
		return getRandomVector2fWithin(new Vector2f(xMin, yMin), new Vector2f(xMax, yMax));
	}

	public static Vector2f getRandomVector2fWithin(final Vector2f min, final Vector2f max)
	{
		return new Vector2f(getRandomFloatBetween(min.x, max.x), getRandomFloatBetween(min.y, max.y));
	}

	public static Map<Border, Float> isInBorder(final Vector2f point, final Rectangle rectangle, final float boundary)
	{
		final Map<Border, Float> borderRatios = new EnumMap<Border, Float>(Border.class);
		final float x = point.x;
		final float y = point.y;
		final float borderWidth = rectangle.width * boundary;
		final float borderHeight = rectangle.height * boundary;
		if (rectangle.x <= x && x <= rectangle.x + borderWidth)
		{
			borderRatios.put(Border.LEFT, 1 - (x - rectangle.x) / borderWidth);
		}
		else if (rectangle.x + rectangle.width - borderWidth <= x && x <= rectangle.x + rectangle.width)
		{
			borderRatios.put(Border.RIGHT, 1 - (rectangle.x + rectangle.width - x) / borderWidth);
		}
		if (rectangle.y <= y && y <= rectangle.y + borderHeight)
		{
			borderRatios.put(Border.DOWN, 1 - (y - rectangle.y) / borderHeight);
		}
		else if (rectangle.y + rectangle.height - borderHeight <= y && y <= rectangle.y + rectangle.height)
		{
			borderRatios.put(Border.UP, 1 - (rectangle.y + rectangle.height - y) / borderHeight);
		}
		return borderRatios;
	};

	public static boolean near(final double x, final double y)
	{
		return nearZero(x - y);
	}

	public static boolean near(final float x, final float y)
	{
		return near((double) x, (double) y);
	}

	public static boolean nearZero(final double x)
	{
		return Math.abs(x) < 0.00001;
	}

	public static boolean nearZero(final float x)
	{
		return Geometry.nearZero((double) x);
	}
}