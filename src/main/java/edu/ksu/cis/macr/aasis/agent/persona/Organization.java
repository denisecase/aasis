/*
 * Organization.java
 *
 * See License.txt file the license agreement.
 */
package edu.ksu.cis.macr.aasis.agent.persona;


import edu.ksu.cis.macr.aasis.agent.persona.factory.ProxyPersonaFactory;
import edu.ksu.cis.macr.aasis.org.IOrganizationSpecification;
import edu.ksu.cis.macr.aasis.org.OrganizationSpecification;
import edu.ksu.cis.macr.aasis.simulator.player.Player;
import edu.ksu.cis.macr.aasis.spec.OrganizationFocus;
import edu.ksu.cis.macr.goal.model.InstanceParameters;
import edu.ksu.cis.macr.goal.model.SpecificationParameters;
import edu.ksu.cis.macr.obaa_pp.ec.staticorg.IAccomplishable;
import edu.ksu.cis.macr.obaa_pp.objects.*;
import edu.ksu.cis.macr.obaa_pp.org.Synchronizer;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * A holon operates as both an agent and an organization. The {@code Organization} class provides the ability to execute
 * the an organization as a whole.  An organization may be complex - consisting of hundreds of agents and many
 * organizations -  or simple, consisting of a single organization-agent such as a home agent.  In all cases, we run an
 * agent-organization holon by running the * {@code Organization}.  We use a discrete turn-based simulation.  The
 * organization will execute a 1 turn (as will each agent in the organization).  Once all agents have finished their
 * turn, the organization completes the turn and a new turn (time slice) will begin with a new set of sensor data form the
 * {@code Physical System Simulator}.
 *
 * @author Christopher Zhong / modified Denise Case
 * @see java.lang.Runnable
 */
