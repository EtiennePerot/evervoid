package com.evervoid.network;

import java.lang.reflect.InvocationTargetException;

import com.evervoid.json.Json;

/**
 * This class compiles a list of {@link PartialMessage} into the {@link EVMessage} they represent. The
 */
public class EVMessageBuilder
{
    /**
     * The assembled partial messages.
     */
    private final String[] aParts;
    /**
     * The type of the message being built.
     */
    private final String aType;

    /**
     * @param type
     *            The type of the final message being built.
     * @param totalParts
     *            The total number of parts in the final message.
     */
    public EVMessageBuilder(final String type, final int totalParts)
    {
        aType = type;
        aParts = new String[totalParts];
    }

    /**
     * Puts the partial message in the spot determined by the message's part field.
     * 
     * @param message
     *            The partial message to place.
     */
    void addPart(final PartialMessage message)
    {
        aParts[message.getPart()] = message.getDecodedMessage();
    }

    /**
     * Compiles the final message out of all parts gotten so far
     * 
     * @return The final message as an EverMessage, or null if we don't have all the parts yet
     */
    EVMessage getMessage()
    {
        String finalJson = "";
        for (final String aPart : aParts) {
            if (aPart == null) {
                return null;
            }
            finalJson += aPart;
        }
        try {
            // now we have to find the constructor for the type of the message
            final Class<?> cl = Class.forName(aType);
            if (finalJson.equals("{}")) {
                // TODO there has to be a better way to do this.  Is this a NULL NODE?
                // if there is no content then use the default constructor
                // this just makes creating EVMessage subclasses easier
                return (EVMessage) cl.newInstance();
            }
            final java.lang.reflect.Constructor<?> co = cl.getConstructor(Json.class);
            return (EVMessage) co.newInstance(Json.fromString(finalJson));

        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
        return null; // TODO throw an exception here instead
        // TODO here in case test code fails: return new EVMessage(Json.fromString(finalJson), aType);
    }
}
