package com.evervoid.network.connection;

import com.evervoid.state.action.Turn;

/**
 * A Turn message; holds a list of Actions
 */
public class TurnMessage extends JsonMessage
{
	/**
	 * Constructor; Takes a Turn instance and prepares it for sending
	 * 
	 * @param turn
	 */
	public TurnMessage(final Turn turn)
	{
		super(turn);
	}

	/**
	 * Once sent, gets back the deserialized Turn object
	 */
	public Turn getTurn()
	{
		return new Turn(getJson());
	}
}
