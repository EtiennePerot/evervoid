package client;

import com.jme3.math.Vector2f;

public abstract class ClientView extends everNode
{
	public abstract void onMouseMove(final String name, final float isPressed, final float tpf, Vector2f position);
}
