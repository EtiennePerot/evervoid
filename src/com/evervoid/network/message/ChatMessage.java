package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.evervoid.state.Color;

public class ChatMessage extends EVMessage
{
    public ChatMessage(final Json json)
    {
        super(json);
    }

    /**
     * Client-side constructor: Only send the message
     * 
     * @param message
     *            The message to send
     */
    public ChatMessage(final String message)
    {
        this(null, null, message);
    }

    /**
     * Server-side constructor: Holds all the message information
     * 
     * @param player
     *            The player who sent the message
     * @param playerColor
     *            The color of that player
     * @param message
     *            The message contents
     */
    public ChatMessage(final String player, final Color playerColor, final String message)
    {
        super(new Json().setAttribute("message", message).setAttribute("player", player)
                        .setAttribute("color", playerColor));
    }
}
