package com.evervoid.state.action.player;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;

public class ReceiveIncome extends PlayerAction
{
	private final ResourceAmount aAmount;

	public ReceiveIncome(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aAmount = new ResourceAmount(j.getAttribute("amount"));
	}

	public ReceiveIncome(final Player player, final EVGameState state, final ResourceAmount amount)
			throws IllegalEVActionException
	{
		super(player, player, state);
		aAmount = amount.clone();
	}

	@Override
	protected void executeAction()
	{
		aPlayer.addResource(aAmount);
	}

	@Override
	public String getDescription()
	{
		String description = "";
		for (final String resource : aAmount.getNames()) {
			description += resource + ": " + aAmount.getValue(resource) + ", ";
		}
		return description + "for player " + aPlayer.getNickname();
	}

	@Override
	protected boolean isValidPlayerAction()
	{
		return aPlayer.getResources().isCompatibleWith(aAmount);
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setAttribute("amount", aAmount);
	}
}
