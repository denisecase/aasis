package edu.ksu.cis.macr.aasis.agent.persona;

import edu.ksu.cis.macr.obaa_pp.objects.IDisplayInformation;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;


/**
 Allows EC {@code IPersona} to send and receive communications. It can be performed an unlimited number of times per turn. However, there is little sense
 in performing the action more than once as there will probably be no difference in the data returned for subsequent
 action within the same turn.  However, the sending and receiving of contents is not instantaneous. Sent contents
 will only be received in the next turn.

 @author Christopher Zhong, Denise Case
 */
public class InternalCommunicationCapability extends AbstractOrganizationCapability implements IInternalCommunicationCapability {
    private static final Logger LOG = LoggerFactory.getLogger(InternalCommunicationCapability.class);
    /**
     * {@code MIN_RANGE} is the minimum range allowed by the {@code IInternalCommunicationCapability}.
     */
    public static final int MIN_RANGE_IN_FEET = 1;
    /**
     * {@code MAX_RANGE} is the maximum range allowed by the {@code IInternalCommunicationCapability}.
     */
    public static final int MAX_RANGE_IN_FEET = 5;
    private static final boolean debug = false;
    /**
     * {@code contents} is a queue that holds contents that can be received by the agent.
     */
    private final Queue<CommunicationPacket> contents = new ConcurrentLinkedQueue<>();
    /**
     * {@code channels} provides a mechanism for extensions to this communication protocol so that only contents that are
     * flagged will be sent to the correct extension.
     */
    private final Map<String, ICommunicationChannel> channels = new HashMap<>();
    /**
     * {@code incoming} is a queue that holds incoming contents that will be received by the agent in the next turn.
     */
    private Queue<CommunicationPacket> incoming = new ConcurrentLinkedQueue<>();
    /**
     * {@code range} is the current range of the {@code IInternalCommunicationCapability} .
     */
    private int range;

    /**
     * {@code sendFailure} is the failure rate for sending contents. {@code sendFailure} ranges from {@code 0.0} to {@code 1.0} , where {@code 0.0} means that all contents are sent successfully, and {@code 1.0} means that no contents are
     * sent successfully.
     */
    private double sendFailure;

    /**
     * {@code receiveFailure} is the failure rate for receiving contents. {@code receiveFailure} ranges from {@code 0.0} to
     * {@code 1.0}. where {@code 0.0} means that all contents are received successfully, and {@code 1.0} means that no
     * contents are received successfully.
     */
    private double receiveFailure;

    /**
     Constructs a new instance of {@code PowerCommunication}.

     @param owner - the entity to which this capability belongs.
     @param org - the {@code Organization} in which this {@code IAgent} acts.
     */
    public InternalCommunicationCapability(IPersona owner, IOrganization org) {
        super(IInternalCommunicationCapability.class, owner, org);
        LOG.debug("Entering InternalCommunicationCapability constructor(owner={},org={}).", owner, org);
        LOG.debug("Exiting InternalCommunicationCapability constructor.");

    }

    /**
     Constructs a new instance of {@code IInternalCommunicationCapability}.

     @param owner the {@code IAgent} to which the {@code IInternalCommunicationCapability} belongs to.
     @param org the {@code Organization} in which the {@code IInternalCommunicationCapability} interacts with.
     @param range the range of the {@code IInternalCommunicationCapability}.
     @param sendFailure the failure rate for sending contents. {@code sendFailure} ranges from {@code 0.0} to {@code 1.0},
     where {@code 0.0} means that all contents are sent successfully, and {@code 1.0} means that no contents are sent
     successfully.
     @param receiveFailure the failure rate for receiving contents. {@code receiveFailure} ranges from {@code 0.0} to
     {@code 1.0}. where {@code 0.0} means that all contents are received successfully, and {@code 1.0} means that no
     contents are received successfully.
     */
    public InternalCommunicationCapability(final IPersona owner, final IOrganization org, final int range,
                                           final double sendFailure, final double receiveFailure) {
        this(IInternalCommunicationCapability.class, owner, org, range, sendFailure, receiveFailure);
        LOG.debug("Entering InternalCommunicationCapability constructor(owner={},org={},range={},sendFailure={},receiveFailure={}).", owner, org, range, sendFailure, receiveFailure);
        LOG.debug("Exiting InternalCommunicationCapability constructor.");
    }

