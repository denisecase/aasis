/*
 * AbstractExecutableCapability.java
 * 
 * Created on Sep 8, 2004
 * 
 * See License.txt file the license agreement.
 */
package edu.ksu.cis.macr.aasis.agent.persona;


import edu.ksu.cis.macr.obaa_pp.ec.AgentDisabledException;
import edu.ksu.cis.macr.obaa_pp.events.OrganizationEvents;
import edu.ksu.cis.macr.obaa_pp.objects.IDisplayInformation;
import edu.ksu.cis.macr.obaa_pp.objects.IIntangibleObject;
import edu.ksu.cis.macr.obaa_pp.objects.ITangibleObject;
import edu.ksu.cis.macr.organization.model.Capability;
import edu.ksu.cis.macr.organization.model.CapabilityImpl;
import edu.ksu.cis.macr.organization.model.identifiers.ClassIdentifier;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code AbstractOrganizationCapability} provides the ec structure needed by {@code ICapability} to function within the
 * simulation.
 *
 * @author Christopher Zhong
 * @version $Revision: 1.2.4.7 $, $Date: 2012/02/06 23:15:41 $
 * @since 1.0
 */
public abstract class AbstractOrganizationCapability extends CapabilityImpl implements IAbstractOrganizationCapability {
    /**
     * The minimum failure rate. 0.0 indicates perfect reliability. Currently set to 0.0.
     */
    public static final double MIN_FAILURE = 0.0;
    /**
     * The maximum failure rate. 1.0 would fail every time. Currently set to 0.0 - everything is perfectly reliable.
     */
    public static final double MAX_FAILURE = 0.0;
    /**
     * {@code ELEMENT_PARAMETER}
     */
    protected static final String ELEMENT_PARAMETER = "parameter";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractOrganizationCapability.class);
    private static final boolean debug = false;
    /**
     * The {@code Organization} in which the {@code ICapability} exists and interacts with.
     */
    private final IOrganization organization;
    protected OrganizationEvents organizationEvents;
    /**
     * The {@code IExecutionComponent} to which this {@code ICapability} belongs to.
     */
    protected IPersona owner;
    /**
     * {@code ELEMENT_CAPABILITY}
     */
    protected String ELEMENT_CAPABILITY = "capability";
    /**
     * {@code ATTRIBUTE_PACKAGE}
     */
    protected String ATTRIBUTE_PACKAGE = "package";
    /**
     * {@code ATTRIBUTE_TYPE}
     */
    protected String ATTRIBUTE_TYPE = "type";
    private String agentTypeName;

    /**
     * Constructs a new instance of {@code AbstractOrganizationCapability}.
     *
     * @param <CapabilityType> the type of the {@code ICapability}.
     * @param capabilityClass  the {@code Class} of {@code ICapability}.
     * @param owner            the {@code IAgent} to which the {@code ICapability} belongs to.
     * @param organization     the {@code IOrganization} in which the {@code ICapability} interacts with.
     */
    protected <CapabilityType extends Capability> AbstractOrganizationCapability(final Class<CapabilityType>
                                                                                         capabilityClass,
                                                                                 final IPersona owner,
                                                                                 final IOrganization organization) {
        super(new ClassIdentifier(capabilityClass));
        this.owner = owner;
        this.organization = organization;
        this.agentTypeName = owner.toString();
    }

    public IOrganization getOrganization() {
        return organization;
    }

    public String getAgentTypeName() {
        return agentTypeName;
    }

    public void setAgentTypeName(String agentTypeName) {
        this.agentTypeName = agentTypeName;
    }

    /**
     * Determine the status of the {@code IAgent} and throws {@code AgentDisabledException} if the {@code IAgent} is
     * disabled.
     *
     * @throws AgentDisabledException if {@code IAgent} is disabled.
     */
    protected void agentStatus() throws AgentDisabledException {
        LOG.debug("Entering agentStatus()");
        if (!getOwner().isAlive()) {
            throw new AgentDisabledException();
        }
        LOG.debug("Exiting agentStatus()");
    }

    /**
     * @return the executorAbbreviation
     */
    @Override
    public String getExecutorAbbreviation() {
        return agentTypeName;
    }

    @Override
    public abstract double getFailure();

    /**
     * Returns the {@code IExecutionComponent} to which this {@code ICapability} belongs to.
     *
     * @return the {@code IExecutionComponent} to which this {@code ICapability} belongs to.
     */
    protected IPersona getOwner() {
        return owner;
    }

    public void setOwner(IPersona owner) {
        this.owner = owner;
    }

    /**
     * Returns the {@code IAgent} by the given name.
     *
     * @param identifier the name of the {@code IAgent} to retrieve.
     * @return the {@code IAgent} if it exists, {@code null} otherwise.
     */
    protected IPersona getPersona(
            final UniqueIdentifier identifier) {
        return organization.getAgent(identifier);
    }

    /**
     * Returns the collection of {@code IAgent} in the organization.
     *
     * @return the collection of {@code IAgent}.
     */
    protected Collection<IPersona> getPersona() {
        return organization.getAllPersona();
    }

    /**
     * Returns a {@code Collection} of {@code ITangibleObject} in the {@code Organization}.
     *
     * @return a {@code Collection} of {@code ITangibleObject} in the {@code Organization}.
     */
    protected Collection<ITangibleObject> getTangibleObjects() {
        return organization.getTangibleObjects();
    }


    /**
     * Locks the data.
     */
    protected void lockData() {
        organization.lockData();
    }

    @Override
    public void populateCapabilitiesOfDisplayObject(
            final IDisplayInformation displayInformation) {
        final Map<String, String> fields = new HashMap<>();
        displayInformation.addCapability(getIdentifier(), fields);
    }


    /**
     * Removes the given {@code IIntangibleObject} from the {@code Organization}.
     *
     * @param intangibleObject the {@code IIntangibleObject} to be removed.
     */
    protected void removeIntangibleObject(
            final IIntangibleObject intangibleObject) {
        organization.removeIntangibleObject(intangibleObject);
    }

    /**
     * Removes the given {@code ITangibleObject} from the {@code Organization}.
     *
     * @param tangibleObject the {@code ITangibleObject} to be removed.
     */
    protected void removeTangibleObject(
            final ITangibleObject tangibleObject) {
        organization.removeTangibleObject(tangibleObject);
    }

    @Override
    public abstract void reset();


    @Override
    public Element toElement(final Document document) {
        final Element capability = document
                .createElement(ELEMENT_CAPABILITY);
        capability.setAttribute(ATTRIBUTE_TYPE, getClass()
                .getSimpleName());
        capability.setAttribute(ATTRIBUTE_PACKAGE, getClass()
                .getPackage().getName());
        return capability;
    }

    /**
     * Unlocks the data.
     */
    protected void unlockData() {
        organization.unlockData();
    }

    /**
     * Updates the change list with the given {@code DisplayInformation}.
     *
     * @param displayInformation the {@code DisplayInformation} to be added to the change list.
     */
    protected void updateChangeList(
            final IDisplayInformation displayInformation) {
        organization.notifyDisplayChanges(displayInformation);
    }


}
