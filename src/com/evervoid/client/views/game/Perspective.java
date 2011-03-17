package com.evervoid.client.views.game;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.interfaces.EVInputListener;
import com.evervoid.client.views.EverView;
import com.jme3.math.Vector2f;

public abstract class Perspective implements EVInputListener
{
	private EverView aContentNode;
	private final GameView aGameView;
	private MiniView aMiniNode;
	private EverView aPanelNode;

	public Perspective(final GameView gameview)
	{
		aGameView = gameview;
	}

	public EverView getContentView()
	{
		return aContentNode;
	}

	protected GameView getGameView()
	{
		return aGameView;
	}

	public MiniView getMiniView()
	{
		return aMiniNode;
	}

	public EverView getPanelView()
	{
		return aPanelNode;
	}

	/**
	 * Called when a new turn has been received.
	 */
	public void newTurn()
	{
		// Overridden by subclasses
	}

	/**
	 * Called when the user switches away from this perspective
	 */
	public void onDefocus()
	{
		// Overridden by subclasses
	}

	/**
	 * Called when the user switches to this perspective
	 */
	public void onFocus()
	{
		// Overridden by subclasses
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (aContentNode != null && aContentNode.onKeyPress(key, tpf)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onKeyPress(key, tpf)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		if (aContentNode != null && aContentNode.onKeyRelease(key, tpf)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onKeyRelease(key, tpf)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (aContentNode != null && aContentNode.onLeftClick(position, tpf)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onLeftClick(position, tpf)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onLeftRelease(final Vector2f position, final float tpf)
	{
		if (aContentNode != null && aContentNode.onLeftRelease(position, tpf)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onLeftRelease(position, tpf)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		if (aContentNode != null && aContentNode.onMouseMove(position, tpf)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onMouseMove(position, tpf)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		if (aContentNode != null && aContentNode.onMouseWheelDown(delta, tpf, position)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onMouseWheelDown(delta, tpf, position)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		if (aContentNode != null && aContentNode.onMouseWheelUp(delta, tpf, position)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onMouseWheelUp(delta, tpf, position)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onRightClick(final Vector2f position, final float tpf)
	{
		if (aContentNode != null && aContentNode.onRightClick(position, tpf)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onRightClick(position, tpf)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onRightRelease(final Vector2f position, final float tpf)
	{
		if (aContentNode != null && aContentNode.onRightRelease(position, tpf)) {
			return true;
		}
		if (aPanelNode != null && aPanelNode.onRightRelease(position, tpf)) {
			return true;
		}
		return false;
	}

	protected void setComponents(final EverView content, final EverView panel, final MiniView mini)
	{
		setContent(content);
		setPanel(panel);
		setMini(mini);
	}

	protected void setContent(final EverView content)
	{
		aContentNode = content;
	}

	protected void setMini(final MiniView mini)
	{
		aMiniNode = mini;
	}

	protected void setPanel(final EverView panel)
	{
		aPanelNode = panel;
	}
}
