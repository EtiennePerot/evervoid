package com.evervoid.network;

import com.evervoid.json.Json;
import com.jme3.network.connection.Client;

public interface EVNetworkObserver
{
	void messageReceived(String type, Client client, Json content);
}
