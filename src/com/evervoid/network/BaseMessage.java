package com.evervoid.network;

import com.jme3.network.message.GZIPCompressedMessage;

/**
 * Basic EverVoid message class. Always compressed.
 */
public abstract class BaseMessage extends GZIPCompressedMessage
{
}
