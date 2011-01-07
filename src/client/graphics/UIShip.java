package client.graphics;

import client.graphics.geometry.GridPoint;
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
	private ShipTrail aTrail;

	public UIShip(final SolarSystemGrid grid, final GridPoint location)
	{
		super(grid, location);
		aGridTranslation.setDuration(1); // Set moving speed
	}

	public UIShip(final SolarSystemGrid grid, final int row, final int column)
	{
		this(grid, new GridPoint(column, row));
	}

	@Override
	protected void buildSprite()
	{
		final Sprite baseSprite = new Sprite("ships/square/scout_base.png");
		addSprite(baseSprite);
		aColorableSprite = new Sprite("ships/square/scout_color.png");
		addSprite(aColorableSprite);
		aTrail = new ShipTrail();
		addSprite(aTrail, Sprite.sSpriteScale * baseSprite.getWidth(), 0);
	}

	@Override
	public void faceTowards(final GridPoint target)
	{
		if (aState == ShipState.SELECTED)
		{
			super.faceTowards(target);
		}
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		super.frame(f);
		aTrail.setGradualState(aGridTranslation.getMovingSpeed());
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

	public void moveShip(final GridPoint destination)
	{
		if (aState == ShipState.SELECTED)
		{
			faceTowards(destination);
			aMovementDelta = MovementDelta.fromDelta(aGridLocation, destination);
			super.smoothMoveTo(destination);
			aState = ShipState.MOVING;
		}
	}

	public void moveShip(final int row, final int column)
	{
		moveShip(new GridPoint(column, row));
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
