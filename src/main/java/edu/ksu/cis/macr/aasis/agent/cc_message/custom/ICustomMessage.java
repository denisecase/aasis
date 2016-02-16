package edu.ksu.cis.macr.aasis.agent.cc_message.custom;

import edu.ksu.cis.macr.aasis.agent.cc_message.IBaseMessage;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

import java.io.IOException;

/**
 * {@code IPowerMessage} provides an interface for describing messages between agents containing {@code IPowerMessageContent}.
 */
public interface ICustomMessage extends IBaseMessage<Performative> {
    /**
     * /** Constructs a new internal message.
     *
     * @param sender       - String name of the agent sending the message
     * @param receiver     - String name of the agent to whom the message is sent
     * @param performative - the {@code PowerPerformative} indicating the type of message
     * @param content      - the message content
     * @return the IPowerMessage created
     */
    public static ICustomMessage createLocal(final UniqueIdentifier sender, final UniqueIdentifier receiver,
                                             final Performative performative, final Object content) {
        return new CustomMessage(sender, receiver, performative, content);
    }

    /**
     * /** Constructs a new organization message.
     *
     * @param senderString   - String name of the agent sending the message
     * @param receiverString - String name of the agent to whom the message is sent
     * @param performative   - the {@code PowerPerformative} indicating the type of message
     * @param content        - the message content
     * @return - the IPowerMessage created
     */
    public static ICustomMessage createRemote(final String senderString, final String receiverString,
                                              final Performative performative, final Object content) {
        return new CustomMessage(senderString, receiverString, performative, content);
    }

    /**
     * Deserialize the message.
     *
     * @param bytes - an array of bytes
     * @return the deserialized message
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    @Override
    Object deserialize(byte[] bytes) throws Exception;

    /**
     * Serialize the message.
     *
     * @return a byte array with the contents.
     * @throws java.io.IOException - If an I/O error occurs.
     */
    @Override
    byte[] serialize() throws IOException;
}
