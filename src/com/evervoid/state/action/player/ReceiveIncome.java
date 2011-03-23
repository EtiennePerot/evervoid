package com.evervoid.state.action.player;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.player.Player;

public class ReceiveIncome extends PlayerAction
{
	private final int aAmount;
	private final String aResourceName;

	public ReceiveIncome(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aResourceName = j.getStringAttribute("resource");
		aAmount = j.getIntAttribute("amount");
	}

	public ReceiveIncome(final Player player, final EVGameState state, final String resourceName, final int amountChange)
			throws IllegalEVActionException
	{
		super(player, "ChangeResourceAction", player, state);
		aResourceName = resourceName;
		aAmount = amountChange;
	}

	@Override
	public void execute()
	{
		aPlayer.addResource(aResourceName, aAmount);
	}

	@Override
	protected boolean isValidPlayerAction()
	{
		final int current = aPlayer.getResourceValue(aResourceName);
		return current + aAmount >= 0;
		// TODO - check upper bound
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("resource", aResourceName);
		j.setIntAttribute("amount", aAmount);
		return j;
	}
}
