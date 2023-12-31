/**
 * PhysicalSystemSimulator.java
 *
 * Copyright 2012 Kansas State University MACR Laboratory
 * http://macr.cis.ksu.edu/ Department of Computing & Information Sciences
 *
 * See License.txt file for the license agreement.
 *
 */
package edu.ksu.cis.macr.aasis.simulators;

import edu.ksu.cis.macr.aasis.config.RunManager;
import edu.ksu.cis.macr.aasis.simulator.clock.Clock;
import edu.ksu.cis.macr.aasis.simulator.player.Player;
import edu.ksu.cis.macr.aasis.simulators.view.SimulatorView;
import edu.ksu.cis.macr.obaa_pp.actuator.ISetting;
import edu.ksu.cis.macr.obaa_pp.sensor.IRead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * The {@code PhysicalSystemSimulator} initializes adapters to simulate the sensors and actuators interfacing with the
 * adaptive system.  Ours is set to first try to connect to MatLab. If MatLab is available, it will get the first time
 * slice of data from the smart meter simulator as an array with as many rows as there are node locations (assumed sensors)
 * in the system, each of which corresponds to a particular device location. There are as many columns as there are sensed
 * data items for each node.  For example, in our IEEE 62-node test case, there will be 62 rows in the array and 11
 * adapter values:  1 PA Column 1 - Phase A P load 2 QA Column 2 - Phase A Q load 3 PB Column 3 - Phase B P load 4 QB
 * Column 4 - Phase B Q load 5 PC Column 5 - Phase C P load 6 QC Column 6 - Phase C Q load 7 PG Column 7 - P (generation) 8
 * QG Column 8 - Q (generation) 9 VA Column 9 - Voltage Phase A 10 VB Column 10 - Voltage Phase B 11 VC Column 11 - Voltage
 * Phase C  If a functioning MatLab instance is not used, the class will either read from a static smartmeter array
 * based on the desired row number and "current" timeSlice OR will look in the current simulation folder for .csv adapter
 * templates for each Smart Meter - For each Smart Meter name there needs to be a .csv file (i.e. SmartMeter02.csv) in the
 * simulation folder. In order to use this adapter the .csv template will be used to generate a .csv file in the adapter/
 * folder for each smart meter with updated date and times.  A singleton means there will be one instance per JVM.
 */
public enum PhysicalSystemSimulator {
    /**
     * Singleton instance of the overall physical system simulator (one per JVM).
     */
    INSTANCE;


    private static final Logger LOG = LoggerFactory.getLogger(PhysicalSystemSimulator.class);
    private static final int MAX_TIME_SLICES = 80000;
    private static final boolean debug = false;
    private static ISimulator adapter = null;
    private static SimulatorView sensorFrame;
    private static long timeSlice;


    /**
     * Actuator capabilities call their action on the {@code PhysicalSystemSimulator} and do not need to know which adapter is
     * being used to simulate the action on the environment.
     *
     * @param actuatorName - the name of the actuator taking the action
     * @param timeSlice    - the number of timeSlices elapsed since the simulation began
     * @param setting      - the setting information describing the action
     */
    public static void actAt(final String actuatorName, final long timeSlice, final ISetting<?> setting) {
        if (debug)
            LOG.debug("Issuing control action for {} at {} given {}", actuatorName, timeSlice, setting.toString());
        // change smart inverter name to smart meter name so the PSS can look it up in matlab_smartmeter_row.properties
        String smartMeterName = getSmartMeterNameFromSmartInverterName(actuatorName);
        adapter.issueControlAction(smartMeterName, timeSlice, setting);
    }

    private static String getSmartMeterNameFromSmartInverterName(final String smartInverterName) {
        return smartInverterName.replace('I', 'M');
    }

    public static IRead<?> getRead(String smartMeterName) {
        return adapter.getReadAt(smartMeterName, Clock.getTimeSlicesElapsedSinceStart());
    }

    public static IRead<?> getReadAt(String smartMeterName, long timeSlice) {
        if (debug) LOG.debug("Calling senseAt at timeSlice {} for {} ", timeSlice, smartMeterName);
        if (adapter == null) {
            LOG.error("Simulator does not have a valid adapter.");
            System.exit(-1);
        }
        return adapter.getReadAt(smartMeterName, timeSlice);
    }

    /**
     * @return the testCase
     */
    private static String getTestCase() {
        final String testCase = RunManager.getTestCaseName();
        return testCase;
    }

    /**
     * @return the timeSlice
     */
    public static long getTimeSlice() {
        return timeSlice;
    }

    /**
     * @param inputTimeSlice the timeSlice to set
     */
    public static void setTimeSlice(long inputTimeSlice) {
        timeSlice = inputTimeSlice;
    }

