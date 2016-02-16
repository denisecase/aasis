package edu.ksu.cis.macr.aasis.agent.ec_cap;

import edu.ksu.cis.macr.aasis.agent.cc_message.IBaseMessage;
import edu.ksu.cis.macr.aasis.agent.persona.IPersona;

import java.util.Queue;

/**
 * Provides an interface for describing the ability to communicate about
 * organizational behavior.
 */
public interface IOrganizationCommunicationCapability{

    void channelContent(Object content);

    String getCommunicationChannelID();

    Queue<IBaseMessage<?>> getOrganizationMessages();

    IBaseMessage<?> receive();

    int messages();

    IPersona getOwner();

    boolean send(IBaseMessage<?> message);

}
