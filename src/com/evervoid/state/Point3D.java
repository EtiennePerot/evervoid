package com.evervoid.state;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.math.FastMath;

public class Point3D implements Jsonable
{
	public static Point3D fromJson(final Json j)
	{
		return new Point3D(j.getIntAttribute("x"), j.getIntAttribute("y"), j.getIntAttribute("z"));
	}

	public final int x;
	public final int y;
	public final int z;

	public Point3D(final int x, final int y, final int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double distanceTo(final Point3D pPoint)
	{
		final int deltaX = x - pPoint.x;
		final int deltaY = y - pPoint.y;
		final int deltaZ = z - pPoint.z;
		final float sumSquares = FastMath.sqr(deltaX) + FastMath.sqr(deltaY) + FastMath.sqr(deltaZ);
		return FastMath.sqrt(sumSquares);
	}

	public double getDistanceToOrigin()
	{
		return distanceTo(new Point3D(0, 0, 0));
	}

	@Override
	public Json toJson()
	{
		return new Json().setIntAttribute("x", x).setIntAttribute("y", y).setIntAttribute("z", z);
	}

	@Override
	public String toString()
	{
		return "Point3D[" + x + "; " + y + ";" + z + "]";
	}
}
