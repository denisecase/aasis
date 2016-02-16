package edu.ksu.cis.macr.aasis.agent.persona;



import edu.ksu.cis.macr.obaa_pp.objects.IDisplayInformation;
import edu.ksu.cis.macr.organization.model.Capability;
import edu.ksu.cis.macr.organization.model.simple.SimpleCapability;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 The interface for an abstract capability used to assign tasks in an organization.
 */
public interface IAbstractOrganizationCapability extends SimpleCapability, Capability, ICapability {

  String getExecutorAbbreviation();

  @Override
  void populateCapabilitiesOfDisplayObject(IDisplayInformation displayInformation);

  @Override
  Element toElement(Document document);
}
