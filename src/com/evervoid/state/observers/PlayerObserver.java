package com.evervoid.state.observers;

import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;

public interface PlayerObserver
{
	public void playerDefeat(Player player);

	public void playerDisconnect(Player player);

	public void playerIncome(Player player, ResourceAmount amount);

	public void playerVictory(Player player);
}
