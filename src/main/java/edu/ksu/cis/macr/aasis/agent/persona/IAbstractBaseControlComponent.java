/*
 * ISimulatorControlComponent.java
 *
 * Created on Aug 15, 2006
 *
 * See License.txt file the license agreement.
 */
package edu.ksu.cis.macr.aasis.agent.persona;


import edu.ksu.cis.macr.obaa_pp.ec.IControlComponent;
import edu.ksu.cis.macr.obaa_pp.ec_cap.IInternalCommunicationCapability;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

/**
 * The {@code IAbstractControlComponent} interface provides the basic functionality required for the control component part
 * of an agent that is common to both masters and slaves.
 *
 * @author Christopher Zhong and Denise Case
 * @see IControlComponent
 * @see IInternalCommunicationCapability.ICommunicationChannel
 */
public interface IAbstractBaseControlComponent extends IPersonaControlComponent, IInternalCommunicationCapability.ICommunicationChannel {
    /**
     * The {@code UniqueIdentifier} that uniquely identifies the {@code IAgent}.
     *
     * @return the {@code UniqueIdentifier} that uniquely identifies the {@code IAgent}.
     */
    UniqueIdentifier getIdentifier();


    /**
     * The organization reasoning code.
     */
    void executeControlComponentPlan();

    /**
     * Returns the {@code String} that identifies the {@code ICommunicationChannel} that is used by the {@code IControlComponent}.
     *
     * @return the {@code String} that identifies the {@code ICommunicationChannel} that is used by the {@code IControlComponent}.
     */
    String getCommunicationChannelID();

    IPersona getPersona();

    void setPersona(IPersona persona);

    /**
     * Updates the organizational reasoning about changes on the {@code IAgent}.
     */
    void updateAgentInformation();
}
