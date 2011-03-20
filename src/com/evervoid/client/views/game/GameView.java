package com.evervoid.client.views.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.interfaces.EVGameMessageListener;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.ComposedView;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.galaxy.GalaxyPerspective;
import com.evervoid.client.views.solar.SolarPerspective;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.Turn;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.player.Player;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;

public class GameView extends ComposedView implements EVGameMessageListener
{
	public enum PerspectiveType
	{
		GALAXY, PLANET, SOLAR;
	}

	private static GameView sInstance = null;

	public static void addAction(final Action action)
	{
		sInstance.aCurrentLocalTurn.addAction(action);
	}

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

	public static void commitTurn()
	{
		EVClientEngine.sendTurn(sInstance.aCurrentLocalTurn);
		sInstance.aCurrentLocalTurn = new Turn();
		// TODO: "Freeze" UI until we get turn back from server
	}

	public static void delAction(final Action action)
	{
		sInstance.aCurrentLocalTurn.delAction(action);
	}

	public static void deregisterTurnListener(final TurnListener listener)
	{
		sInstance.aTurnListeners.remove(listener);
	}

	public static EVGameState getGameState()
	{
		return sInstance.aGameState;
	}

	public static Player getPlayer()
	{
		return sInstance.aLocalPlayer;
	}

	public static void registerTurnListener(final TurnListener listener)
	{
		sInstance.aTurnListeners.add(listener);
	}

	public static void setGameState(final EVGameState state)
	{
		sInstance.aGameState = state;
	}

	private Perspective aActivePerspective = null;
	private final BottomBarView aBottomBar;
	private final BottomBarRightView aBottomBarRight;
	private final InGameChatView aChatView;
	private final Map<EverView, AnimatedAlpha> aContentAlphaAnimations = new HashMap<EverView, AnimatedAlpha>();
	private EverView aContentView = null;
	private Turn aCurrentLocalTurn = new Turn();
	/**
	 * The galaxy view, always stored as player will often be returning to this
	 */
	private final GalaxyPerspective aGalaxyPerspective;
	private EVGameState aGameState;
	private final Player aLocalPlayer;
	private MiniView aMiniView = null;
	private EverView aPanelView = null;
	private Bounds aPerspectiveBounds;
	private Perspective aPreviousPerspective;
	private final Map<SolarSystem, SolarPerspective> aSolarPerspectives = new HashMap<SolarSystem, SolarPerspective>();
	private boolean aSwitchingPerspective = false;
	private final TopBarView aTopBar;
	private final Set<TurnListener> aTurnListeners = new HashSet<TurnListener>();

	public GameView(final EVGameState state, final Player player)
	{
		sInstance = this;
		aGameState = state;
		aLocalPlayer = player;
		aTopBar = new TopBarView();
		addView(aTopBar);
		aBottomBar = new BottomBarView();
		addView(aBottomBar);
		aBottomBarRight = new BottomBarRightView();
		aBottomBarRight.getNewTransform().translate(0, 0, aBottomBar.getVisibleZ());
		addView(aBottomBarRight);
		aChatView = new InGameChatView();
		addView(aChatView);
		aPerspectiveBounds = new Bounds(0, aTopBar.getHeight(), EverVoidClient.getWindowDimension().width,
				EverVoidClient.getWindowDimension().height - aBottomBar.getHeight() - aTopBar.getHeight());
		aGalaxyPerspective = new GalaxyPerspective(this, aGameState.getGalaxy(), aPerspectiveBounds);
		primePerspective(aGalaxyPerspective);
		for (final SolarSystem ss : state.getSolarSystems()) {
			final SolarPerspective perspective = new SolarPerspective(this, ss);
			aSolarPerspectives.put(ss, perspective);
			primePerspective(perspective);
		}
		aActivePerspective = aGalaxyPerspective;
		changePerspective(PerspectiveType.SOLAR, aGameState.getHomeSolarSystem(aLocalPlayer));
		EVClientEngine.registerGameListener(this);
		resolutionChanged();
	}

