package edu.ksu.cis.macr.aasis.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The primary focus of a targeted type of communications and messaging used in the simulation.
 */
public enum MessagingFocus implements IMessagingFocus {
    AGENT_INTERNAL(0),
    /**
     * Special exchange for general inter-agent communication.
     */
    GENERAL(1),
    GENERAL_PARTICIPATE(2);


    private static final Logger LOG = LoggerFactory.getLogger(MessagingFocus.class);
    private final int value;


    MessagingFocus(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of the type.
     *
     * @return - the integer value (1 is the top level of the hierarchy)
     */
    public int getIntegerValue() {
        return this.value;
    }
}
