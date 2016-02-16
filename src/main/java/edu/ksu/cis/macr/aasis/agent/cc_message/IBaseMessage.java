package edu.ksu.cis.macr.aasis.agent.cc_message;

import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

import java.io.IOException;

/**
 * Interface for the ec generic message class that can work both internally and externally in distributed systems.
 */
public interface IBaseMessage<T extends Enum<T>> {
    /**
     * Deserialize the message.
     *
     * @param bytes - an array of bytes
     * @return the deserialized {@code ParticipateMessage}
     * @throws Exception - if an exception occurs.
     */
    Object deserialize(final byte[] bytes) throws Exception;

    /**
     * Return the content of the {@code Message}.
     *
     * @return the content of the {@code Message}.
     */
    Object getContent();

    /**
     * Return the receiver's {@code UniqueIdentifier}.
     *
     * @return the {@code UniqueIdentifier} of the receiver.
     */
    UniqueIdentifier getLocalReceiver();

    /**
     * Return the sender's {@code UniqueIdentifier}.
     *
     * @return the {@code UniqueIdentifier} of the sender.
     */
    UniqueIdentifier getLocalSender();

    /**
     * Return the performative of the {@code Message}.
     *
     * @return the performative of the {@code Message}.
     */
    T getPerformativeType();

    /**
     * Return a String representing the receiver when using AMQP messaging.
     *
     * @return the name of the receiver.
     */
    String getRemoteReceiver();

    /**
     * Return a String representing the sender when using AMQP messaging.
     *
     * @return the name of the sender.
     */
    String getRemoteSender();

    boolean isLocal();

    /**
     * Serialize the message.
     *
     * @return a byte array with the contents.
     * @throws IOException - If an I/O error occurs.
     */
    byte[] serialize() throws IOException;
}
