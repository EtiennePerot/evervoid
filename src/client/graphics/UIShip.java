package client.graphics;

import java.awt.Point;

import client.graphics.geometry.Geometry.MovementDelta;
import client.views.solar.SolarSystemGrid;

import com.jme3.math.ColorRGBA;

public class UIShip extends Prop implements Colorable
{
	protected static enum ShipState
	{
		INACTIVE, MOVING, SELECTABLE, SELECTED;
	}

	private Sprite aColorableSprite;
	private MovementDelta aMovementDelta;
	private ShipState aState = ShipState.INACTIVE;

	public UIShip(final SolarSystemGrid grid, final int row, final int column)
	{
		this(grid, new Point(column, row));
	}

	public UIShip(final SolarSystemGrid grid, final Point location)
	{
		super(grid, location);
	}

	@Override
	protected void buildSprite()
	{
		addSprite("ships/square/scout_base.png");
		aColorableSprite = addSprite("ships/square/scout_color.png");
	}

	@Override
	public void faceTowards(final Point target)
	{
		if (aState == ShipState.SELECTED)
		{
			super.faceTowards(target);
		}
	}

	@Override
	public void hasMoved()
	{
		super.hasMoved();
		if (aMovementDelta != null)
		{
			faceTowards(aMovementDelta.getAngle());
		}
		// FIXME: Should be "inactive" after moving
		// but selected for now because it's more convenient for testing
		aState = ShipState.SELECTED;
	}

	public void moveShip(final int row, final int column)
	{
		moveShip(new Point(column, row));
	}

	public void moveShip(final Point destination)
	{
		if (aState == ShipState.SELECTED)
		{
			faceTowards(destination);
			aMovementDelta = MovementDelta.fromDelta(aGridLocation, destination);
			super.smoothMoveTo(destination);
			aState = ShipState.MOVING;
		}
	}

	public void select()
	{
		aState = ShipState.SELECTED;
	}

	@Override
	public void setHue(final ColorRGBA hue)
	{
		aColorableSprite.setHue(hue);
	}

	@Override
	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		aColorableSprite.setHue(hue, multiplier);
	}
}
