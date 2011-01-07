package client.views.solar;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.GameView;
import client.everVoidClient;
import client.graphics.FrameUpdate;
import client.graphics.Grid.HoverMode;
import client.graphics.UIShip;
import client.graphics.geometry.Geometry;
import client.graphics.geometry.Transform;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class SolarSystemView extends GameView
{
	private final SolarSystemGrid aGrid;
	private final Transform aGridOffset;
	/**
	 * Rectangle defining the visible part of the grid.
	 */
	private Rectangle aGridScrollRegion = new Rectangle(0, 0, everVoidClient.sScreenWidth, everVoidClient.sScreenHeight);
	private final Vector2f aGridTranslation = new Vector2f();
	// TODO: Remove lol
	private final List<UIShip> aLolShips = new ArrayList<UIShip>();
	private UIShip tmpShip;

	/**
	 * Default constructor which initiates a new Solar System View.
	 */
	public SolarSystemView()
	{
		super();
		aGrid = new SolarSystemGrid();
		addNode(aGrid);
		aGrid.setHandleHover(HoverMode.ON);
		aGrid.setHoverColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
		aGridOffset = aGrid.getNewTransform();
		aGridOffset.setMinimumConstraint(aGridScrollRegion.width - aGrid.getTotalWidth(), aGridScrollRegion.height
				- aGrid.getTotalHeight());
		aGridOffset.setMaximumConstraint(2, 2);
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		aGridOffset.move(aGridTranslation.mult(f.aTpf));
		// Hovered square
		final Vector2f gridPosition = getGridPosition(f.getMousePosition());
		final Point gridPoint = aGrid.handleOver(gridPosition);
		tmpShip.faceTowards(gridPoint);
	}

	/**
	 * Get the position of the origin of the cell in which the given position is
	 * located.
	 * 
	 * @param position
	 *            Vector representing a position in 2-D space.
	 * @return The origin of the cell in which the position is located.
	 */
	protected Vector2f getGridPosition(final Vector2f position)
	{
		return position.add(aGridOffset.getTranslation2f().negate());
	}

	@Override
	public void onMouseClick(final Vector2f position, final float tpf)
	{
		final Point gridPoint = aGrid.getCellAt(getGridPosition(position));
		tmpShip.moveShip(gridPoint);
		for (final UIShip s : aLolShips)
		{
			s.select(); // FIXME: lol hax
			s.moveShip(FastMath.rand.nextInt(aGrid.getRows()), FastMath.rand.nextInt(aGrid.getColumns()));
		}
	}

	@Override
	public void onMouseMove(final String name, final float tpf, final Vector2f position)
	{
		// Recompute grid scrolling speed
		aGridTranslation.set(0, 0);
		for (final Map.Entry<Geometry.Border, Float> e : Geometry.isInBorder(position, aGridScrollRegion,
				Constants.GRID_SCROLL_BORDER).entrySet())
		{
			aGridTranslation.addLocal(-e.getKey().getXDirection() * e.getValue() * Constants.GRID_SCROLL_SPEED, -e
					.getKey().getYDirection() * e.getValue() * Constants.GRID_SCROLL_SPEED);
		}
	}

	@Override
	public void resolutionChanged()
	{
		aGridScrollRegion = new Rectangle(0, 0, everVoidClient.sScreenWidth, everVoidClient.sScreenHeight);
	}

	/**
	 * Temporary; delete once engine is done
	 */
	public void sampleGame()
	{
		for (int i = 0; i < 20; i++)
		{
			final UIShip lolship = new UIShip(aGrid, FastMath.rand.nextInt(48), FastMath.rand.nextInt(48));
			lolship.setHue(ColorRGBA.randomColor());
			aLolShips.add(lolship);
		}
		tmpShip = new UIShip(aGrid, 4, 10);
		tmpShip.setHue(ColorRGBA.Red);
		tmpShip.select();
	}
}
