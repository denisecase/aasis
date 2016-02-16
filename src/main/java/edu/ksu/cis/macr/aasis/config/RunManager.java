package edu.ksu.cis.macr.aasis.config;

import edu.ksu.cis.macr.aasis.messaging.MessagingFocus;
import edu.ksu.cis.macr.aasis.simulator.clock.Clock;
import edu.ksu.cis.macr.aasis.simulator.player.Player;
import edu.ksu.cis.macr.aasis.types.IAgentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Reads the information provided in the run.properties file when the system first starts. Use this to add new new simulation variables and avoid hard-coding 'magic
 * user-defined constants as needed. Variables should be in all lower case letters. Do not use quotations. All properties are
 * automatically read as strings. Do not include empty spaces after the equal signs.Conversions from strings to ints,
 * arrays, etc should be done here when possible. If additional information is needed, then just pass along the string (less preferred). A singleton means there
 * will be one instance per JVM.
 */
public enum RunManager {
    /**
     * Singleton instance of the run manager that reads user settings for this run of the simulation (one per JVM).
     */
    INSTANCE;


    public static final boolean FORCE_STOP_AFTER_INITIALLY_CONNECTED = false;
    public static final double INITIAL_CONNECTION_THRESHOLD_FRACTION = 1.00;  // 1.00 means 100% must connect
    private static final String CUR_DIR = System.getProperty("user.dir");  // singleton - if you can count the instances of a class,
    private static final Logger LOG = LoggerFactory.getLogger(RunManager.class);
    private static String absolutePathToConfigsFolder;
    private static String absolutePathToLogConfigFile;
    private static String absolutePathToLogbackXMLFile;
    private static String absolutePathToMatLabCodeFolder;
    private static String absolutePathToMatLabDataFolder;
    private static String absolutePathToSourceFolder;
    private static String absolutePathToStandardAgentModelsFolder;
    private static String absolutePathToStandardOrganizationModelsFolder;
    private static String absolutePathToStandardPropertiesFolder;
    private static String absolutePathToTestCaseFolder;
    private static String absolutePathtoSensorDataFile;
    private static String absolutePathtoStaticLayoutImageFile;
    private static HashMap<IAgentType, ArrayList<String>> agentMap = new HashMap<>();
    private static boolean communicationInBrowser;
    private static final boolean debug = false;
    private static int deliveryCheckTime_ms = 500;
    private static boolean fullyConnected = false;
    private static boolean initializeGoalsInConstructors;
    private static boolean initiallyConnected = false;
    private static boolean isLoaded = Boolean.FALSE;
    private static boolean isStopped = false;
    private static int planningHorizonInMinutes;
    private static Properties properties;
    private static HashSet<String> registered = new HashSet<>();
    private static Boolean showSensors;
    private static int standardWaitTime_ms = 100;
    private static String testCaseName;
    private static String topGoal;
    private static int totalConnectionCount;
    private static Boolean useLiveMatLab;
    private static int messageDelayMinMS;
    private static int messageDelayMaxMX;
    private static int fractionMessagesDelayed;


    public static String getAbsolutePathToConfigsFolder() {
        return absolutePathToConfigsFolder;
    }

    public static String getAbsolutePathToLogbackXMLFile() {
        return RunManager.absolutePathToLogbackXMLFile;
    }

    public static String getAbsolutePathToMatLabCodeFolder() {
        return absolutePathToMatLabCodeFolder;
    }

    public static String getAbsolutePathToMatLabDataFolder() {
        return absolutePathToMatLabDataFolder;
    }

    public static String getAbsolutePathToSensorDataFile() {
        return absolutePathtoSensorDataFile;
    }

    public static String getAbsolutePathToStandardAgentGoalModel(IAgentType agentType) {
        LOG.info("Getting standard agent goal model for {}", agentType);
        String strFile;
        if (agentType.toString().endsWith("Agent")) {
            strFile = getAbsolutePathToStandardAgentModelsFolder() + "/" +
                    agentType.toString() + "GoalModel.goal";
            LOG.info("Goal model = {}", strFile);
        } else {
            strFile = getAbsolutePathToStandardAgentModelsFolder() + "/" +
                    agentType.toString() + "AgentGoalModel.goal";
            LOG.info("Agent Goal model = {}", strFile);
        }

        File f = new File(strFile);
        if (f.exists() && !f.isDirectory()) {
            if (debug) LOG.info("{} Standard agent goal model file is {}", agentType.toString(),
                    strFile);
        } else {
            LOG.error("ERROR: {} Standard agent goal model file {} not found. ",
                    agentType.toString(), strFile);
        }
        return strFile;
    }

