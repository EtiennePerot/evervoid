package com.evervoid.client.views.solar;

import java.util.List;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.GridNode;
import com.evervoid.client.graphics.MultiSprite;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.AnimatedFloatingTranslation;
import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.client.graphics.geometry.MathUtils.MovementDelta;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Prop;

public abstract class UIProp extends GridNode
{
	protected static enum PropState
	{
		INACTIVE, MOVING, SELECTABLE, SELECTED;
	}

	protected AnimatedRotation aFaceTowards = getNewRotationAnimation();
	protected GridLocation aFacing = null;
	protected AnimatedFloatingTranslation aFloatingAnimation;
	private MovementDelta aMovementDelta;
	private UIControl aPanelUI = null;
	protected Prop aProp;
	protected final AnimatedAlpha aPropAlpha = getNewAlphaAnimation();
	// Do NOT make aPropState protected; use getter instead
	private PropState aPropState = PropState.SELECTABLE;
	protected SolarGrid aSolarSystemGrid;
	protected MultiSprite aSprite = new MultiSprite();
	protected boolean aSpriteReady = false;

	public UIProp(final SolarGrid grid, final GridLocation location, final Prop prop)
	{
		super(grid, location);
		addNode(aSprite);
		aSolarSystemGrid = grid;
		aProp = prop;
		addToGrid();
		aPropAlpha.setDuration(0.5f);
	}

	protected EverNode addSprite(final EverNode sprite)
	{
		return aSprite.addSprite(sprite);
	}

	protected EverNode addSprite(final EverNode sprite, final float x, final float y)
	{
		return aSprite.addSprite(sprite, x, y);
	}

	protected EverNode addSprite(final SpriteData info)
	{
		return aSprite.addSprite(info);
	}

	protected EverNode addSprite(final String image)
	{
		return aSprite.addSprite(new Sprite(image));
	}

	protected EverNode addSprite(final String image, final float x, final float y)
	{
		return aSprite.addSprite(new Sprite(image), x, y);
	}

	protected abstract UIControl buildPanelUI();

	/**
	 * Called by subclasses when they have obtained sufficient data to be able to build their sprite.
	 */
	protected void buildProp()
	{
		buildSprite();
		aSpriteReady = true;
	}

	/**
	 * Overridden by subclasses; called when the sprite should be built.
	 */
	protected abstract void buildSprite();

	/**
	 * Called by subclasses when they desire to have a floating animation. Automatically starts the animation
	 * 
	 * @param duration
	 *            The duration of the floating animation
	 * @param offset
	 *            The maximum distance the animation may go
	 */
	protected void enableFloatingAnimation(final float duration, final float offset)
	{
		aFloatingAnimation = getNewFloatingTranslationAnimation();
		aFloatingAnimation.setToleratedOffset(offset).setDuration(duration).start();
	}

	public void faceTowards(final float angle)
	{
		faceTowards(angle, null);
	}

	public void faceTowards(final float angle, final Runnable runnable)
	{
		aFacing = null;
		aFaceTowards.setTargetPitch(angle).start(runnable);
	}

	public void faceTowards(final GridLocation target)
	{
		if (target != null && !target.equals(aFacing)) {
			aFaceTowards.setTargetPoint2D(aGrid.getCellCenter(target).subtract(getCellCenter())).start();
			aFacing = target;
		}
	}

	public void faceTowards(final GridLocation target, final Runnable callback)
	{
		if (target != null && !target.equals(aFacing)) {
			setState(PropState.MOVING);
			aFaceTowards.setTargetPoint2D(aGrid.getCellCenter(target).subtract(getCellCenter())).start(callback);
			aFacing = target;
		}
	}

	public float getFacingDirection()
	{
		return aFaceTowards.getRotationPitch();
	}

	/**
	 * Returns the UI that should be shown in the panel when this prop is selected. Called by the perspective. Do not override
	 * this; override buildPanelUI instead
	 * 
	 * @return The UI to show in the bottom panel
	 */
	public final UIControl getPanelUI()
	{
		if (aPanelUI == null) {
			refreshUI();
		}
		return aPanelUI;
	}

	Prop getProp()
	{
		return aProp;
	}

	protected PropState getPropState()
	{
		return aPropState;
	}

	public SolarGrid getSolarSystemGrid()
	{
		return aSolarSystemGrid;
	}

	boolean isMovable()
	{
		return false; // Not movable by default
	}

	boolean isSelectable()
	{
		return aPropState.equals(PropState.SELECTABLE);
	}

	protected void refreshUI()
	{
		if (aPanelUI == null) {
			aPanelUI = new UIControl();
		}
		aPanelUI.delAllChildUIs();
		aPanelUI.addUI(buildPanelUI(), 1);
	}

	public void setState(final PropState propState)
	{
		aPropState = propState;
		if (aPropState.equals(PropState.INACTIVE)) {
			aPropAlpha.setTargetAlpha(0.5f).start();
		}
		else {
			aPropAlpha.setTargetAlpha(1).start();
		}
	}

	/**
	 * Actually calls the underlying smoothMoveTo, once faceTowards is done
	 */
	private void smoothActualMoveTo(final List<GridLocation> moves, final Runnable callback)
	{
		super.smoothMoveTo(moves, callback);
	}

	/**
	 * Shadow the GridNode's smoothMoveTo in order to add a faceTowards in between.
	 */
	@Override
	public void smoothMoveTo(final List<GridLocation> moves, final Runnable callback)
	{
		setState(PropState.MOVING);
		if (moves.isEmpty()) {
			if (aMovementDelta != null) {
				faceTowards(aMovementDelta.getAngle());
			}
			smoothActualMoveTo(moves, callback);
		}
		else {
			final GridLocation next = moves.get(0);
			aMovementDelta = MovementDelta.fromDelta(aGridLocation, next);
			faceTowards(next, new Runnable()
			{
				@Override
				public void run()
				{
					smoothActualMoveTo(moves, callback);
				}
			});
		}
	}
}
