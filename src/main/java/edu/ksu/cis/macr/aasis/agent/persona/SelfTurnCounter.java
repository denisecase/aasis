package edu.ksu.cis.macr.aasis.agent.persona;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code SelfTurnCounter} singleton synchronizes the turn count across all self organizations.  If an agent
 * disconnects and restarts, it will join the simulation at the active turn count.
 */
public enum SelfTurnCounter {
    INSTANCE;
    private static final Logger LOG = LoggerFactory.getLogger(SelfTurnCounter.class);
    private static int numberDoneWithCurrentTurn = 0;
    private static int numberOfOrganizations = 0;
    private static long turns = 0;


    public static int getNumberDoneWithCurrentTurn() {
        return numberDoneWithCurrentTurn;
    }

    public static void setNumberDoneWithCurrentTurn(int numberDoneWithCurrentTurn) {
        SelfTurnCounter.numberDoneWithCurrentTurn = numberDoneWithCurrentTurn;
    }

    public static int getNumberOfOrganizations() {
        return SelfTurnCounter.numberOfOrganizations;
    }

    public synchronized static void setNumberOfOrganizations(int numberOfOrganizations) {
        LOG.info("Entering setNumberOfOrganizations(numberOfOrganizations={})",numberOfOrganizations);
        SelfTurnCounter.numberOfOrganizations = numberOfOrganizations;
    }

    /**
     * Get the current turn for all self organizations.
     *
     * @return - the current number of turns.
     */
    public static long getTurns() {
        return SelfTurnCounter.turns;
    }

    /**
     * Set current turn number
     *
     * @param turns - the current turn number
     */
    public synchronized static void setTurns(final long turns) {
        SelfTurnCounter.turns = turns;
    }
}
