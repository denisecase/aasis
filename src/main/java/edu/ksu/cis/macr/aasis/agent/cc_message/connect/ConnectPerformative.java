package edu.ksu.cis.macr.aasis.agent.cc_message.connect;

/**
 * Performatives indicate a primary characteristic of the message and may be used to route behavior in plans.
 * It indicates the types of messages exchanged between agents wanting
 * to establish a valid connection and begin operating in a shared organization.
 */
public enum ConnectPerformative {
    /**
     * An initial query to initiate a connection.
     */
    SENDING_HELLO,
    /**
     * Reply to the initial query to initiate a connection.
     */
    SENDING_HELLO_REPLY,
    /**
     * Request to participate in given organization.
     */
    CONNECT_REQUEST,
    /**
     * Reply to a request to participate in given organization.
     */
    CONNECT_CONFIRM
}
