package edu.ksu.cis.macr.aasis.self;

import edu.ksu.cis.macr.aasis.agent.cc_m.IPersonaControlComponentMaster;
import edu.ksu.cis.macr.aasis.agent.persona.IOrganization;
import edu.ksu.cis.macr.aasis.types.IAgentType;


/**
 * Defines the reusable, recursive foundation for implementing multiply-governed,
 * goal-directed intelligent cyber-physical systems.  It has a set of associated specification files, including:
 * -- an AgentGoalModel.goal the defines the agents goals and which can be configured to reflect differing degrees of
 * selfishness or community interest.  -- an AgentRoleModel.role that describes the relationships between goals, roles,
 * and capabilities.  -- an Agent.xml file that describes the types of sub-agents (called persona) including one master
 * subagent, and one persona for each affiliated organization.  -- an Environment.xml file (optional) specifying other
 * objects (besides agents) that may be involved in the system.  -- an Initialize.xml file specifying the customizable
 * goal guidelines describing who the agent should connect with and who their default master will be.   For
 * standard organizational functionality, see {@code Organization}.   A holonic SelfOrganization may consist of
 * multiple levels or may be a single organization or entity. The holonic nature allows an SelfOrganization to function as
 * appropriate given both the possible physical connections and the temporal connections available.
 */
public interface IInnerOrganization extends IOrganization {

    IAgentType getAgentType();

    void setAgentType(IAgentType agentType);

    IPersonaControlComponentMaster getSelfControlComponentMaster();

    void loadAgentFile();

    void loadObjectFile();

    void run();

}
