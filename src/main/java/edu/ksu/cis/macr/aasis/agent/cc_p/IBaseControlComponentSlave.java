package edu.ksu.cis.macr.aasis.agent.cc_p;

import edu.ksu.cis.macr.aasis.agent.cc_a.IAbstractControlComponent;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

/**
 * {@code IControlComponentSlave} provides an interface for describing a control component slave capable of accepting and
 * executing agent assignments.
 */
public interface IBaseControlComponentSlave extends IAbstractControlComponent {

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

    boolean isMaster();

    boolean isSlave();
}
