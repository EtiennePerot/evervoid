package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient.NodeType;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.interfaces.EVInputListener;
import com.jme3.math.Vector2f;

/**
 * An EverView is a large interface component displayed to the user. It handles displaying, bounding, and input. The EverView
 * class itself is meant to be subclassed by all actual views.
 */
public abstract class EverView extends EverNode implements EVInputListener
{
	/**
	 * The {@link Bounds} of the view; usually screen-wide, but doesn't have to be.
	 */
	private Bounds aBounds;

	/**
	 * Constructor; default constructor uses screen-wise bounds.
	 */
	protected EverView()
	{
		this(Bounds.getWholeScreenBounds());
	}

	/**
	 * Constructor
	 * 
	 * @param pBound
	 *            The {@link Bounds} of the {@link EverView}
	 */
	protected EverView(final Bounds pBound)
	{
		aBounds = pBound;
	}

	/**
	 * @return The {@link Bounds} of this {@link EverView}
	 */
	public Bounds getBounds()
	{
		return aBounds;
	}

	/**
	 * @return The height of the {@link Bounds} of this {@link EverView}
	 */
	public int getBoundsHeight()
	{
		return aBounds.height;
	}

	/**
	 * @return The width of the {@link Bounds} of this {@link EverView}
	 */
	public int getBoundsWidth()
	{
		return aBounds.width;
	}

	/**
	 * @return The {@link NodeType} of the EverView; EverViews are meant to be user interfaces, so this should return
	 *         {@link NodeType}.TWODIMENSION.
	 */
	public NodeType getNodeType()
	{
		return NodeType.TWODIMENSION;
	}

	/**
	 * Called when the {@link EverView} is displayed on screen
	 */
	public void onDefocus()
	{
	}

	/**
	 * Called when the {@link EverView} is hidden from the screen
	 */
	public void onFocus()
	{
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onLeftRelease(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		return false;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		return false;
	}

	@Override
	public boolean onRightClick(final Vector2f position, final float tpf)
	{
		return false;
	}

	@Override
	public boolean onRightRelease(final Vector2f position, final float tpf)
	{
		return false;
	}

	/**
	 * Resize this {@link EverView} to new {@link Bounds}
	 * 
	 * @param pBounds
	 *            The new {@link Bounds} to use.
	 */
	public void setBounds(final Bounds pBounds)
	{
		aBounds = pBounds.clone();
	}
}