    public static String getAbsolutePathToStandardAgentModelsFolder() {
        return absolutePathToStandardAgentModelsFolder;
    }

    public static String getAbsolutePathToStandardAgentRoleModel(IAgentType agentType) {
        LOG.info("Getting standard agent role model for {}", agentType);
        String strFile;
        if (agentType.toString().endsWith("Agent")) {
            strFile = getAbsolutePathToStandardAgentModelsFolder() + "/" +
                    agentType.toString() + "RoleModel.role";
            LOG.info("Role model = {}", strFile);
        } else {
            strFile = getAbsolutePathToStandardAgentModelsFolder() + "/" +
                    agentType.toString() + "AgentRoleModel.role";
            LOG.info("Agent Role model = {}", strFile);
        }
        File f = new File(strFile);
        if (f.exists() && !f.isDirectory()) {
            if (debug) LOG.info("{} Standard agent role model file is {}", agentType.toString(), strFile);
        } else {
            LOG.error("ERROR: {} Standard agent role model file {} not found. ",
                    agentType.toString(), strFile);
        }
        return strFile;
    }

    public static String getAbsolutePathToStandardOrganizationModelsFolder() {
        return absolutePathToStandardOrganizationModelsFolder;
    }

    public static String getAbsolutePathToStandardPropertiesFolder() {
        return absolutePathToStandardPropertiesFolder;
    }

    public static String getAbsolutePathToTestCaseFolder() {
        return absolutePathToTestCaseFolder;
    }

    public static String getAbsolutePathtoStaticLayoutImageFile() {
        return absolutePathtoStaticLayoutImageFile;
    }

    public static boolean getCommunicationInBrowser() {
        return communicationInBrowser;
    }

    public static void setCommunicationInBrowser(boolean commInBrowser) {
        communicationInBrowser = commInBrowser;
    }

    public static int getDeliveryCheckTime_ms() {
        return deliveryCheckTime_ms;
    }

    public synchronized static int getPlanningHorizonInMinutes() {
        return planningHorizonInMinutes;
    }

    public synchronized static Boolean getShowSensors() {
        return showSensors;
    }

    public static int getStandardWaitTime_ms() {
        return standardWaitTime_ms;
    }

    public static String getTestCaseName() {
        return testCaseName;
    }

    public static int getTotalConnectionCount() {
        // there are 2 parallel organizations
        totalConnectionCount = 2 * RunManager.getCountAllAgents() - 1;
        return totalConnectionCount;
    }

    public synchronized static int getCountAllAgents() {
        int total = 0;
        for (IAgentType t : RunManager.agentMap.keySet()) {
            total = total + RunManager.getAgentCountByType(t);
        }
        return total;
    }

    public static int getAgentCountByType(IAgentType agentType) {
        return RunManager.agentMap.get(agentType).size();
    }

    public static Boolean getUseLiveMatLab() {
        return useLiveMatLab;
    }

    public synchronized static boolean isFullyConnected() {
        return RunManager.fullyConnected;
    }

    public static boolean isInitializeGoalsInConstructors() {
        return RunManager.initializeGoalsInConstructors;
    }

    public static boolean isStopped() {
        return isStopped;
    }

