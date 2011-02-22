package com.evervoid.server;

import com.evervoid.json.Json;
import com.evervoid.network.GameStateMessage;
import com.evervoid.network.TurnMessage;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Turn;
import com.jme3.network.connection.Client;

public class EVGameEngine implements EVServerMessageObserver
{
	private static EVGameEngine sInstance;

	protected static EVGameEngine getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVGameEngine();
		}
		return sInstance;
	}

	protected EVServerEngine aServer;
	private EVGameState aState;

	private EVGameEngine()
	{
		sInstance = this;
		aServer = EVServerEngine.getInstance();
		EVServerEngine.registerListener(this);
	}

	private void calculateTurn(final Turn turn)
	{
		// TODO - magic
		aServer.sendAll(new TurnMessage(turn));
	}

	@Override
	public void messageReceived(final String type, final Client client, final Json content)
	{
		if (type.equals("handshake")) {
			// FIXME: Server should not handle handshake messages at all
			aServer.send(client, new GameStateMessage(aState));
		}
		else if (type.equals("turn")) {
			calculateTurn(new Turn(content, aState));
		}
		else if (type.equals("startgame")) {
			aServer.send(client, new GameStateMessage(aState));
		}
	}

	protected void setState(final EVGameState state)
	{
		aState = state;
	}
}
