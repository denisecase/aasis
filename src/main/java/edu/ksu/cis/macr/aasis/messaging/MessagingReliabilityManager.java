package edu.ksu.cis.macr.aasis.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * The {@code MessagingReliabilityManager} singleton provides support for simulating the impacts of messages reliability
 * and delays on the system behavior.
 */
public enum MessagingReliabilityManager {
    // TODO: Denise : specify implementation of custom delays and reliability.
    /**
     * Singleton instance of the MessagingReliabilityManager (one per JVM).
     */
    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(MessagingReliabilityManager.class);
    private static final boolean debug = false;
    private static double communicationDelay_millisecs = 0.0;
    private static double communicationReliability = 1.0;
    private static boolean isLoaded = Boolean.FALSE;

    public static double getCommunicationDelay() {
        if (!isLoaded) load();
        return communicationDelay_millisecs;
    }

    public static void load() {
        String curDir = System.getProperty("user.dir");
        String path = curDir + "/src/main/resources/configs/";

        // get Scenario file, name, and path from main instance.xml
        File configFile = new File(path + "messaging_reliability.xml");
        if (!configFile.exists()) {
            //TODO: Greg: Add the absolute path to the messaging_reliability.xml file to run.properties and the IPDS/AASIS RunManagers.
            curDir = curDir + "/aasis";          // Total hack
            path = curDir + "/src/main/resources/configs/";
            configFile = new File(path + "appconfig.xml");  // for IDEA
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ConfigurationException(e);
        }
        if (db == null) {
            throw new ConfigurationException();
        }
        Document configDoc = null;
        try {
            configDoc = db.parse(configFile);
        } catch (SAXException | IOException f) {
            throw new ConfigurationException(f);
        }

        Element docEle = configDoc.getDocumentElement();
        Element localDefaultReliability = (Element) docEle.getElementsByTagName("DefaultCommunicationReliability").item(0);
        Element localDefaultDelay = (Element) docEle.getElementsByTagName("DefaultCommunicationDelay_millisecs").item(0);
        Element localPowerReliability = (Element) docEle.getElementsByTagName("DefaultCommunicationReliability").item(0);
        Element localPowerDelay = (Element) docEle.getElementsByTagName("DefaultCommunicationDelay_millisecs").item(0);

        communicationReliability = getReliabilityValue(localDefaultReliability);
        communicationDelay_millisecs = getDelayValue(localDefaultDelay);
        double powerCommunicationReliability = getReliabilityValue(localPowerReliability);
        double powerCommunicationDelay_millisecs = getDelayValue(localPowerDelay);
        isLoaded = true;
    }

    private static Double getReliabilityValue(Element element) {
        try {
            String strValue = element.getAttribute("value");
            return Double.parseDouble(strValue);
        } catch (Exception ex) {
            LOG.info("Value not available.. using standard reliability.");
        }
        return 1.0;
    }

    private static Double getDelayValue(Element element) {
        try {
            String strValue = element.getAttribute("value");
            return Double.parseDouble(strValue);
        } catch (Exception ex) {
            LOG.info("Value not available.. using standard delay.");
        }
        return 0.0;
    }

    public static double getCommunicationReliability() {
        if (!isLoaded) load();
        return communicationReliability;
    }

    static class ConfigurationException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        ConfigurationException(Throwable cause) {
            super(cause);
        }

        ConfigurationException() {
            super();
        }
    }
}