    public static void load() {
        setProperties(new Properties());
        final File f = new File(CUR_DIR, "run.properties");
        LOG.info("Reading user-specified information for {}.", f.getAbsolutePath());

        // try loading from the current directory
        try {
            try (FileInputStream fileInputStream = new FileInputStream(f)) {
                properties = new Properties();
                properties.load(fileInputStream);
            }
            for (Object o : new TreeSet<>(properties.keySet())) {
                final String key = (String) o;
                final String value = properties.getProperty(key);
                LOG.info("\t Reading run property {}: {}", key, value);
            }
            isLoaded = true;
            initializeStepMode(getValue("stepmode"));
            initializeStepDelay(getValue("stepdelay"));
            initializeAbsolutePathToSourceFolder(getValue("sourcepath"));
            initializeAbsolutePathToLogConfigFile(getValue("logpath"));
            initializeAbsolutePathToConfigsFolder(getValue("configpath"));
            initializeSimulationStartTime(getValue("startdate"), getValue("starttime"));
            initializeAbsolutePathToMatLabCodeFolder(getValue("matlabcodepath"));
            initializeAbsolutePathToMatLabDataFolder(getValue("matlabdatapath"));
            initializeAbsolutePathToStandardAgentModelsFolder(getValue("standardagentmodelspath"));
            initializeAbsolutePathToStandardOrganizationModelsFolder(getValue("standardorganizationmodelspath"));
            initializeAbsolutePathToStandardPropertiesFolder(getValue("standardpropertiespath"));
            initializeAbsolutePathtoSensorDataFile(getValue("configpath"), getValue("datafile"));
            initializeTestCaseName(getValue("configpath"), getValue("testcase"));
            initializeGetAbsolutePathToTestCaseFolder(getValue("configpath"), getValue("testcase"));
            initializeUseLiveMatLab(getValue("uselivematlab"));
            initializeMaxTimeSlices(getValue("maxtimeslices"));
            initializeTopGoal(getValue("topgoal"));
            initializeLengthOfTimesliceInMilliseconds(getValue("lengthoftimesliceinmilliseconds"));
            initializePlanningHorizonInMinutes(getValue("planninghorizoninminutes"));
            initializeInitializeGoalsInConstructors(getValue("initializegoalsinconstructors"));
            initalizeCommunicationsInBrowser(getValue("showCommunicationsInBrowser"));
            initializeStaticLayoutImageResource(getValue("staticlayoutimageresource"));
            initializeShowSensors(getValue("showsensors"));
            initializeDeliveryCheckTime(getValue("deliverychecktimems"));
            initializeWaitTime(getValue("standardwaitms"));
            initializeMessageDelayMinMS(getValue("messagedelayminms"));
            initializeMessageDelayMaxMX(getValue("messagedelaymaxmx"));
            initializeFractionMessagesDelayed(getValue("fractionmessagesdelayed"));
        } catch (FileNotFoundException e) {
            LOG.error("Run properties file not found.");
        } catch (IOException e) {
            LOG.error("Run properties file - error reading contents.");
        }
    }

    /**
     * @param properties the properties to set
     */
    private static void setProperties(Properties properties) {
        RunManager.properties = properties;
    }

    private static void initializeStepMode(final String stepmode) {
        if (stepmode.trim().toLowerCase().equals(
                "yes") || stepmode.trim().toLowerCase().equals(
                "true") || stepmode
                .trim().toLowerCase().equals("on")) {
            Player.setStepMode(Player.StepMode.STEP_BY_STEP);
        }
    }

    private static void initializeStepDelay(final String stepdelay) {
        try {
            Player.setStepDelayInMilliseconds(Integer.parseInt(stepdelay.toLowerCase()));
        } catch (Exception e) {
            Player.setStepDelayInMilliseconds(0L);
        }
    }

    private static void initializeAbsolutePathToSourceFolder(final String sourcepath) {
        final String strFolder = CUR_DIR + sourcepath.trim();
        final String title = "source folder";
        RunManager.setAbsolutePathToSourceFolder(
                verifyFolderExists(strFolder, title));
    }

    public static void setAbsolutePathToSourceFolder(String absolutePathToSourceFolder) {
        RunManager.absolutePathToSourceFolder = absolutePathToSourceFolder;
    }

    private static String verifyFolderExists(final String strFolder, final String title) {
        final File f = new File(strFolder);
        if (f.exists() && f.isDirectory()) {
            if (debug) LOG.debug("{} path is {}", title, strFolder);
        } else {
            LOG.error("ERROR: {} path {} not found. ", title, strFolder);
        }
        return strFolder;
    }

