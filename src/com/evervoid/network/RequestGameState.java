package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;

public class RequestGameState extends EverMessage
{
	/**
	 * Constructor; state may be null since this is sent on the client side
	 * 
	 * @param clientState
	 *            The state to synchronize to
	 */
	public RequestGameState(final EVGameState clientState)
	{
		super(new Json().setStringAttribute("gamehash", clientState == null ? "" : clientState.toJson().getHash()));
	}
}
