package edu.ksu.cis.macr.aasis.agent.persona;

import edu.ksu.cis.macr.obaa_pp.ec.base.IExecutionComponent;
import edu.ksu.cis.macr.obaa_pp.ec_task.ITask;
import edu.ksu.cis.macr.obaa_pp.events.IEventManager;
import edu.ksu.cis.macr.obaa_pp.objects.ITangibleObject;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

/**
 * The {@code IAgent} interface provides the basic functionality required for all autonomous agents. All autonomous agents
 * run their own organization of representatives or persona.  One self persona runs the internal organization as the
 * self master - the brains of the agents. There is typically one additional persona for each affiliated organization in
 * which the agent participates.  The internal organization is directed by the AgentGoalModel.goal and
 * AgentRoleModel.role and can be configured to act more selfishly or more cooperatively.  The agent begins operation
 * with a "ConnectAll" goal and will use the Self Control goal guidelines to determine the other agents with which it should
 * attempt to form a connection.  The participation goal guidelines indicate whether the agent should act as the
 * default head of its affiliated application-specific organizations or as part of the body.
 *
 * @author Christopher Zhong, Denise Case
 * @see IExecutionComponent
 * @see ITangibleObject
 */
public interface IPersona extends IPersonaExecutionComponent,  ITangibleObject, Runnable {

    public abstract void execute();

    /**
     Returns the {@code Organization} which contains information about the goals and roles.

     @return the  {@code Organization}.
     */
    IOrganization getOrganization();

    void doAssignmentTaskCompleted(IPersona agent, ITask assignedTask);

    String getHostFromIdentifier(UniqueIdentifier uniqueIdentifier);

    /**
     Returns the {@code IControlComponent} of this {@code IExecutionComponent}.

     @return the {@code IControlComponent} of this {@code IExecutionComponent}.
     */
    IPersonaControlComponent getPersonaControlComponent();
    /**
     Get the agent name e.g. N43.

     @return - the String name of the agent.
     */
    String getIdentifierString();

    /**
     @param identifierString the identifierString to set
     */
    void setIdentifierString(String identifierString);

    /**
     Returns the {@code IOrganizationEvents} of this {@code IPersona}.

     @return the {@code IOrganizationEvents} of this {@code IPersona}.
     */
    IEventManager getOrganizationEvents();

    void setOrganizationEvents(IEventManager organizationEvents);

    String getOrganizationFromIdentifier(UniqueIdentifier uniqueIdentifier);

    String getOrganizationFromPersonaName(String agentUniqueIdentifier);

}