    /**
     Constructs a new instance of {@code IInternalCommunicationCapability}.

     @param <CommunicationType> the sub-type of * * * * * * * * the {@code IInternalCommunicationCapability}.
     @param communicationClass the {@code Class} of a sub-class of the {@code IInternalCommunicationCapability}.
     @param owner the {@code IAgent} to which the {@code IInternalCommunicationCapability} belongs to.
     @param org the {@code Organization} in which the {@code IInternalCommunicationCapability} interacts with.
     @param range the range of the {@code IInternalCommunicationCapability}.
     @param sendFailure the failure rate for sending contents. {@code sendFailure} ranges from {@code 0.0} to {@code 1.0},
     where {@code 0.0} means that all contents are sent successfully, and {@code 1.0} means that no contents are sent
     successfully.
     @param receiveFailure the failure rate for receiving contents. {@code receiveFailure} ranges from {@code 0.0} to
     {@code 1.0}. where {@code 0.0} means that all contents are received successfully, and {@code 1.0} means that no
     contents are received successfully.
     */
    public <CommunicationType extends IInternalCommunicationCapability> InternalCommunicationCapability(
            final Class<CommunicationType> communicationClass,
            final IPersona owner,
            final IOrganization org, final int range,
            final double sendFailure, final double receiveFailure) {
        super(communicationClass, owner, org);
        setRange(range);
        setSendFailure(sendFailure);
        setReceiveFailure(receiveFailure);
        LOG.debug("Exiting <CommunicationType extends IInternalCommunicationCapability> InternalCommunicationCapability constructor.");
    }

    @Override
    public boolean addChannel(final String channelID,
                              final ICommunicationChannel channel) {
        LOG.debug("Entering addChannel(channelID={},channel={})",channelID,channel);
        if (channelID == null || channel == null) {
            throw new AssertionError();
        }
        return !this.channels.containsKey(channelID) && this.channels.put(channelID, channel) == null;
    }


    @Override
    public boolean broadcast(final String channelID, final Object content) {
        LOG.debug("Entering broadcast(channelID={},content={})", channelID, content);
        return broadcast(channelID, content, false);
    }

    /**
     Broadcasts the given {@code content} on a {@code ICommunicationChannel} by the given {@code String} that identifies
     the {@code ICommunicationChannel}.  If the given {@code String} identifying the {@code ICommunicationChannel} is
     {@code null}, then the {@code content} is not associated with any channels.

     @param channelID the {@code channelID} associated with the {@code content}.
     @param content the {@code content} to be received by everyone.
     @param includeSelf {@code true} if the {@code content} is also to be received by the sender, {@code false} otherwise.
     @return {@code true} if the messages was sent, {@code false} otherwise.
     */
    private boolean broadcast(final String channelID, final Object content,
                              final boolean includeSelf) {
        LOG.debug("Entering broadcast(channelID={},content={}, includeSelf={})", channelID, content, includeSelf);
        agentStatus();
        final double fSend = ThreadLocalRandom.current().nextDouble();
        if (fSend > getSendFailure()) {
            broadcastMessage(channelID, content, includeSelf);
        }
        updateChangeList(getOwner().toDisplayInformation());
        return true;
    }

    @Override
    public boolean broadcastIncludeSelf(final String channelID,
                                        final Object content) {
        LOG.debug("Entering broadcastIncludeSelf(channelID={},content={})", channelID, content);
        return broadcast(channelID, content, true);
    }

    /**
     Broadcasts a messages to every {@code IAgent}, only if the {@code IAgent} is within range.

     @param channelID the type of messages, used * * * * * * * * by {@code ICommunicationChannel} .
     @param content the messages to be sent.
     @param includeSelf {@code true} if the messages is also for the sender, {@code false} otherwise.
     */
    private void broadcastMessage(final String channelID, final Object content,
                                  final boolean includeSelf) {
        LOG.debug("Entering broadcastMessage(channelID={},content={}, includeSelf={})", channelID, content, includeSelf);
        lockData();
        try {
            Collection<IPersona> allPersona = getPersona();
            if (debug) LOG.debug("persona={}, includeSelf={}", allPersona.size(), includeSelf);
            for (IPersona persona : allPersona) {
                if (includeSelf || !getOwner().equals(persona)) {
                    sendLocalMessage(persona, channelID, content, true);
                }
            }
        } finally {
            unlockData();
            LOG.debug("Incoming={}", this.getIncoming().size());
            LOG.debug("Exiting broadcastMessage(channelID={},content={}, includeSelf={})", channelID, content, includeSelf);
        }

    }

    @Override
    public Collection<Entry<String, ICommunicationChannel>> getChannels() {
        return channels.entrySet();
    }

    @Override
    public double getFailure() {
        return (getSendFailure() + getReceiveFailure()) / 2;
    }

