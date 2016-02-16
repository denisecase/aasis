package edu.ksu.cis.macr.aasis.simulator.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Provides the ability to start, step, and stop the simulation as it plays.  This is just
 * an initial start. The {@code Player} describes the way the simulation should play. <p> Set
 * maxtimeslices to zero to run continuously.
 */
public enum Player {
    /**
     * Singleton instance of the player (one per JVM).
     */
    INSTANCE;

    // only one instance of player per JVM
    private static final Logger LOG = LoggerFactory.getLogger(Player.class);
    private static final boolean debug = false;
    private static boolean isPaused = false;
    private static long stepDelayInMilliseconds = 100L;  // start with no delay
    private static StepMode stepMode = StepMode.RUN_CONTINUOUSLY;


    /**
     * Contstruct the instance of this class.
     */
    Player() {
        setStepMode(StepMode.RUN_CONTINUOUSLY);
        setPaused(false);
    }

    /**
     * Find out if the simulation is currently paused.
     *
     * @return - true if paused, false if running.
     */
    public static boolean getPaused() {
        return Player.isPaused;
    }

    /**
     * Pause or unpause the simulation.
     *
     * @param isPaused - true to pause the simulation, false to start it running again.
     */
    public static void setPaused(final boolean isPaused) {
        Player.isPaused = isPaused;
    }

    /**
     * Triggers one step.  If in StepMode.RUN_CONTINUOUSLY, it will pause for the length of the stepDelayInMilliseconds.
     * If in StepMode.STEP_BY_STEP, it will wait for the user to advance.
     */
    public static void step() {
        if (Player.getStepMode() == StepMode.STEP_BY_STEP) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("\nStep mode is ON. Press the enter key to continue");
                scanner.nextLine();
            }
        } else if (Player.getStepDelayInMilliseconds() > 0) {
            try {
                if (debug) LOG.debug("________________________________________________________________\n");
                Thread.sleep(Player.getStepDelayInMilliseconds());
            } catch (InterruptedException ex) {
                LOG.error("Error - invalid step delay.  {}", ex.getMessage());
            }
        }
    }

    /**
     * Set whether the simulation should run continously or step-by-step, advancing when the user inidicates.
     *
     * @return - either StepMode.CONTINOUSLY or StepMode.STEP_BY_STEP
     */
    public static StepMode getStepMode() {
        return Player.stepMode;
    }

    /**
     * Set whether the simulation should run continously or step-by-step, advancing when the user inidicates.
     *
     * @param stepMode - the mode to set
     */
    public static void setStepMode(final StepMode stepMode) {
        Player.stepMode = stepMode;
    }

    /**
     * Get the time each step should pause when running continuously.
     *
     * @return long - the step delay in milliseconds
     */
    public static long getStepDelayInMilliseconds() {
        return Player.stepDelayInMilliseconds;
    }

    /**
     * Set the time each step should pause when running continuously.  Has no effect when running in manual step mode.
     *
     * @param stepDelayInMilliseconds - the desired step delay in milliseconds
     */
    public static void setStepDelayInMilliseconds(final long stepDelayInMilliseconds) {
        Player.stepDelayInMilliseconds = stepDelayInMilliseconds;
    }

    /*
    * Parses Agent Model and verifies that all files referenced in the model
    * exist within the project and are readable using the static
    * checkFile() method in this class.
    */
    public void pause() {
        Player.setPaused(true);
    }

    /*
     * Parses Agent Model and verifies that all files referenced in the model
     * exist within the project and are readable using the static
     * checkFile() method in this class.
     */
    public void unpause() {
        Player.setPaused(false);
    }

    /**
     * This enum describes how the simulation will proceed.
     */
    public enum StepMode {
        /**
         * Wait for the user to advance.
         */
        STEP_BY_STEP,
        /**
         * Run continously.  If stepDelayInMilliseconds  is greater than 0, it will pause for that length of
         * time automatically during each step.
         */
        RUN_CONTINUOUSLY
    }
}
