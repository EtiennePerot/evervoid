package com.evervoid.client.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.game.InGameChatView;
import com.jme3.math.Vector2f;

/**
 * An EverUIView is an {@link EverView} with a specialization to contain elements from the UI library ({@link UIControl}s). It
 * also handles stacking of multiple {@link UIControl} sets, for popup windows. It is better to use this class for
 * {@link UIControl} stacking rather than multiple {@link EverUIView}s in a {@link ComposedView}, because {@link ComposedView}
 * does not deal with views in a stacking order.
 */
public abstract class EverUIView extends EverView
{
	/**
	 * The list of all {@link UIControl}s to display. Ordered in stacking order (last element is displayed on top)
	 */
	private final List<UIControl> aAllUIs = new ArrayList<UIControl>();
	/**
	 * Whether this {@link EverUIView} should catch key events or ignore them. Useful when an {@link EverUIView} wants a lower
	 * {@link EverView} to catch input events.
	 */
	private boolean aCatchKeyEvents = true;
	/**
	 * The {@link AnimatedAlpha} used for the fade in/out transition.
	 */
	private final AnimatedAlpha aDisplayAlpha;
	/**
	 * Whether this {@link EverUIView} is currently visible the user or not
	 */
	private boolean aDisplayed = true;
	/**
	 * The target alpha value of this {@link EverUIView}. Useful for views that should stay semi-transparent, such as the
	 * {@link InGameChatView}.
	 */
	private double aDisplayedMaxAlpha = 1;
	/**
	 * A reference to the top-most {@link UIControl} on display. Null when this {@link EverUIView} contains no {@link UIControl}
	 * s.
	 */
	private UIControl aTopUI = null;
	/**
	 * Maps {@link UIControl} to the {@link Transform} objects used to make sure they are displayed in the correct order along
	 * the Z axis.
	 */
	private final Map<UIControl, Transform> aZOffsets = new HashMap<UIControl, Transform>();

	/**
	 * Constructor
	 * 
	 * @param root
	 *            The first {@link UIControl} to display
	 */
	public EverUIView(final UIControl root)
	{
		aDisplayAlpha = getNewAlphaAnimation();
		aDisplayAlpha.setDuration(0); // Animation is instant by default; can be changed using setAppearDuration
		aTopUI = root;
		aAllUIs.add(aTopUI);
		addNode(aTopUI);
		resolutionChanged();
	}

	/**
	 * Add a control with no spring
	 * 
	 * @param control
	 *            The control to add
	 */
	protected void addUI(final UIControl control)
	{
		if (aTopUI == null) {
			return;
		}
		aTopUI.addUI(control);
	}

	/**
	 * Add a control
	 * 
	 * @param control
	 *            The control to add
	 * @param spring
	 *            The spring value
	 */
	protected void addUI(final UIControl control, final int spring)
	{
		if (aTopUI == null) {
			return;
		}
		aTopUI.addUI(control, spring);
	}

	/**
	 * Pop the top-most {@link UIControl} from the stack, if any. If there was a {@link UIControl} below it, it becomes the new
	 * top-most {@link UIControl}.
	 */
	protected void deleteUI()
	{
		if (aTopUI == null) {
			return;
		}
		aAllUIs.remove(aTopUI);
		final Transform zOffset = aZOffsets.get(aTopUI);
		aTopUI.smoothDisappear(0.4f, new Runnable()
		{
			@Override
			public void run()
			{
				if (zOffset != null) {
					zOffset.delete();
					aZOffsets.remove(aTopUI);
				}
			}
		});
		if (!aAllUIs.isEmpty()) {
			aTopUI = aAllUIs.get(aAllUIs.size() - 1);
			aDisplayed = true;
		}
		else {
			aTopUI = null;
			aDisplayed = false;
		}
	}

	/**
	 * @return The computed height of the top-most {@link UIControl}, or null if there is no {@link UIControl} being displayed
	 *         at all.
	 */
	public Integer getComputedHeight()
	{
		if (aTopUI == null) {
			return null;
		}
		return aTopUI.getComputedHeight();
	}

	/**
	 * @return The computed width of the top-most {@link UIControl}, or null if there is no {@link UIControl} being displayed at
	 *         all.
	 */
	public Integer getComputedWidth()
	{
		if (aTopUI == null) {
			return null;
		}
		return aTopUI.getComputedWidth();
	}