public class Organization implements IOrganization {
    /**
     * {@code ELEMENT_AGENTS} is the description of agent(s).
     */
    public static final String ELEMENT_AGENTS = "agents";
    private static final Logger LOG = LoggerFactory.getLogger(Organization.class);
    private static final Boolean debug = false;
    /**
     * A reentrantLock to allow the simulation of the {@code Organization} to be paused.
     */
    protected final Lock pauseLock = new ReentrantLock();
    /**
     * A {@code BlockingQueue} containing {@code DisplayInformation} on {@code IAttributable}s that has been removed from the
     * {@code Organization}.
     */
    protected final BlockingQueue<IDisplayInformation>
            removedDisplayChangesQueue = new LinkedBlockingQueue<>();
    /**
     * Provides a point of synchronization for all active {@code IAgent} participants. {@code IAgent} that requires more than
     * a turn to perform some task should not be allowed to finish that task until all other {@code IAgent} have completed
     * their similar tasks.  This system can be viewed as a time slice allocating system, where each {@code IAgent} are
     * given a certain number of time to perform some action.
     */
    protected final Synchronizer scheduler = new Synchronizer(this);
    /**
     * A {@code Map} of {@code ITangibleObject} that exists in the {@code Organization}.  Each {@code ITangibleObject} is
     * indexed by their {@code UniqueIdentifier}.
     */
    protected final Map<UniqueIdentifier, ITangibleObject> tangibleObjects =
            new ConcurrentHashMap<>();
    /**
     * A {@code BlockingQueue} containing {@code DisplayInformation} on {@code IAttributable}s that has changed in {@code Organization}.
     */
    protected final BlockingQueue<IDisplayInformation>
            updatedDisplayChangesQueue = new LinkedBlockingQueue<>();
    /**
     * A {@code Map} of {@code IAgent} that exists in the {@code Organization}.  Each {@code IAgent} is indexed by their
     * {@code UniqueIdentifier}.
     */
    protected Map<UniqueIdentifier, IPersona> agents = new ConcurrentHashMap<>();
    /**
     * A {@code Queue} of {@code IAgent} that has requested to join the {@code Organization}. {@code IAgent} are only able to
     * join the {@code Organization} at the beginning of every turn, not during a turn. Hence the need for a {@code Queue}.
     */
    protected Queue<IPersona> personaWaitingToJoin = new
            ConcurrentLinkedQueue<>();
    /**
     * The wait time or delay between each turn. Allows control of how fast the simulation can run. Sleep time is specified
     * as milliseconds.  A sleep time of 0 millisecond means the simulation is running at realtime. The default sleep
     * time is 0 millisecond.
     */
    protected AtomicLong sleepTime = new AtomicLong();
    /**
     * Indicates if the simulation has terminated.
     */
    protected AtomicBoolean terminated = new AtomicBoolean();
    /**
     * Records the number of turns that has elapsed since the simulation started.
     */
    protected AtomicLong turns = new AtomicLong();
    /**
     * A reentrantLock to synchronize data access to the data structures in the {@code Organization}.
     */
    protected Lock dataLock = new ReentrantLock();
    /**
     * A {@code Map} of {@code IAttributable} that exists in the {@code Organization}.  Each {@code IAttributable} is
     * indexed by their {@code UniqueIdentifier}.
     */
    protected Map<UniqueIdentifier, IAttributable> environmentObjects =
            new ConcurrentHashMap<>();
    /**
     * The set of goal specification parameters for goals in the goal model.
     */
    protected SpecificationParameters goalSpecificationParameters = new
            SpecificationParameters();
    /**
     * A {@code Queue} that stores the changes to the {@code Organization} since the start of the simulation.
     * Potentially, this can be used as a playback of the changes that happened in the instance. As a playback, the
     * information required to store the changes are limited to only observable changes.
     */
    protected Queue<IDisplayInformation> history = new
            ConcurrentLinkedQueue<>();
    /**
     * A {@code Map} of {@code IIntangibleObject} that exists in the {@code Organization}.  Each {@code IIntangibleObject} is indexed by their {@code UniqueIdentifier}.
     */
    protected Map<UniqueIdentifier, IIntangibleObject> intangibleObjects
            = new ConcurrentHashMap<>();
    /**
     * A {@code Map} that maps the {@code Class} of the {@code IAttributable} to a collection of {@code IAttributable} of the
     * same {@code Class}.
     */
    protected Map<Class<? extends IAttributable>,
            Collection<IAttributable>> objectsByClass = new
            ConcurrentHashMap<>();
    /**
     * The set of goal parameters and their values for this instance of the goal model.
     */
    protected Map<UniqueIdentifier, Object> goalParameterValues;
    /**
     * A {@code boolean} the determines whether a history is being recorded.
     */
    protected boolean recordHistory;
    protected IOrganizationSpecification organizationSpecification;
    protected IAccomplishable terminationCriteria = null;
    protected String localMaster;
    protected String name;
    protected String remoteMaster;
    protected OrganizationFocus focus;
    protected String orgModelFolder;

    /**
     * Constructs a new instance of {@code Organization}. Used to be limited to one organization per JVM.
     *
     * @param folder   - The folder the Organization is in.
     * @param focus    - an Enum defining the focus of the Organization.
     * @param goalfile - the name of the goal file.
     * @param rolefile - the name of the roal file.
     * @param topGoal  - the name of the top goal.
     */
    public Organization(final String folder, final OrganizationFocus focus, final String goalfile, final String rolefile, final String topGoal) {
        LOG.info("Creating organization from {}. Focus={}.", folder, focus);
        this.recordHistory = false;
        File f = new File(folder);
        if (!f.exists()) {
            LOG.error("ERROR: Could not create organization from {}. Folder could not be found. Focus={}.", folder, focus);
        }
        this.name = f.getName();
        this.setLocalMaster("Master Not Yet Determined");
        this.focus = focus;
        LOG.debug("Creating internal spec with folder={}, focus={}, goalfile={}, rolefile={}, topGoal={}", folder, focus, goalfile, rolefile, topGoal);
        this.organizationSpecification = new OrganizationSpecification(folder, focus, goalfile, rolefile, topGoal);
        goalParameterValues = new HashMap<>();
    }

