package edu.ksu.cis.macr.aasis.org;

import edu.ksu.cis.macr.aasis.config.RunManager;
import edu.ksu.cis.macr.aasis.spec.OrganizationFocus;
import edu.ksu.cis.macr.aasis.types.AgentType;
import edu.ksu.cis.macr.aasis.types.IAgentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@code OrganizationSpecification} contains the information needed to direct the behavior of the {@code IOrganization}.
 */
public class OrganizationSpecification implements IOrganizationSpecification {
    private static final Logger LOG = LoggerFactory.getLogger(OrganizationSpecification.class);
    private static final Boolean debug = false;
    private String orgNameWithSuffix;  // e.g. orgN43A
    private String agentfile;
    private String objectfile;
    private String rolefile;
    private String goalfile;
    private String topgoal;
    private String goalparametersfile;
    private OrganizationFocus focus;
    private String orgModelFolder;
    private IAgentType agentType = null;

    /**
     * Constructs a new organization specification.
     */
    public OrganizationSpecification(final String strFolder, final OrganizationFocus focus, final String goalfile, final String rolefile, final String topGoal) {
        this.focus = focus;
        LOG.debug("Creating internal spec with folder={}, focus={}, goalfile={}, rolefile={}, topGoal={}", strFolder, focus, goalfile, rolefile, topGoal);
        if (focus == null) {
            this.focus = OrganizationFocus.External;
        } else this.focus = focus;
        LOG.info(
                "\t ---- (w/files) Reading the specification for {} from {}", this.focus.toString().toUpperCase(), strFolder);

        File folder = new File(strFolder);
        this.orgNameWithSuffix = folder.getName();

        // get the specified top goal from the run manager (same for all)
        this.setTopGoal(topGoal);
        LOG.info("Top goal = {}", topGoal);
        this.setGoalFile(goalfile);
        LOG.info("Goal file  = {}", goalfile);
        this.setRoleFile(rolefile);
        LOG.info("Role file = {}", rolefile);

        // get the specification files
        File[] files = folder.listFiles();
        if (files == null) {
            LOG.error("No organization specification files were found in {}.", folder);
            // note it and continue
        }
        for (File file : files) {
            String filename = file.getAbsolutePath();
            if (filename.contains("Agent.xml")) {
                this.setAgentFile(file.getAbsolutePath());
                LOG.info("Agent file  = {}", file.getAbsolutePath());
            } else if (filename.contains("Environment.xml")) {
                this.setObjectFile(filename);
            } else if (filename.contains("Initialize.xml")) {
                this.setGoalParametersFile(filename);
                LOG.info("Initialize file  = {}", file.getAbsolutePath());
            } else if (goalfile == "" && filename.contains("GoalModel.goal")) {
                this.setGoalFile(filename);
            } else if (rolefile == "" && filename.contains("RoleModel.role")) {
                this.setRoleFile(filename);
            }
        }
        LOG.info("Goal file  = {}", this.getGoalFile());
        LOG.info("Role file  = {}", this.getRoleFile());
        LOG.info("---- Construction of organization specification complete. Verifying agent file. {} {}",
                this.goalfile, this.rolefile);
        this.verifyAgentFile();
    }

    /**
     * Constructs a new organization specification.
     */
    public OrganizationSpecification(final String strFolder, final OrganizationFocus focus, String orgModelFolder, final String goalfile, final String rolefile, final String topGoal) {
        this.focus = focus;
        LOG.debug("Creating internal spec with folder={}, focus={}, orgModelFolder={},goalfile={}, rolefile={}, topGoal={}", strFolder, focus, orgModelFolder, goalfile, rolefile, topGoal);
        if (focus == null) {
            this.focus = OrganizationFocus.External;
        } else this.focus = focus;
        LOG.info(
                "\t ---- (w/files) Reading the specification for {} from {}", this.focus.toString().toUpperCase(), strFolder);

        File folder = new File(strFolder);
        this.orgNameWithSuffix = folder.getName();

        // get the specified top goal from the run manager (same for all)
        this.setTopGoal(topGoal);
        LOG.info("Top goal = {}", topGoal);
        this.setGoalFile(goalfile);
        LOG.info("Goal file  = {}", goalfile);
        this.setRoleFile(rolefile);
        LOG.info("Role file = {}", rolefile);

        // get the specification files
        File[] files = folder.listFiles();
        if (files == null) {
            LOG.error("No organization specification files were found in {}.", folder);
            // note it and continue
        }
        for (File file : files) {
            String filename = file.getAbsolutePath();
            if (filename.contains("Agent.xml")) {
                this.setAgentFile(file.getAbsolutePath());
            } else if (filename.contains("Environment.xml")) {
                this.setObjectFile(filename);
            } else if (filename.contains("Initialize.xml")) {
                this.setGoalParametersFile(filename);
            } else if (goalfile == "" && filename.contains("GoalModel.goal")) {
                this.setGoalFile(filename);
            } else if (rolefile == "" && filename.contains("RoleModel.role")) {
                this.setRoleFile(filename);
            }
        }
        LOG.info("---- Construction of organization specification complete. Verifying agent file. {} {}",
                this.goalfile, this.rolefile);
        this.verifyAgentFile();
    }

