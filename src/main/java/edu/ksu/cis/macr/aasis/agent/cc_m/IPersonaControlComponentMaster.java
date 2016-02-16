package edu.ksu.cis.macr.aasis.agent.cc_m;


import edu.ksu.cis.macr.aasis.agent.cc_p.IBaseControlComponentSlave;
import edu.ksu.cis.macr.aasis.simulator.player.IPlayable;
import edu.ksu.cis.macr.goal.model.InstanceParameters;
import edu.ksu.cis.macr.goal.model.InstanceTreeChanges;
import edu.ksu.cis.macr.organization.model.Role;

import java.util.Set;

/**
 * Provides an interface for describing a control component master capable of selecting and
 * issuing agent assignments.
 */
public interface IPersonaControlComponentMaster extends IBaseControlComponentSlave {

    InstanceParameters getTopGoalInstanceParameters();

    void reorganize();

    InstanceTreeChanges getInitialGoalModelChangeList(InstanceParameters topInstanceParameters);

    Set<Role> setInitialRoles();

    void updateInitialActiveGoals(InstanceTreeChanges changeList);

    IPlayable getPlayerCapability();

    void setPlayerCapability(final IPlayable playerCapability);


}