	/**
	 * @return The number of {@link UIControl}s being stack in this {@link EverUIView}.
	 */
	public int getNumOfUIs()
	{
		return aAllUIs.size();
	}

	/**
	 * @return Whether this {@link EverUIView} is currently visible to the user or not.
	 */
	protected boolean isDisplayed()
	{
		return aDisplayed;
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (!aDisplayed || !aCatchKeyEvents || aTopUI == null) {
			return false;
		}
		return aTopUI.onKeyPress(key);
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		if (!aDisplayed || !aCatchKeyEvents || aTopUI == null) {
			return false;
		}
		aTopUI.onKeyRelease(key);
		return true;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (!aDisplayed || aTopUI == null) {
			return false;
		}
		return aTopUI.click(position);
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		if (!aDisplayed || aTopUI == null) {
			return false;
		}
		return aTopUI.onMouseMove(position);
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		if (!aDisplayed || aTopUI == null) {
			return false;
		}
		return aTopUI.onMouseWheelDown(delta, position);
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		if (!aDisplayed || aTopUI == null) {
			return false;
		}
		return aTopUI.onMouseWheelUp(delta, position);
	}

	/**
	 * Add a UI to the top of the {@link UIControl} stack.
	 * 
	 * @param root
	 *            The {@link UIControl} to push on top of the stack
	 */
	public void pushUI(final UIControl root)
	{
		aTopUI = root;
		if (aTopUI != null) {
			aAllUIs.add(root);
			addNode(aTopUI);
			aDisplayed = true;
			aTopUI.smoothAppear(0.4f);
			final Transform zOffset = aTopUI.getNewTransform();
			aZOffsets.put(aTopUI, zOffset);
			float totalZ = 0;
			for (final UIControl ui : aAllUIs) {
				if (!aTopUI.equals(ui)) {
					totalZ += ui.getMaxZOffset();
				}
			}
			zOffset.translate(0, 0, totalZ);
		}
		else {
			aDisplayed = false;
		}
		setBounds(getBounds());
	}

	@Override
	public void resolutionChanged()
	{
		setBounds(getBounds());
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		for (final UIControl ui : aAllUIs) {
			ui.setBounds(bounds);
		}
	}

	/**
	 * Sets whether this EverUIView should catch or shouldn't catch key events by default.
	 * 
	 * @param catchEvents
	 *            To catch key events or not
	 */
	protected void setCatchKeyEvents(final boolean catchEvents)
	{
		aCatchKeyEvents = catchEvents;
	}

	/**
	 * Hide or show this {@link EverUIView}.
	 * 
	 * @param displayed
	 *            true to show, false to hide.
	 */
	public void setDisplayed(final boolean displayed)
	{
		setDisplayed(displayed, null);
	}

	/**
	 * Hide or show this {@link EverUIView}.
	 * 
	 * @param displayed
	 *            true to show, false to hide.
	 * @param callback
	 *            Optional callback {@link Runnable} that will be run when the animation is complete. Set to null if not needed,
	 *            or use the simpler version of this function.
	 */
	public void setDisplayed(final boolean displayed, final Runnable callback)
	{
		aDisplayed = displayed;
		aDisplayAlpha.setTargetAlpha(aDisplayed ? aDisplayedMaxAlpha : 0).start(callback);
	}

	/**
	 * Set the maximum target alpha of this {@link EverUIView}. Useful for views that should stay semi-transparent, such as the
	 * {@link InGameChatView}.
	 * 
	 * @param maxAlpha
	 *            The maximum target alpha for this {@link EverUIView}.
	 */
	protected void setDisplayMaxAlpha(final double maxAlpha)
	{
		aDisplayedMaxAlpha = maxAlpha;
	}

	/**
	 * Set the duration of the fade in/out animation
	 * 
	 * @param duration
	 *            The duration of the fade in/out animation
	 */
	protected void setFadeDuration(final double duration)
	{
		aDisplayAlpha.setDuration(duration);
	}

	/**
	 * Replace the top-most {@link UIControl} by a new one. Use pushUI() instead if deleting the current {@link UIControl} is
	 * not desired.
	 * 
	 * @param newRoot
	 *            The {@link UIControl} that should replace the current top-most {@link UIControl}.
	 */
	public void switchUI(final UIControl newRoot)
	{
		if (aTopUI != newRoot) {
			deleteUI();
			pushUI(newRoot);
		}
	}
}
