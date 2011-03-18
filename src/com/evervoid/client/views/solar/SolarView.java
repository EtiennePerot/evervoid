package com.evervoid.client.views.solar;

import java.util.Map;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EVInputManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.AnimatedScaling;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.graphics.geometry.MathUtils.AxisDelta;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.GameView.PerspectiveType;
import com.evervoid.state.SolarSystem;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class SolarView extends EverView implements EVFrameObserver
{
	private static final int sFadeOutSeconds = 5;
	/**
	 * Maximum zoom exponent of the grid
	 */
	public static final int sGridMaxZoomExponent = 1;
	/**
	 * Pixels to leave between edge of the screen and start of the grid
	 */
	private static final float sGridMinimumBorderOffset = 2;
	/**
	 * Ratio of the screen sides allocated for grid movement
	 */
	public static final float sGridScrollBorder = 0.1f;
	/**
	 * Maximum grid scrolling speed (in pixels/seconds)
	 */
	public static final float sGridScrollSpeed = 1024f;
	/**
	 * Delay that the user has to wait before being able to go from fully-zoomed-out solar view to galaxy view
	 */
	private static final float sGridToGalaxyDelay = 0.5f;
	/**
	 * Percentage of Bounds dimension that will be avoided for ensuring GridLocation visibility
	 */
	private static final float sGridVisibilityMargin = 0.025f;
	/**
	 * Duration of the zoom animation when using the mouse. Public because it is also used by the Star field
	 */
	public static final float sGridZoomDuration = 0.4f;
	/**
	 * At each scroll wheel event, multiply or divide the zoom by this amount
	 */
	public static final float sGridZoomFactor = 1.5f;
	/**
	 * Delay between successive zooms using the keyboard zoom shortcut
	 */
	private static final float sGridZoomInterval = 0.1f;
	/**
	 * Main solar system grid
	 */
	private final SolarGrid aGrid;
	private final AnimatedAlpha aGridAlphaFade;
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
	/**
	 * Handles repetitive zooming with the keyboard
	 */
	private final FrameTimer aKeyboardZoomTimer;
	private float aLastHoverTime = 0;
	private AxisDelta aLastZoomDirection = null;
	/**
	 * Handles delay until zooming out to galaxy view is permitted
	 */
	private final FrameTimer aMinZoomDelayTimer;
	private final SolarPerspective aPerspective;
	private SolarStarfield aStarfield = null;

	/**
	 * Default constructor which initiates a new Solar System View.
	 */
	public SolarView(final SolarSystem solarsystem, final SolarPerspective perspective)
	{
		resolutionChanged();
		aPerspective = perspective;
		aGrid = new SolarGrid(this, solarsystem);
		addNode(aGrid);
		aGridAlphaFade = aGrid.getLineAlphaAnimation();
		aGridOffset = aGrid.getNewTranslationAnimation();
		aGridScale = aGrid.getNewScalingAnimation();
		aGridOffset.setDuration(sGridZoomDuration);
		aGridScale.setDuration(sGridZoomDuration);
		aGridDimensions.set(aGrid.getTotalWidth(), aGrid.getTotalHeight());
		aMinZoomDelayTimer = new FrameTimer(sGridToGalaxyDelay, 1);
		aKeyboardZoomTimer = new FrameTimer(new Runnable()
		{
			@Override
			public void run()
			{
				gridZoom(getLastZoomDirection());
			}
		}, sGridZoomInterval);
	}

	private void adjustGrid()
	{
		translateGrid(null, 0);
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
		if (gridDimension.x <= bounds.width) {
			finalT.setX(bounds.x + bounds.width / 2 - gridDimension.x / 2);
		}
		else {
			finalT.setX(MathUtils.clampFloat(bounds.x + bounds.width - gridDimension.x, translation.x, sGridMinimumBorderOffset
					+ bounds.x));
		}
		if (gridDimension.y <= bounds.height) {
			finalT.setY(bounds.y + bounds.height / 2 - gridDimension.y / 2);
		}
		else {
			finalT.setY(MathUtils.clampFloat(bounds.y + bounds.height - gridDimension.y, translation.y,
					sGridMinimumBorderOffset + bounds.y));
		}
		return finalT;
	}

	/**
	 * Ensures that a given GridLocation is visible within this view's bounds
	 * 
	 * @param location
	 *            The grid-based rectangle of location to check
	 */
	void ensureLocationVisible(final Rectangle location)
	{
		final Rectangle screenPos = location.mult(aGridScale.getScaleAverage()).add(aGridOffset.getTranslation2f());
		final Bounds visibleBounds = getBounds().contract(sGridVisibilityMargin);
		if (!visibleBounds.contains(screenPos.x, screenPos.y, screenPos.width, screenPos.height)) {
			// Need to scroll to that location
			float targetX = 0f;
			float targetY = 0f;
			if (screenPos.x < visibleBounds.x) {
				targetX = visibleBounds.x - screenPos.x;
			}
			else if (screenPos.x + screenPos.width >= visibleBounds.x + visibleBounds.width) {
				targetX = visibleBounds.x + visibleBounds.width - screenPos.x - screenPos.width;
			}
			if (screenPos.y < visibleBounds.y) {
				targetY = visibleBounds.y - screenPos.y;
			}
			else if (screenPos.y + screenPos.height >= visibleBounds.y + visibleBounds.height) {
				targetY = visibleBounds.y + visibleBounds.height - screenPos.y - screenPos.height;
			}
			scrollGrid(new Vector2f(targetX, targetY), SolarGrid.sKeyboardAutoScrollInterval);
		}
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		if (!aGridScale.isInProgress()) {
			aGrid.autoscroll(f.aTpf, aGridScale.getScaleAverage(), false);
			scrollGrid(aGridTranslationStep.mult(f.aTpf), 0);
			if (!aGridTranslationStep.equals(Vector2f.ZERO)) {
				hoverGrid(f.getMousePosition(), f.aTpf);
			}
		}
		else if (!aGrid.isLastAutoScrolled()) {
			hoverGrid(f.getMousePosition(), f.aTpf);
		}
	}

	/**
	 * Get the grid-relative position of the given screen position. Takes into account grid translation and grid scale
	 * 
	 * @param position
	 *            Vector representing a position in screen space.
	 * @return The origin of the cell in which the position is located (in world space).
	 */
	private Vector2f getGridPosition(final Vector2f position)
	{
		return position.subtract(aGridOffset.getTranslation2f()).divide(aGridScale.getScaleAverage());
	}

	private AxisDelta getLastZoomDirection()
	{
		return aLastZoomDirection;
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
			if (aGrid.getTotalWidth() * rescale > getBoundsWidth() || aGrid.getTotalHeight() * rescale > getBoundsHeight()) {
				// We can zoom out by that much
				aGridZoomExponent--;
				return rescale; // Already computed
			}
			// Otherwise we've reached the minimum zoom level
			aGridZoomMinimum = true;
			return Math.min(getBoundsWidth() / aGrid.getTotalWidth(), getBoundsHeight() / aGrid.getTotalHeight());
		}
	}

	SolarPerspective getPerspective()
	{
		return aPerspective;
	}

	/**
	 * Handles zooming requests; does not necessarily rescale
	 * 
	 * @param delta
	 *            Zoom in/out
	 */
	private void gridZoom(final AxisDelta delta)
	{
		if (delta == null) {
			return;
		}
		if (delta.equals(AxisDelta.UP)) {
			final Float newScale = getNewZoomLevel(AxisDelta.UP);
			if (newScale != null) {
				rescaleGrid(newScale);
			}
		}
		else {
			if (aGridZoomMinimum && aMinZoomDelayTimer.isDone()) {
				// We've reached the furthest zoom level and the delay has been elapsed; switch perspective to Galaxy at that
				// point
				GameView.changePerspective(PerspectiveType.GALAXY);
			}
			else {
				final Float newScale = getNewZoomLevel(AxisDelta.DOWN);
				if (newScale != null) {
					rescaleGrid(newScale);
				}
			}
		}
	}

	private void hoverGrid(final Vector2f mousePosition, final float tpf)
	{
		final Vector2f gridPosition = getGridPosition(mousePosition);
		if (aGrid.hover(gridPosition)) {
			if (aLastHoverTime != 0) {
				aLastHoverTime = 0;
				aGridAlphaFade.setTargetAlpha(1).setDuration(0.25f).start();
			}
		}
		else {
			aLastHoverTime += tpf;
			if (aLastHoverTime > sFadeOutSeconds && aGridAlphaFade.getTargetAlpha() != 0) {
				aGridAlphaFade.setTargetAlpha(0).setDuration(5).start();
			}
		}
	}

	public void newTurn()
	{
		// FIXME: This is hax for demo
		aGrid.newTurn();
	}

	/**
	 * Move the Starfield to this view whenever the user switches to it
	 */
	@Override
	public void onDefocus()
	{
		EVFrameManager.deregister(this);
		aMinZoomDelayTimer.stop();
		delNode(aStarfield);
		aStarfield = null;
	}

	/**
	 * Move the Starfield to this view whenever the user switches to it
	 */
	@Override
	public void onFocus()
	{
		EVFrameManager.register(this);
		aStarfield = SolarStarfield.getInstance();
		addNode(aStarfield);
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		// Handle keyboard zoom
		if (EVInputManager.shiftPressed()) {
			if (KeyboardKey.UP.equals(key)) {
				aLastZoomDirection = AxisDelta.UP;
				aKeyboardZoomTimer.restart().runNow();
				return true;
			}
			else if (KeyboardKey.DOWN.equals(key)) {
				aLastZoomDirection = AxisDelta.DOWN;
				aKeyboardZoomTimer.restart().runNow();
				return true;
			}
		}
		return aGrid.onKeyPress(key, aGridScale.getScaleAverage());
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		if (KeyboardKey.UP.equals(key) || KeyboardKey.DOWN.equals(key)) {
			aLastZoomDirection = null;
			aKeyboardZoomTimer.stop();
			// Do not return here; do forward to aGrid's controller
		}
		return aGrid.onKeyRelease(key);
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		aGrid.leftClick(getGridPosition(position));
		return true;
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		// Recompute grid scrolling speed
		aGridTranslationStep.set(0, 0);
		// Looks ugly, but is actually pretty clean
		for (final Map.Entry<MathUtils.Border, Float> e : MathUtils.isInBorder(position, aGridScrollRegion, sGridScrollBorder)
				.entrySet()) {
			aGridTranslationStep.addLocal(-e.getKey().getXDirection() * e.getValue() * sGridScrollSpeed, -e.getKey()
					.getYDirection() * e.getValue() * sGridScrollSpeed);
		}
		hoverGrid(position, tpf);
		return true;
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		gridZoom(AxisDelta.DOWN);
		return true;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		gridZoom(AxisDelta.UP);
		return true;
	}

	@Override
	public boolean onRightClick(final Vector2f position, final float tpf)
	{
		aGrid.rightClick(getGridPosition(position));
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
		Vector2f gridTranslation = aGrid.getZoomFocusLocation().clone();
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
		aGridScale.setTargetScale(newScale).start(new Runnable()
		{
			@Override
			public void run()
			{
				// Reset seconds since min zoom
				aMinZoomDelayTimer.restart();
			}
		});
		// Set and start translation animation; will not conflict with the grid
		// boundary movement
		translateGrid(gridTranslation, aGridScale.getDuration());
	}

	@Override
	public void resolutionChanged()
	{
		// This needs to be separate from setBounds, because the grid's scrolling area should be on the screen edges no matter
		// what.
		aGridScrollRegion = new Rectangle(0, 0, EverVoidClient.getWindowDimension().width,
				EverVoidClient.getWindowDimension().height);
		adjustGrid();
	}

	/**
	 * Scroll grid by the specified amount, checking bounds
	 * 
	 * @param translation
	 *            Relative target translation of the grid
	 * @param duration
	 *            Animation duration (0 for no animation)
	 */
	private void scrollGrid(final Vector2f translation, final float duration)
	{
		if (aGridOffset != null) {
			translateGrid(constrainGrid(aGridOffset.getTranslation2f().add(translation)), duration);
		}
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		adjustGrid();
	}

	/**
	 * Do the actual grid translation
	 * 
	 * @param translation
	 *            Absolute translation of the grid
	 * @param duration
	 *            Duration of the animation (0 for no animation)
	 */
	private void translateGrid(Vector2f translation, final float duration)
	{
		if (aGridOffset == null) {
			return;
		}
		if (translation == null) {
			translation = constrainGrid();
		}
		if (duration != 0) {
			aGridOffset.setDuration(duration);
			aGridOffset.smoothMoveTo(translation).start();
		}
		else if (aStarfield != null) {
			final Vector2f delta = translation.subtract(aGridOffset.getTranslation2f());
			aStarfield.scrollBy(delta);
			aGridOffset.translate(translation);
		}
	}
}