    private static void initializeAbsolutePathToLogConfigFile(final String logpath) {
        final String strFile = CUR_DIR + logpath.trim();
        final String title = "log configuration file";
        RunManager.setAbsolutePathToLogbackXMLFile(
                verifyFileExists(strFile, title));
    }

    public static void setAbsolutePathToLogbackXMLFile(final String absolutePathToLogbackXMLFile) {
        RunManager.absolutePathToLogbackXMLFile = absolutePathToLogbackXMLFile;
    }

    private static String verifyFileExists(final String strFile, final String title) {
        final File f = new File(strFile);
        if (f.exists() && !f.isDirectory()) {
            if (debug) LOG.debug("title is {}", strFile);
        } else {
            LOG.info("INFO: {} {} not found. ", title, strFile);
        }
        return strFile;
    }

    private static void initializeAbsolutePathToConfigsFolder(final String configpath) {
        final String strFolder = CUR_DIR + configpath.trim();
        final String title = "config folder";
        RunManager.setAbsolutePathToConfigsFolder(
                verifyFolderExists(strFolder, title));
    }

    public static void setAbsolutePathToConfigsFolder(String absolutePathToConfigsFolder) {
        RunManager.absolutePathToConfigsFolder = absolutePathToConfigsFolder;
    }

    /**
     * @param startdate in format "Jan 2 2013"
     * @param starttime in format "11:00 AM"
     */
    private static void initializeSimulationStartTime(final String startdate, final String starttime) {
        try {
            final Date date = new SimpleDateFormat("MMM d yyyy h:m a",
                    Locale.ENGLISH).parse(startdate.trim() + " " + starttime
                    .trim());
            if (debug) LOG.debug("\tSimulation begins at: {} ", date);
            final GregorianCalendar cal = new GregorianCalendar(
                    TimeZone.getTimeZone("America/Chicago"), Locale.US);
            cal.setTime(date);
            cal.set(Calendar.MILLISECOND, 0);
            Clock.setSimulationStartTime(cal);
            Clock.setSimulationTime(cal);
            if (debug) LOG.debug("\tRun Manager simulation start time is: {}", cal);
        } catch (ParseException e) {
            LOG.error(
                    "ERROR: cannot read simulation start time from run" +
                            ".properties. Must be  in " +
                            "SimpleDateFormat('MMM d yyyy h:m a', " +
                            "Locale.ENGLISH) like startdate=Aug 19 2013 and " +
                            "starttime=11:00 AM"
            );
            System.exit(-1);
        }
    }

    private static void initializeAbsolutePathToMatLabCodeFolder(final String matlabcodepath) {
        String connector = "//";
        final String relpath = matlabcodepath.trim();
        if (relpath.startsWith("/")) {
            connector = "";
        }
        final String strFolder = CUR_DIR + connector + relpath;
        final String title = "MatLab code folder";
        RunManager.setAbsolutePathToMatLabCodeFolder(
                verifyFolderExists(strFolder, title));
    }

    public static void setAbsolutePathToMatLabCodeFolder(
            final String absolutePathToMatLabCodeFolder) {
        if (RunManager.absolutePathToMatLabCodeFolder == null) {
            RunManager.absolutePathToMatLabCodeFolder =
                    absolutePathToMatLabCodeFolder;
        } else {
            throwException();
        }
    }

    public static int throwException() {
        // throw new RuntimeException("Cannot overwrite scenario settings.");
        return 0;
    }

    private static void initializeAbsolutePathToMatLabDataFolder(final String matlabdatapath) {
        String connector = "//";
        final String relpath = matlabdatapath.trim();
        if (relpath.startsWith("/")) {
            connector = "";
        }
        final String strFolder = CUR_DIR + connector + relpath;
        final String title = "MatLab code folder";
        RunManager.setAbsolutePathToMatLabDataFolder(
                verifyFolderExists(strFolder, title));
    }

    public static void setAbsolutePathToMatLabDataFolder(String absolutePathToMatLabDataFolder) {
        RunManager.absolutePathToMatLabDataFolder = absolutePathToMatLabDataFolder;
    }