    /**
     * Constructs a new instance of an organization.
     */
    public Organization() {
        goalParameterValues = new HashMap<>();
    }


    /**
     * Constructs a new instance of {@code Organization}.  This constructor is declared private to allow no more than one
     * instance of the {@code Organization} to be running on the same JVM.
     */
    public Organization(String folder, OrganizationFocus focus) {
        LOG.info("Creating organization from {}. Focus={}.", folder, focus);
        this.recordHistory = false;
        File f = new File(folder);
        if (!f.exists()) {
            LOG.error("ERROR: Could not create organization from {}. Folder could not be found. Focus={}.", folder, focus);
        }
        this.name = f.getName();
        this.setLocalMaster("Master Not Yet Determined");
        this.focus = focus;
        this.organizationSpecification = new OrganizationSpecification(folder, focus);
        goalParameterValues = new HashMap<>();
    }

    /**
     * Constructs a new instance of {@code Organization}.  This constructor is declared private to allow no more than one
     * instance of the {@code Organization} to be running on the same JVM.
     */
    public Organization(String folder, OrganizationFocus focus, String orgModelFolder) {
        LOG.info("Creating organization from {}. Focus={}.", folder, focus);
        this.recordHistory = false;
        File f = new File(folder);
        if (!f.exists()) {
            LOG.error("ERROR: Could not create organization from {}. Folder could not be found. Focus={}.", folder, focus);
        }
        this.name = f.getName();
        this.setLocalMaster("Master Not Yet Determined");
        this.focus = focus;
        this.organizationSpecification = new OrganizationSpecification(folder, focus, orgModelFolder);
        goalParameterValues = new HashMap<>();
    }

    /**
     * Constructs a new instance of {@code Organization}.
     */
    public Organization(String folder, OrganizationFocus focus, String orgModelFolder, String goalFilePath, String roleFilePath, String topGoal) {
        LOG.info("Creating organization from {}. Focus={}.", folder, focus);
        this.recordHistory = false;
        File f = new File(folder);
        if (!f.exists()) {
            LOG.error("ERROR: Could not create organization from {}. Folder could not be found. Focus={}.", folder, focus);
        }
        this.name = f.getName();
        this.setLocalMaster("Master Not Yet Determined");
        this.focus = focus;
        this.organizationSpecification = new OrganizationSpecification(folder, focus, orgModelFolder, goalFilePath, roleFilePath, topGoal);
        goalParameterValues = new HashMap<>();
    }

    @Override
    public String getOrgModelFolder() {
        return orgModelFolder;
    }

    @Override
    public void setOrgModelFolder(String orgModelFolder) {
        this.orgModelFolder = orgModelFolder;
    }

    /**
     * @return the goalParameterValues
     */
    @Override
    public Map<UniqueIdentifier, Object> getGoalParameterValues() {
        return this.goalParameterValues;
    }

    @Override
    public void setGoalParameterValues(InstanceParameters params) {
        this.goalParameterValues = params.getParameters();
    }

    @Override
    public String getLocalMaster() {
        return localMaster;
    }

