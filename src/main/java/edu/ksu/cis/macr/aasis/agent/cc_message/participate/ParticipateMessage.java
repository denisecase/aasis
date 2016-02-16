package edu.ksu.cis.macr.aasis.agent.cc_message.participate;

import edu.ksu.cis.macr.aasis.agent.cc_message.BaseMessage;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * {@code ParticipateMessage} extends {@code Message }. from AssignmentSetModification created by Christopher Zhong,
 * Revision 1.1 modified by Denise Case
 *
 * @author Christopher Zhong
 * @version $Revision: 1.1
 * @since 1.0
 */
public class ParticipateMessage extends BaseMessage<ParticipatePerformative> implements IParticipateMessage {
    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ParticipateMessage.class);
    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Constructs a new instance of a {@code ParticipateMessage}.
     */
    public ParticipateMessage() {
        super("", "", ParticipatePerformative.ASSIGNMENT, null);
        LOG.debug("Created new ParticipateMessage {}", this.toString());
    }

    /**
     * Initialize a new instance of this class.
     *
     * @param sender       - the {@code UniqueIdentifier} of the sender
     * @param receiver     -  the {@code UniqueIdentifier} of the receiver
     * @param performative - the {@code ParticipatePerformative} defining the type of message
     * @param content      - the message content object
     */
    public ParticipateMessage(final UniqueIdentifier sender, final UniqueIdentifier receiver,
                              final ParticipatePerformative performative, final Object content) {
        super(sender, receiver, performative, content);
    }

    /**
     * Initialize a new instance of this class.
     *
     * @param sender       - the {@code String} name of the sender
     * @param receiver     -  the {@code String} name of the receiver
     * @param performative - the {@code ParticipatePerformative} defining the type of message
     * @param content      - the message content object
     */
    public ParticipateMessage(final String sender, final String receiver, final ParticipatePerformative performative, final Object content) {
        super(sender, receiver, performative, content);
    }

    public synchronized static IParticipateMessage createLocalParticipateMessage(final UniqueIdentifier localSender, final UniqueIdentifier localReceiver,
                                                                                 final ParticipatePerformative performative, final Object content) {
        return new ParticipateMessage(localSender, localReceiver, performative, content);
    }

    public synchronized static ParticipateMessage createParticipateMessage() {
        return new ParticipateMessage();
    }

    public synchronized static IParticipateMessage createRemoteParticipateMessage(final String remoteSender, final String remoteReceiver, final ParticipatePerformative performative,
                                                                                  final Object content) {
        return new ParticipateMessage(remoteSender, remoteReceiver, performative, content);
    }

    /**
     * Deserialize the message.
     *
     * @param bytes - an array of bytes
     * @return the deserialized {@code ParticipateMessage}
     * @throws Exception - if an exception occurs.
     */
    @Override
    public Object deserialize(final byte[] bytes) throws Exception {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInput o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
    }

    /**
     * Serialize the message.
     *
     * @return a byte array with the contents.
     * @throws IOException - If an I/O error occurs.
     */
    @Override
    public byte[] serialize() throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutput o = new ObjectOutputStream(b)) {
                o.writeObject(this);
            }
            return b.toByteArray();
        }
    }
}
