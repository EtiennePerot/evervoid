package com.evervoid.server;

import com.jme3.network.connection.Client;

public class EverMessageSendingException extends Exception
{
	private static final long serialVersionUID = 1L;
	private final Client aClient;

	public EverMessageSendingException(final Client client)
	{
		aClient = client;
	}

	public Client getClient()
	{
		return aClient;
	}
}