    /**
     * Constructs a new organization specification.
     */
    public OrganizationSpecification(final String strFolder, OrganizationFocus focus) {
        this(strFolder, focus, "");
    }

    /**
     * Constructs a new organization specification.
     */
    public OrganizationSpecification(final String strFolder, final OrganizationFocus focus, final String orgModelFolder) {
        this.focus = focus;
        this.orgModelFolder = orgModelFolder;

        LOG.info(
                "\t ---- (3 params) Reading the specification for {} from {}", this.focus.toString().toUpperCase(), strFolder);
        File folder = new File(strFolder);
        this.orgNameWithSuffix = folder.getName(); // e.g. org43

        // get the specified topgoal from the config (same for all)
        // this.setTopGoal(Scenario.getTopGoal());
        this.setTopGoal("Succeed");

        // get the specification files
        File[] files = folder.listFiles();
        if (files == null) {
            LOG.error("No organization specification files were found in {}.",
                    folder);
            // note it and continue
        }
        for (File file : files) {
            String filename = file.getAbsolutePath();
            if (filename.contains("Agent.xml")) {
                this.setAgentFile(file.getAbsolutePath());
                if (debug) LOG.debug("Specification - Agent file is {}", this.getAgentFile());
            } else if (filename.contains("Environment.xml")) {
                this.setObjectFile(filename);
                if (debug) LOG.debug("Specification - Object file is {}", this.getObjectFile());
            } else if (filename.contains("Initialize.xml")) {
                this.setGoalParametersFile(filename);
                if (debug) LOG.debug("Specification - Goal Parameters file is {}", this.getGoalParametersFile());
            } else if (filename.contains("GoalModel.goal")) {
                this.setGoalFile(filename);
                if (debug) LOG.debug("Specification - Goal model is {}", this.getGoalFile());
            } else if (filename.contains("RoleModel.role")) {
                this.setRoleFile(filename);
                if (debug) LOG.debug("Specification - Role model is {}", this.getRoleFile());
            }
        }

        // Default standard agent models...................
        this.agentType = getAgentTypeFromFolder(this.orgNameWithSuffix);

        if (this.focus == OrganizationFocus.Agent && this.goalfile == null) {
            this.goalfile = RunManager.getAbsolutePathToStandardAgentGoalModel(agentType);
            if (debug) LOG.debug("Specification - Goal model is {}", this.getGoalFile());
        }
        if (this.focus == OrganizationFocus.Agent && this.rolefile == null) {
            this.rolefile = RunManager.getAbsolutePathToStandardAgentRoleModel(agentType);
            if (debug) LOG.debug("Specification - Role model is {}", this.getRoleFile());
        }
        if (this.focus == OrganizationFocus.External && this.goalfile == null) {
            this.goalfile = RunManager.getAbsolutePathToStandardOrganizationModelsFolder() + "//" + this.orgModelFolder + "//" + agentType + "GoalModel.goal";
            LOG.debug("Specification - Goal model is {}", this.getGoalFile());
        }
        if (this.focus == OrganizationFocus.External && this.rolefile == null) {
            this.rolefile = RunManager.getAbsolutePathToStandardOrganizationModelsFolder() + "//" + this.orgModelFolder + "//" + agentType + "RoleModel.role";
            LOG.debug("Specification - Role model is {}", this.getRoleFile());
        }
        if (debug) LOG.debug(
                "---- Construction of organization specification complete. " +
                        "Verifying agent file. {} {}",
                goalfile, rolefile);
        this.verifyAgentFile();
    }

    @Override
    public void setTopGoal(final String topgoal) {
        if (debug) LOG.debug("Top goal is {}.", topgoal);
        this.topgoal = Objects.requireNonNull(topgoal);
        // topgoal should be checked to see if it exists in the
        // associated goal model.
    }

    @Override
    public void setAgentFile(final String agentfile) {
        if (debug) LOG.debug("Setting agent file to {}.", agentfile);
        this.agentfile = agentfile;
    }


    @Override
    public String getAgentFile() {
        return this.agentfile;
    }


    @Override
    public void setObjectFile(final String objectfile) {
        if (debug) LOG.debug("Setting objectfile file to {}.", objectfile);
        this.objectfile = objectfile;
    }


    @Override
    public String getObjectFile() {
        return this.objectfile;
    }

