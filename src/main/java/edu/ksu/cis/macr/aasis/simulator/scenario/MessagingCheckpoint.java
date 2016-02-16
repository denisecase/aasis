package edu.ksu.cis.macr.aasis.simulator.scenario;


import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A scenario {@code MessagingCheckpoint} provides a means to stop execution of a run after a given condition has been
 * reached.
 */
public class MessagingCheckpoint<S extends Enum<?>, SenderRole extends UniqueIdentifier, R extends Enum<?>, ReceiverRole extends UniqueIdentifier> implements IMessagingCheckpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MessagingCheckpoint.class);
    private static final boolean debug = false;
    private int expectedCount;
    private int actualCountSent;
    private S senderAgentType;  // e.g. HomeAgent
    private R receiverAgentType;
    private UniqueIdentifier senderRole;  // e.g. be sub role or self management role
    private UniqueIdentifier receiverRole;
    private int actualCountReceived;
    private double tolerance;


    private MessagingCheckpoint(S senderAgentType, R receiverAgentType, UniqueIdentifier senderRole, UniqueIdentifier receiverRole) {
        this.senderAgentType = senderAgentType;
        this.receiverAgentType = receiverAgentType;
        this.senderRole = senderRole;
        this.receiverRole = receiverRole;
    }

    public static <S extends Enum<?>, SenderRole extends UniqueIdentifier, R extends Enum<?>, ReceiverRole extends UniqueIdentifier>
    MessagingCheckpoint<S, SenderRole, R, ReceiverRole> createMessagingCheckpoint(S senderAgentType, R receiverAgentType, UniqueIdentifier senderRole, UniqueIdentifier receiverRole) {
        return new MessagingCheckpoint<>(senderAgentType, receiverAgentType, senderRole, receiverRole);
    }

    public int getActualCountReceived() {
        return actualCountReceived;
    }

    public void setActualCountReceived(int actualCountReceived) {
        this.actualCountReceived = actualCountReceived;
    }

    @Override
    public int getActualCountSent() {
        return actualCountSent;
    }

    @Override
    public void setActualCountSent(int actualCountSent) {
        this.actualCountSent = actualCountSent;
    }

    @Override
    public int getExpectedCount() {
        return expectedCount;
    }

    @Override
    public void setExpectedCount(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public void addRecieved(String sender, String receiver, long timeSlice) {
        final int prior = this.actualCountReceived;
        setActualCountReceived(prior + 1);
    }

    @Override
    public void addSent(String sender, String receiver, long timeSlice) {
        final int prior = this.actualCountReceived;
        setActualCountReceived(prior + 1);
    }

    @Override
    public double getPercentComplete() {
        return (double) actualCountSent / (double) expectedCount;
    }

    @Override
    public boolean isComplete() {
        return (actualCountSent >= expectedCount * tolerance);
    }
}
