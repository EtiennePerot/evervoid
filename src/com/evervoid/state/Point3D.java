package com.evervoid.state;

import com.jme3.math.FastMath;

public class Point3D
{
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
		return FastMath.sqr(deltaX) + FastMath.sqr(deltaY) + FastMath.sqr(deltaZ);
	}

	public double getDistanceToOrigin()
	{
		return FastMath.sqrt(FastMath.sqr(x) + FastMath.sqr(y) + FastMath.sqr(z));
	}

	@Override
	public String toString()
	{
		return "Point3D[" + x + "; " + y + ";" + z + "]";
	}
}