    private static void initializeAbsolutePathToStandardAgentModelsFolder(String path) {
        final String strFolder = CUR_DIR + path.trim();
        final String title = "standard agent models folder";
        RunManager.setAbsolutePathToStandardAgentModelsFolder(verifyFolderExists(strFolder, title));
    }

    public static void setAbsolutePathToStandardAgentModelsFolder(final String path) {
        if (RunManager.absolutePathToStandardAgentModelsFolder == null) {
            RunManager.absolutePathToStandardAgentModelsFolder = path;
        }
    }

    private static void initializeAbsolutePathToStandardOrganizationModelsFolder(final String path) {
        final String strFolder = CUR_DIR + path.trim();
        final String title = "standard org models folder";
        RunManager.setAbsolutePathToStandardOrganizationModelsFolder(verifyFolderExists(strFolder, title));
    }

    public static void setAbsolutePathToStandardOrganizationModelsFolder(final String path) {
        if (RunManager.absolutePathToStandardOrganizationModelsFolder == null) {
            RunManager.absolutePathToStandardOrganizationModelsFolder = path;
        }
    }

    private static void initializeAbsolutePathToStandardPropertiesFolder(final String standardpropertiespath) {
        String connector = "//";
        final String relpath = standardpropertiespath.trim();
        if (relpath.startsWith("/")) {
            connector = "";
        }
        final String strFolder = CUR_DIR + connector + relpath;
        final String title = "Standard properties folder";
        RunManager.setAbsolutePathToStandardPropertiesFolder(
                verifyFolderExists(strFolder, title));
    }

    public static void setAbsolutePathToStandardPropertiesFolder(String absolutePathToStandardPropertiesFolder) {
        RunManager.absolutePathToStandardPropertiesFolder = absolutePathToStandardPropertiesFolder;
    }

    private static void initializeAbsolutePathtoSensorDataFile(final String configpath, final String datafile) {
        final String strFile = getAbsolutePathToConfigsFolder(configpath.trim()) + datafile.trim();
        final String title = "Sensor data file";
        RunManager.setAbsolutePathToSensorDataFile(verifyFileExists(strFile, title));
    }

    private static String getAbsolutePathToConfigsFolder(final String configpath) {
        String connector = "/";
        if (configpath.trim().endsWith("/")) {
            connector = "";
        }
        final String strFolder = CUR_DIR + configpath + connector;
        final String title = "Configs folder";
        return verifyFolderExists(strFolder, title);
    }

    public static void setAbsolutePathToSensorDataFile(
            final String absolutePathToSensorDataFile) {
        if (RunManager.absolutePathtoSensorDataFile == null) {
            RunManager.absolutePathtoSensorDataFile =
                    absolutePathToSensorDataFile;
        } else {
            throwException();
        }
    }

    private static void initializeTestCaseName(final String configpath, final String testcase) {
        String connector = "//";
        if (configpath.endsWith("/") || testcase.startsWith("/")) {
            connector = "";
        }
        final String strFolder = CUR_DIR + configpath.trim() + connector + testcase.trim();
        final String title = "Test case folder";
        if (!verifyFolderExists(strFolder, title).isEmpty()) {
            RunManager.setTestCaseName(testcase.trim());
        }
    }

    public static void setTestCaseName(String testCaseName) {
        RunManager.testCaseName = testCaseName;
    }

    private static void initializeGetAbsolutePathToTestCaseFolder(final String configpath, final String testcase) {
        String connector = "//";
        if (configpath.endsWith("/") || testcase.startsWith("/")) {
            connector = "";
        }
        final String strFolder = CUR_DIR + configpath.trim() + connector + testcase.trim();
        final String title = "Test case folder";
        RunManager.setAbsolutePathToTestCaseFolder(verifyFolderExists(strFolder, title));
    }

    public static void setAbsolutePathToTestCaseFolder(String absolutePathToTestCaseFolder) {
        RunManager.absolutePathToTestCaseFolder = absolutePathToTestCaseFolder;
    }

    private static void initializeUseLiveMatLab(final String uselivematlab) {
        final String strUseLiveMatLab = uselivematlab.trim().toLowerCase();
        RunManager.setUseLiveMatLab(
                strUseLiveMatLab.equals("yes") || strUseLiveMatLab.equals(
                        "true") || strUseLiveMatLab.equals("y")
        );
    }