    @Override
    public void setGoalParametersFile(final String goalparametersfile) {
        if (debug) LOG.debug("Setting initial goal parameters file to {}",
                goalparametersfile);
        this.goalparametersfile = goalparametersfile;
    }

    @Override
    public String getGoalParametersFile() {
        return this.goalparametersfile;
    }

    @Override
    public void setGoalFile(final String myFile) {
        this.goalfile = myFile;
    }


    @Override
    public void setRoleFile(final String myFile) {
        this.rolefile = myFile;
    }

    /**
     * Custom algorithm to determine the type of agent. Before evaluation, the algorithm will remove any "self" or "org"
     * strings and base the result on the first character.
     *
     * @param agentFolderName - the folder name with the specification files.
     */
    public IAgentType getAgentTypeFromFolder(final String agentFolderName) {
        if (this.focus == OrganizationFocus.External) return null;
        String name = agentFolderName.replace("self", "").replace("org", "").toUpperCase().trim();
        String firstChar = name.substring(0, 1);
        String lastTwoChar = name.substring(name.length() - 2);
        IAgentType agentType = AgentType.Agent;
        return agentType;
    }

    @Override
    public String getGoalFile() {
        return this.goalfile;
    }


    @Override
    public String getRoleFile() {
        return this.rolefile;
    }

    /*
     * Parses Agent Model and verifies that all files referenced in the model
     * exist within the project and are readable using the static
     * checkFile() method in this class.
     */
    @Override
    public boolean verifyAgentFile() {
        if (debug) LOG.debug("Verifying all file references in agent file: {}", this.agentfile);
        File file = new File(this.agentfile.trim());
        if (!file.exists()) {
            LOG.info("ERROR: Agent file could not be found: {}", this.agentfile);
            // System.exit(-82);
        }

        // Gets agent elements from Agent model
        NodeList personaNodes = null;
        try {
            final String ELEMENT_AGENTS = "agents";
            personaNodes = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(file)
                    .getElementsByTagName(ELEMENT_AGENTS);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            LOG.info("Problem verifying agent file {}.", this.agentfile);
            //  System.exit(-83);
        }

        final String curDir = System.getProperty("user.dir");
        final String codePath = "/src/main/java/";
        final String defaultFolder = curDir + codePath;

        if (debug) LOG.debug("The main agent folder (without packages) is {}", defaultFolder);
        List<String> fileList = new ArrayList<>();
        fileList.clear();
        fileList = listAgentFiles(personaNodes, fileList, defaultFolder);

        for (String path : fileList) {
            final File f = new File(path);
            if (checkFile(f)) {
                if (debug) LOG.debug(String.format("%s file verified.", f.getAbsolutePath()));
            } else {
                LOG.info("ERROR: {} file not verifiable", f.toString());
                //    System.exit(-84);
            }
        }
        if (debug) LOG.debug("Verification of agent file in the organization specification complete.");
        return true;
    }

    public String getOrgModelFolder() {
        return orgModelFolder;
    }

    public void setOrgModelFolder(String orgModelFolder) {
        this.orgModelFolder = orgModelFolder;
    }

// --------------------- Interface IOrganizationSpecification ---------------------

    @Override
    public boolean checkFile(final File file) {
        if (file.exists()) {
            if (file.isFile()) {
                if (file.canRead()) {
                    return true;
                } else {
                    LOG.error(String.format(
                            "Checking file, but file (%s) cannot be read",
                            file));
                    System.exit(-21);
                }
            } else {
                LOG.error(String.format(
                        "Checking file, but file (%s) is not a file", file));
                System.exit(-22);
            }
        } else {
            LOG.info("Checking file for {}, but file {} does not exist",
                    this.orgNameWithSuffix, file.getAbsolutePath());
            // System.exit(-23);
        }
        return false;
    }

    @Override
    public String getTopGoal() {
        return this.topgoal;

    }

    @Override
    public void initialize() {
        LOG.info(
                "\t--------------- INITIALIZING ORG SPECIFICATION FROM {} " + "------------------------------",
                this.orgNameWithSuffix);

        this.checkFile(new File(this.goalfile));
        this.checkFile(new File(this.rolefile));
        this.checkFile(new File(this.agentfile));
        this.checkFile(new File(this.objectfile));
        this.checkFile(new File(this.goalparametersfile));
        this.verifyAgentFile();
    }

