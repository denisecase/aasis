package edu.ksu.cis.macr.aasis.org;

import edu.ksu.cis.macr.obaa_pp.ec.staticorg.IAccomplishable;
import edu.ksu.cis.macr.obaa_pp.ec.staticorg.ICollectionDisplayable;
import edu.ksu.cis.macr.obaa_pp.ec.staticorg.IDirectable;
import edu.ksu.cis.macr.obaa_pp.org.IMemorable;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

/**
 The {@code IOrganization} defines the interface for an {@code Organization}.  They have an associated set of
 specification files, including a: selfgoalmodel, selfrolemodel, an agentfile, and environment file (optional), a
 specified top goal, and the goal guidelines initialize file.  For standard organizational functionality, see {@code Organization}.
 */
public interface IBaseOrganization extends Runnable, ICollectionDisplayable,
        IMemorable, IDirectable, IAccomplishable {

  String getLocalMaster();

  String getName();

  void setName(String name);

  IAccomplishable getTerminationCriteria();

  void setTerminationCriteria(IAccomplishable terminationCriteria);

  public void initialize();

  /**
   Sets the values of the goal parameters.

   @param name - the name of the goal parameter.
   @param value - the value of the goal parameter.
   */
  public abstract void setGoalParameter(final UniqueIdentifier name,
                                        final double value);

  void setGoalParameter(UniqueIdentifier name, Object val);

  String verboseToString();
}