    /**
     Returns the incoming {@code Queue} of {@code CommunicationPacket}.

     @return the incoming {@code Queue} of {@code CommunicationPacket}.
     */
    public Queue<CommunicationPacket> getIncoming() {
        LOG.debug("Entering getIncoming(). incoming={}", incoming);
        return incoming;
    }

    /**
     Gets the range of the {@code IInternalCommunicationCapability} capability.

     @return the range of the {@code IInternalCommunicationCapability} capability.
     */
    public int getRange() {
        return range;
    }

    /**
     Sets the range of the {@code InternalCommunicationCapability} capability.

     @param range the new range of the {@code InternalCommunicationCapability} capability.
     */
    public void setRange(final int range) {
        if (range < MIN_RANGE_IN_FEET) this.range = MIN_RANGE_IN_FEET;
        else if (range > MAX_RANGE_IN_FEET) this.range = MAX_RANGE_IN_FEET;
        else this.range = range;
    }

    /**
     Returns the failure rate for receiving contents.

     @return the failure rate for receiving contents.
     */
    public double getReceiveFailure() {
        return receiveFailure;
    }

    /**
     Sets the failure rate for receiving contents.

     @param receiveFailure the new failure rate for receiving contents.
     */
    public void setReceiveFailure(final double receiveFailure) {
        this.receiveFailure = receiveFailure < MIN_FAILURE ? MIN_FAILURE
                : receiveFailure > MAX_FAILURE ? MAX_FAILURE
                : receiveFailure;
    }

    /**
     Returns the failure rate for sending contents.

     @return the failure rate for sending contents.
     */
    public double getSendFailure() {
        return sendFailure;
    }

    /**
     Sets the failure rate for sending contents.

     @param sendFailure the new failure rate for sending contents.
     */
    public void setSendFailure(final double sendFailure) {
        this.sendFailure = sendFailure < MIN_FAILURE ? MIN_FAILURE
                : sendFailure > MAX_FAILURE ? MAX_FAILURE
                : sendFailure;
    }

    @Override
    public void populateCapabilitiesOfDisplayObject(
            final IDisplayInformation displayInformation) {
        super.populateCapabilitiesOfDisplayObject(displayInformation);
        final Map<String, String> fields = displayInformation
                .getCapability(getIdentifier());
        fields.put("range", Integer.toString(getRange()));
        fields.put("sendFailure", Double.toString(sendFailure));
        fields.put("receiveFailure", Double.toString(receiveFailure));
    }

    @Override
    public Object receive() {
        LOG.debug("Entering receive()");
        agentStatus();
        if (!contents.isEmpty()) {
            final CommunicationPacket message = contents.poll();
            return message.content;
        }
        return null;
    }

    @Override
    public boolean removeChannel(final String channelID) {
        if (channelID == null) throw new AssertionError();
        return channels.remove(channelID) != null;
    }


    @Override
    public boolean replaceChannel(final String channelID,
                                  final ICommunicationChannel channel) {
        if (channelID == null || channel == null) {
            throw new AssertionError();
        }
        if (channels.containsKey(channelID)) {
            channels.put(channelID, channel);
        }
        return false;
    }

    @Override
    public void reset() {
        if (debug) LOG.debug("Entering reset(). incoming={}", this.getIncoming().size());
        while (!getIncoming().isEmpty()) {

            final CommunicationPacket packet = getIncoming().poll();
            if (debug) LOG.debug("incoming packet={}", packet);
            final ICommunicationChannel channel = channels.get(packet.channel);
            if (ThreadLocalRandom.current().nextDouble() > getReceiveFailure()) {
                /* messages is successfully received */
                if (channel == null) {
                    contents.add(packet);
                    if (debug) LOG.debug("Adding message to null channel={} contents={}", channel, contents);
                } else {
                    try {
                        if (debug) LOG.debug("Adding message to channel={} contents={}", channel, contents);
                        LOG.debug("ChannelContentTEST: reset in CommImpl. packet.channel={}. packet.content={}. ", packet.channel, packet.content.toString());

                        channel.channelContent(packet.content);

                        LOG.debug("ChannelContentOK: reset in CommImpl. packet.channel={}. packet.content={}. ", packet.channel, packet.content.toString());


                    } catch (Exception e) {
                        LOG.error("ChannelContentERROR: reset in CommImpl. packet.channel={}. packet.content={}. Message={}", packet.channel, packet.content.toString(), e.getMessage());
                        //   System.exit(-92);
                    }
                }
            } else {
                /* messages is lost */
                contents.poll();
            }
        }
    }

