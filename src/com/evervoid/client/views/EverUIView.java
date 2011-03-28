package com.evervoid.client.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.ui.UIControl;
import com.jme3.math.Vector2f;

public abstract class EverUIView extends EverView
{
	private final List<UIControl> aAllUIs = new ArrayList<UIControl>();
	private boolean aCatchKeyEvents = true;
	private final AnimatedAlpha aDisplayAlpha;
	private boolean aDisplayed = true;
	private double aDisplayedMaxAlpha = 1;
	private UIControl aTopUI = null;
	private final Map<UIControl, Transform> aZOffsets = new HashMap<UIControl, Transform>();

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
	 * Clear this EverUIView from the current UI displayed
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

	public Integer getComputedHeight()
	{
		if (aTopUI == null) {
			return null;
		}
		return aTopUI.getComputedHeight();
	}

	public Integer getComputedWidth()
	{
		if (aTopUI == null) {
			return null;
		}
		return aTopUI.getComputedWidth();
	}

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
		aTopUI.onKeyPress(key);
		return true;
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

	protected void setDisplayDuration(final double duration)
	{
		aDisplayAlpha.setDuration(duration);
	}

	public void setDisplayed(final boolean displayed)
	{
		aDisplayed = displayed;
		aDisplayAlpha.setTargetAlpha(aDisplayed ? aDisplayedMaxAlpha : 0).start();
	}

	protected void setDisplayMaxAlpha(final double maxAlpha)
	{
		aDisplayedMaxAlpha = maxAlpha;
	}

	public void switchUI(final UIControl newRoot)
	{
		deleteUI();
		pushUI(newRoot);
	}
}
