package edu.ksu.cis.macr.aasis.agent.cc_p;

import edu.ksu.cis.macr.aasis.simulator.clock.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.GregorianCalendar;
import java.util.TreeSet;

/**
 * The domain model that keeps track of the established connections between agent self organizations.
 */
public enum ConnectionModel {
    INSTANCE;
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionModel.class);
    private static final boolean debug = false;
    private static TreeSet<String> connectionSet = new TreeSet<>();
    private static int countTotalInitialConnections;
    private static int numberOfAgents;
    private static GregorianCalendar simulationTime;
    private static int simulationTimeSlices;

    public synchronized static String getConnectionList() {
        StringBuilder s = new StringBuilder();
        ConnectionModel.connectionSet.forEach(s::append);
        return s.toString();
    }

    public static int getCountTotalInitialConnections() {
        // two parallel organizations - everyone except the top has exactly one parent.
        //TODO: refactor initial connection count to be scaleable
        countTotalInitialConnections = 2 * (ConnectionModel.getNumberOfAgents() - 1);
        return countTotalInitialConnections;
    }

    public static void setCountTotalInitialConnections(int countTotalInitialConnections) {
        ConnectionModel.countTotalInitialConnections = countTotalInitialConnections;
    }

    public static String getLayoutImageSource() {
        String layoutImageSource = "/src/main/java/edu/ksu/cis/macr/aasis/organization/views/images" +
                "/01nodemap.png";
        return layoutImageSource;
    }

    public static int getNumberOfAgents() {
        return numberOfAgents;
    }

    public static void setNumberOfAgents(int numberOfAgents) {
        ConnectionModel.numberOfAgents = numberOfAgents;
    }

    public static GregorianCalendar getSimulationTime() {
        return ConnectionModel.simulationTime;
    }

    public static void setSimulationTime(final GregorianCalendar simulationTime) {
        LOG.debug("setSimulationTime={}", simulationTime.toZonedDateTime().toString());
        ConnectionModel.simulationTime = simulationTime;
    }

    public static int getSimulationTimeSlices() {
        return ConnectionModel.simulationTimeSlices;
    }

    private synchronized static void setSimulationTimeSlices(final int simulationTimeSlices) {
        LOG.debug("Entering setSimulationTimeSlices(simulationTimeSlices={})", simulationTimeSlices);
        ConnectionModel.simulationTimeSlices = simulationTimeSlices;
    }

    public static String getSummaryString() {
        final int parents = ConnectionModel.getCountTotalInitialConnections();
        int Hnum = ConnectionModel.getCountHomeConnections();
        int Nnum = ConnectionModel.getCountNeighborhoodConnections();
        int Lnum = ConnectionModel.getCountLateralConnections();
        int Fnum = ConnectionModel.getCountFeederConnections();
        int num = ConnectionModel.getConnectionSet().size();
        int missing = parents - num;
        return num + " of " + parents + " connections established. Missing " + missing + ". H/N/L/F=" + Hnum + "-" + Nnum + "-" + Lnum + "-" + Fnum;
    }

    public static int getCountHomeConnections() {
        int count = 0;
        for (String s : ConnectionModel.getConnectionSet()) {
            if (s.startsWith("H")) count += 1;
        }
        return count;
    }

    public static TreeSet<String> getConnectionSet() {
        return ConnectionModel.connectionSet;
    }

    public static int getCountNeighborhoodConnections() {
        int count = 0;
        for (String s : ConnectionModel.getConnectionSet()) {
            if (s.startsWith("N")) count += 1;
        }
        return count;
    }

    public static int getCountLateralConnections() {
        int count = 0;
        for (String s : ConnectionModel.getConnectionSet()) {
            if (s.startsWith("L")) count += 1;
        }
        return count;
    }

    public static int getCountFeederConnections() {
        int count = 0;
        for (String s : ConnectionModel.getConnectionSet()) {
            if (s.startsWith("F")) count += 1;
        }
        return count;
    }

    public static boolean includes(final String connection) {
        LOG.debug("Checking to see if connection list already has {}.", connection);
        return ConnectionModel.getConnectionList().contains(connection);
    }

    public synchronized static void insertNewConnection(final String connection) {
        LOG.info("UPDATE: CONNECTION established: {}.", connection);
        ConnectionModel.connectionSet.add(connection);
    }

    public synchronized static void updateConnectionModel() {
        LOG.debug("Entering updateConnectionModel()");
        setSimulationTime(Clock.getSimulationTime());
        setSimulationTimeSlices(Clock.getTimeSlicesElapsedSinceStart());
        LOG.debug("update connection model complete.");
    }
}