    @Override
    public boolean send(final UniqueIdentifier toAgent, final String filter,
                        final Object content) {
        agentStatus();
        final double fSend = ThreadLocalRandom.current().nextDouble();
        if (fSend > getSendFailure()) {
            final boolean send = sendMessage(toAgent, filter, content);
            updateChangeList(getOwner().toDisplayInformation());
            return send;
        }
        updateChangeList(getOwner().toDisplayInformation());
        return false;
    }


    @Override
    public boolean sendLocal(final UniqueIdentifier receiverIdentifier, final String filter, final Object content) {
        if (debug)
            LOG.debug("Entering sendLocal(receiverIdentifier={}, filter={}, content={}.", receiverIdentifier, filter, content);
        this.agentStatus();
        final double fSend = ThreadLocalRandom.current().nextDouble();
        if (debug) LOG.debug("fSend={}.", fSend);
        if (fSend > this.getSendFailure()) {
            LOG.debug("will send");
            boolean send;
            if (receiverIdentifier.toString().startsWith("broadcast")) {
                LOG.info("Broadcasting local message: {}.", filter);
                send = broadcast(filter, content, true);
            } else {
                if (debug) LOG.debug("Targeted local message.");
                send = this.sendLocalMessage(receiverIdentifier, filter, content);
            }
            updateChangeList(getOwner().toDisplayInformation());
            return send;
        }
        LOG.info("failed to send");
        updateChangeList(getOwner().toDisplayInformation());
        return false;
    }


    /**
     Sends a messages to the given {@code IAgent}, only if the {@code IAgent} is within range.

     @param toAgent the {@code IAgent} to receive the messages.
     @param channelID the type of messages, used * * * * * * * * by {@code ICommunicationChannel} .
     @param content the messages to be sent.
     @return {@code true} if the messages was sent successfully, {@code false} otherwise.
     */
    private boolean sendLocalMessage(final UniqueIdentifier toAgent,
                                     final String channelID, final Object content) {
        this.lockData();
        try {
            final IPersona receiverAgent = this.getPersona(toAgent);
            if (receiverAgent != null) {
                return sendLocalMessage(receiverAgent, channelID, content, false);
            }
            if (debug) LOG.debug("sendLocalMessage(IAgent, String, Object) Agent {} does not exist", toAgent);

            return false;
        } finally {
            unlockData();
        }
    }

    /**
     Sends a messages to the given {@code IAgent}, only if the {@code IAgent} is within range.

     @param toAgent the {@code IAgent} to receive the messages.
     @param channelID the type of messages, used * * * * * * * * by {@code ICommunicationChannel} .
     @param content the messages to be sent.
     @param broadcast indicates whether the messages is a broadcast messages or not.
     @return {@code true} if the messages was sent successfully, {@code false} otherwise.
     */
    private boolean sendLocalMessage(final IPersona toAgent, final String channelID, final Object content,
                                                  final boolean broadcast) {
        LOG.debug("\tEntering sendLocalMessage(toAgent={},channelID={}, content={}, broadcast={})", toAgent, channelID, content, broadcast);
        InternalCommunicationCapability capability = (InternalCommunicationCapability) toAgent
                .getCapability(IInternalCommunicationCapability.class);
        if (capability != null) {
            LOG.debug("toAgent={}, capability={}", toAgent, capability);
            final CommunicationPacket communicationPacket = new CommunicationPacket(
                    getOwner().getUniqueIdentifier(), broadcast ? null
                    : toAgent.getUniqueIdentifier(), channelID, content);

            LOG.debug("\t  incoming  before={}", capability.getIncoming());
            capability.getIncoming().add(communicationPacket);
            LOG.debug("\t  incoming  after={}", capability.getIncoming());
            LOG.debug("\tExiting sendLocalMessage: communicationPacket={}", communicationPacket);
            return true;
        }
        return false;
    }

    /**
     Sends a messages to the given {@code IAgent}, only if the {@code IAgent} is within range.

     @param toAgent the {@code IAgent} to receive the messages.
     @param channelID the type of messages, used * * * * * * * * by {@code ICommunicationChannel} .
     @param content the messages to be sent.
     @return {@code true} if the messages was sent successfully, {@code false} otherwise.
     */
    private boolean sendMessage(final UniqueIdentifier toAgent,
                                final String channelID, final Object content) {
        LOG.debug("Entering sendMessage(toAgent={},channelID, content={})", toAgent, channelID, content);
        lockData();
        try {
            final IPersona receiverAgent = getPersona(toAgent);
            if (receiverAgent != null) {
                return sendMessage(receiverAgent, channelID, content, false);
            }
            if (debug) LOG.debug("sendMessage(IAgent, String, Object) Agent {} do not exist", toAgent);

            return false;
        } finally {
            unlockData();
        }
    }

