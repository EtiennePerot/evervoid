package com.evervoid.network;

import com.evervoid.state.action.Turn;
import com.jme3.network.serializing.Serializable;

/**
 * A Turn message; holds a list of Actions
 */
@Serializable(id = 3)
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