    @Override
    public void setLocalMaster(String localMaster) {
        this.localMaster = localMaster;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * The organization specification containing information about the files that define this organization.
     */
    @Override
    public IOrganizationSpecification getOrganizationSpecification() {
        return this.organizationSpecification;
    }

    public void setOrganizationSpecification(final IOrganizationSpecification organizationSpecification) {
        this.organizationSpecification = organizationSpecification;
    }

    @Override
    public String getRemoteMaster() {
        return remoteMaster;
    }

    @Override
    public void setRemoteMaster(String remoteMaster) {
        this.remoteMaster = remoteMaster;
    }

    /**
     * The {@code ITerminable} that determines when the simulation stops.
     */
    public IAccomplishable getTerminationCriteria() {
        return terminationCriteria;
    }

    public void setTerminationCriteria(final IAccomplishable terminationCriteria) {
        this.terminationCriteria = terminationCriteria;
    }

    @Override
    public String toString() {
        return (this.getClass().getSimpleName() + " with " + this.getAllPersona()
                .size() + " agents, " +
                "" + this.getTangibleObjects().size() + " tangible objects, " +
                "" + this.getIntangibleObjects().size() +
                " intangible objects, " + this.getObjects().size() + " " +
                "organization objects, " +
                "and  " + this.getRegisteredParties() + " registered parties.");
    }

    /**
     * Returns the number of agent threads still running.
     *
     * @return the number of agent threads still running.
     * @see java.util.concurrent.Phaser#getRegisteredParties()
     */
    @Override
    public int getRegisteredParties() {
        return this.scheduler.getRegisteredParties();
    }

    @Override
    public void initialize() {
        LOG.error("initialize() must be overwritten.");
        System.exit(-91);
    }

    @Override
    public void initializeGoals() {

    }

    /**
     * Sets the values of the goal parameters.
     *
     * @param name  - the name of the goal parameter.
     * @param value - the value of the goal parameter.
     */
    @Override
    public void setGoalParameter(final UniqueIdentifier name,
                                 final double value) {

    }

    @Override
    public void setGoalParameter(final UniqueIdentifier name,
                                 final Object val) {
        addGoalParameter(name, val);
    }

    @Override
    public String verboseToString() {
        final String s = "";
        final Collection<IAttributable> collection = this.getObjects();

        for (final IAttributable o : collection) {
            for (final IAttribute a : o.getAttributes()) {
                s.concat(a.getClass().getName() + " ");
            }
        }
        return s;
    }

// --------------------- Interface ICollectionDisplayable ---------------------

    public synchronized void loadPersona() {
        try {
            final Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(this.getOrganizationSpecification().getAgentFile());
            Objects.requireNonNull(document);
            ProxyPersonaFactory.setupPersonaList(
                    document.getElementsByTagName(ELEMENT_AGENTS),
                    this);
        } catch (ParserConfigurationException | SAXException |
                IOException e) {
            LOG.error("Error loading agent file ({}): ", this.getOrganizationSpecification().getAgentFile(), e);
            System.exit(-55);
        }
    }

    @Override
    public Collection<IDisplayInformation> getRemovedDisplayChanges() {
        final Collection<IDisplayInformation> result = new ArrayList<>();
        this.removedDisplayChangesQueue.drainTo(result);
        return result;
    }

// --------------------- Interface IDirectable ---------------------

    @Override
    public Collection<IDisplayInformation> getUpdatedDisplayChanges()
            throws InterruptedException {
        final Collection<IDisplayInformation> result = new ArrayList<>();
        IDisplayInformation displayInformation;
        while ((displayInformation = this.updatedDisplayChangesQueue.take())
                == null) {
        }
        result.add(displayInformation);
        this.updatedDisplayChangesQueue.drainTo(result);
        return result;
    }

    /**
     * Adds a new goal parameter from the given parameter name and value.
     *
     * @param name  - the name of the goal parameter.
     * @param value - the goal parameter object.
     */
    @Override
    public void addGoalParameter(final UniqueIdentifier name,
                                 Object value) {
        this.goalParameterValues.put(name, value);
    }

    /**
     * Adds a new goal parameter from the given parameter name and value.
     *
     * @param name  - the name of the goal parameter.
     * @param value - the value of the goal parameter.
     */
    @Override
    public void addGoalParameter(final UniqueIdentifier name,
                                 final double value) {
        this.goalParameterValues.put(name, value);
    }

    /**
     * Adds a {@code ITerminable} to this {@code Organization}.
     *
     * @param terminationCriteria the {@code ITerminable} to be added.
     */
    public void addTerminationCriteria(final IAccomplishable terminationCriteria) {
        this.setTerminationCriteria(terminationCriteria);
    }

// --------------------- Interface IMemorable ---------------------

    @Override
    public void loadState(final String filename) {
    }

    @Override
    public void loadState(final File file) {
    }

    @Override
    public void saveState(final String filename) {
    }

    @Override
    public void saveState(final File file) {
    }

// --------------------- Interface IOrganization ---------------------


    @Override
    public void addPersona() {
        LOG.error("run() must be overwritten in SelfOrganization and " +
                "ExternalOrganization.");
        System.exit(-93);
    }

    /**
     * Informs the {@code Organization} that a {@code IAgent} is no longer running in the {@code Organization}.  The
     * {@code IAgent} will be no longer be schedule to run the next turn.
     */
    @Override
    public void disableAgent() {
        if (debug) LOG.debug("disableAgent() Already Waiting : {} Parties : {}", this.scheduler
                .getArrivedParties(), this
                .scheduler.getRegisteredParties());

        this.scheduler.arriveAndDeregister();
    }

    /**
     * Informs the {@code Organization} that a {@code IAgent} is going start running in the {@code Organization}.  The
     * {@code IAgent} will be be schedule to run the next turn.
     */
    @Override
    public void enableAgent() {
        if (debug)
            LOG.debug("Organization enableAgent() Already Waiting : {} Parties : {}", this.scheduler.getArrivedParties(), this.scheduler.getRegisteredParties());
        this.scheduler.register();
    }

    /**
     * Puts the current {@code IAgent} on hold waiting for the next turn.
     */
    @Override
    public void endTurn() {
        if (debug)
            LOG.debug("Entering endTurn() Already Waiting : {} Parties : {}", this.scheduler.getArrivedParties(), this.scheduler.getRegisteredParties());
        this.scheduler.arriveAndAwaitAdvance();
    }

    /**
     * Adds agents to the organization according to information in the given file.
     *
     * @param agentfile the relative path and file of the agent XML file, e.g. configs/Scenario01/Agent.xml", containing
     *                  information about the agents available in this organization.
     */
    @Override
    public void loadAgentModel(final String agentfile) {
    }

    /**
     * Acquires the locks to access the data structures in the {@code Organization}.
     */
    @Override
    public void lockData() {
        this.dataLock.lock();
    }

    @Override
    public void setOrganizationEventsFromControlComponent() {
    }

    /**
     * Releases the locks for access to the data structures in the {@code Organization}.
     */
    @Override
    public void unlockData() {
        this.dataLock.unlock();
    }

    /**
     * Adds an {@code IAgent} agent to the {@code Organization}.
     *
     * @param persona the {@code IAgent} to be added to the {@code Organization}.
     * @return {@code true} if the {@code IAgent} was successfully added to the {@code Organization}, {@code false}
     * otherwise.
     */
    @Override
    public boolean addAgent(final IPersona persona) {
        lockData();
        try {
            /* if the agent already exists or is going to exists, do nothing */
            if (this.agents.containsKey(persona.getUniqueIdentifier()) || this
                    .personaWaitingToJoin.contains(persona)) {
                final String message = String.format("Agent (%s) Already " +
                        "Exists!", persona.getUniqueIdentifier());
                if (debug) LOG.debug(message);
                return false;
            } else {
                /*
                 * the agent will be added to the instance if the simulation
                 * has not yet started running. otherwise, the agent will be can
                 * only be added at the end of a turn.
                 */
                if (this.turns.get() == 0) {
                    if (addTangibleObject(persona)) {
                        if (debug) LOG.info("Successfully added persona {}.", persona.getUniqueIdentifier());
                        this.agents.put(persona.getUniqueIdentifier(), persona);
                        this.personaWaitingToJoin.add(persona);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    LOG.debug("addAgent(IAgent) {}",
                            String.format("Agent (%s) Waiting In Queue",
                                    persona.getUniqueIdentifier())
                    );
                    this.personaWaitingToJoin.add(persona);
                    return true;
                }
            }
        } finally {
            unlockData();
        }
    }

    /**
     * Add an intangible object to the organization.
     *
     * @param intangibleObject - the intangible object to be added.
     * @return - true if added, false if not.
     */
    @Override
    public boolean addIntangibleObject(IIntangibleObject intangibleObject) {
        return false;
    }

    /**
     * Add an environment object to the organization.
     *
     * @param environmentObject - the environment object to add.
     */
    @Override
    public boolean addObject(final IAttributable environmentObject) {
        lockData();
        try {
            if (this.environmentObjects.containsKey(environmentObject
                    .getObjectIdentifier())) {
                final String message = String.format("Organization Object " +
                        "(%s) Already Exists", environmentObject);
                LOG.debug("addObject(Environment object) {}", message);
                return false;
            }
            this.environmentObjects.put(environmentObject.getObjectIdentifier
                    (), environmentObject);
            addObjectByClass(environmentObject);
            informDisplayChanges(environmentObject);
            return true;
        } finally {
            unlockData();
        }
    }

    /**
     * Adds the given {@code ITangibleObject} to the {@code Organization}.
     *
     * @param tangibleObject the {@code ITangibleObject} to be added to the {@code Organization}.
     * @return {@code true} if the {@code ITangibleObject} was successfully added to the {@code Organization}, {@code false}
     * otherwise.
     */
    @Override
    public boolean addTangibleObject(final ITangibleObject
                                             tangibleObject) {
        lockData();
        try {
            if (this.tangibleObjects.containsKey(tangibleObject
                    .getObjectIdentifier())) {
                final String message = String.format("Tangible Object (%s) " +
                        "Already Exists", tangibleObject);
                LOG.debug("addTangibleObject(ITangibleObject) {}", message);
                return false;
            }
            this.tangibleObjects.put(tangibleObject.getObjectIdentifier(),
                    tangibleObject);
            addObjectByClass(tangibleObject);
            informDisplayChanges(tangibleObject);
            return true;
        } finally {
            unlockData();
        }
    }

    /**
     * Returns whether the given {@code IAgent} exists or not.
     *
     * @param identifier the {@code IAgent} to be checked.
     * @return {@code true} if the {@code IAgent} * * * * * * exists, {@code false} otherwise.
     */
    @Override
    public boolean containsAgent(final UniqueIdentifier identifier) {
        return this.agents.containsKey(identifier);
    }

    /**
     * Returns whether the given {@code IIntangibleObject} exists in the {@code Organization}.
     *
     * @param intangibleObject the {@code IIntangibleObject} to be checked.
     * @return {@code true} if the {@code IIntangibleObject} exists in the organization, {@code false} otherwise.
     */
    @Override
    public boolean containsIntangibleObject(IIntangibleObject intangibleObject) {
        return false;
    }

    /**
     * Returns whether the given {@code ITangibleObject} exists in the {@code Organization}.
     *
     * @param tangibleObject the {@code ITangibleObject} to be checked.
     * @return {@code true} if the {@code ITangibleObject} exists in the organization, {@code false} otherwise.
     */
    @Override
    public boolean containsTangibleObject(ITangibleObject tangibleObject) {
        return false;
    }

    /**
     * Returns the {@code IExecutionComponent} by the given identifier.
     *
     * @param identifier the unique identifier of the {@code IAgent} to retrieve.
     * @return the {@code IExecutionComponent} if it exists, {@code null} otherwise.
     */
    @Override
    public IPersona getAgent(final UniqueIdentifier identifier) {
        return this.agents.get(identifier);
    }

    /**
     * Returns the {@code Collection} of {@code IAgent} in the {@code Organization}.
     *
     * @return the {@code Collection} of {@code IAgent}.
     */
    @Override
    public Collection<IPersona> getAllPersona() {
        LOG.debug("Entering getAllPersona(). agents={}", agents);

        if (this.agents.isEmpty()) {
            if (debug) LOG.debug("There are are no agents in this {} organization" +
                    "", this.name);
        } else {
            if (debug) LOG.debug("There are {} agents in this {} organization: {}",
                    agents.size(), this.name, this.agents.values());
        }
        return this.agents.values();
    }

    /**
     * Returns the {@code Collection} of {@code IIntangibleObject} in the {@code Organization}.
     *
     * @return the {@code Collection} of {@code IIntangibleObject}.
     */
    @Override
    public Collection<IIntangibleObject> getIntangibleObjects() {
        return new ArrayList<>(this.intangibleObjects.values());
    }

    /**
     * Returns the {@code Collection} of {@code IAttributable} in the {@code Organization}.
     *
     * @return the {@code Collection} of {@code IAttributable}.
     */
    @Override
    public Collection<IAttributable> getObjects() {
        return new ArrayList<>(this.environmentObjects.values());
    }

    /**
     * Returns a {@code Collection} of {@code IAttributable} of the given {@code Class}.
     *
     * @param objectClass the {@code Class} to retrieve.
     * @return a {@code Collection} of {@code IAttributable} of the given {@code Class}, {@code null} otherwise.
     */
    @Override
    public Collection<IAttributable> getObjectsByClass(final Class<?
            extends IAttributable> objectClass) {
        final Collection<IAttributable> result = new ArrayList<>();
        final Collection<IAttributable> c = this.objectsByClass.get
                (objectClass);
        if (c != null) {
            result.addAll(c);
        }
        return result;
    }

    /**
     * Returns the {@code Collection} of {@code ITangibleObject} in the {@code Organization}.
     *
     * @return the {@code Collection} of {@code ITangibleObject}.
     */
    @Override
    public Collection<ITangibleObject> getTangibleObjects() {
        return new ArrayList<>(this.tangibleObjects.values());
    }

    /**
     * Removes the given {@code IIntangibleObject} from the {@code Organization}.
     *
     * @param intangibleObject the {@code IIntangibleObject} to be removed.
     */
    @Override
    public void removeIntangibleObject(final IIntangibleObject intangibleObject) {
        lockData();
        try {
            this.intangibleObjects.remove(intangibleObject.getObjectIdentifier());
            removeObjectByClass(intangibleObject);
            informDisplayChanges(intangibleObject);
        } finally {
            unlockData();
        }
    }

    /**
     * Removes the given {@code IPersona} from the {@code Organization}.
     *
     * @param persona the {@code IPersona} to be removed.
     */
    @Override
    public void removePersona(final IPersona persona) {
        lockData();
        try {
            this.agents.remove(persona.getUniqueIdentifier());
            removeTangibleObject(persona);
        } finally {
            unlockData();
        }
    }

    /**
     * Removes the given {@code ITangibleObject} from the {@code Organization}.
     *
     * @param tangibleObject the {@code ITangibleObject} to be removed.
     */
    @Override
    public void removeTangibleObject(final ITangibleObject
                                             tangibleObject) {
        lockData();
        try {
            this.tangibleObjects.remove(tangibleObject.getObjectIdentifier());
            removeObjectByClass(tangibleObject);
            informDisplayChanges(tangibleObject);
        } finally {
            unlockData();
        }
    }


    /**
     * Code to be executed when the organization ends.
     */
    @Override
    public void cleanUp() {

    }

    /**
     * Determines if the {@code ITerminable} has been met.
     *
     * @return {@code true} if the {@code ITerminable} is met, {@code false} otherwise.
     */
    @Override
    public boolean isAccomplished() {
        return false;
    }

    @Override
    public void loadTopGoalGuidelines() {
        LOG.info("Begin reading top goal guidelines for DEFAULT ORG {}. " +
                        "OVERWRITE this in the derived organization class.",
                this.getName());

        // get the source file (initialize.xml)

        String absPathToFile = this.getOrganizationSpecification().getGoalParametersFile();
        if (debug) LOG.debug("Initial top goal guidelines will be read from {}", absPathToFile);

        this.goalParameterValues = new HashMap<UniqueIdentifier, Object>();
        LOG.info("Organization guidelines (i.e. top goal parameter values) set for {}. {}", this.getName(), this.goalParameterValues);
    }

    /**
     * Load the initial goal guidelines as defined in the specification files.
     *
     * @param goalParameterValues - the map of the goal parameters (aka guidelines) provided
     */
    @Override
    public void loadInitialGoalGuidelines(Map<UniqueIdentifier, Object> goalParameterValues) {
        if (debug)
            LOG.info("Begin setting guidelines for DEFAULT org {}. OVERWRITE THIS IN THE DERIVED ORGANIZATION.", this.getName());
        if (debug) LOG.debug("Initial goal parameters set from {}", goalParameterValues);
        if (debug) LOG.debug("{} items in the goal parameter values key set.", goalParameterValues.size());

        // transfer in the collation provided
        for (UniqueIdentifier key : goalParameterValues.keySet()) {
            this.getGoalParameterValues().put(key, goalParameterValues.get(key));
            if (debug) LOG.debug("Getting input goal guideline. Key= {} Value={}", key, goalParameterValues.get(key));
        }
    }

    @Override
    public void init() {
        if (debug) LOG.debug("OVERWRITE construction org initializer in derived class. {}", this.getName());
    }


    @Override
    public void run() {
        LOG.error("run() must be overwritten in SelfOrganization and " + "ExternalOrganization.");
        System.exit(-92);
    }


    /**
     * Adds the given {@code IAttributable} to a {@code Collection} of {@code IAttributable} of the same {@code Class}.
     *
     * @param object the {@code IAttributable} to be added.
     */
    private void addObjectByClass(final IAttributable object) {
        if (!this.objectsByClass.containsKey(object.getClass())) {
            final ArrayList<IAttributable> list = new ArrayList<>();
            this.objectsByClass.put(object.getClass(), list);
        }
        this.objectsByClass.get(object.getClass()).add(object);
    }

    /**
     * Informs the {@code Organization} of changes for the given {@code IAttributable}.
     *
     * @param object the {@code IAttributable} to caused changes in the {@code Organization}.
     */
    private void informDisplayChanges(final IAttributable object) {
        notifyDisplayChanges(object.toDisplayInformation());
    }

    /**
     * Adds the given {@code DisplayInformation} to the history {@code Queue} and the changes {@code BlockingQueue}.
     * Since interaction in the {@code Organization} is achieved only through {@code ICapability}, this method should only be
     * utilized by {@code ICapability}.
     *
     * @param displayInformation the {@code DisplayInformation} to be added to the history {@code Queue} and the * * * * * *
     *                           * * * * * changes {@code BlockingQueue}.
     */
    @Override
    public void notifyDisplayChanges(final IDisplayInformation
                                             displayInformation) {
        if (this.recordHistory) {
            this.history.add(displayInformation);
        }

        if (this.tangibleObjects.containsKey(displayInformation.getIdentifier
                ()) || this.intangibleObjects
                .containsKey(displayInformation.getIdentifier())) {
            this.updatedDisplayChangesQueue.add(displayInformation);
        } else {
            this.removedDisplayChangesQueue.add(displayInformation);
        }
    }

    /**
     * Removes the given {@code IAttributable} from a {@code Collection} of {@code IAttributable} of the same {@code Class}.
     *
     * @param object the {@code IAttributable} to be removed.
     */
    private void removeObjectByClass(final IAttributable object) {
        this.objectsByClass.get(object.getClass()).remove(object);
    }

    public void step() {
        Player.step();
    }
}
