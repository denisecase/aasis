package edu.ksu.cis.macr.aasis.agent.persona.factory;

import edu.ksu.cis.macr.aasis.agent.persona.ICapability;
import edu.ksu.cis.macr.aasis.agent.persona.IOrganization;
import edu.ksu.cis.macr.aasis.agent.persona.IPersona;
import edu.ksu.cis.macr.aasis.spec.OrganizationFocus;
import edu.ksu.cis.macr.obaa_pp.objects.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * The {@code PersonaFactory} assists with the creation of persona in organizations.
 */
public class PersonaFactory extends ObjectFactory {
    /**
     * {@code ELEMENT_AGENT}
     */
    protected static final String ELEMENT_AGENT = "agent";
    /**
     * {@code ELEMENT_ATTRIBUTE}
     */
    protected static final String ELEMENT_ATTRIBUTE = "attribute";
    /**
     * {@code ELEMENT_CAPABILITY}
     */
    protected static final String ELEMENT_CAPABILITY = "capability";
    /**
     * {@code ELEMENT_ORGANIZATION}
     */
    protected static final String ELEMENT_ORGANIZATION = "organization";
    private static final Logger LOG = LoggerFactory.getLogger(PersonaFactory.class);
    private static final boolean debug = false;


//  /**
//   Setup agents with the given list of agents into the given organization.
//
//   @param agentsList the node list of agents.
//   @param organization the {@code IOrganization} to set up the agents.
//   */
//  public static void setupPersonaList(NodeList agentsList, IOrganization organization) {
//    agentsList = Objects.requireNonNull(agentsList,
//            "ERROR - persona cannot be null.");
//    if (agentsList.getLength() < 1) {
//      LOG.error("Error: Agent list can't be empty.");
//      return;
//    }
//    for (int i = 0; i < agentsList.getLength(); i++) {
//      if (debug) LOG.debug("");
//      LOG.info("***********************  {}: GETTING CONSTRUCTORS FOR PERSONA TYPE {} of {}" +
//                      " setupPersonaList() ************************************",
//              organization.getName(), i + 1, agentsList.getLength());
//
//      final Element agentsElement = (Element) agentsList.item(i);
//      final String stringPackage = getPackageName(agentsElement);
//      final String agentType = getType(agentsElement);
//
//      final NodeList orgList = agentsElement.getElementsByTagName(ELEMENT_ORGANIZATION);
//      final Node orgNode = orgList.item(0);
//      Element orgElement = null;
//      if (1 == orgNode.getNodeType()) {
//        orgElement = (Element) orgNode;
//      }
//
//      final String orgControlPackage = getPackageName(orgElement);
//      final String orgControlType = getType(orgElement);
//
//      LOG.info("orgControlType: {}", orgControlType);
//
//
//      final AttributeConstructorValues[] attributes = setupAttributes(agentsElement
//              .getElementsByTagName(ELEMENT_ATTRIBUTE));
//
//      final CapabilityConstructorValues[] capabilityValues = setupCapabilities(
//              agentsElement.getElementsByTagName(ELEMENT_CAPABILITY),
//              organization);
//
//      final NodeList personaList = agentsElement
//              .getElementsByTagName(ELEMENT_AGENT);
//
//      for (int j = 0; j < personaList.getLength(); j++) {
//
//        LOG.info("");
//        LOG.info("....................INSTANTIATING PERSONA OF THIS TYPE .....................................................");
//
//        final Node agentElement = personaList.item(j);
//        final String personaName = agentElement.getFirstChild().getNodeValue();
//        LOG.info("Instantiating persona {}. Getting {} constructor.", personaName, agentType);
//
//        Constructor<? extends IPersona> sec_con = PersonaFactory.getExecutionComponentConstructor(
//                PersonaFactory.getExecutionComponentClass(
//                        stringPackage + "." + agentType));
//        LOG.info("This  organization's top goal is: {}", organization.getOrganizationSpecification().getTopGoal());
//        LOG.info("Constructing an EC persona:           {} which will be given an associated CC component.", personaName);
//
//        // create a new Execution Component IAgent for this persona (which has a CC either Master or Slave).............................
//
//        createEC(organization, orgElement, attributes, capabilityValues, personaName, sec_con);
//      }
//    }
//  }

