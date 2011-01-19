package com.evervoid.client;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.views.galaxy.GalaxyView;
import com.evervoid.client.views.solar.SolarSystemView;
import com.evervoid.state.galaxy.Point3D;
import com.evervoid.state.solar.SolarSystem;
import com.jme3.math.Vector2f;

public class ClientInput
{
	public void onAction(final ClientView view, final String name, final boolean isPressed, final float tpf,
			final Vector2f position)
	{
		if (name.equals("Mouse click")) {
			// Forward mouse clicks to game view
			if (isPressed) {
				view.onMouseClick(position, tpf);
			}
			else {
				view.onMouseRelease(position, tpf);
			}
		}
		else if (name.equals("Click g")) {
			final Map<SolarSystem, Point3D> galaxy = new HashMap<SolarSystem, Point3D>();
			EverVoidClient.changeView(new GalaxyView(galaxy));
		}
		else if (name.equals("Click s")) {
			EverVoidClient.changeView(new SolarSystemView(new SolarSystem(48)));
		}
	}

	public void onAnalog(final ClientView view, final String name, final float delta, final float tpf, final Vector2f position)
	{
		if (name.equals("Mouse move")) {
			// Forward mouse movement to game view
			view.onMouseMove(name, tpf, position);
		}
		else if (name.equals("Mouse wheel up")) {
			view.onMouseWheelUp(delta, tpf, position);
		}
		else if (name.equals("Mouse wheel down")) {
			view.onMouseWheelDown(delta, tpf, position);
		}
	}
}
