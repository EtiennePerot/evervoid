package com.evervoid.state.player;

public interface PlayerObserver
{
	public void playerDefeat(Player player);

	public void playerDisconnect(Player player);

	public void playerIncome(Player player, ResourceAmount amount);

	public void playerVictory(Player player);
}