    public static void setUseLiveMatLab(final Boolean useLiveMatLab) {
        if (RunManager.useLiveMatLab == null) {
            RunManager.useLiveMatLab = useLiveMatLab;
        } else {
            throwException();
        }
    }

    private static void initializeMaxTimeSlices(final String maxtimeslices) {
        try {
            int max = Integer.parseInt(maxtimeslices.trim());
            Clock.setMaxTimeSlices(max);
        } catch (Exception e) {
            LOG.error("ERROR: maximum time slices could not be read. {}", maxtimeslices);
        }
    }

    private static void initializeTopGoal(final String topgoal) {
        RunManager.setTopGoal(topgoal.trim());
    }

    public static void setTopGoal(final String topGoal) {
        RunManager.topGoal = topGoal;
    }

    private static void initializeLengthOfTimesliceInMilliseconds(final String lengthoftimesliceinmilliseconds) {
        try {
            int msecs = Integer.parseInt(lengthoftimesliceinmilliseconds.trim());
            Clock.setLengthOfTimesliceInMilliseconds(msecs);
        } catch (Exception e) {
            LOG.error("ERROR: length of time slice could not be read. {}", lengthoftimesliceinmilliseconds);
        }
    }

    private static void initializePlanningHorizonInMinutes(String planninghorizoninminutes) {
        try {
            int planHorizon = Integer.parseInt(planninghorizoninminutes.trim());
            RunManager.setPlanningHorizonInMinutes(planHorizon);
        } catch (Exception e) {
            LOG.error("ERROR: length of planning horizon could not be read. {}", planninghorizoninminutes);
        }
    }

    public synchronized static void setPlanningHorizonInMinutes(final int planningHorizonInMinutes) {
        RunManager.planningHorizonInMinutes = planningHorizonInMinutes;
    }

    private static void initializeInitializeGoalsInConstructors(
            final String initializegoalsinconstructors) {
        RunManager.setInitializeGoalsInConstructors(Boolean.TRUE);
        try {
            final String strValue = initializegoalsinconstructors.trim().toLowerCase();
            RunManager.setInitializeGoalsInConstructors(
                    strValue.equals("yes") || strValue.equals("true") || strValue.equals("y"));
        } catch (Exception e) {
            // continue
        }
    }

    public static void setInitializeGoalsInConstructors(final boolean initializeGoalsInConstructors) {
        RunManager.initializeGoalsInConstructors = initializeGoalsInConstructors;
    }

    private static void initalizeCommunicationsInBrowser(final String commInBrowser) {
        final String CommInBrowser = commInBrowser.trim().toLowerCase();
        RunManager.setCommunicationInBrowser(
                CommInBrowser.equals("yes") || CommInBrowser.equals(
                        "true") || CommInBrowser.equals("y") || CommInBrowser.equals("on")
        );
    }

    private static void initializeStaticLayoutImageResource(String staticlayoutimageresource) {
        final String strFile = CUR_DIR + "/resources/" + staticlayoutimageresource.trim();
        final String title = "static layout image resource file";
        RunManager.setStaticLayoutImageResourceFileName(
                verifyFileExists(strFile, title));
    }

    public static void setStaticLayoutImageResourceFileName(
            String absolutePathtoStaticLayoutImageFile) {
        if (RunManager.absolutePathtoStaticLayoutImageFile == null) {
            RunManager.absolutePathtoStaticLayoutImageFile = absolutePathtoStaticLayoutImageFile;
        } else {
            throwException();
        }
    }

    private static void initializeShowSensors(String showsensors) {
        final String strShowSensors = showsensors.trim().toLowerCase();
        RunManager.setShowSensors(
                strShowSensors.equals("yes") || strShowSensors.equals(
                        "true") || strShowSensors.equals("y")
        );
    }

    public synchronized static void setShowSensors(final Boolean showSensors) {
        if (RunManager.showSensors == null) {
            RunManager.showSensors = showSensors;
        } else {
            throwException();
        }
    }

