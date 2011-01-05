package client;

import com.jme3.math.Vector2f;

public class ClientInput
{
	public void onAction(final ClientView view, final String name, final boolean isPressed, final float tpf,
			final Vector2f position)
	{
		if (name.equals("Mouse click"))
		{
			// Forward mouse clicks to game view
			if (isPressed)
			{
				view.onMouseClick(position, tpf);
			}
			else
			{
				view.onMouseRelease(position, tpf);
			}
		}
	}

	public void onAnalog(final ClientView view, final String name, final float delta, final float tpf,
			final Vector2f position)
	{
		// Forward mouse movement to game view
		view.onMouseMove(name, tpf, position);
	}
}
