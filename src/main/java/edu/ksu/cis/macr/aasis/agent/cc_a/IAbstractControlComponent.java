/*
 * ISimulatorControlComponent.java
 *
 * Created on Aug 15, 2006
 *
 * See License.txt file the license agreement.
 */
package edu.ksu.cis.macr.aasis.agent.cc_a;

import edu.ksu.cis.macr.aasis.agent.persona.IInternalCommunicationCapability;
import edu.ksu.cis.macr.aasis.agent.persona.IPersona;
import edu.ksu.cis.macr.aasis.agent.persona.IPersonaControlComponent;
import edu.ksu.cis.macr.obaa_pp.cc.reorg.IReorganizationAlgorithm;
import edu.ksu.cis.macr.obaa_pp.ec.IControlComponent;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

//import edu.ksu.cis.macr.aasis.organizer.IReorganizationAlgorithm;

/**
 The {@code IAbstractControlComponent} interface provides the basic functionality required for the control component part
 of an agent that is common to both masters and slaves.

 @author Christopher Zhong and Denise Case
 @see IControlComponent
 */
public interface IAbstractControlComponent extends IPersonaControlComponent, IInternalCommunicationCapability.ICommunicationChannel {

    /**
     * The organization reasoning code.
     */
    void executeControlComponentPlan();

    /**
     Returns the {@code String} that identifies the {@code ICommunicationChannel} that is used by the {@code IControlComponent}.

     @return the {@code String} that identifies the {@code ICommunicationChannel} that is used by the {@code IControlComponent}.
     */
    String getCommunicationChannelID();

    /**
     The {@code UniqueIdentifier} that uniquely identifies the {@code IAgent}.

     @return the {@code UniqueIdentifier} that uniquely identifies the {@code IAgent}.
     */
    UniqueIdentifier getIdentifier();

    String getName();

    IPersona getOwner();

    void setOwner(IPersona owner);

    /**
     * Updates the organizational reasoning about changes on the {@code IAgent}.
     */
    void updateAgentInformation();

    void setReorganizationAlgorithm(IReorganizationAlgorithm reorganizationAlgorithm);

    /**
     Returns the current {@code IReorganizationAlgorithm} of this {@code IControlComponent}.

     @return the {@code IReorganizationAlgorithm} of this {@code IControlComponent}.
     */
    IReorganizationAlgorithm getReorganizationAlgorithm();






}
