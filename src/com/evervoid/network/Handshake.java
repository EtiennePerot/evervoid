package com.evervoid.network;

import com.jme3.network.serializing.Serializable;

/**
 * Handshake message sent by the client when first connecting to a server
 */
@Serializable(id = 1)
public class Handshake extends BaseMessage
{
}
