package edu.ksu.cis.macr.aasis.agent.persona;

import edu.ksu.cis.macr.goal.model.InstanceParameters;
import edu.ksu.cis.macr.obaa_pp.ec.base.IExecutionComponent;
import edu.ksu.cis.macr.organization.model.InstanceGoal;

/**
 * IPersonaExecutionComponent
 */
public interface IPersonaExecutionComponent extends IExecutionComponent {

    void addCapability(ICapability capability);


    /**
     Adds the given {@code InstanceGoal} to the current {@code Queue} of {@code InstanceGoal} to be processed.

     @param instanceGoal the {@code InstanceGoal} to be processed.
     */
    void addGoalModification(InstanceGoal<InstanceParameters> instanceGoal);


}
