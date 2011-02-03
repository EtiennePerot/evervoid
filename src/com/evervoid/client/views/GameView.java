package com.evervoid.client.views;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.Key;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.views.galaxy.GalaxyPerspective;
import com.evervoid.client.views.solar.SolarSystemPerspective;
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

	/**
	 * Switches to a new view in-game. This has no effect if ViewManager isn't in in-game mode. Assumes the selected view type
	 * needs no arguments.
	 * 
	 * @param type
	 *            The type of view to switch to
	 */
	public static void changePerspective(final PerspectiveType type)
	{
		changePerspective(type, null);
	}

	/**
	 * Switches to a new view in-game. This has no effect if ViewManager isn't in in-game mode
	 * 
	 * @param type
	 *            The type of view to switch to
	 * @param arg
	 *            If the specified view type requires an argument
	 */
	public static void changePerspective(final PerspectiveType type, final Object arg)
	{
		sInstance.switchPerspective(type, arg);
	}

	public static void collideWithRay(final Ray ray, final CollisionResults results)
	{
		sInstance.aContentView.collideWith(ray, results);
	}

	private Perspective aActivePerspective = null;
	private final BottomBarView aBottomBar;
	private final Map<EverView, AnimatedAlpha> aContentAlphaAnimations = new HashMap<EverView, AnimatedAlpha>();
	private EverView aContentView = null;
	/**
	 * The galaxy view, always stored as player will often be returning to this
	 */
	private final GalaxyPerspective aGalaxyPerspective;
	private EverView aPanelView = null;
	private Perspective aPreviousPerspective;
	private final Map<SolarSystem, SolarSystemPerspective> aSolarPerspectives = new HashMap<SolarSystem, SolarSystemPerspective>();
	private final EverVoidGameState aState;
	private boolean aSwitchingPerspective = false;
	private final TopBarView aTopBar;

	public GameView(final EverVoidGameState state)
	{
		sInstance = this;
		aState = state;
		aTopBar = new TopBarView();
		addView(aTopBar);
		aBottomBar = new BottomBarView();
		addView(aBottomBar);
		aGalaxyPerspective = new GalaxyPerspective(this, aState.getGalaxy());
		changePerspective(PerspectiveType.SOLAR, aState.getTempSolarSystem());
	}

	private AnimatedAlpha getContentAlphaAnimation(final EverView view)
	{
		if (!aContentAlphaAnimations.containsKey(view)) {
			final AnimatedAlpha transform = view.getNewAlphaAnimation();
			transform.setDuration(0.5f);
			aContentAlphaAnimations.put(view, transform);
		}
		return aContentAlphaAnimations.get(view);
	}

	private final Bounds getDefaultContentBounds()
	{
		// FIXME: Temporary
		// Remember that this is from the bottom left corner
		return new Bounds(0, aBottomBar.getHeight(), EverVoidClient.getWindowDimension().width,
				EverVoidClient.getWindowDimension().height - aBottomBar.getHeight() - ((int) aTopBar.getHeight()));
	}

	private SolarSystemPerspective getSolarSystemPerspective(final SolarSystem ss)
	{
		if (!aSolarPerspectives.containsKey(ss)) {
			aSolarPerspectives.put(ss, new SolarSystemPerspective(this, ss));
		}
		return aSolarPerspectives.get(ss);
	}

	@Override
	public boolean onKeyPress(final Key key, final float tpf)
	{
		if (key.equals(Key.G)) {
			changePerspective(PerspectiveType.GALAXY);
		}
		if (super.onKeyPress(key, tpf)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onKeyPress(key, tpf);
	}

	@Override
	public boolean onKeyRelease(final Key key, final float tpf)
	{
		if (super.onKeyRelease(key, tpf)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onKeyRelease(key, tpf);
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
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		if (super.onMouseMove(position, tpf)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onMouseMove(position, tpf);
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
		if (aSwitchingPerspective || perspective.equals(aActivePerspective)) {
			// Don't do anything
			return;
		}
		aSwitchingPerspective = true;
		if (aContentView != null) {
			final EverView oldContent = aContentView; // Final variable needed to be accessible in Runnable
			getContentAlphaAnimation(oldContent).setTargetAlpha(0).start(new Runnable()
			{
				@Override
				public void run()
				{
					EverVoidClient.delRootNode(oldContent);
				}
			});
		}
		if (aPanelView != null) {
			final EverView oldPanel = aPanelView; // Final variable needed to be accessible in Runnable
			getContentAlphaAnimation(oldPanel).setTargetAlpha(0).start(new Runnable()
			{
				@Override
				public void run()
				{
					EverVoidClient.delRootNode(oldPanel);
				}
			});
		}
		aPreviousPerspective = aActivePerspective;
		aActivePerspective = perspective;
		aContentView = perspective.getContentView();
		aPanelView = perspective.getPanelView();
		if (aContentView != null) {
			EverVoidClient.addRootNode(aContentView.getNodeType(), aContentView);
			aContentView.setBounds(getDefaultContentBounds());
			getContentAlphaAnimation(aContentView).setTargetAlpha(1).start(new Runnable()
			{
				@Override
				public void run()
				{
					aSwitchingPerspective = false;
				}
			});
		}
		if (aPanelView != null) {
			EverVoidClient.addRootNode(aPanelView.getNodeType(), aPanelView);
			getContentAlphaAnimation(aPanelView).setTargetAlpha(1).start();
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
