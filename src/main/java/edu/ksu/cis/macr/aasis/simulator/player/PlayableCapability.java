package edu.ksu.cis.macr.aasis.simulator.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@code PlayableCapability} provides its owner the ability interact with the {@code Player} singleton.
 */
public class PlayableCapability implements IPlayable {
    private static final Logger LOG = LoggerFactory.getLogger(
            PlayableCapability.class);
    private static final boolean debug = false;
    private final String identifierString;


    /**
     * Creates a new player capability for the owner specified in the identifier string.
     *
     * @param identifierString - the owner's unique name
     */
    public PlayableCapability(final String identifierString) {
        this.identifierString = identifierString;
        if (debug) LOG.debug("New {} {}", identifierString, this);
    }

    @Override
    public String toString() {
        return "PlayableCapability{" +
                "identifierString='" + identifierString +
                '}';
    }

    @Override
    public boolean getPaused() {
        return Player.getPaused();
    }

    @Override
    public void setPaused(boolean isPaused) {
        Player.setPaused(isPaused);
    }

    @Override
    public long getStepDelayInMilliseconds() {
        return Player.getStepDelayInMilliseconds();
    }

    @Override
    public void setStepDelayInMilliseconds(final long stepDelayInMilliseconds) {
        Player.setStepDelayInMilliseconds(stepDelayInMilliseconds);
    }

    /**
     * @return getStepMode - true if user must hit Enter to advance; false if simulation runs continuously
     */
    @Override
    public Player.StepMode getStepMode() {
        return Player.getStepMode();
    }

    @Override
    public void setStepMode(final Player.StepMode stepMode) {
        Player.setStepMode(stepMode);
    }

    @Override
    public void pause() {
        Player.setPaused(true);
    }

    @Override
    public void step() {
        Player.step();
    }

    @Override
    public void unpause() {
        Player.setPaused(false);
    }
}
