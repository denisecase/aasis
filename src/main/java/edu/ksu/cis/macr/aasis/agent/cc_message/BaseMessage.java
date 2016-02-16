package edu.ksu.cis.macr.aasis.agent.cc_message;

import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * The ec {@code Message} class is used by the {@code edu.ksu.cis.macr.aasis.agent.persona.IInternalCommunicationCapability}
 * capability to send and receive messages.  Message provides a ec class for simple, serializable messages.  The
 * only requirement for the messages content is that it be a serializable Java object.  The specific content type and
 * attributes are application- and message-specific.  In RabbitMQ, names of senders and receivers equate to topics. The
 * sender name should be the unique topic that addresses the specific agent sending the messages, which allows the localReceiver
 * to respond to the messages.  The performativeType is the type of message, generally corresponding to the agent notion of
 * performativeType such as inform, accept, agree, propose, query, etc. These can be standard performatives or
 * application-specific.  I have included serialize and deserialize methods that appear to be required for sending and
 * receiving messages over RabbitMQ (it seems to want a byte array). The implementation is likely not efficient and a
 * better understanding of the RabbitMQ API might yield a better solution.  General message class updated and extended
 * by D. Case and G. Martin based on an initial RabbitMQ implementation created by S. DeLoach.
 *
 * @param <T> the enumerated PerformativeType
 */
