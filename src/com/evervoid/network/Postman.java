package com.evervoid.network;

import java.util.List;

import com.evervoid.utils.LoggerUtils;
import com.jme3.network.MessageConnection;

/**
 * Separates a {@link EVMessage} into smaller {@link PartialMessage}s and sends them to a {@link MessageConnection} . Sending
 * can be done synchronously or asynchronously. Note that ALL SENDING MUST BE DONE THROUGH THE POSTMAN. EVMessages are not
 * serializable, only PartialMessages are.
 */
class Postman extends Thread
{
	/**
	 * The destination to which the message will be delivered.
	 */
	private final MessageConnection aDestination;
	/**
	 * The message to deliver.
	 */
	private final EVMessage aMessage;

	/**
	 * @param destination
	 *            The destination where the message should be delivered.
	 * @param message
	 *            The message to send.
	 */
	Postman(final MessageConnection destination, final EVMessage message)
	{
		aDestination = destination;
		aMessage = message;
		setName("Postman");
	}

	@Override
	public void run()
	{
		try {
			send();
		}
		catch (final EVMessageSendingException e) {
			// Do nothing, can't notify back anymore at this point
		}
	}

	/**
	 * Splits the contained EVMessage into smaller PartialMessages and then sends them.
	 * 
	 * @return Whether the send was successful.
	 * @throws EVMessageSendingException
	 *             If we couldn't reach the client.
	 */
	protected boolean send() throws EVMessageSendingException
	{
		final List<PartialMessage> messages = aMessage.getMessages();
		int partIndex = 1;
		final int parts = messages.size();
		for (final PartialMessage part : messages) {
			try {
				LoggerUtils.info("Postman Sending to " + aDestination + ", part " + partIndex + "/" + parts);
				synchronized (aDestination) {
					// make sure we're the only one using this connection
					aDestination.send(part);
				}
				if (partIndex != parts) { // If it's not the last message
					Thread.sleep(100); // Wait a bit
				}
			}
			catch (final NullPointerException e) {
				// Happens when inner client (inside the jME classes) doesn't get removed properly.
				throw new EVMessageSendingException(aDestination);
			}
			catch (final InterruptedException e) {
				// Not going to happen
			}
			partIndex++;
		}
		return true;
	}
}
