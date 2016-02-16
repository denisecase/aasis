package edu.ksu.cis.macr.aasis.simulator.clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The {@code Clock} singleton provides information about the date and time of the simulated world, based on current time
 * slice (units of time since the simuation began) maintained by the {@code Player} singleton.
 */
public enum Clock {
    /**
     * Singleton instance of the clock (one per JVM).
     */
    INSTANCE;


    private static final Logger LOG = LoggerFactory.getLogger(Clock.class);
    private static final boolean debug = false;
    private static int lengthOfTimesliceInMilliseconds;
    private static int maxTimeSlices;
    private static GregorianCalendar simulationStartTime;
    private static GregorianCalendar simulationTime;
    private static int timeSlicesElapsedSinceStart;


    /**
     * Set the amount of time in milliseconds that passes with each sensor time slice in the simulation.
     *
     * @return - the length of time in milliseconds
     */
    private static int getLengthOfTimesliceInMilliseconds() {
        return lengthOfTimesliceInMilliseconds;
    }

    /**
     * Set the amount of time in milliseconds that passes with each sensor time slice in the simulation.
     *
     * @param lengthOfTimesliceInMilliseconds - the length of time in milliseconds
     */
    public static void setLengthOfTimesliceInMilliseconds(final int lengthOfTimesliceInMilliseconds) {
        Clock.lengthOfTimesliceInMilliseconds = lengthOfTimesliceInMilliseconds;
    }

    /**
     * Set the maximum number of time slices that the simulation will run before stopping.
     *
     * @return - the maximum number of time slices
     */
    public static int getMaxTimeSlices() {
        return maxTimeSlices;
    }

    /**
     * Set the maximum number of time slices that the simulation will run before stopping.
     *
     * @param maxTimeSlices - the maximum number of time slices
     */
    public static void setMaxTimeSlices(final int maxTimeSlices) {
        Clock.maxTimeSlices = maxTimeSlices;
    }

    /**
     * Get the current "real world" simulation time.
     *
     * @return the current "real world" simulation time
     */
    public static GregorianCalendar getSimulationTime() {
        GregorianCalendar start = getSimulationStartTime();
        int timeslices = getTimeSlicesElapsedSinceStart();
        int len = getLengthOfTimesliceInMilliseconds();
        int elapsedMilliseconds = timeslices * len;
        //if (debug) LOG.debug("That's {} ms, or {} seconds that have passed.", Integer.valueOf(elapsedMilliseconds),
        // Double.valueOf(elapsedMilliseconds / 1000.));
        start.add(Calendar.MILLISECOND, elapsedMilliseconds);
        if (debug) LOG.debug("The new time is {}.", start.getTime());
        return (GregorianCalendar) start.clone();
    }

    /**
     * Set the current "real world" simulation time.
     *
     * @param simulationTime - the current "real world" simulation time
     */
    public static void setSimulationTime(final GregorianCalendar simulationTime) {
        Clock.simulationTime = simulationTime;
    }

    /**
     * Get the simulation "real world" date and time at the start of the simulation.
     *
     * @return - the simluation start date and time
     */
    public static GregorianCalendar getSimulationStartTime() {
        return (GregorianCalendar) simulationStartTime.clone();
    }

    /**
     * Set the simulation "real world" date and time at the start of the simulation.
     *
     * @param simulationStartTime - the simluation start date and time
     */
    public static void setSimulationStartTime(final GregorianCalendar simulationStartTime) {
        Clock.simulationStartTime = simulationStartTime;
    }

    /**
     * Get the number of time slices that have elapsed since the simulation began.
     *
     * @return the timeSlicesElapsedSinceStart
     */
    public static int getTimeSlicesElapsedSinceStart() {
        return timeSlicesElapsedSinceStart;
    }

    /**
     * Set the number of time slices that have elapsed since the simulation began.
     *
     * @param timeSlicesElapsedSinceStart the timeSlicesElapsedSinceStart to set
     */
    public static void setTimeSlicesElapsedSinceStart(final int timeSlicesElapsedSinceStart) {
        if (debug) LOG.debug("Time slice was {}.", Clock.getTimeSlicesElapsedSinceStart());
        Clock.timeSlicesElapsedSinceStart = timeSlicesElapsedSinceStart;
        if (debug) LOG.debug("Time slice updated to {}.", Clock.getTimeSlicesElapsedSinceStart());
    }

    @Override
    public String toString() {
        return String.format("Clock{INSTANCE=%s, simulationStartTime=%s, simulationTime=%s, " +
                        "timeSlicesElapsedSinceStart=%d, maxTimeSlices=%d, lengthOfTimesliceInMilliseconds=%d}", INSTANCE,
                simulationStartTime, simulationTime, timeSlicesElapsedSinceStart, maxTimeSlices,
                lengthOfTimesliceInMilliseconds);
    }
}
