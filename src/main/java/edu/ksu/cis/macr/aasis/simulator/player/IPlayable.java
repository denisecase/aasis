package edu.ksu.cis.macr.aasis.simulator.player;

/**
 * Interface for runnable objects that can be controlled by the {@code Player} singleton.
 */
public interface IPlayable {
    boolean getPaused();

    void setPaused(boolean isPaused);

    long getStepDelayInMilliseconds();

    void setStepDelayInMilliseconds(long stepDelayInMilliseconds);

    Player.StepMode getStepMode();

    void setStepMode(Player.StepMode stepMode);

    /*
    * Pauses simulation and verifies that all files referenced in the model
    * exist within the project and are readable using the static
    * checkFile() method in this class.
    */
    void pause();

    void step();

    /*
   * Parses Agent Model and verifies that all files referenced in the model
   * exist within the project and are readable using the static
   * checkFile() method in this class.
   */
    void unpause();
}