	public void backPerspective()
	{
		switchPerspective1(aPreviousPerspective);
	}

	private final Bounds getDefaultContentBounds()
	{
		// Remember that this is from the bottom left corner
		return new Bounds(0, aBottomBar.getHeight(), EverVoidClient.getWindowDimension().width,
				EverVoidClient.getWindowDimension().height - aBottomBar.getHeight() - ((int) aTopBar.getHeight()));
	}

	private SolarPerspective getSolarSystemPerspective(final SolarSystem ss)
	{
		return aSolarPerspectives.get(ss);
	}

	/**
	 * Return the AnimatedAlpha object associated to an EverView in the game
	 * 
	 * @param view
	 *            The view to get the AnimatedAlpha for
	 * @return The corresponding AnimatedAlpha
	 */
	private AnimatedAlpha getSubviewAlphaAnimation(final EverView view)
	{
		if (!aContentAlphaAnimations.containsKey(view)) {
			final AnimatedAlpha transform = view.getNewAlphaAnimation();
			transform.setDuration(0.5f);
			aContentAlphaAnimations.put(view, transform);
		}
		return aContentAlphaAnimations.get(view);
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (super.onKeyPress(key, tpf)) {
			return true;
		}
		if (key.equals(KeyboardKey.G)) {
			changePerspective(PerspectiveType.GALAXY);
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onKeyPress(key, tpf);
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
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
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (super.onLeftClick(position, tpf)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		if (aPreviousPerspective != null && aMiniView != null && aMiniView.onLeftClick(position, tpf)) {
			return true;
		}
		return aActivePerspective.onLeftClick(position, tpf);
	}

	@Override
	public boolean onLeftRelease(final Vector2f position, final float tpf)
	{
		if (super.onLeftRelease(position, tpf)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onLeftRelease(position, tpf);
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

	@Override
	public boolean onRightClick(final Vector2f position, final float tpf)
	{
		if (super.onRightClick(position, tpf)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onRightClick(position, tpf);
	}

	@Override
	public boolean onRightRelease(final Vector2f position, final float tpf)
	{
		if (super.onRightRelease(position, tpf)) {
			return true;
		}
		if (aActivePerspective == null) {
			return false;
		}
		return aActivePerspective.onRightRelease(position, tpf);
	}

	/**
	 * Attach and detach a perspective in order to keep it fresh in video memory
	 * 
	 * @param perspective
	 *            The perspective to attach/detach
	 */
	private void primePerspective(final Perspective perspective)
	{
		final EverView content = perspective.getContentView();
		final EverView panel = perspective.getPanelView();
		final EverView mini = perspective.getMiniView();
		if (content != null) {
			EverVoidClient.addRootNode(content.getNodeType(), content);
		}
		if (panel != null) {
			EverVoidClient.addRootNode(panel.getNodeType(), panel);
		}
		if (mini != null) {
			EverVoidClient.addRootNode(mini.getNodeType(), mini);
		}
		perspective.onFocus();
		if (content != null) {
			EverVoidClient.delRootNode(content);
		}
		if (panel != null) {
			EverVoidClient.delRootNode(panel);
		}
		if (mini != null) {
			EverVoidClient.delRootNode(mini);
		}
		perspective.onDefocus();
	}

	public void receivedChat(final String player, final Color playerColor, final String message)
	{
		aChatView.receivedChat(player, playerColor, message);
	}

	@Override
	public void receivedTurn(final Turn turn)
	{
		aGameState.commitTurn(turn);
		aGalaxyPerspective.newTurn();
		for (final SolarPerspective solar : aSolarPerspectives.values()) {
			solar.newTurn();
		}
	}

	@Override
	public void resolutionChanged()
	{
		super.resolutionChanged();
		final Dimension screen = EverVoidClient.getWindowDimension();
		aPerspectiveBounds = new Bounds(0, aTopBar.getHeight(), screen.width, screen.height - aBottomBar.getHeight()
				- aTopBar.getHeight());
		aBottomBarRight.setBounds(aBottomBar.getRightBounds());
		// TODO: Reset bounds on active perspective
		aChatView.setBounds(new Bounds(screen.width - InGameChatView.sChatDimension.width, screen.height - aTopBar.getHeight()
				- InGameChatView.sChatDimension.height, InGameChatView.sChatDimension.width,
				InGameChatView.sChatDimension.height));
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
				switchPerspective1(aGalaxyPerspective);
				break;
			case SOLAR:
				if (arg == null) {
					arg = aGameState.getHomeSolarSystem(aLocalPlayer);
				}
				switchPerspective1(getSolarSystemPerspective((SolarSystem) arg));
				break;
		}
	}

	/**
	 * First step of perspective switching: Make current perspective go away
	 * 
	 * @param perspective
	 *            The perspective to switch to
	 */
	private void switchPerspective1(final Perspective perspective)
	{
		if (aSwitchingPerspective || perspective.equals(aActivePerspective)) {
			// Don't do anything
			return;
		}
		aSwitchingPerspective = true;
		if (aMiniView != null) {
			final EverView oldMini = aMiniView; // Final variable needed to be accessible in Runnable
			getSubviewAlphaAnimation(oldMini).setTargetAlpha(0).start(new Runnable()
			{
				@Override
				public void run()
				{
					EverVoidClient.delRootNode(oldMini);
				}
			});
		}
		if (aPanelView != null) {
			final EverView oldPanel = aPanelView; // Final variable needed to be accessible in Runnable
			getSubviewAlphaAnimation(oldPanel).setTargetAlpha(0).start(new Runnable()
			{
				@Override
				public void run()
				{
					EverVoidClient.delRootNode(oldPanel);
				}
			});
		}
		if (aContentView != null) {
			final EverView oldContent = aContentView; // Final variable needed to be accessible in Runnable
			getSubviewAlphaAnimation(oldContent).setTargetAlpha(0).start(new Runnable()
			{
				@Override
				public void run()
				{
					switchPerspective2(perspective);
				}
			});
		}
		else {
			// If there was no previous perspective, skip to step 2 right away
			switchPerspective2(perspective);
		}
	}

	/**
	 * Second step of perspective switching: Make new perspective appear
	 * 
	 * @param perspective
	 *            The perspective to switch to
	 */
	private void switchPerspective2(final Perspective perspective)
	{
		if (aActivePerspective != null) {
			aActivePerspective.onDefocus();
			if (aContentView != null) {
				EverVoidClient.delRootNode(aContentView);
			}
		}
		aPreviousPerspective = aActivePerspective;
		if (aPreviousPerspective == null) {
			aMiniView = null;
		}
		else {
			aMiniView = aPreviousPerspective.getMiniView();
		}
		aActivePerspective = perspective;
		aContentView = perspective.getContentView();
		aPanelView = perspective.getPanelView();
		aActivePerspective.onFocus();
		if (aContentView != null) {
			EverVoidClient.addRootNode(aContentView.getNodeType(), aContentView);
			aContentView.setBounds(getDefaultContentBounds());
			final AnimatedAlpha panelOpacity = getSubviewAlphaAnimation(aContentView);
			panelOpacity.setAlpha(0);
			panelOpacity.setTargetAlpha(1).start(new Runnable()
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
			aPanelView.setBounds(aBottomBar.getMiddleBounds());
			final AnimatedAlpha panelOpacity = getSubviewAlphaAnimation(aPanelView);
			panelOpacity.setAlpha(0).translate(0, 0, aBottomBar.getVisibleZ());
			panelOpacity.setTargetAlpha(1).start();
		}
		if (aMiniView != null) {
			EverVoidClient.addRootNode(aMiniView.getNodeType(), aMiniView);
			aMiniView.setBounds(aBottomBar.getLeftBounds());
			final AnimatedAlpha panelOpacity = getSubviewAlphaAnimation(aMiniView);
			panelOpacity.setAlpha(0).translate(0, 0, aBottomBar.getVisibleZ());
			panelOpacity.setTargetAlpha(1).start();
		}
	}
}
