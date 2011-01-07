package client;

import com.jme3.math.Vector2f;

public abstract class ClientView extends EverNode
{
	public void onMouseClick(final Vector2f position, final float tpf)
	{
	}

	public void onMouseMove(final String name, final float tpf, final Vector2f position)
	{
	}

	public void onMouseRelease(final Vector2f position, final float tpf)
	{
	}

	public void onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
	}

	public void onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
	}
}
