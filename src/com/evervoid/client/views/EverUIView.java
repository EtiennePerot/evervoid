package com.evervoid.client.views;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.UIControl;
import com.jme3.math.Vector2f;

public abstract class EverUIView extends EverView
{
	private UIControl aRootUI;

	public EverUIView(final UIControl root)
	{
		aRootUI = root;
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
		currentRoot.smoothDisappear(0.4);
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
			addNode(aRootUI);
			aRootUI.smoothAppear(0.4);
		}
		setBounds(getBounds());
	}
}
