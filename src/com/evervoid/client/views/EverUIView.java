package com.evervoid.client.views;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.ui.UIControl;
import com.jme3.math.Vector2f;

public abstract class EverUIView extends EverView
{
	private final Map<UIControl, AnimatedAlpha> aAlphaAnimations = new HashMap<UIControl, AnimatedAlpha>();
	private UIControl aRootUI;

	public EverUIView(final UIControl root)
	{
		aRootUI = root;
		aAlphaAnimations.put(aRootUI, aRootUI.getNewAlphaAnimation());
		addNode(aRootUI);
		resolutionChanged();
	}

	protected void addUI(final UIControl control)
	{
		if (aRootUI == null) {
			return;
		}
		aRootUI.addUI(control);
	}

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
		if (aRootUI == null) {
			return;
		}
		final UIControl currentRoot = aRootUI; // Need final variable to access in Runnable
		aAlphaAnimations.get(currentRoot).setTargetAlpha(0).setDuration(0.4).start(new Runnable()
		{
			@Override
			public void run()
			{
				delNode(currentRoot);
			}
		});
	}

	/**
	 * Deletes reference to a certain UI that was once attached to this EverUIView. Used for garbage collection completeness
	 * 
	 * @param ui
	 *            The UI to delete
	 */
	public void delUI(final UIControl ui)
	{
		if (aRootUI != null && aRootUI.equals(ui)) {
			deleteUI();
		}
		aAlphaAnimations.remove(ui);
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (aRootUI == null) {
			return false;
		}
		aRootUI.onKeyPress(key);
		return true;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		if (aRootUI == null) {
			return false;
		}
		aRootUI.onKeyRelease(key);
		return true;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (aRootUI == null) {
			return false;
		}
		aRootUI.click(position);
		return true;
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		if (aRootUI == null) {
			return false;
		}
		aRootUI.onMouseMove(position);
		return true;
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

	public void switchUI(final UIControl newRoot)
	{
		deleteUI();
		aRootUI = newRoot;
		if (aRootUI != null) {
			if (!aAlphaAnimations.containsKey(aRootUI)) {
				aAlphaAnimations.put(aRootUI, aRootUI.getNewAlphaAnimation());
			}
			final AnimatedAlpha alpha = aAlphaAnimations.get(aRootUI);
			alpha.setAlpha(0);
			resolutionChanged();
			alpha.setTargetAlpha(1).setDuration(0.4).start();
			addNode(aRootUI);
		}
	}
}