    /**
     Sends a messages to the given {@code IAgent}, only if the {@code IAgent} is within range.

     @param toAgent the {@code IAgent} to receive the messages.
     @param channelID the type of messages, used * * * * * * * * by {@code ICommunicationChannel} .
     @param content the messages to be sent.
     @param broadcast indicates whether the messages is a broadcast messages or not.
     @return {@code true} if the messages was sent successfully, {@code false} otherwise.
     */
    private boolean sendMessage(final IPersona toAgent,
                                final String channelID, final Object content,
                                final boolean broadcast) {
        LOG.debug("Entering sendMessage(toAgent={},channelID, content={}, broadcast={})", toAgent, channelID, content, broadcast);
        InternalCommunicationCapability capability = (InternalCommunicationCapability) toAgent
                .getCapability(IInternalCommunicationCapability.class);
        if (capability != null) {
            final CommunicationPacket communicationPacket = new CommunicationPacket(
                    getOwner().getUniqueIdentifier(), broadcast ? null
                    : toAgent.getUniqueIdentifier(), channelID, content);
            capability.getIncoming().add(communicationPacket);
            return true;
        }
        if (debug)
            LOG.debug("sendMessage(IAgent, IAgent, Object, boolean) Agent {} do not have the communication capability.", toAgent);

        return false;
    }


    @Override
    public Element toElement(final Document document) {
        final Element capability = super.toElement(document);

        Element parameter = (Element) capability.appendChild(document.createElement(ELEMENT_PARAMETER));
        parameter.setAttribute(ATTRIBUTE_TYPE, int.class.getSimpleName());
        parameter.appendChild(document.createTextNode(Integer.toString(getRange())));

        parameter = (Element) capability.appendChild(document.createElement(ELEMENT_PARAMETER));
        parameter.setAttribute(ATTRIBUTE_TYPE, double.class.getSimpleName());
        parameter.appendChild(document.createTextNode(Double.toString(sendFailure)));

        parameter = (Element) capability.appendChild(document.createElement(ELEMENT_PARAMETER));
        parameter.setAttribute(ATTRIBUTE_TYPE, double.class.getSimpleName());
        parameter.appendChild(document.createTextNode(Double.toString(receiveFailure)));

        return capability;
    }

    /**
     The {@code CommunicationPacket} class is an internal header representation of contents for the {@code IInternalCommunicationCapability} class.

     @author Christopher Zhong
     @version $Revision: 1.7.4.7 $, $Date: 2011/09/19 14:25:45 $
     @since 1.0
     */
    private static class CommunicationPacket {

        /**
         * The sender of the {@code CommunicationPacket}.
         */
        private final UniqueIdentifier sender;
        /**
         * The receiver of the {@code CommunicationPacket}.
         */
        private final UniqueIdentifier receiver;
        /**
         * The channel of the {@code CommunicationPacket}.
         */
        private final String channel;
        /**
         * The content of the {@code CommunicationPacket}.
         */
        private final Object content;

        /**
         Constructs a new instance of {@code CommunicationPacket}.

         @param sender the sender of the {@code CommunicationPacket}.
         @param receiver the receiver for * * * * * * * * the {@code CommunicationPacket}, a {@code null} value indicates
         that this {@code CommunicationPacket} is a broadcast.
         @param channel the type of {@code CommunicationPacket}, used by {@code ICommunicationChannel}.
         @param content the content of the {@code CommunicationPacket}.
         */
        CommunicationPacket(final UniqueIdentifier sender,
                            final UniqueIdentifier receiver, final String channel,
                            final Object content) {
            if (sender == null || content == null) {
                throw new IllegalArgumentException(String.format("Neither sender (%s) nor content (%s) can be null.",
                        sender, content));
            }
            this.sender = sender;
            this.receiver = receiver;
            this.channel = channel;
            this.content = content;

            checkSerializability(content);
        }

        /**
         * Determines of the given {@code Object} is {@code Serializable}.
         *
         * @param content the {@code Object} to be checked.
         * @return {@code true} if the given {@code Object} is {@code Serializable}, {@code false} otherwise.
         */
        private boolean checkSerializability(final Object content) {
            try {
                new ObjectOutputStream(new OutputStream() {
                    @Override
                    public void write(final int b) throws IOException {
                        /* do nothing */
                    }
                }).writeObject(content);
                return true;
            } catch (final IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public String toString() {
            return String.format("From:%s, To: %s, At: %s, With: %s", sender,
                    receiver, channel, content);
        }
    }
}