    private static void initializeDeliveryCheckTime(String input) {
        try {
            int msecs = Integer.parseInt(input.trim());
            RunManager.setDeliveryCheckTime_ms(msecs);
        } catch (Exception e) {
            LOG.error("ERROR: delivery checktime in ms could not be read. {}", input);
        }
    }

    public static void setDeliveryCheckTime_ms(int deliveryCheckTime_ms) {
        RunManager.deliveryCheckTime_ms = deliveryCheckTime_ms;
    }

    private static void initializeWaitTime(String input) {
        try {
            int msecs = Integer.parseInt(input.trim());
            RunManager.setStandardWaitTime_ms(msecs);
        } catch (Exception e) {
            LOG.error("ERROR: standard wait time in ms could not be read. {}", input);
        }
    }

    public static void setStandardWaitTime_ms(int standardWaitTime_ms) {
        RunManager.standardWaitTime_ms = standardWaitTime_ms;
    }

    private static void initializeMessageDelayMinMS(String input) {
        try {
            int minMS = Integer.parseInt(input.trim());
            RunManager.setMessageDelayMinMS(minMS);
        } catch (Exception e) {
            LOG.error("ERROR: message delay min could not be read. {}", input);
        }
    }

    public static void setMessageDelayMinMS(int delayMin_MS) {
        RunManager.messageDelayMinMS = delayMin_MS;
    }

    private static void initializeMessageDelayMaxMX(String input) {
        try {
            int maxMX = Integer.parseInt(input.trim());
            RunManager.setMessageDelayMaxMX(maxMX);
        } catch (Exception e) {
            LOG.error("ERROR: message delay max could not be read. {}", input);
        }
    }

    public static void setMessageDelayMaxMX(int delayMax_MX) {
        RunManager.messageDelayMaxMX = delayMax_MX;
    }

    private static void initializeFractionMessagesDelayed(String input) {
        try {
            int fractionDelayed = Integer.parseInt(input.trim());
            RunManager.setFractionMessagesDelayed(fractionDelayed);
        } catch (Exception e) {
            LOG.error("ERROR: fraction message delayed could not be read. {}", input);
        }
    }

    public static void setFractionMessagesDelayed(int fracDelayed) {
        RunManager.fractionMessagesDelayed = fracDelayed;
    }

    public static String getValue(String propertyName) {
        if (!isLoaded) {
            RunManager.load();
        }
        return RunManager.properties.getProperty(propertyName);
    }

    public synchronized static void registered(final String identifierString, final MessagingFocus messagingFocus) {
        if (messagingFocus.equals(MessagingFocus.GENERAL)) {
            registered.add(identifierString);
            if (debug) LOG.info("{} is the {} agent to register.", identifierString, registered.size());
        }
    }

    public static void setAbsolutePathToLogConfigFile(String absolutePathToLogConfigFile) {
        RunManager.absolutePathToLogConfigFile = absolutePathToLogConfigFile;
    }

    public static void setAbsolutePathToSensorDataFile(final String configpath, final String pathToDataFile) {
        String connector = "/";
        if (configpath.trim().endsWith("/")) {
            connector = "";
        }
        RunManager.absolutePathtoSensorDataFile = CUR_DIR + configpath.trim() + connector + pathToDataFile.trim();
    }

    public synchronized static void setInitiallyConnected(final boolean initiallyConnected) {
        RunManager.initiallyConnected = initiallyConnected;
        if (RunManager.isInitiallyConnected() && RunManager.FORCE_STOP_AFTER_INITIALLY_CONNECTED)
            terminateSuccessfully();
        if (RunManager.isInitiallyConnected() && !RunManager.FORCE_STOP_AFTER_INITIALLY_CONNECTED)
            LOG.info(" ..........INITIALLY FULLY CONNECTED ");
    }

    private static void terminateSuccessfully() {
        LOG.info("TERMINATING: Ending partial run successfully. See Scenario for force stop conditions.");
        System.exit(-777);
    }

    public synchronized static boolean isInitiallyConnected() {
        return RunManager.initiallyConnected;
    }

    public static void setStopMonitor(final boolean isStopped) {
        RunManager.isStopped = isStopped;
    }

    public static void main(String[] args) throws IOException {
        load();
    }
}
