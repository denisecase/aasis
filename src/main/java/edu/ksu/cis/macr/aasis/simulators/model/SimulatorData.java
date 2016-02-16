/**
 * SimulatorData.java
 *
 * Copyright 2012 Kansas State University MACR Laboratory
 * http://macr.cis.ksu.edu/ Department of Computing & Information Sciences
 *
 * See License.txt file for the license agreement.
 *
 */
package edu.ksu.cis.macr.aasis.simulators.model;


import edu.ksu.cis.macr.obaa_pp.sensor.IRead;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Stores data for a display object  Uses a time slice date for
 * a key and stores meter readings in a HashTable
 */
public class SimulatorData implements Serializable {
    private static final long serialVersionUID = 730728011300902192L;
    // SmartMeterSensorsFrame Name
    private final String deviceName;
    // Meter IEgaugeAdaptible
    private HashMap<String, IRead> data;


    /*
     * Constructor, takes SmartMeterSensorsFrame deviceName as parameter and initializes
     * the HashMap data.
     */
    public SimulatorData(String deviceName) {
        this.deviceName = deviceName;
        data = new HashMap<>();
    }

    /*
     * Returns deviceName
     */
    public String getDeviceName() {
        return deviceName;
    }

    /*
     * Takes a date as parameter and returns meter read for date parameter.
     * Else returns null if date not found in
     */
    public IRead getReadByDate(final Date date) {
        final String d = date.toString();

        if (data.containsKey(d)) {
            return data.get(d);
        }

        return null;
    }
}
