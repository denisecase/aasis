package edu.ksu.cis.macr.aasis.agent.persona;


import edu.ksu.cis.macr.aasis.org.IBaseOrganization;
import edu.ksu.cis.macr.aasis.org.IOrganizationSpecification;
import edu.ksu.cis.macr.obaa_pp.objects.IDisplayInformation;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

import java.util.Collection;
import java.util.Map;

/**
 The {@code IOrganization} defines the interface for an {@code Organization}.  They have an associated set of
 specification files, including a: selfgoalmodel, selfrolemodel, an agentfile, and environment file (optional), a
 specified top goal, and the goal guidelines initialize file.  For standard organizational functionality, see {@code Organization}.
 */
public interface IOrganization extends IBaseOrganization, IPersonaPopulatable {

    String getOrgModelFolder();

    void setOrgModelFolder(String orgModelFolder) ;

    void addPersona();

    void disableAgent();

    void enableAgent();

    void endTurn();

    @Override
    String getLocalMaster();

    void setLocalMaster(String name);

    @Override
    String getName();

    @Override
    void setName(String name);

    int getRegisteredParties();

    IOrganizationSpecification getOrganizationSpecification();

    String getRemoteMaster();

    void setRemoteMaster(String name);

    /**
     Returns a {@code Collection} of {@code DisplayInformation} on {@code IAttributable}s that has been removed from the
     {@code Organization} since this method was last called.

     @return a {@code Collection} of {@code DisplayInformation}.
     */
    @Override
    Collection<IDisplayInformation> getRemovedDisplayChanges();

    /**
     Returns a {@code Collection} of {@code DisplayInformation} on {@code IAttributable}s that has changed since this
     method was last called.

     @return the {@code Collection} of {@code IAttributable}.
     @throws InterruptedException
     */
    @Override
    Collection<IDisplayInformation> getUpdatedDisplayChanges()
            throws InterruptedException;

    @Override
    public void initialize();

    void initializeGoals();

    /**
     Adds agents to the SelfOrganization organization according to information in the given file.

     @param agentfile the relative path and file of the agent XML file, e.g. configs/Scenario01/Agent.xml", containing
     information about the agents available in this SelfOrganization system.
     */
    abstract void loadAgentModel(final String agentfile);

    void lockData();

    void loadTopGoalGuidelines();

    void loadInitialGoalGuidelines(Map<UniqueIdentifier, Object> goalParameterValues);

    void init();

    @Override
    void notifyDisplayChanges(IDisplayInformation displayInformation);

    /**
     Sets the values of the goal parameters.

     @param name - the name of the goal parameter.
     @param value - the value of the goal parameter.
     */
    @Override
    public abstract void setGoalParameter(final UniqueIdentifier name,
                                          final double value);

    @Override
    void setGoalParameter(UniqueIdentifier name, Object val);

    void setOrganizationEventsFromControlComponent();

    void unlockData();

    @Override
    String verboseToString();

    void loadPersona();
}
