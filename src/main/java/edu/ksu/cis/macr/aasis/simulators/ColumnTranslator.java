package edu.ksu.cis.macr.aasis.simulators;

import edu.ksu.cis.macr.aasis.config.RunManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

/**
 * Singleton class that reads the list of data values along with their corresponding column
 * identifiers in MatLab from a given column.properties file.
 */
public enum ColumnTranslator {
    /**
     * Singleton instance of the electrical data column translator (one per JVM).
     */
    INSTANCE;


    static final String CUR_DIR = System.getProperty("user.dir");
    private static final String FILENAME = "electrical_data_value_column.properties";
    private static final Logger LOG = LoggerFactory.getLogger(ColumnTranslator.class);
    private static final boolean debug = false;
    private static boolean isLoaded = false;
    private static int numValues = -1;
    private static Properties properties = null;


    /**
     * Lookup the column number for a given electric value in the list of electrical data values.
     *
     * @param dataValueName - the name of the electrical property, e.g. "PhaseAPload"
     * @return - the integer column in the smart meter data file with data for that electrical value.
     */
    public static int getColID(final String dataValueName) {
        if (!isLoaded) {
            ColumnTranslator.load();
        }
        if (ColumnTranslator.getNumDataValues() < 1) {
            return ColumnTranslator.getNumDataValues();
        }
        String strCol = ColumnTranslator.properties.getProperty(dataValueName);
        return Integer.parseInt(strCol);
    }

    /**
     * Read in the list of electrical properties and their associated column numbers in the sensor data file.
     */
    public static void load() {
        setProperties(new Properties());
        File f = new File(RunManager.getAbsolutePathToConfigsFolder() + "/standardproperties", FILENAME);
        LOG.info(" READING ELECTRICAL DATA VALUES INFORMATION .......... from {}", f.getAbsolutePath());

        try {
            try (FileInputStream fileInputStream = new FileInputStream(f)) {
                properties = new Properties();
                properties.load(fileInputStream);
            }
            Enumeration enuKeys;
            final Iterator<Object> iterator = properties.keySet().iterator();
            numValues = 0;
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = properties.getProperty(key);
                if (debug) LOG.debug("\t{}: {}", key, value);
                numValues++;
            }
            isLoaded = true;
        } catch (FileNotFoundException e) {
            LOG.error("ERROR: {} file not found.", FILENAME);
            System.exit(-5);
        } catch (IOException e) {
            LOG.error("ERROR: {} file - error reading contents.", FILENAME);
            System.exit(-6);
        }
    }

    /**
     * Set the properties from a given set.
     *
     * @param properties the given set of properties
     */
    private static void setProperties(final Properties properties) {
        ColumnTranslator.properties = properties;
    }

    /**
     * Get the total number of entries in this file.
     *
     * @return int - the number of properties specified
     */
    public static int getNumDataValues() {
        if (!isLoaded) {
            ColumnTranslator.load();
        }
        return ColumnTranslator.numValues;
    }
}
