package edu.ksu.cis.macr.aasis.agent.cc_a.comm;


import edu.ksu.cis.macr.aasis.agent.cc_message.IBaseMessage;
import edu.ksu.cis.macr.aasis.agent.persona.IInternalCommunicationCapability;
import edu.ksu.cis.macr.aasis.agent.persona.IPersona;
import edu.ksu.cis.macr.organization.model.Capability;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 Supports internal CC communications for organization-related messages.
 Sent via EC.
 Created by Denise Case based on AbstractControlComponent by Chris Zhong.
 */
public class InternalOrganizationCommunicationCapability implements IInternalOrganizationCommunicationCapability {
    private static final Logger LOG = LoggerFactory.getLogger(InternalOrganizationCommunicationCapability.class);
    private static final boolean debug = false;
     /**
     * The
     * {@code ICommunicationChannel} keyword for registering organization
     * getNumberOfMessages.
     */
    private static final String COMMUNICATION_CHANNEL_ID = "Organization Messages";

    /**
     * A
     * {@code Queue} that holds
     * {@code Message} sent to this
     * {@code IAbstractControlComponent}.
     */
    private final Queue<IBaseMessage<?>> internalOrganizationMessages;

    private final IPersona persona;

    public InternalOrganizationCommunicationCapability(final IPersona persona) {
        this.persona = persona;
        this.internalOrganizationMessages = new ConcurrentLinkedQueue<>();
    }

    /**
     * Broadcasts an
     * {@code Message}.
     *
     * @param message the {@code Message} to be sent.
     * @return {@code true} if the {@code Message} was
     * sent, {@code false} otherwise.
     */
    public final boolean broadcast(final IBaseMessage<?> message) {
        LOG.debug("Entering broadcast(message={}",message);
        Objects.requireNonNull(getPersonaExecutionComponent(),
                "execution component cannot be null");
        Objects.requireNonNull(
                getPersonaExecutionComponent().getCapability(IInternalCommunicationCapability.class),
                "capability cannot be null");
        Objects.requireNonNull(getCommunicationChannelID(),
                "communication channelID cannot be null");
        try {
            return getPersonaExecutionComponent().getCapability(IInternalCommunicationCapability.class)
                    .broadcast(getCommunicationChannelID(), message);
        } catch (Exception e) {
            LOG.error("ERROR broadcasting message {}: {}", message, e.getMessage());
         //  throw new RuntimeException(e.getMessage(), e);
            System.exit(-99);
        }
        return false;
    }

    /**
     * Broadcasts an
     * {@code Message}. The sender will also receive the
     * {@code Message}.
     *
     * @param message the {@code Message} to be sent.
     * @return {@code true} if the {@code Message} was
     * sent, {@code false} otherwise.
     */
    @Override
    public final boolean broadcastIncludeSelf(final IBaseMessage<?> message) {
        LOG.debug("Entering broadcastIncludeSelf(message={}",message);

        IPersona p = getPersonaExecutionComponent();
        LOG.debug("persona={}",p);

        Capability c = Objects.requireNonNull(p.getCapability(IInternalCommunicationCapability.class), "ERROR: getCapability(IInternalCommunicationCapability.class) cannot be null. ");
        LOG.debug("IInternalCommunicationCapability={}",c);

        LOG.debug("COMMUNICATION_CHANNEL_ID={}",getCommunicationChannelID());

        boolean success = getPersonaExecutionComponent().getCapability(IInternalCommunicationCapability.class)
                .broadcastIncludeSelf(getCommunicationChannelID(), message);
        LOG.debug("Exiting broadcastIncludeSelf: success={} message={}",success, message);
        return success;
    }

    @Override
    public final void channelContent(final Object content) {
        LOG.debug("Entering channelContent(). Gets raw content and adds message. content={}", content);
        internalOrganizationMessages.add((IBaseMessage<?>) content);
    }

    @Override
    public IBaseMessage<?> receiveLocal() {
        LOG.debug("Entering receiveLocal(). internalOrganizationMessages.size()",this.internalOrganizationMessages.size());
        return internalOrganizationMessages.poll();
    }

    @Override
    public final boolean sendLocal(final IBaseMessage<?> message) {
        LOG.debug("Entering sendLocal(message={})",message);
        boolean success = false;

        final UniqueIdentifier localReceiver = message.getLocalReceiver();
        LOG.debug("localReceiver={}",localReceiver);

        final String communicationChannelID = getCommunicationChannelID();
        LOG.debug("communicationChannelID={}",communicationChannelID);

        IInternalCommunicationCapability capability = getPersonaExecutionComponent().getCapability(IInternalCommunicationCapability.class);
        LOG.debug("capability={}",capability);

        success = capability.sendLocal(localReceiver, communicationChannelID, message);

        if (!success) {
            LOG.error("ERROR sending message (local={}): {}", message.isLocal(), message);
            System.exit(-2224);
        }
        LOG.debug("Exiting sendLocal: success={}",success);
        return success;
    }

    @Override
    public final String getCommunicationChannelID() {
        return COMMUNICATION_CHANNEL_ID;
    }

    @Override
    public final IPersona getPersonaExecutionComponent() {
        return persona;
    }

    @Override
    public final int messages() {
        return internalOrganizationMessages.size();
    }

    @Override
    public final Queue<IBaseMessage<?>> getInternalOrganizationMessages() {
        return this.internalOrganizationMessages;
    }

    @Override
    public final String toString() {
        return "InternalOrganizationCommunicationCapability{" +
                "internalOrganizationMessages=" + internalOrganizationMessages +
                ", persona=" + persona +
                '}';
    }
}