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

	public int distanceTo(final Point3D pPoint)
	{
		final int deltaX = x - pPoint.x;
		final int deltaY = y - pPoint.y;
		final int deltaZ = z - pPoint.z;
		return (int) (FastMath.sqr(deltaX) + FastMath.sqr(deltaY) + FastMath.sqr(deltaZ));
	}

	public double getDistanceToOrigin()
	{
		return Math.sqrt(x * x + y * y + z * z);
	}
}
