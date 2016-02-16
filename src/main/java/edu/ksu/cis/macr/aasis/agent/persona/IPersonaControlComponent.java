package edu.ksu.cis.macr.aasis.agent.persona;

import edu.ksu.cis.macr.obaa_pp.cc.gr.IGoalModel;
import edu.ksu.cis.macr.obaa_pp.cc.om.IOrganizationModel;
import edu.ksu.cis.macr.obaa_pp.cc.reorg.IReorganizationAlgorithm;
import edu.ksu.cis.macr.obaa_pp.events.IEventManager;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

/**
 The {@code IControlComponent} interface defines the basic functionality of the Control Component from the
 Organization-Based Agent Architecture. It supports both organization masters and slaves.

 @author Christopher Zhong Revision: 1.9.6.1 $, Date: 2011/06/09 18:03:22 modified */
public interface IPersonaControlComponent {

  /**
   Returns the {@code IExecutionComponent} of this {@code IControlComponent}.

   @return the {@code IExecutionComponent} of this {@code IControlComponent}.
   */
  IPersona getPersonaExecutionComponent();

  /**
   Returns the {@code IGoalModel} of this {@code IControlComponent}.

   @return the {@code IGoalModel} of * * this {@code IControlComponent}.
   */
  IGoalModel getGoalModel();

  /**
   Gets the unique identifier for the local organization master.

   @return - the {@code UniqueIdentifier} of this local organization's master.
   */
  UniqueIdentifier getLocalMaster();

  /**
   Sets the local organization master.

   @param localMaster - the {@code UniqueIdentifier} of the sole master and supervisor for this holonic organization.
   */
  void setLocalMaster(UniqueIdentifier localMaster);

  /**
   Returns the {@code IOrganizationEvents} of this {@code IExecutionComponent}.

   @return the {@code IOrganizationEvents} of this {@code IExecutionComponent}.
   */
  IEventManager getOrganizationEvents();

  /**
   Returns the current {@code IOrganizationModel} of this {@code IControlComponent}.

   @return the current {@code IOrganizationModel} of * * this    {@code IControlComponent}.
   */
  IOrganizationModel getOrganizationModel();

    boolean isMaster();

    boolean isSlave();

}
