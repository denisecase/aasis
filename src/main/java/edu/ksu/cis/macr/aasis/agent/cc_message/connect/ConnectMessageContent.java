package edu.ksu.cis.macr.aasis.agent.cc_message.connect;

import java.io.Serializable;

/**
 * Provides message payload content needed to establish simulated secure communications with afflicated agents.
 */
public final class ConnectMessageContent implements Serializable, IConnectMessageContent {
    private static final long serialVersionUID = 1L;
    private final String senderAgentAbbrev;
    private final String receiverAgentAbbrev;
    private final String organizationAbbrev;
    private final String expectedMasterAbbrev;
    private final String message;
    private final double delay = 0.0;


    private ConnectMessageContent(String senderAgentAbbrev, String receiverAgentAbbrev, String organizationAbbrev, String expectedMasterAbbrev, String message) {
        this.senderAgentAbbrev = senderAgentAbbrev;
        this.receiverAgentAbbrev = receiverAgentAbbrev;
        this.organizationAbbrev = organizationAbbrev;
        this.expectedMasterAbbrev = expectedMasterAbbrev;
        this.message = message;
    }

    /**
     * Constructs a new instance of an immutable message content for establishing connections between agents.
     *
     * @param senderAgentAbbrev    - the agent sending the message
     * @param receiverAgentAbbrev  - the agent receiving the message
     * @param organizationAbbrev   - the organization in which the agents will participate
     * @param expectedMasterAbbrev - the expected master of the organization
     * @param message              - the custom message content
     * @return - the immutable message content
     */
    public synchronized static IConnectMessageContent createConnectMessageContent(final String senderAgentAbbrev, final String receiverAgentAbbrev,
                                                                                  final String organizationAbbrev, final String expectedMasterAbbrev, final String message) {
        return new ConnectMessageContent(senderAgentAbbrev, receiverAgentAbbrev, organizationAbbrev, expectedMasterAbbrev, message);
    }

    @Override
    public double getDelay() {
        return delay;
    }

    /**
     * @return the expectedMasterAbbrev
     */
    @Override
    public String getExpectedMasterAbbrev() {
        return expectedMasterAbbrev;
    }

    /**
     * @return the message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @return the organizationAbbrev
     */
    @Override
    public String getOrganizationAbbrev() {
        return organizationAbbrev;
    }

    @Override
    public String toString() {
        return "ConnectMessageContent{" +
                "delay=" + delay +
                ", expectedMasterAbbrev='" + expectedMasterAbbrev + '\'' +
                ", messages='" + message + '\'' +
                ", organizationAbbrev='" + organizationAbbrev + '\'' +
                ", receiverAgentAbbrev='" + receiverAgentAbbrev + '\'' +
                ", senderAgentAbbrev='" + senderAgentAbbrev + '\'' +
                '}';
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
        stream.defaultWriteObject();
    }
}
