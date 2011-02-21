package com.evervoid.state.geometry;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.math.FastMath;

public class Point3D implements Jsonable
{
	public static Point3D fromJson(final Json j)
	{
		return new Point3D(j.getFloatAttribute("x"), j.getFloatAttribute("y"), j.getFloatAttribute("z"));
	}

	public final float x;
	public final float y;
	public final float z;

	public Point3D(final float x, final float y, final float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D(final int x, final int y, final int z)
	{
		this((float) x, (float) y, (float) z);
	}

	public float distanceTo(final Point3D pPoint)
	{
		final float deltaX = x - pPoint.x;
		final float deltaY = y - pPoint.y;
		final float deltaZ = z - pPoint.z;
		final float sumSquares = FastMath.sqr(deltaX) + FastMath.sqr(deltaY) + FastMath.sqr(deltaZ);
		return FastMath.sqrt(sumSquares);
	}

	public float getDistanceToOrigin()
	{
		return distanceTo(new Point3D(0, 0, 0));
	}

	public boolean sameAs(final Point3D pPoint2)
	{
		return x == pPoint2.x && y == pPoint2.y && z == pPoint2.z;
	}

	public Point3D scale(final float scale)
	{
		return new Point3D(x * scale, y * scale, z * scale);
	}

	@Override
	public Json toJson()
	{
		return new Json().setFloatAttribute("x", x).setFloatAttribute("y", y).setFloatAttribute("z", z);
	}

	@Override
	public String toString()
	{
		return "Point3D[" + x + "; " + y + ";" + z + "]";
	}
}
