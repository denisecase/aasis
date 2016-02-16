package edu.ksu.cis.macr.aasis.simulator.scenario;

import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

/**
 * A scenario {@code IMessagingCheckpoint} provides a means to stop execution of a run after a set of messages has been
 * sent or received.
 */
public interface IMessagingCheckpoint<SenderAgentType extends Enum<?>, SenderRole extends UniqueIdentifier, ReceiverAgentType extends Enum<?>, ReceiverRole extends UniqueIdentifier> {
    void addRecieved(String sender, String receiver, long timeSlice);

    void addSent(String sender, String receiver, long timeSlice);

    /*
        Get the actual count of this type of message.
         */
    int getActualCountSent();

    /**
     * Set the actual count of this type of message.
     *
     * @param actualCount the actual count of this type of message.
     */
    void setActualCountSent(int actualCount);

    /*
  Get the total expected count of this type of message.
   */
    int getExpectedCount();

    /**
     * Set the expected count of this type of message.
     *
     * @param expectedCount the expected count of this type of message.
     */
    void setExpectedCount(int expectedCount);

    double getPercentComplete();

    /**
     * Get the tolerance for this being complete.  A tolerance of 1.0 means all messages must be received to be considered
     * complete, 0.95 means 95% percoent of the messages must succeed.
     *
     * @return - the tolerance multiplier for qualifying as complete.
     */
    double getTolerance();

    /**
     * Set the tolerance for this being complete.  A tolerance of 1.0 means all messages must be received to be considered
     * complete.
     *
     * @param tolerance -  the tolerance multiplier for this being complete.
     */
    void setTolerance(double tolerance);

    boolean isComplete();
}
