package client.graphics;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class GridCell extends PlainRectangle
{
	private final int aColumn;
	private final int aRow;

	public GridCell(final int row, final int column, final Vector3f origin, final float width, final float height,
			final ColorRGBA fill)
	{
		super(origin, width, height, fill);
		aRow = row;
		aColumn = column;
	}

	public int[] getGridLocation()
	{
		final int[] location = { aRow, aColumn };
		return location;
	}
}
