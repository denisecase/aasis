package edu.ksu.cis.macr.aasis.messaging;

/**
 * Interface for the various messaging managers; different types of organizations may have different means of communication.
 */
public interface IMessagingManager {
    /**
     * Return a string that can be added to the agent-to-agent part of the routing key to group messages with a common focus.
     *
     * @param focus - the type or focus of this message
     * @return - a String that will be used as a pre or post fix as part of the queue name.
     */
    //  static String getQueueFocus(final IMessagingFocus focus);
}
