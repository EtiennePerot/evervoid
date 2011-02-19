package com.evervoid.server;

import com.evervoid.json.Json;
import com.jme3.network.connection.Client;

public interface EVServerObserver
{
	void messageReceived(String type, Client client, Json content);
}