public class BaseMessage<T extends Enum<T>> implements Serializable, IBaseMessage<T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseMessage.class);
    private static final boolean debug = false;
    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The local Sender's {@code UniqueIdentifier} of the {@code Message}.
     */
    private UniqueIdentifier localSender;
    /**
     * The local Receiver's {@code UniqueIdentifier} of the {@code Message}.
     */
    private UniqueIdentifier localReceiver;
    /**
     * The performativeType of the {@code Message}.
     */
    private T performativeType;
    /**
     * The content of the {@code Message}.
     */
    private Object content;
    /**
     * The String address of the sender of the {@code Message} when using AMQP messaging.
     */
    private String remoteSender;
    /**
     * The String representation of receiver of the {@code Message} when using AMQP messaging.
     */
    private String remoteReceiver;


    /**
     * Constructs a new instance of {@code Message}.
     *
     * @param localSender      the localSender's {@code UniqueIdentifier} of the {@code Message}.
     * @param localReceiver    the localReceiver's {@code UniqueIdentifier} for the {@code Message}.
     * @param performativeType the performativeType of * * * * * * * the {@code Message}.
     * @param content          the content of the {@code Message}.
     */
    protected BaseMessage(final UniqueIdentifier localSender, final UniqueIdentifier localReceiver,
                          final T performativeType, final Object content) {
        if (localSender == null || localReceiver == null || performativeType == null) {
            throw new IllegalArgumentException(String.format("Parameters (localSender: %s, localReceiver: %s, " +
                    "performativeType: %s) cannot be null", localSender, localReceiver, performativeType));
        }
        this.remoteSender = "";
        this.remoteReceiver = "";
        this.localSender = localSender;
        this.localReceiver = localReceiver;
        this.performativeType = performativeType;
        this.content = content;
        if (debug) LOG.debug("Created new ec Message {}", this);
    }

    /**
     * Constructs a new instance of {@code Message}.
     *
     * @param remoteSender     the string address of the sender of the {@code Message}. Used with AMQP messaging.
     * @param remoteReceiver   the string address of the receiver of the {@code Message}. Used with AMQP messaging.
     * @param performativeType the performativeType of * * * * * * * the {@code Message}.
     * @param content          the content of the {@code Message}.
     */
    protected BaseMessage(final String remoteSender, final String remoteReceiver, final T performativeType,
                          final Object content) {
        if (remoteSender == null || remoteReceiver == null || performativeType == null) {
            throw new IllegalArgumentException(String.format("Parameters (senderMQ: %s, receiverMQ: %s, " +
                    "performativeType: %s) cannot be null", remoteSender, remoteReceiver, performativeType));
        }
        this.localSender = null;
        this.localReceiver = null;
        this.remoteSender = remoteSender;
        this.remoteReceiver = remoteReceiver;
        this.performativeType = performativeType;
        this.content = content;
        if (debug) LOG.debug("Created new ec Message {}", this);
    }

    public static <T extends Enum<T>> IBaseMessage<T> createMessage(final String remoteSender, final String remoteReceiver, final T performative,
                                                                    final Object content) {
        return new BaseMessage<>(remoteSender, remoteReceiver, performative, content);
    }

    public static <T extends Enum<T>> IBaseMessage<T> createMessage(final UniqueIdentifier localSender, final UniqueIdentifier localReceiver,
                                                                    final T performative, final Object content) {
        return new BaseMessage<>(localSender, localReceiver, performative, content);
    }

    public static String host(String persona) {
        String host = persona.replace("_F", "");
        if (persona.contains("in")) {
            host = persona.substring(persona.indexOf("in") + 2);
        }
        return host;
    }

    @Override
    public Object getContent() {
        return content;
    }

    @Override
    public UniqueIdentifier getLocalReceiver() {
        return this.localReceiver;
    }

    /**
     * Return the localSender's {@code UniqueIdentifier}.
     *
     * @return the {@code UniqueIdentifier} of the localSender.
     */
    @Override
    public UniqueIdentifier getLocalSender() {
        return this.localSender;
    }

    @Override
    public T getPerformativeType() {
        return this.performativeType;
    }

    /**
     * Return a String representing the receiver when using AMQP messaging.
     *
     * @return the name of the localReceiver.
     */
    @Override
    public String getRemoteReceiver() {
        return this.remoteReceiver;
    }

    /**
     * Return a String representing the sender when using AMQP messaging.
     *
     * @return the name of the sender.
     */
    @Override
    public String getRemoteSender() {
        return this.remoteSender;
    }

    @Override
    public String toString() {
        if (this.getLocalSender() == null) {
            return String.format("%s(remoteSender:%s, remoteReceiver:%s, performativeType:%s, content:%s)",
                    getClass().getSimpleName(), getRemoteSender(), getRemoteReceiver(), getPerformativeType(),
                    getContent());
        } else {
            return String.format("%s(localSender:%s, localReceiver:%s, performativeType:%s, content:%s)",
                    getClass().getSimpleName(), getLocalSender().toString(), getLocalReceiver().toString(), getPerformativeType(),
                    getContent());
        }
    }

    /**
     * Deserialize the message.
     *
     * @param bytes - an array of bytes
     * @return the deserialized {@code Message}
     * @throws Exception - if an exception occurs.
     */
    public synchronized Object deserialize(final byte[] bytes) throws Exception {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInput o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
    }

    @Override
    public boolean isLocal() {
        boolean isLocal = false;
        String h1 = "";
        String h2 = "";
        if (!this.getRemoteReceiver().equals("")) {
            h1 = BaseMessage.host(this.getRemoteReceiver());
            h2 = BaseMessage.host(this.getRemoteSender());
            if (h1.equals(h2)) {
                isLocal = true;
            }
        } else if (this.getLocalReceiver() != null) {
            h1 = BaseMessage.host(this.getLocalReceiver().toString());
            h2 = BaseMessage.host(this.getLocalSender().toString());
            if (h1.equals(h2)) {
                isLocal = true;
            }
        }

        if (isLocal) {
            if (debug) LOG.debug("Local participation - the message is {} ", this);
        } else {
            if (debug) LOG.debug("Remote participation - the message is {} ", this);
        }
        //  return isLocal;
        return true;
    }

    /**
     * Serialize the message.
     *
     * @return a byte array with the contents.
     * @throws IOException - If an I/O error occurs.
     */
    @Override
    public synchronized byte[] serialize() throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutput o = new ObjectOutputStream(b)) {
                o.writeObject(this);
            }
            return b.toByteArray();
        }
    }
}