    protected static void createEC(IOrganization organization, Element orgElement, AttributeConstructorValues[] attributes, ICapabilityConstructorValues[] capabilityValues, String agentName, Constructor<? extends IPersona> sec_con) {
        final IPersona persona = Objects
                .requireNonNull(newExecutionComponent(sec_con, organization, agentName,
                                orgElement, OrganizationFocus.Agent),
                        "Error - The executionComponent is null.");

        LOG.info("Created EC {}. Getting attribute constructor values.", agentName);
        for (final AttributeConstructorValues a : attributes) {
            persona.addAttribute(newAttribute(a.constructor, a.parameters));
        }
        if (debug) LOG.debug("Getting {} EC capability constructor values.", capabilityValues.length);
        for (final ICapabilityConstructorValues v : capabilityValues) {
            if (debug) LOG.debug("\tAdding: {}", v.getConstructor());
            v.getParameters()[0] = persona;
            if (debug) LOG.debug("Adding capability {} to execution_component {}.", v.getConstructor(),
                    persona.getIdentifierString());
            persona.addCapability(newCapability(v.getConstructor(), v.getParameters()));
        }
    }

    /**
     * Instantiates a new instance of {@code IAgent} with the given {@code Constructor}, organization, identifier, and
     * organization.
     *
     * @param constructor  the {@code Constructor} for the new instance.
     * @param organization the organization where the new instance goes.
     * @param identifier   the {@code String} by which to identify the new instance.
     * @param knowledge    the organization information for the agent.
     * @return a new instance of {@code IAgent}.
     */
    public static IPersona newExecutionComponent(
            Constructor<? extends IPersona> constructor,
            IOrganization organization, String identifier, Element knowledge, OrganizationFocus focus) {
        if (debug) LOG.info("Creating NEW EXECUTION COMPONENT of the type IPersona.");

        constructor = Objects.requireNonNull(constructor,
                "New execution component agent (used to be SEC) constructor cannot be null.");
        organization = Objects.requireNonNull(organization,
                "New execution component organization (organization) cannot be null.");
        identifier = Objects.requireNonNull(identifier, "New execution component identifier cannot be null.");

        return instantiate(constructor, organization, identifier, knowledge, focus);
    }

