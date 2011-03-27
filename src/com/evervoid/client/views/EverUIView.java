package com.evervoid.client.views;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.ui.UIControl;
import com.jme3.math.Vector2f;

public abstract class EverUIView extends EverView
{
	private boolean aCatchKeyEvents = true;
	private final AnimatedAlpha aDisplayAlpha;
	private boolean aDisplayed = true;
	private double aDisplayedMaxAlpha = 1;
	private UIControl aRootUI;

	public EverUIView(final UIControl root)
	{
		aDisplayAlpha = getNewAlphaAnimation();
		aDisplayAlpha.setDuration(0); // Animation is instant by deault; can be changed using setAppearDuration
		aRootUI = root;
		addNode(aRootUI);
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
		if (aRootUI == null) {
			return;
		}
		aRootUI.addUI(control);
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
		if (aRootUI == null) {
			return;
		}
		aRootUI.addUI(control, spring);
	}

	/**
	 * Clear this EverUIView from the current UI displayed
	 */
	protected void deleteUI()
	{
		aDisplayed = false;
		if (aRootUI == null) {
			return;
		}
		final UIControl currentRoot = aRootUI; // Need final variable to access in Runnable
		currentRoot.smoothDisappear(0.4f);
	}

	public Integer getComputedHeight()
	{
		if (aRootUI == null) {
			return null;
		}
		return aRootUI.getComputedHeight();
	}

	public Integer getComputedWidth()
	{
		if (aRootUI == null) {
			return null;
		}
		return aRootUI.getComputedWidth();
	}

	protected boolean isDisplayed()
	{
		return aDisplayed;
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (!aDisplayed || !aCatchKeyEvents || aRootUI == null) {
			return false;
		}
		aRootUI.onKeyPress(key);
		return true;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		if (!aDisplayed || !aCatchKeyEvents || aRootUI == null) {
			return false;
		}
		aRootUI.onKeyRelease(key);
		return true;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (!aDisplayed || aRootUI == null) {
			return false;
		}
		return aRootUI.click(position);
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		if (!aDisplayed || aRootUI == null) {
			return false;
		}
		return aRootUI.onMouseMove(position);
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		if (!aDisplayed || aRootUI == null) {
			return false;
		}
		return aRootUI.onMouseWheelDown(delta, position);
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		if (!aDisplayed || aRootUI == null) {
			return false;
		}
		return aRootUI.onMouseWheelUp(delta, position);
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
		if (aRootUI != null) {
			aRootUI.setBounds(bounds);
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
		aRootUI = newRoot;
		if (aRootUI != null) {
			addNode(aRootUI);
			aDisplayed = true;
			aRootUI.smoothAppear(0.4f);
		}
		setBounds(getBounds());
	}
}
