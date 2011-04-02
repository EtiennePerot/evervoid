package com.evervoid.network;

import com.evervoid.json.Json;

public class JoinErrorMessage extends EverMessage
{
	public JoinErrorMessage(final String errorMessage)
	{
		super(new Json(errorMessage));
	}
}
