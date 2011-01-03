package client.graphics;

import java.awt.Point;

import client.EverNode;
import client.graphics.geometry.AnimatedTranslation;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GridNode extends EverNode
{
	private final Grid aGrid;
	private Point aGridLocation;
	private final AnimatedTranslation aGridTranslation = getNewTranslationAnimation();

	public GridNode(final Grid grid, final Point location)
	{
		aGrid = grid;
		aGrid.registerNode(this, location);
		aGridLocation = location;
		updateTranslation();
		aGridTranslation.setDuration(1); // FIXME: temp
	}

	public Vector3f getCellCenter()
	{
		return aGrid.getCellCenter(aGridLocation);
	}

	public Vector2f getTranslation()
	{
		return aGridTranslation.getTranslation2f();
	}

	public void moveTo(final Point destination)
	{
		aGrid.nodeMoved(this, aGridLocation, destination);
		aGridLocation = destination;
	}

	public void smoothMoveTo(final Point destination)
	{
		aGrid.nodeMoved(this, aGridLocation, destination);
		aGridLocation = destination;
		aGridTranslation.smoothMoveTo(aGrid.getCellCenter(destination)).start(new Runnable()
		{
			@Override
			public void run()
			{
				if (!aGridTranslation.isInProgress())
				{
					System.out.println("Callback: yeah");
					updateTranslation();
				}
				else
				{
					System.out.println("Callback: nope");
				}
			}
		});
	}

	protected void updateTranslation()
	{
		aGridTranslation.setTranslationNow(getCellCenter());
	}
}
