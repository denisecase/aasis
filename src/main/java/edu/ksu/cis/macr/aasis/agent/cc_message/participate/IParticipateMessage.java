package edu.ksu.cis.macr.aasis.agent.cc_message.participate;

import edu.ksu.cis.macr.aasis.agent.cc_message.IBaseMessage;

import java.io.IOException;

/**
 * Interface for a message used to for organizational admistration (e.g. issuing assignments).
 */
public interface IParticipateMessage extends IBaseMessage<ParticipatePerformative> {
    /**
     * Deserialize the message.
     *
     * @param bytes - an array of bytes
     * @return the deserialized message
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    Object deserialize(byte[] bytes) throws Exception;

    /**
     * Serialize the message.
     *
     * @return a byte array with the contents.
     * @throws IOException - If an I/O error occurs.
     */
    @Override
    byte[] serialize() throws IOException;
}