    /**
     * Runs a simulated time slice progression depicting simulated sensors and actuators.
     *
     * @param args - String array of arguments
     * @throws IOException
     * @throws ParseException
     */
    public static void main(final String args[]) throws IOException, ParseException {
        // get the user-defined settings for this simulation
        RunManager.load();

        // read the configuration information and attach the adapter specified (e.g. local instance of  matlab)
        PhysicalSystemSimulator.initialize();

        // set the number of timeSlices to process (if 0, then use some reasonable max)
        int max = MAX_TIME_SLICES;
        if (Clock.getMaxTimeSlices() > 0) {
            max = Clock.getMaxTimeSlices();
        }
        LOG.info("Will run for {} timeSlices.", max);

        // create and display the associated view (same as launcher)
        createAndShowSensorData();
        step();

        // get new data and update the display once for each time slice
        for (int t = 1; t <= max; t++) {
            Clock.setTimeSlicesElapsedSinceStart(t);
            getSensorDataAndUpdateDisplay(t);   // called by RunManager when running Launcher
            step();
        }
        printDashedLine();
    }

    public static void initialize() {
        LOG.info("INITIALIZING PHYSICAL SYSTEM SIMULATOR ......................................");
        if (RunManager.getUseLiveMatLab() && adapter == null) {
            adapter = new MatLabSimulator();
            if (!adapter.isConnected()) {
                LOG.error("ERROR: Attempting to use live smartmeter. Cannot connect to smartmeter adapter. Use an offline source or try again.");
                System.exit(-1);
            }
        } else {
            try {
                adapter = new OfflineMatLabSimulator();
                adapter.initialize(RunManager.getTestCaseName(), RunManager.getAbsolutePathToSensorDataFile());
            } catch (Exception e) {
                LOG.error("ERROR: IO exception trying to get new OfflineMatLabSmartMeterSimulator.");
                System.exit(-1);
            }
            //  adapter.initialize(Scenario.getTestCaseName(), Scenario.getAbsolutePathToSensorDataFile());
        }
        LOG.info("Creating data simulation for {}.................", getTestCase());
        LOG.info("\t'useLiveMatLab' is set to {}.", RunManager.getUseLiveMatLab());
        LOG.info("\tPhysicalSystemSimulator.getDatafileandpath()is {}", RunManager.getAbsolutePathToSensorDataFile());
        LOG.info("Data simulation initialization for {} complete.", RunManager.getTestCaseName());
    }

    /**
     * Create and show the associated sensor data.
     */
    public static void createAndShowSensorData() {
        ArrayList<IRead<?>> allReadings = PhysicalSystemSimulator.getAllSensorReadings(0);
        LOG.info("There are {} sensor readings available in this test case.", allReadings.size());
    }

    /**
     * Returns an array list of SensorReadings of a given type.
     *
     * @param timeSlice - the integer number of timeSlices elapsed
     * @return {@code ArrayLis} of {@code IRead} objects
     */
    public static ArrayList<IRead<?>> getAllSensorReadings(long timeSlice) {
        if (adapter == null) {
            LOG.error("Simulator does not have a valid adapter.");
            System.exit(-1);
        }
        LOG.info("Getting all sensor readings at time slice {}.", timeSlice);
        ArrayList<IRead<?>> arr = null;
        try {
            arr = adapter.getAllReadsAt(timeSlice);
        } catch (Exception e) {
            LOG.error("ERROR: could not getAllReadsAt at timeSlice {} ", timeSlice);
            System.exit(-1);
        }
        if (debug) LOG.debug("Sensor readings: {}", arr);
        return arr;
    }

    /**
     * Get new data for this time slice from MatLab and update the sensor data display window.  Each SmartMeterRead has a
     * partial set of new {@code ElectricalData} plus the full set of Electrical data for the prior time slice.  Called
     * from Run Manager once all PV smart inverter control actions have been received.
     *
     * @param timeSlice - the number of timeSlices elapsed since the simulation began
     * @throws ParseException
     */
    public static void getSensorDataAndUpdateDisplay(final long timeSlice) throws ParseException {
        LOG.debug("Getting data for time slice {} of {}.", timeSlice, Clock.getMaxTimeSlices());

        // set my timeSlice to the one provided
        PhysicalSystemSimulator.timeSlice = timeSlice;

        // get updated list of SmartMeterSensorReadings for all nodes from the user-specified adapter.
        // Each SmartMeterRead has a partial set of new ElectricalData plus the full set of Electrical
        // data for the prior time slice.
        ArrayList<IRead<?>> newData = getAllSensorReadings(timeSlice);
        if (debug) LOG.debug("new data to display is {}", newData);

        // log results for the 4 hard-coded PV nodes
        if (timeSlice > 0) {
            //   retrieveAndLogPhaseASensorDataForAllFourPVNodes(timeSlice, 44 - 1, newData);
        }

        // update the Sensor Data display frame with the new data
//    if (sensorFrame != null && newData != null) {
//      sensorFrame.updateData(new SmartMeterSimulatorTableModel(newData));
//      sensorFrame.setTimeSliceNumber(timeSlice);
//      sensorFrame.setSimulationTime(Clock.getSimulationTime());
//    }
    }

    private static void step() {
        Player.step();
    }

    private static void printDashedLine() {
        LOG.info("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}
