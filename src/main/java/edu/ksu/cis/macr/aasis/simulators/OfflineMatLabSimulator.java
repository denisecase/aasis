package edu.ksu.cis.macr.aasis.simulators;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import edu.ksu.cis.macr.aasis.simulator.clock.Clock;
import edu.ksu.cis.macr.obaa_pp.actuator.ISetting;
import edu.ksu.cis.macr.obaa_pp.sensor.IRead;
import edu.ksu.cis.macr.obaa_pp.sensor.Read;
import edu.ksu.cis.macr.obaa_pp.sensor.SensorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code OfflineMatLabSimulator} implements the {@code ISimulator} interface and is used to interact with devices
 * simulated in MatLab.
 */
public class OfflineMatLabSimulator implements ISimulator {
    final static String CUR_DIR = System.getProperty("user.dir");
    private static final Logger LOG = LoggerFactory.getLogger(OfflineMatLabSimulator.class);
    private static double[][] dataArray;
    private static String fullDataPathAndFile;
    private static String testCaseName;
    private boolean debug = false;


    public OfflineMatLabSimulator() {
    }

    /**
     * @return the dataArray
     */
    public static double[][] getDataArray() {
        return OfflineMatLabSimulator.dataArray;
    }

    /**
     * @param aDataArray the dataArray to set
     */
    public static void setDataArray(double[][] aDataArray) {
        OfflineMatLabSimulator.dataArray = aDataArray;
    }

    public static String getFullDataPathAndFile() {
        return fullDataPathAndFile;
    }

    public static void setFullDataPathAndFile(final String fullDataPathAndFile) {
        OfflineMatLabSimulator.fullDataPathAndFile = fullDataPathAndFile;
    }

    /**
     * @return the testCaseName
     */
    public static String getTestCaseName() {
        return testCaseName;
    }

    /**
     * @param aTestCaseName the testCaseName to set
     */
    public static void setTestCaseName(String aTestCaseName) {
        testCaseName = aTestCaseName;
    }

    @Override
    public String toString() {
        return "OfflineMatLabSimulator{" +
                "debug=" + debug +
                '}';
    }

    @Override
    public ArrayList<IRead<?>> getAllReadsAt(long timeSlice) {
        LOG.info("Getting all sensor readings from data adapter for time slice {}.", timeSlice);
        ArrayList<IRead<?>> allReads = new ArrayList<IRead<?>>();

        int numDataValueColumnsInSet = ColumnTranslator.getNumDataValues();
        int cursor = (int) timeSlice * numDataValueColumnsInSet;
        LOG.debug("cursor : {}", cursor);

        if (debug) {
            LOG.debug("cursor : {}", cursor);
            for (int r = 0; r < 62; r++) {
                for (int j = 0; j < 11; j++) {
                    LOG.debug("  data[{}][{}] : {}", r, j, getDataArray()[r][j]);
                }
            }
        }
        // TODO: Greg:  Replace hard-coded NUM_ROWS by reading it from the associated test case when using offline matlab data.
        // TODO: Physical simulation needs to provide NUM_ROWS to the data adapters.

        final int NUM_ROWS = 62;
//    for (int i = 0; i < NUM_ROWS; i++) {
//      IElectricalData current = new ElectricalData();
//      int n = 0;
//      current.setPhaseAPload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setPhaseAQload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setPhaseBPload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setPhaseBQload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setPhaseCPload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setPhaseCQload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setPgeneration(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setQgeneration(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setPhaseAvoltage(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setPhaseBvoltage(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//      current.setPhaseCvoltage(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//
//      IRead reading = new Read();
//      reading.setElectricalData(current);
//
//      if (timeSlice == 0) {
//        reading.setPreviousElectricalData(current);
//      } else {
//        cursor = ((int) timeSlice - 1) * numDataValueColumnsInSet;
//        IElectricalData previous = new ElectricalData();
//        n = 0;
//        previous.setPhaseAPload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setPhaseAQload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setPhaseBPload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setPhaseBQload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setPhaseCPload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setPhaseCQload(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setPgeneration(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setQgeneration(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setPhaseAvoltage(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setPhaseBvoltage(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        previous.setPhaseCvoltage(OfflineMatLabSimulator.getDataArray()[i][cursor + n++]);
//        reading.setPreviousElectricalData(previous);
//      }
//      IRead<?> reading = new Read<SensorType>();
//      reading.setSensorReadObject(reading);
//      allReads.add(reading);
//    }
        return allReads;
    }

