package com.evervoid.client;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.client.interfaces.EVGlobalMessageListener;
import com.evervoid.client.views.ErrorMessageView;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.LoadingView;
import com.evervoid.client.views.LogoView;
import com.evervoid.client.views.credits.CreditsView;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.lobby.LobbyView;
import com.evervoid.client.views.mainmenu.MainMenuView;
import com.evervoid.client.views.preferences.PreferencesView;
import com.evervoid.client.views.serverlist.ServerListView;
import com.evervoid.json.Json;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;
import com.evervoid.utils.EVUtils;
import com.jme3.math.Vector2f;

/**
 * Only handles switch between Game view, Main menu view, etc. Does not handle switching between subviews of the Game view.
 */
public class EVViewManager implements EVGlobalMessageListener, EVFrameObserver
{
	/**
	 * All possible everVoid views.
	 */
	public enum ViewType
	{
		/**
		 * The credits view.
		 */
		CREDITS,
		/**
		 * The error view.
		 */
		ERROR,
		/**
		 * The in game view.
		 */
		GAME,
		/**
		 * The loading screen.
		 */
		LOADING,
		/**
		 * The lobby views.
		 */
		LOBBY,
		/**
		 * The startup logo view.
		 */
		LOGO,
		/**
		 * The main menu.
		 */
		MAINMENU,
		/**
		 * The preferences menu.
		 */
		PREFERENCES,
		/**
		 * The discovery server list.
		 */
		SERVERLIST,
		/**
		 * The showroom.
		 */
		SHOWROOM
	}

	/**
	 * The view manager instance.
	 */
	private static EVViewManager sInstance;

	public static void deregisterView(final ViewType type, final Runnable callback)
	{
		getInstance().deregister(type, callback);
	}

	/**
	 * Displays the error to the user, returns to the current view when the user exits the error view.
	 * 
	 * @param errorMessage
	 *            The message to display to the user.
	 */
	public static void displayError(final String errorMessage)
	{
		displayError(errorMessage, null);
	}

	/**
	 * Displays the error to the user, returns to the current view when the user exits the error view and then runs the
	 * callback.
	 * 
	 * @param errorMessage
	 *            the message to display to the user.
	 * @param callback
	 *            the callback to run once the user returns from the error.
	 */
	public static void displayError(final String errorMessage, final Runnable callback)
	{
		schedule(new Runnable()
		{
			@Override
			public void run()
			{
				final ViewType previousView = sInstance.aActiveViewType;
				registerView(ViewType.ERROR, new ErrorMessageView(errorMessage, new Runnable()
				{
					@Override
					public void run()
					{
						EVViewManager.switchTo(previousView);
						EVViewManager.deregisterView(ViewType.ERROR, callback);
					}
				}));
				switchTo(ViewType.ERROR);
			}
		});
	}