    /**
     * Generating the list of agent files from the nodes.
     * It both updates the agent list parameter passed in and returns it.
     *
     * @param elementList
     * @param fileList
     * @param defaultFolder
     * @return
     */
    public List<String> listAgentFiles(final NodeList elementList, List<String> fileList, String defaultFolder) {
        fileList.clear(); // start empty
        LOG.debug("The file list has {} entries.", fileList.size());
        final String ELEMENT_ORGANIZATION = "organization";
        final String ELEMENT_CAPABILITY = "capability";
        final String ATTRIBUTE_PACKAGE = "package";
        final String ATTRIBUTE_CLASS = "type";
        final String JAVA_EXT = ".java";

        for (int i = 0; i < elementList.getLength(); i++) {
            // Get current Element
            Element agent = (Element) elementList.item(i);
            // Get package and class orgNameWithSuffix attributes from XML file
            String packageName = agent.getAttribute(ATTRIBUTE_PACKAGE);
            if (debug) LOG.debug("The package is {}.", packageName);
            String className = agent.getAttribute(ATTRIBUTE_CLASS);
            if (debug) LOG.debug("The className is {}.", className);

            String packageAsFolders = packageName.replace(".", "/");
            if (debug) LOG.debug("The packageAsFolder is {}.", packageAsFolders);

            String newDefaultFolder = getNewDefaultFolder(defaultFolder, packageName);
            String path = newDefaultFolder + packageAsFolders + "/" + className + JAVA_EXT;
            if (debug) LOG.debug("The abs path to check is {}.", path);

            // log each file
            fileList.stream().filter(f -> debug).forEach(f -> LOG.debug("List includes {}.", path));

            if (!fileList.contains(path)) {
                fileList.add(path);
                if (debug) LOG.debug("Added. The file list now has {} entries.", fileList.size());
            }
            if (debug)  LOG.debug("Getting org files.");
            this.listSubFiles(agent.getElementsByTagName(ELEMENT_ORGANIZATION), fileList, defaultFolder);
            if (debug) LOG.debug("Getting capability files.");
            this.listSubFiles(agent.getElementsByTagName(ELEMENT_CAPABILITY), fileList, defaultFolder);
        }
        fileList.stream().filter(path -> debug).forEach(path -> LOG.debug("List now includes {}.", path));
        return fileList;
    }

    public List<String> listSubFiles(final NodeList elementList,
                                     final List<String> fileList, final String defaultFolder) {

        if (debug) LOG.debug("The default folder is {}.", defaultFolder);
        final String ATTRIBUTE_PACKAGE = "package";
        final String ATTRIBUTE_CLASS = "type";
        final String JAVA_EXT = ".java";
        for (int i = 0; i < elementList.getLength(); i++) {
            // Get current Element
            final Element element = (Element) elementList.item(i);
            // Get package and class orgNameWithSuffix attributes from XML file
            final String packageName = element.getAttribute(ATTRIBUTE_PACKAGE);
            if (debug) LOG.debug("The package is {}.", packageName);
            final String className = element.getAttribute(ATTRIBUTE_CLASS);
            if (debug) LOG.debug("The className is {}.", className);

            final String newDefaultFolder = getNewDefaultFolder(defaultFolder, packageName);
            // Find user's current directory
            //final String curDir = System.getProperty("user.dir");
            // Build path to use for agent file
            final String path = newDefaultFolder + packageName.replace(".", "/") + "/" + className + JAVA_EXT;
            if (debug) LOG.debug("The path is {}.", path);
            if (!fileList.contains(path)) {
                fileList.add(path);
            }
        }
        return fileList;
    }

    private String getNewDefaultFolder(String defaultFolder, String packageName) {
        // typical default folder might be something like  U:\_KSU\phd_project\ipds/src/main/java/
        // package might be something like edu.ksu.cis.macr.aasis.agent.persona.capabilities
        // note that a core aasis capability won't be found in a domain-specific project like ipds
        // Therefore, find the part just before XXXX/src/main/java (e.g. ipds)
        // and change it to aasis
        if (debug) LOG.debug("Calculating path given default folder={} and packageName={}.", defaultFolder, packageName);
        String newDefaultFolder;
        if (packageName.contains(".aasis.")) {
            String currentProjectName = getCurrentProjectName(defaultFolder);
            newDefaultFolder = defaultFolder.replace(currentProjectName, "aasis");
        } else {
            newDefaultFolder = defaultFolder;

        }
        if (debug) LOG.debug("The new default folder is {}.", newDefaultFolder);
        return newDefaultFolder;
    }

    private String getCurrentProjectName(String defaultFolder) {
        if (debug) LOG.debug("Calculating project name given default folder={}.", defaultFolder);
        String projectName = "ipds";
        // typical default folder might be something like  U:\_KSU\phd_project\ipds/src/main/java/
        //TODO: Greg:  parse the string to find just the project name (e.g. ipds) and return it
        // in the aasis project, run gradle clean distZip
        // go to assis build / distributions  and unzip the archive file.
        // Look in the lib folder. Grab the aasis jarfile and replace it in ipds\src\main\resources\lib\aasis-1.0-SNAPSHOT\lib
        // in the ipds project, run gradle clean run
        if (debug) LOG.debug("The project name in {} = {}.", defaultFolder, projectName);
        return projectName;
    }
}