    /**
     * Get the discrete reading for current time slice for given smart meter (maintained by the {@code Clock}).
     *
     * @param deviceName - the string name of the smart meter
     * @return {@code IRead} with the reading
     */
    @Override
    public IRead<?> getRead(String deviceName) {
        return getReadAt(deviceName, Clock.getTimeSlicesElapsedSinceStart());
    }

    @Override
    public IRead<?> getReadAt(String deviceName, long timeSlice) {
        if (timeSlice < 0) {
            return null;
        }
        if (deviceName.isEmpty()) {
            return null;
        }
        LOG.debug("GetDiscreteReading for {} at time slice {}", deviceName, timeSlice);

        int rowID = RowTranslator.getRowID(deviceName);
        if (rowID == -1) {
            LOG.error("Cannot find smart meter {}, please check configurations.", deviceName);
            System.exit(-1);
        }

        LOG.debug("GetDiscreteReading {} is at rowID {}", deviceName, rowID);

        int numDataValueColumnsInSet = ColumnTranslator.getNumDataValues();
        int cursor = (int) timeSlice * numDataValueColumnsInSet;
        if (debug) {
            LOG.debug("cursor : {}", cursor);
            for (int j = 0; j < 11; j++) {
                LOG.debug("  data[{}][{}] : {}", rowID, j, getDataArray()[rowID][j]);
            }
        }
        IRead<SensorType> reading = new Read<>();
        reading.setSensorReadObject(reading);
        return reading;
    }

    @Override
    public void initialize(String testCaseName, String fullDataPathAndFile) {
        LOG.info("\tUsing offline MatLab data adapter: {} for {}", fullDataPathAndFile, testCaseName);
        setFullDataPathAndFile(fullDataPathAndFile);
        setTestCaseName(testCaseName);
        File file;
        try {
            file = new File(fullDataPathAndFile);
            LOG.info("\tFile: {}", file.toString());
            //LOG.info("   File.exists {}", file.exists());
            MatFileReader matFileReader = new MatFileReader(file);
            //LOG.info("MatFileReader: {}", matFileReader.toString());
            MLDouble mlDouble = (MLDouble) matFileReader.getMLArray("OUTPUT");
            OfflineMatLabSimulator.setDataArray(mlDouble.getArray());
        } catch (FileNotFoundException ex) {
            LOG.error("FileNotFoundException Error intializing offline smartmeter adapter.");
            System.exit(-1);
        } catch (IOException ex) {
            LOG.error("IOException Error intializing offline smartmeter adapter. ");
            System.exit(-1);
        }
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    /**
     * Updates the sensor matrix to reflect new settings such as Smart Inverter Reactive Power settings. Only the live
     * smartmeter can do this, both offline smartmeter and csv will not react to control actions.
     *
     * @param deviceName - the name of the associated smart meter, e.g. SM-44 set in matlab_smartmeter_row.properties
     * @param timeSlice  - confirming the time slice to be updated
     * @param setting    - the information about the action to be taken.
     */
    @Override
    public boolean issueControlAction(final String deviceName, final long timeSlice,
                                      final ISetting<?> setting) {
        boolean success = false;
        if (debug) LOG.debug("Not implemented in the offline MatLab adapter.");
        return success;
    }

    /**
     * Updates the sensor matrix to reflect new settings such as Smart Inverter Reactive Power settings. Only the live
     * smartmeter can do this, both offline smartmeter and csv will not react to control actions.
     *
     * @param deviceName - the name of the associated smart meter, e.g. SM-44 set in matlab_smartmeter_row.properties
     * @param timeslice  - confirming the timeslice to be updated
     * @param read       - the full two-part reading (electrical data for previous and this timeslice.)
     * @return - true if updated, false if not updated
     */
    public boolean issueControlAction(final String deviceName, final long timeslice, final IRead read) {
        return false;
    }
}
