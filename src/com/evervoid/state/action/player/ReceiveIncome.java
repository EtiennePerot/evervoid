package com.evervoid.state.action.player;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;

/**
 * ReceivingIncome is a concrete subclass of the PlayerAction class. It is used to modify a Player's current Resource stock.
 * Negative amounts of income will decrease the Player's stock.
 */
public class ReceiveIncome extends PlayerAction
{
	/**
	 * The amount by which to modify the Player's current resources.
	 */
	private final ResourceAmount aAmount;

	/**
	 * Json Deserializer; the Json must meet the ReceiveIncom Json Protocol.
	 * 
	 * @throws IllegalEVActionException
	 *             If the Json does not meet the required protocol, or the action is somehow malformed.
	 */
	public ReceiveIncome(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aAmount = new ResourceAmount(j.getAttribute("amount"));
	}

	/**
	 * @param player
	 *            Determines both the acting and receiving Players.
	 * @param state
	 *            The State on which this Action will be carried out.
	 * @param amount
	 *            The amount by which to modify the Player's current resources.
	 * @throws IllegalEVActionException
	 *             If the action is malformed.
	 */
	public ReceiveIncome(final Player player, final EVGameState state, final ResourceAmount amount)
			throws IllegalEVActionException
	{
		super(player, player, state);
		aAmount = amount.clone();
	}

	@Override
	protected void executeAction()
	{
		getSender().addResources(aAmount);
	}

	@Override
	public String getDescription()
	{
		String description = "";
		for (final String resource : aAmount.getNames()) {
			description += resource + ": " + aAmount.getFormattedValue(resource) + ", ";
		}
		return description + "for player " + getSender().getNickname();
	}

	@Override
	protected boolean isValidPlayerAction()
	{
		return getSender().getResources().isCompatibleWith(aAmount);
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setAttribute("amount", aAmount);
	}
}
