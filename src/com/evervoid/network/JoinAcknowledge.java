package com.evervoid.network;

import com.evervoid.json.Json;

public class JoinAcknowledge extends EverMessage
{
	public JoinAcknowledge()
	{
		super(new Json(), "joinack");
	}
}
