package com.evervoid.client.views.solar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.FrameObserver;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.Grid.HoverMode;
import com.evervoid.client.graphics.geometry.AnimatedScaling;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.graphics.geometry.MathUtils.AxisDelta;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.gamedata.RaceData;
import com.evervoid.state.GridLocation;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class SolarSystemView extends EverView implements FrameObserver
{
	// TODO: Constantify this
	/**
	 * Color of hovered squares on the grid
	 */
	private static final ColorRGBA sGridHoverColor = new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f);
	/**
	 * Maximum zoom exponent of the grid
	 */
	private static final int sGridMaxZoomExponent = 4;
	/**
	 * Pixels to leave between edge of the screen and start of the grid
	 */
	private static final float sGridMinimumBorderOffset = 2;
	/**
	 * Duration of the zoom animation
	 */
	private static final float sGridZoomDuration = .5f;
	/**
	 * At each scroll wheel event, multiply or divide the zoom by this amount
	 */
	private static final float sGridZoomFactor = 1.5f;
	/**
	 * Main solar system grid
	 */
	private final SolarSystemGrid aGrid;
	/**
	 * Total dimensions of the grid, including scale
	 */
	private final Vector2f aGridDimensions = new Vector2f();
	/**
	 * Offset of the grid relative to the screen
	 */
	private final AnimatedTranslation aGridOffset;
	/**
	 * Scale (zoom) of the grid
	 */
	private final AnimatedScaling aGridScale;
	/**
	 * Rectangle defining the scrolling region of the grid (area where mouse input matters)
	 */
	private Rectangle aGridScrollRegion = new Rectangle(0, 0, 1, 1);
	/**
	 * Relative translation that the grid should undergo at each second due to the cursor being on the borders of the screen
	 */
	private final Vector2f aGridTranslationStep = new Vector2f();
	/**
	 * Current zoom exponent
	 */
	private int aGridZoomExponent = 0;
	/**
	 * Whether the minimum zoom level has been reached or not
	 */
	private boolean aGridZoomMinimum = false;
	private final List<UIPlanet> aPlanetList = new ArrayList<UIPlanet>();
	private final Set<UIShip> aShipList = new HashSet<UIShip>();
	private UIShip tmpShip;

	/**
	 * Default constructor which initiates a new Solar System View.
	 */
	public SolarSystemView(final SolarSystem ss)
	{
		super();
		resolutionChanged();
		EVFrameManager.register(this);
		aGrid = new SolarSystemGrid(this, ss);
		addNode(aGrid);
		aGrid.setHandleHover(HoverMode.ON);
		aGrid.setHoverColor(sGridHoverColor);
		aGridOffset = aGrid.getNewTranslationAnimation();
		aGridScale = aGrid.getNewScalingAnimation();
		aGridOffset.setDuration(sGridZoomDuration);
		aGridScale.setDuration(sGridZoomDuration);
		aGridDimensions.set(aGrid.getTotalWidth(), aGrid.getTotalHeight());
		getProps(ss);
	}

	/**
	 * Compute and set the grid dimensions.
	 */
	public void computeGridDimensions()
	{
		aGridDimensions.set(aGrid.getTotalWidth(), aGrid.getTotalHeight()).multLocal(aGridScale.getScaleAverage());
	}

	private Vector2f constrainGrid()
	{
		if (aGridOffset != null) {
			return constrainGrid(aGridOffset.getTranslation2f());
		}
		return null;
	}

	private Vector2f constrainGrid(final Vector2f translation)
	{
		return constrainGrid(translation, aGridDimensions, getBounds().getRectangle());
	}

	private Vector2f constrainGrid(final Vector2f translation, final Vector2f gridDimension, final Rectangle bounds)
	{
		final Vector2f finalT = new Vector2f();
		if (gridDimension.x < bounds.width) {
			finalT.setX(bounds.x + bounds.width / 2 - gridDimension.x / 2);
		}
		else {
			finalT.setX(MathUtils.clampFloat(bounds.x + bounds.width - gridDimension.x, translation.x, sGridMinimumBorderOffset
					+ bounds.x));
		}
		if (gridDimension.y < bounds.height) {
			finalT.setY(bounds.y + bounds.height / 2 - gridDimension.y / 2);
		}
		else {
			finalT.setY(MathUtils.clampFloat(bounds.y + bounds.height - gridDimension.y, translation.y,
					sGridMinimumBorderOffset + bounds.y));
		}
		return finalT;
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		if (!aGridScale.isInProgress()) {
			scrollGrid(aGridTranslationStep.mult(f.aTpf));
		}
		// Take care of square
		final Vector2f gridPosition = getGridPosition(f.getMousePosition());
		final GridLocation hoveredPoint = aGrid.handleHover(gridPosition, tmpShip.getDimension());
		tmpShip.faceTowards(hoveredPoint);
	}

	/**
	 * Get the position of the origin of the cell in which the given screen position (usually cursor position) is located.
	 * Includes grid scale
	 * 
	 * @param position
	 *            Vector representing a position in screen space.
	 * @return The origin of the cell in which the position is located (in world space).
	 */
	protected Vector2f getGridPosition(final Vector2f position)
	{
		return position.subtract(aGridOffset.getTranslation2f()).divide(aGridScale.getScaleAverage());
	}

	private Float getNewZoomLevel(final AxisDelta exponentDelta)
	{
		if (exponentDelta.equals(AxisDelta.UP)) {
			// Zooming in
			if (aGridZoomMinimum) {
				// We've reached minimum zoom, so just zoom to last known
				// non-minimum level
				aGridZoomMinimum = false;
				return (float) FastMath.pow(sGridZoomFactor, aGridZoomExponent);
			}
			if (aGridZoomExponent < sGridMaxZoomExponent) {
				// We can zoom some more
				aGridZoomExponent++;
				return (float) FastMath.pow(sGridZoomFactor, aGridZoomExponent);
			}
			// Reached maximum zoom level
			return null;
		}
		else {
			// Zooming out
			if (aGridZoomMinimum) {
				// Can't zoom out any more
				return null;
			}
			final float rescale = FastMath.pow(sGridZoomFactor, aGridZoomExponent - 1);
			if (aGrid.getTotalWidth() * rescale > aGridScrollRegion.width
					|| aGrid.getTotalHeight() * rescale > aGridScrollRegion.height) {
				// We can zoom out by that much
				aGridZoomExponent--;
				return rescale; // Already computed
			}
			// Otherwise we've reached the minimum zoom level
			aGridZoomMinimum = true;
			return Math.min(aGridScrollRegion.width / aGrid.getTotalWidth(), aGridScrollRegion.height / aGrid.getTotalHeight());
		}
	}

	/**
	 * Gets all the props in the SolarSystem and adds them to the View.
	 */
	private void getProps(final SolarSystem ss)
	{
		// TODO - remove, this is for testing
		tmpShip = new UIShip(aGrid, new Ship(new Player("me").setRace(RaceData.getRaceData("ROUND")), new GridLocation(4, 4),
				"BIGASS"));
		tmpShip.select();
		// Get all the props
		final Iterator<Prop> iter = ss.getIterator();
		while (iter.hasNext()) {
			final Prop temp = iter.next();
			if (temp instanceof Ship) {
				aShipList.add(new UIShip(aGrid, (Ship) temp));
			}
			else if (temp instanceof Planet) {
				aPlanetList.add(new UIPlanet(aGrid, (Planet) temp));
			}
		}
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		final GridLocation gridPoint = aGrid.getCellAt(getGridPosition(position), tmpShip.getDimension());
		if (gridPoint != null) {
			tmpShip.moveShip(gridPoint);
			for (final UIShip s : aShipList) {
				s.select(); // FIXME: lol hax
				s.moveShip(new GridLocation(FastMath.rand.nextInt(aGrid.getColumns()), FastMath.rand.nextInt(aGrid.getRows())));
			}
		}
		return true;
	}

	@Override
	public boolean onMouseMove(final float tpf, final Vector2f position)
	{
		// Recompute grid scrolling speed
		aGridTranslationStep.set(0, 0);
		for (final Map.Entry<MathUtils.Border, Float> e : MathUtils.isInBorder(position, aGridScrollRegion,
				Constants.GRID_SCROLL_BORDER).entrySet()) {
			aGridTranslationStep.addLocal(-e.getKey().getXDirection() * e.getValue() * Constants.GRID_SCROLL_SPEED, -e.getKey()
					.getYDirection() * e.getValue() * Constants.GRID_SCROLL_SPEED);
		}
		return true;
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		final Float newScale = getNewZoomLevel(AxisDelta.DOWN);
		if (newScale != null) {
			rescaleGrid(newScale);
		}
		return true;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		final Float newScale = getNewZoomLevel(AxisDelta.UP);
		if (newScale != null) {
			rescaleGrid(newScale);
		}
		return true;
	}

	/**
	 * Rescale the solar system grid
	 * 
	 * @param newScale
	 *            The new (absolute) scale to reach
	 */
	private void rescaleGrid(final float newScale)
	{
		// Headache warning: Badass vector math ahead
		// First, compute the dimension that the grid will have after rescaling
		final Vector2f targetGridDimension = new Vector2f(aGrid.getTotalWidth(), aGrid.getTotalHeight()).mult(newScale);
		Vector2f gridTranslation = getGridPosition(EverVoidClient.sCursorPosition);
		// Then, compute the delta from the pointed-at cell before the scaling
		// to the same cell after the scaling
		gridTranslation.multLocal(aGridScale.getScaleAverage() - newScale);
		// Add that to the current world-coordinates offset of the grid
		gridTranslation.addLocal(aGridOffset.getTranslation2f());
		// We now have the good offset in world coordinates, but in case of an
		// intense zoom out, we need to make sure to constrain the offset to
		// stay on the screen
		gridTranslation = constrainGrid(gridTranslation, targetGridDimension, getBounds().getRectangle());
		// End of badass vector math - phew
		// Set and start scale animation
		aGridScale.setTargetScale(newScale).start();
		// Set and start translation animation; will not conflict with the grid
		// boundary movement
		aGridOffset.smoothMoveTo(gridTranslation).start();
	}

	@Override
	public void resolutionChanged()
	{
		// This needs to be separate from setBounds, because the grid's scrolling area should be on the screen edges no matter
		// what.
		aGridScrollRegion = new Rectangle(0, 0, EverVoidClient.getWindowDimension().width,
				EverVoidClient.getWindowDimension().height);
		if (aGridOffset != null) {
			aGridOffset.translate(constrainGrid());
		}
	}

	private void scrollGrid(final Vector2f translation)
	{
		if (aGridOffset != null) {
			aGridOffset.translate(constrainGrid(aGridOffset.getTranslation2f().add(translation)));
		}
	}

	@Override
	protected void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		if (aGridOffset != null) {
			aGridOffset.translate(constrainGrid());
		}
	}
}
