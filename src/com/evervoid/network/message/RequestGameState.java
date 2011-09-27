package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.evervoid.state.EVGameState;

public class RequestGameState extends EVMessage
{
	/**
	 * Constructor; state may be null since this is sent on the client side
	 * 
	 * @param clientState
	 *            The state to synchronize to
	 */
	public RequestGameState(final EVGameState clientState)
	{
		super(new Json().setAttribute("gamehash", clientState == null ? "" : clientState.toJson().getHash()));
	}
}
