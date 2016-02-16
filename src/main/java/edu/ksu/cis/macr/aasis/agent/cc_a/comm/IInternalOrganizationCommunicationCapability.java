package edu.ksu.cis.macr.aasis.agent.cc_a.comm;

import edu.ksu.cis.macr.aasis.agent.cc_message.IBaseMessage;
import edu.ksu.cis.macr.aasis.agent.persona.IPersona;

import java.util.Queue;

/**
 * Handles organization-related messages.
 */
public interface IInternalOrganizationCommunicationCapability {

    void channelContent(Object content);

    IBaseMessage<?> receiveLocal();

    boolean sendLocal(IBaseMessage<?> message);

    String getCommunicationChannelID();

    IPersona getPersonaExecutionComponent();

    int messages();

    Queue<IBaseMessage<?>> getInternalOrganizationMessages();

    boolean broadcast(final IBaseMessage<?> message);

    boolean broadcastIncludeSelf(final IBaseMessage<?> message);
}