	/**
	 * @return The view manager instance, will create one if none exists.
	 */
	protected static EVViewManager getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVViewManager();
		}
		return sInstance;
	}

	public static boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		return getInstance().aActiveView.onKeyPress(key, tpf);
	}

	public static boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		return getInstance().aActiveView.onKeyRelease(key, tpf);
	}

	public static boolean onLeftClick(final Vector2f position, final float tpf)
	{
		return getInstance().aActiveView.onLeftClick(position, tpf);
	}

	public static boolean onLeftRelease(final Vector2f position, final float tpf)
	{
		return getInstance().aActiveView.onLeftRelease(position, tpf);
	}

	public static boolean onMouseMove(final Vector2f position, final float tpf)
	{
		return getInstance().aActiveView.onMouseMove(position, tpf);
	}

	public static boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		return getInstance().aActiveView.onMouseWheelDown(delta, tpf, position);
	}

	public static boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		return getInstance().aActiveView.onMouseWheelUp(delta, tpf, position);
	}

	public static boolean onRightClick(final Vector2f position, final float tpf)
	{
		return getInstance().aActiveView.onRightClick(position, tpf);
	}

	public static boolean onRightRelease(final Vector2f position, final float tpf)
	{
		return getInstance().aActiveView.onRightRelease(position, tpf);
	}

	/**
	 * Prepares all the static views for loading.
	 */
	public static void prepareViews()
	{
		getInstance().initViews();
	}

	public static void registerView(final ViewType type, final EverView view)
	{
		getInstance().register(type, view);
	}

	/**
	 * Schedule a UI job to be executed on the next frame update
	 * 
	 * @param job
	 */
	public static void schedule(final Runnable job)
	{
		getInstance().aUIJobs.add(job);
	}

	/**
	 * Switches the current view to the given type.
	 * 
	 * @param type
	 *            The view to switch to.
	 */
	public static void switchTo(final ViewType type)
	{
		schedule(new Runnable()
		{
			@Override
			public void run()
			{
				getInstance().switchView(type);
			}
		});
	}

	/**
	 * The view currently showing.
	 */
	private EverView aActiveView = null;
	/**
	 * The type of the view currently showing.
	 */
	private ViewType aActiveViewType = null;
	/**
	 * UIJobs to perform.
	 */
	private final BlockingQueue<Runnable> aUIJobs = new LinkedBlockingQueue<Runnable>();
	/**
	 * A map of view types to their initialized EverViews. Saves us from recreating views every time and allows views to retain
	 * state.
	 */
	private final Map<ViewType, EverView> aViewMap = new EnumMap<ViewType, EverView>(ViewType.class);

	/**
	 * Hidden constructor.
	 */
	private EVViewManager()
	{
		sInstance = this;
		EVFrameManager.register(this);
		register(ViewType.LOGO, new LogoView());
		switchView(ViewType.LOGO);
	}

	public void deregister(final ViewType type, final Runnable callback)
	{
		hideView(aViewMap.get(type), callback);
		aViewMap.remove(type);
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		while (!aUIJobs.isEmpty()) {
			EVUtils.runCallback(aUIJobs.poll());
		}
	}

	/**
	 * Hides a view and allows for a callback when the view has finished fading out.
	 * 
	 * @param view
	 *            The view to hide.
	 * @param callback
	 *            The callback to perform when the view has hidden.
	 */
	private void hideView(final EverView view, final Runnable callback)
	{
		if (view == null) {
			EVUtils.runCallback(callback);
			return;
		}
		view.smoothDisappear(0.5f, new Runnable()
		{
			@Override
			public void run()
			{
				view.onDefocus();
				EVUtils.runCallback(callback);
			}
		});
	}

	/**
	 * Create all static views and populte them.
	 */
	private void initViews()
	{
		final MainMenuView homeView = new MainMenuView();
		register(ViewType.MAINMENU, homeView);
		final LoadingView loadingView = new LoadingView();
		register(ViewType.LOADING, loadingView);
		final ServerListView serverListView = new ServerListView();
		register(ViewType.SERVERLIST, serverListView);
		final PreferencesView preferences = new PreferencesView();
		register(ViewType.PREFERENCES, preferences);
		register(ViewType.CREDITS, new CreditsView());
	}

	@Override
	public void receivedChat(final String player, final Color playerColor, final String message)
	{
		schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (aActiveViewType.equals(ViewType.LOBBY)) {
					((LobbyView) aActiveView).receivedChat(player, playerColor, message);
				}
				else if (aActiveViewType.equals(ViewType.GAME)) {
					((GameView) aActiveView).receivedChat(player, playerColor, message);
				}
			}
		});
	}

	@Override
	public void receivedGameState(final EVGameState gameState, final String playerName)
	{
		if (aActiveViewType.equals(ViewType.GAME)) {
			return; // Already in game, don't switch again
		}
		schedule(new Runnable()
		{
			@Override
			public void run()
			{
				// Destroy lobby view
				deregister(ViewType.LOBBY, null);
				// Destroy game view
				deregister(ViewType.GAME, null);
				final GameView gameView = new GameView(gameState, gameState.getPlayerByName(playerName));
				register(ViewType.GAME, gameView);
				switchView(ViewType.GAME);
			}
		});
	}

	@Override
	public void receivedQuit(final Json quitMessage)
	{
		// TODO: warn user that someone has quit
	}

	public void register(final ViewType type, final EverView view)
	{
		aViewMap.put(type, view);
	}

	/**
	 * Switches the current view to the given type.
	 * 
	 * @param type
	 *            The view to switch to.
	 */
	private void switchView(final ViewType type)
	{
		if (type.equals(aActiveViewType)) {
			return;
		}
		aActiveViewType = type;
		hideView(aActiveView, null);
		final EverView newView = aViewMap.get(type);
		aActiveView = newView;
		EverVoidClient.addRootNode(newView.getNodeType(), newView);
		aActiveView.onFocus();
		aActiveView.smoothAppear(0.5f);
	}
}
