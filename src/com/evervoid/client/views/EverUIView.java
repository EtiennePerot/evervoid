package com.evervoid.client.views;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.UIControl;
import com.jme3.math.Vector2f;

public abstract class EverUIView extends EverView
{
	private final UIControl aRootUI;

	public EverUIView(final UIControl root)
	{
		aRootUI = root;
		addNode(aRootUI);
		resolutionChanged();
	}

	protected void addUI(final UIControl control)
	{
		aRootUI.addUI(control);
	}

	protected void addUI(final UIControl control, final int spring)
	{
		aRootUI.addUI(control, spring);
	}

	protected void delAllChildUIs()
	{
		aRootUI.delAllChildUIs();
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		aRootUI.onKeyPress(key);
		return true;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		aRootUI.onKeyRelease(key);
		return true;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		aRootUI.click(position);
		return true;
	}

	@Override
	public void resolutionChanged()
	{
		setBounds(getBounds());
	}

	@Override
	protected void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aRootUI.setBounds(bounds);
	}
}
