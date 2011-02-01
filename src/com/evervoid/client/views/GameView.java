package com.evervoid.client.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.views.galaxy.GalaxyPerspective;
import com.evervoid.client.views.solar.SolarSystemPerspective;
import com.evervoid.state.Dimension;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.SolarSystem;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;

public class GameView extends ComposedView
{
	public enum PerspectiveType
	{
		GALAXY, PLANET, SOLAR;
	}

	private static GameView sInstance = null;

	public static void changePerspective(final PerspectiveType type)
	{
		changePerspective(type, null);
	}

	public static void changePerspective(final PerspectiveType type, final Object arg)
	{
		sInstance.switchPerspective(type, arg);
	}

	public static void collideWithRay(final Ray ray, final CollisionResults results)
	{
		sInstance.aContentView.collideWith(ray, results);
	}

	private Perspective aActivePerspective = null;
	private EverView aContentView = null;
	/**
	 * The galaxy view, always stored as player will often be returning to this
	 */
	private final GalaxyPerspective aGalaxyPerspective;
	private EverView aPanelView = null;
	/**
	 * Stack of previously-active perspectives. The top one is the one displayed in the bottom-left corner of the screen.
	 */
	private final Stack<Perspective> aPerspectives = new Stack<Perspective>();
	private final Map<SolarSystem, SolarSystemPerspective> aSolarPerspectives = new HashMap<SolarSystem, SolarSystemPerspective>();
	private final EverVoidGameState aState;
	private final TopBarView aTopBar;

	public GameView(final EverVoidGameState state)
	{
		sInstance = this;
		aState = state;
		aTopBar = new TopBarView(new Dimension(getBoundsHeight() / 10, getBoundsWidth()));
		addView(aTopBar);
		aGalaxyPerspective = new GalaxyPerspective(this, aState.getGalaxy());
		changePerspective(PerspectiveType.SOLAR, aState.getTempSolarSystem());
	}

	private final Bounds getDefaultContentBounds()
	{
		// FIXME: Temporary
		// Remember that this is from the bottom left corner
		return new Bounds(0, 200, EverVoidClient.getWindowDimension().width, EverVoidClient.getWindowDimension().height - 200
				- ((int) aTopBar.getHeight()));
	}

	private SolarSystemPerspective getSolarSystemPerspective(final SolarSystem ss)
	{
		if (!aSolarPerspectives.containsKey(ss)) {
			aSolarPerspectives.put(ss, new SolarSystemPerspective(this, ss));
		}
		return aSolarPerspectives.get(ss);
	}

	@Override
	public boolean onMouseClick(final Vector2f position, final float tpf)
	{
		if (super.onMouseClick(position, tpf)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onMouseClick(position, tpf);
	}

	@Override
	public boolean onMouseMove(final float tpf, final Vector2f position)
	{
		if (super.onMouseMove(tpf, position)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onMouseMove(tpf, position);
	}

	@Override
	public boolean onMouseRelease(final Vector2f position, final float tpf)
	{
		if (super.onMouseRelease(position, tpf)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onMouseRelease(position, tpf);
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		if (super.onMouseWheelDown(delta, tpf, position)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onMouseWheelDown(delta, tpf, position);
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		if (super.onMouseWheelUp(delta, tpf, position)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onMouseWheelUp(delta, tpf, position);
	}

	private void switchPerspective(final Perspective perspective)
	{
		if (aContentView != null) {
			EverVoidClient.delRootNode(aContentView);
		}
		if (aPanelView != null) {
			EverVoidClient.delRootNode(aPanelView);
		}
		aActivePerspective = perspective;
		aContentView = perspective.getContentView();
		aPanelView = perspective.getContentView();
		if (aContentView != null) {
			EverVoidClient.addRootNode(aContentView.getNodeType(), aContentView);
			aContentView.setBounds(getDefaultContentBounds());
		}
		if (aPanelView != null) {
			EverVoidClient.addRootNode(aPanelView.getNodeType(), aPanelView);
		}
	}

	/**
	 * Switches to a new view in-game. This has no effect if ViewManager isn't in in-game mode
	 * 
	 * @param type
	 *            The type of view to switch to
	 * @param arg
	 *            If the specified view type requires an argument
	 */
	private void switchPerspective(final PerspectiveType type, Object arg)
	{
		switch (type) {
			case GALAXY:
				switchPerspective(aGalaxyPerspective);
				break;
			case SOLAR:
				if (arg == null) {// FIXME: hax
					arg = aState.getTempSolarSystem();
				}
				switchPerspective(getSolarSystemPerspective((SolarSystem) arg));
				break;
		}
	}
}