    /**
     * Instantiates a new instance of {@code ICapability} with the given {@code Constructor} and parameters.
     *
     * @param constructor the {@code Constructor} for the new instance.
     * @param parameters  the parameters for the {@code Constructor}.
     * @return a new instance of {@code ICapability}.
     */
    public static ICapability newCapability(
            final Constructor<? extends ICapability> constructor,
            final Object... parameters) {
        return instantiate(constructor, parameters);
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
            return Class.forName(className).asSubclass(
                    IPersona.class);
        } catch (final ClassNotFoundException e) {
            LOG.error("ERROR: ClassNotFoundException for className={}.",
                    className);
            System.exit(-92);
        }
        return null;
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
            constructor = executionComponentClass.getConstructor(
                    IOrganization.class, String.class, Element.class, OrganizationFocus.class);
        } catch (Exception e) {
            LOG.error("ERROR: getting execution component constructor for {}.",
                    executionComponentClass);
            System.exit(-91);
        }
        return constructor;
    }

    private static String getPackageName(final Element xmlElement) {
        Objects.requireNonNull(xmlElement, "xml Element cannot be null");
        final String packageName = xmlElement
                .getAttribute(ATTRIBUTE_PACKAGE);
        return packageName;
    }

    private static String getType(final Element xmlElement) {
        Objects.requireNonNull(xmlElement, "xml Element cannot be null");
        final String type = xmlElement.getAttribute(ATTRIBUTE_TYPE);
        LOG.info("Type: {}", type);
        return type;
    }

    private static String getValue(final Element xmlElement) {
        Objects.requireNonNull(xmlElement, "xml Element cannot be null");
        return xmlElement.getFirstChild().getNodeValue();
    }

    /**
     * Save agents from the given {@code IOrganization} to the specified file.
     *
     * @param agentFilename the {@code String} representing the file to save to.
     * @param organization  the {@code IOrganization} to save from.
     */
    public static void savePersonaFileByName(final String agentFilename,
                                             final IOrganization organization) {
        savePersonaFile(new File(agentFilename), organization);
    }

    /**
     * Save agents from the given {@code IOrganization} to the specified {@code File}.
     *
     * @param agentFile    the {@code File} to save to.
     * @param organization the {@code IOrganization} to save from.
     */
    public static void savePersonaFile(final File agentFile,
                                       final IOrganization organization) {
        // nothing
    }

    /**
     * Setup the given list of capabilityValues.
     *
     * @param capabilityList the list of capabilityValues.
     * @param organization   the {@code IOrganization} in which the capabilityValues will interact.
     * @return list of capabilityValues.
     */
    protected static ICapabilityConstructorValues[] setupCapabilities(
            final NodeList capabilityList, IOrganization organization) {
        if (debug) LOG.debug("Setting up {} capabilities.", capabilityList.getLength());
        final int defaultParameters = 2;
        final ICapabilityConstructorValues[] capability = new CapabilityConstructorValues[capabilityList
                .getLength()];
        for (int j = 0; j < capabilityList.getLength(); j++) {
            final Element capabilityElement = (Element) capabilityList.item(j);

            final String capabilityPackageName = capabilityElement.getAttribute(ATTRIBUTE_PACKAGE);
            final String capabilityClassName = capabilityElement.getAttribute(ATTRIBUTE_TYPE);

            if (debug) LOG.debug("Setting up {}", capabilityPackageName + "." + capabilityClassName);

            //  final NodeList parameterList = capabilityElement.getElementsByTagName
            //        (ELEMENT_PARAMETER);
            final Element capParameterElement = getDirectChild(capabilityElement, ELEMENT_PARAMETER);

            if (capParameterElement != null) {
                final NodeList parameterList = capabilityElement.getElementsByTagName(ELEMENT_PARAMETER);

                if (debug)
                    LOG.debug("\t   {} has {} parameters. ", capabilityPackageName + "." + capabilityClassName, parameterList.getLength());

                final Class<?>[] parameterTypes = new Class[defaultParameters + parameterList.getLength()];
                final Object[] parameters = new Object[defaultParameters + parameterList.getLength()];

                parameterTypes[0] = IPersona.class;
                parameters[0] = null;
                parameterTypes[1] = IOrganization.class;
                parameters[1] = organization;

                for (int k = 0; k < parameterList.getLength(); k++) {
                    final Element parameterElement = (Element) parameterList
                            .item(k);

                    final String parameterType = parameterElement
                            .getAttribute(ATTRIBUTE_TYPE);
                    parameterTypes[k + defaultParameters] = ObjectFactory.getClass(parameterType);

                    if (parameterType.equals(boolean.class.getSimpleName())) {
                        parameters[k + defaultParameters] = Boolean
                                .parseBoolean(parameterElement.getFirstChild().getNodeValue());
                    } else if (parameterType.equals(byte.class.getSimpleName())) {
                        parameters[k + defaultParameters] = Byte
                                .parseByte(parameterElement.getFirstChild().getNodeValue());
                    } else if (parameterType.equals(char.class.getSimpleName())) {
                        parameters[k + defaultParameters] = parameterElement
                                .getFirstChild().getNodeValue();
                    } else if (parameterType.equals(short.class.getSimpleName())) {
                        parameters[k + defaultParameters] = Short
                                .parseShort(parameterElement.getFirstChild().getNodeValue());
                    } else if (parameterType.equals(int.class.getSimpleName())) {
                        parameters[k + defaultParameters] = Integer
                                .parseInt(parameterElement.getFirstChild().getNodeValue());
                    } else if (parameterType.equals(long.class.getSimpleName())) {
                        parameters[k + defaultParameters] = Long
                                .parseLong(parameterElement.getFirstChild().getNodeValue());
                    } else if (parameterType.equals(float.class.getSimpleName())) {
                        parameters[k + defaultParameters] = Float
                                .parseFloat(parameterElement.getFirstChild().getNodeValue());
                    } else if (parameterType.equals(double.class.getSimpleName())) {
                        parameters[k + defaultParameters] = Double
                                .parseDouble(parameterElement.getFirstChild().getNodeValue());
                    } else if (parameterType.equals(String.class.getSimpleName())) {
                        //if (debug) LOG.debug("The new param is {}", parameterElement.getFirstChild().getNodeValue());
                        //  parameters[k + defaultParameters] = parameterElement.getFirstChild().getNodeValue();
                        if (debug) LOG.debug("\tThe new param is {}", parameterElement.getFirstChild()
                                .getNodeValue());
                        parameters[k + defaultParameters] = parameterElement.getFirstChild()
                                .getNodeValue();
                    } else {
                        parameters[k + defaultParameters] = parameterElement
                                .getFirstChild().getNodeValue();
                    }
                }
                if (debug) LOG.debug("\t capability {}.{}.", capabilityPackageName, capabilityClassName);
                capability[j] = ICapabilityConstructorValues.createCapabilityConstructorValues(
                        getCapabilityActionConstructor(getCapabilityActionClass(capabilityPackageName + "." + capabilityClassName),
                                parameterTypes), parameters);
            } else {
                NodeList parameterList = capabilityElement.getElementsByTagName("missing");
                final Class<?>[] parameterTypes = new Class[defaultParameters
                        + parameterList.getLength()];
                final Object[] parameters = new Object[defaultParameters
                        + parameterList.getLength()];

                parameterTypes[0] = IPersona.class;
                parameters[0] = null;
                parameterTypes[1] = IOrganization.class;
                parameters[1] = organization;
                capability[j] = ICapabilityConstructorValues.createCapabilityConstructorValues(
                        getCapabilityActionConstructor(getCapabilityActionClass(capabilityPackageName + "." + capabilityClassName),
                                parameterTypes), parameters);
            }
        }
        return capability;
    }

    public static Element getDirectChild(Node parent, String name) {
        if (debug) LOG.debug("\t Getting direct xml children.");

        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element && name.equals(child.getNodeName())) {
                if (debug) LOG.debug("\t There's a child element for {}. ", name);
                Element elem = (Element) child;
                if (debug) LOG.debug("\t child: {}.  ", elem.getFirstChild().getNodeValue());
                return elem;
            }
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
                LOG.debug("\t In getCapabilityActionConstructor    {} with params {}", capabilityClass, parameterTypes);
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
     * Returns the {@code Class} of an {@code ICapability}.
     *
     * @param className the {@code String} representing * the {@code Class} of an {@code ICapability}.
     * @return the {@code Class} of an {@code ICapability}.
     */
    public static Class<? extends ICapability> getCapabilityActionClass(final String className) {
        try {
            if (debug)
                LOG.debug("\t Getting capability action class for {}. Must be a subclass of ICapability.", Class.forName(className));
            return Class.forName(className).asSubclass(ICapability.class);
        } catch (final Exception e) {
            LOG.error("ERROR: Could not find the code for capability {}. {}.", className, e.getCause());
            System.exit(-73);
        }
        return null;
    }



    /**
     * An encapsulation of the {@code Constructor} and the parameters for the {@code Constructor} of the {@code ICapability}.
     */
    public static class CapabilityConstructorValues implements ICapabilityConstructorValues {
        /**
         * The {@code Constructor} for the {@code ICapability}.
         */
        public final Constructor<? extends ICapability> constructor;
        /**
         * The parameters for the {@code Constructor}.
         */
        public final Object[] parameters;

        /**
         * Constructs a new instance of {@code CapabilityConstructorValues} .
         *
         * @param constructor the {@code Constructor} of the {@code ICapability}.
         * @param parameters  the parameters for the {@code Constructor}.
         */
        public CapabilityConstructorValues(
                final Constructor<? extends ICapability> constructor,
                final Object... parameters) {
            this.constructor = Objects.requireNonNull(constructor,
                    "constructor must not be null");
            this.parameters = Objects.requireNonNull(parameters,
                    "parameters must not be null");
        }

        public Constructor<? extends ICapability> getConstructor() {
            return constructor;
        }

        public Object[] getParameters() {
            return parameters;
        }
    }
}
