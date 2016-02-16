package edu.ksu.cis.macr.aasis.agent.persona.factory;

import edu.ksu.cis.macr.aasis.agent.persona.ICapability;
import edu.ksu.cis.macr.aasis.agent.persona.IOrganization;
import edu.ksu.cis.macr.aasis.agent.persona.IPersona;
import edu.ksu.cis.macr.aasis.spec.OrganizationFocus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * The {@code ProxyPersonaFactory} assists with the creation of persona in external organizations.  An agent can use
 * these to determine assignments which are issued to the proxy for transmission to the remote agent.
 */
public class ProxyPersonaFactory extends PersonaFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyPersonaFactory.class);
    private static final boolean debug = false;


    /**
     * Returns the {@code Class} of an {@code ICapability}.
     *
     * @param className the {@code String} representing * the {@code Class} of an {@code ICapability}.
     * @return the {@code Class} of an {@code ICapability}.
     */
    public static Class<? extends ICapability> getCapabilityActionClass(
            final String className) {
        try {
            if (debug)
                LOG.debug("In getCapabilityActionClass    {}", Class.forName(className).asSubclass(ICapability.class));
            return Class.forName(className).asSubclass(ICapability.class);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the {@code Constructor} of the {@code ICapability} {@code Class}.
     *
     * @param capabilityClass the {@code Class} of * the {@code ICapability} to get * the {@code Constructor}.
     * @param parameterTypes  the parameters' type for * the {@code Constructor}.
     * @return the {@code Constructor} of * the {@code ICapability} .
     */
    public static Constructor<? extends ICapability> getCapabilityActionConstructor(
            final Class<? extends ICapability> capabilityClass,
            final Class<?>... parameterTypes) {
        Constructor<? extends ICapability> constructor = null;
        try {
            if (debug)
                LOG.debug("In getCapabilityActionConstructor    {} with params {}", capabilityClass, parameterTypes);
            constructor = capabilityClass.getConstructor(parameterTypes);
        } catch (SecurityException e) {
            LOG.error("Security exception in capability action constructor. {}", e.getMessage());
            System.exit(-33);
        } catch (NoSuchMethodException e) {
            LOG.error("No capability action constructor could be found. {}", e.getMessage());
            System.exit(-33);
        }
        return constructor;
    }

    /**
     * Setup agents with the given list of agents into the given organization.
     *
     * @param agentsList the node list of agents.
     * @param org        the {@code IOrganization} to set up the agents.
     */
    public static void setupPersonaList(NodeList agentsList, IOrganization org) {
        agentsList = Objects.requireNonNull(agentsList,
                "ERROR - persona cannot be null.");
        if (agentsList.getLength() < 1) {
            LOG.error("Error: Agent list can't be empty.");
            return;
        }
        for (int i = 0; i < agentsList.getLength(); i++) {
            if (debug) LOG.debug("");
            if (debug)
                LOG.info("***********************  {}: GETTING CONSTRUCTORS FOR PERSONA TYPE {} of {} ************************************", org.getName(), i + 1, agentsList.getLength());

            final Element agentsElement = (Element) agentsList.item(i);
            final String stringPackage = getPackageName(agentsElement);
            final String agentType = getType(agentsElement);

            final NodeList orgList = agentsElement.getElementsByTagName(ELEMENT_ORGANIZATION);
            final Node orgNode = orgList.item(0);
            Element orgElement = null;
            if (1 == orgNode.getNodeType()) {
                orgElement = (Element) orgNode;
            }

            final String orgControlPackage = getPackageName(orgElement);
            final String orgControlType = getType(orgElement);
            if (debug) LOG.debug("orgControlType: {}", orgControlType);

            final AttributeConstructorValues[] attributes = setupAttributes(agentsElement
                    .getElementsByTagName(ELEMENT_ATTRIBUTE));

            final ICapabilityConstructorValues[] capabilityValues = setupCapabilities(
                    agentsElement.getElementsByTagName(ELEMENT_CAPABILITY), org);

            final NodeList personaList = agentsElement.getElementsByTagName(ELEMENT_AGENT);

            for (int j = 0; j < personaList.getLength(); j++) {
                if (debug) LOG.debug("Instantiating an external persona.");
                final Node personaElement = personaList.item(j);
                final String personaName = personaElement.getFirstChild().getNodeValue();
                if (debug) LOG.info("Instantiating persona {}. Getting {} constructor.", personaName, agentType);

                Constructor<? extends IPersona> sec_con = ProxyPersonaFactory.getExecutionComponentConstructor(
                        ProxyPersonaFactory.getExecutionComponentClass(
                                stringPackage + "." + agentType)
                );
                if (debug)
                    LOG.debug("Organization's top goal is: {}", org.getOrganizationSpecification().getTopGoal());
                if (debug)
                    LOG.debug("Constructing EC persona {} which get an associated control component.", personaName);

                // create a new Execution Component IAgent for this persona (which has a CC either Master or Slave).............................

                final IPersona executor = Objects
                        .requireNonNull(createPersona(sec_con, org, personaName, orgElement, OrganizationFocus.External), "Error - The executionComponent is null.");
                if (debug) LOG.debug("Created EC {}. Getting attribute constructor values.", personaName);

                for (final AttributeConstructorValues a : attributes) {
                    executor.addAttribute(newAttribute(a.constructor, a.parameters));
                }


                if (debug) LOG.debug("Getting {} EC capability constructor values.", capabilityValues.length);
                for (final ICapabilityConstructorValues v : capabilityValues) {
                    if (debug) LOG.debug("\tAdding: {}", v.getConstructor());
                    v.getParameters()[0] = executor;
                    if (debug) LOG.debug("Adding capability {} to excutor ({}).", v.getConstructor(),
                            executor.getIdentifierString());
                    executor.addCapability(newCapability(v.getConstructor(), v.getParameters()));
                }
            }
        }
    }

    private static String getPackageName(final Element xmlElement) {
        Objects.requireNonNull(xmlElement, "xml Element cannot be null");
        final String packageName = xmlElement.getAttribute(ATTRIBUTE_PACKAGE);
        return packageName;
    }

    private static String getType(final Element xmlElement) {
        Objects.requireNonNull(xmlElement, "xml Element cannot be null");
        final String type = xmlElement.getAttribute(ATTRIBUTE_TYPE);
        if (debug) LOG.info("Type: {}", type);
        return type;
    }

    /**
     * Returns the {@code Constructor} of the {@code IAgent} {@code Class}.
     *
     * @param executionComponentClass the {@code Class} of the {@code IAgent} to get the {@code Constructor}.
     * @return the {@code Constructor} of the {@code IAgent}.
     */
    public static Constructor<? extends IPersona> getExecutionComponentConstructor(
            final Class<? extends IPersona> executionComponentClass) {
        if (debug) LOG.debug("Getting execution component constructor for {}.", executionComponentClass);
        Constructor<? extends IPersona> constructor = null;
        try {
            constructor = executionComponentClass.getConstructor(IOrganization.class, String.class, Element.class, OrganizationFocus.class);
        } catch (Exception e) {
            LOG.error("ERROR: getting execution component constructor for {}.", executionComponentClass);
            System.exit(-91);
        }
        return constructor;
    }

    /**
     * Returns the {@code Class} of an {@code IAgent}.
     *
     * @param className the {@code String} representing * the {@code Class} of an {@code IAgent}.
     * @return the {@code Class} of an {@code IAgent}.
     */
    public static Class<? extends IPersona> getExecutionComponentClass(
            final String className) {
        if (debug) LOG.debug("getting execution component class for {}", className);
        try {
            return Class.forName(className).asSubclass(IPersona.class);
        } catch (final ClassNotFoundException e) {
            LOG.error("ERROR: ClassNotFoundException for className={}.",
                    className);
            System.exit(-97);
        }
        return null;
    }

    /**
     * Instantiates a new instance of {@code IAgent} with the given {@code Constructor}, organization, identifier, and
     * organization.
     *
     * @param constructor the {@code Constructor} for the new instance.
     * @param org         the organization where the new instance goes.
     * @param identifier  the {@code String} by which to identify the new instance.
     * @param knowledge   the organization information for the agent.
     * @return a new instance of {@code IAgent}.
     */
    public static IPersona createPersona(Constructor<? extends IPersona> constructor,
                                         IOrganization org, String identifier, Element knowledge, OrganizationFocus focus) {
        if (debug) LOG.info("Creating new execution component for {}.", identifier);
        constructor = Objects.requireNonNull(constructor, "New EC agent (used to be SEC) constructor cannot be null.");
        org = Objects.requireNonNull(org, "New EC organization cannot be null.");
        identifier = Objects.requireNonNull(identifier, "New execution component identifier cannot be null.");
        return instantiate(constructor, org, identifier, knowledge, focus);
    }

    /**
     * Instantiates a new instance of {@code ICapability} with the given {@code Constructor} and parameters.
     *
     * @param constructor the {@code Constructor} for the new instance.
     * @param parameters  the parameters for the {@code Constructor}.
     * @return a new instance of {@code ICapability}.
     */
    public static ICapability newCapability(final Constructor<? extends ICapability> constructor, final Object... parameters) {
        return instantiate(constructor, parameters);
    }
}
