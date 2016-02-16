package edu.ksu.cis.macr.aasis.agent.admin;


import edu.ksu.cis.macr.aasis.agent.persona.AbstractOrganizationCapability;
import edu.ksu.cis.macr.aasis.agent.persona.IOrganization;
import edu.ksu.cis.macr.aasis.agent.persona.IPersona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RunGoalModelCapability extends AbstractOrganizationCapability {
    private static final Logger LOG = LoggerFactory.getLogger(RunGoalModelCapability.class);
    private static final boolean debug = false;


    /**
     * Construct a new {@code RunGoalModelCapability} instance.
     *
     * @param owner - the agent possessing this capability.
     * @param org   - the immediate organization in which this agent operates.
     */
    public RunGoalModelCapability(
            final IPersona owner,
            final IOrganization org) {
        super(RunGoalModelCapability.class, owner, org);
    }

    @Override
    public void reset() {
    }

    @Override
    public double getFailure() {
        return 0;
    }

    @Override
    public Element toElement(final Document document) {
        final Element capability = super.toElement(document);
        return capability;
    }
}
