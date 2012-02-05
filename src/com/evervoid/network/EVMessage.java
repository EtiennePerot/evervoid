package com.evervoid.network;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.network.Message;

/**
 * The base class of all messages. Transparently splits itself into multiple PartialMessages. Subclasses should
 * implement their
 * own serialization/deserialization, rather than exposing the underlying JSON structure.
 */
public abstract class EVMessage implements Message
{
    /**
     * The Json containing the message.
     */
    private final Json aMessageContent;

    /**
     * Main constructor; used to create new EverMessages.
     * 
     * @param content
     *            The (Jsonable) content of the message
     */
    protected EVMessage(final Jsonable content)
    {
        if (content == null) {
            aMessageContent = Json.getNullNode();
        } else {
            aMessageContent = content.toJson();
        }
    }

    /**
     * Get the parsed Json object contained in this message. Called by subclasses only.
     * 
     * @return The parsed Json object inside this message
     */
    public Json getContent()
    {
        return aMessageContent;
    }

    /**
     * When it is time to send this message on the network, it needs to be converted to a list of partial messages
     * 
     * @return The list of sendable PartialMessages
     */
    protected List<PartialMessage> getMessages()
    {
        String jsonString = aMessageContent.toString();
        final String hash = aMessageContent.getHash();
        final List<String> parts = new ArrayList<String>();
        while (jsonString.length() > 0) {
            final int partSize = Math.min(PartialMessage.sMaxPartialMessageSize, jsonString.length());
            parts.add(jsonString.substring(0, partSize));
            jsonString = jsonString.substring(partSize);
        }
        final List<PartialMessage> messages = new ArrayList<PartialMessage>(parts.size());
        int partNumber = 0;
        for (final String part : parts) {
            messages.add(new PartialMessage(part, getType(), hash, partNumber, parts.size()));
            partNumber++;
        }
        return messages;
    }

    /**
     * @return The type of this message
     */
    public String getType()
    {
        return getClass().getName();
    }

    @Override
    public boolean isReliable()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Message setReliable(final boolean f)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString()
    {
        return "EverMessage(Type = " + getType() + "; Hash: " + aMessageContent.getHash() + ")";
    }
}
