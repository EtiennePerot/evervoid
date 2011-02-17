package com.evervoid.client;

import java.util.EnumMap;
import java.util.Map;

import com.evervoid.client.interfaces.EVGlobalMessageListener;
import com.evervoid.client.interfaces.EVInputListener;
import com.evervoid.client.views.EverView;
import com.evervoid.json.Json;
import com.jme3.math.Vector2f;

/**
 * Only handles switch between Game view, Main menu view, etc. Does not handle switching between subviews of the Game view.
 */
public class EVViewManager implements EVGlobalMessageListener
{
	public enum ViewType
	{
		GAME, MAINMENU
	}

	private static EVViewManager sInstance;

	private static EVViewManager getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVViewManager();
		}
		return sInstance;
	}

	public static void onKeyPress(final KeyboardKey key, final float tpf)
	{
		getInstance().aInputRelay.onKeyPress(key, tpf);
	}

	public static void onKeyRelease(final KeyboardKey key, final float tpf)
	{
		getInstance().aInputRelay.onKeyRelease(key, tpf);
	}

	public static void onMouseClick(final Vector2f position, final float tpf)
	{
		getInstance().aInputRelay.onMouseClick(position, tpf);
	}

	public static void onMouseMove(final Vector2f position, final float tpf)
	{
		getInstance().aInputRelay.onMouseMove(position, tpf);
	}

	public static void onMouseRelease(final Vector2f position, final float tpf)
	{
		getInstance().aInputRelay.onMouseRelease(position, tpf);
	}

	public static void onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		getInstance().aInputRelay.onMouseWheelDown(delta, tpf, position);
	}

	public static void onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		getInstance().aInputRelay.onMouseWheelUp(delta, tpf, position);
	}

	public static void registerView(final ViewType type, final EverView view)
	{
		getInstance().aViewMap.put(type, view);
		getInstance().aInputRelay = view;
		EverVoidClient.addRootNode(view.getNodeType(), view);
	}

	public static void switchTo(final ViewType type)
	{
		for (final ViewType t : ViewType.values()) {
			if (getInstance().aViewMap.containsKey(t)) {
				EverVoidClient.delRootNode(getInstance().aViewMap.get(t));
			}
		}
		final EverView newView = getInstance().aViewMap.get(type);
		getInstance().aInputRelay = newView;
		EverVoidClient.addRootNode(newView.getNodeType(), newView);
	}

	private EVInputListener aInputRelay;
	private final Map<ViewType, EverView> aViewMap;

	private EVViewManager()
	{
		aInputRelay = null;
		aViewMap = new EnumMap<ViewType, EverView>(ViewType.class);
	}

	@Override
	public void receivedChat(final Json chatMessage)
	{
		// TODO pass the pong to the active view
	}

	@Override
	public void receivedGameState(final Json gameState)
	{
		// TODO - decide who deals with this message.
	}

	@Override
	public void receivedPong(final Json packet)
	{
		// TODO - reafirm we are connected
	}

	@Override
	public void receivedQuit(final Json quitMessage)
	{
		// TODO - warn user that someone has quit≈
	}
}
