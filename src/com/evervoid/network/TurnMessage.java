package com.evervoid.network;

import com.evervoid.state.action.Turn;

/**
 * A Turn message; holds a list of Actions
 */
public class TurnMessage extends EverMessage
{
	/**
	 * Constructor; Takes a Turn instance and prepares it for sending
	 * 
	 * @param turn
	 */
	public TurnMessage(final Turn turn)
	{
		super(turn, "turn");
	}

	/**
	 * Once sent, gets back the deserialized Turn object
	 */
	public Turn getTurn()
	{
		return new Turn(getJson());
	}
}
