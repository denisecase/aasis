package edu.ksu.cis.macr.aasis.agent.admin;


import edu.ksu.cis.macr.aasis.agent.persona.IOrganization;
import edu.ksu.cis.macr.obaa_pp.objects.IAttributable;
import edu.ksu.cis.macr.obaa_pp.objects.IIntangibleObject;
import edu.ksu.cis.macr.obaa_pp.objects.ITangibleObject;
import edu.ksu.cis.macr.obaa_pp.objects.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Initializes basic (non-application-specific) organizations from XML.
 */
public abstract class OrganizationFactory extends ObjectFactory {
    private static final Logger LOG = LoggerFactory.getLogger(OrganizationFactory.class);
    private static final boolean debug = false;

    /**
     * {@code ATTRIBUTE_CATEGORY}
     */
    public static final String ATTRIBUTE_CATEGORY = "category";
    /**
     * {@code ELEMENT_ATTRIBUTE}
     */
    public static final String ELEMENT_ATTRIBUTE = "attribute";
    /**
     * {@code ELEMENT_COMPLEX}
     */
    public static final String ELEMENT_COMPLEX = "complex";
    /**
     * {@code ELEMENT_OBJECTS} is the description of object(s).
     */
    public static final String ELEMENT_OBJECTS = "objects";
    /**
     * {@code ELEMENT_SIMPLE}
     */
    public static final String ELEMENT_SIMPLE = "simple";

    /**
     * Loads up the given {@code Organization} with objects from the given file.
     *
     * @param objectFilename the {@code String} representing the file to load from.
     * @param organization   the {@code Organization} to load into.
     */
    public static void loadObjectFile(final String objectFilename, final IOrganization organization) {
        if (objectFilename == null || objectFilename.isEmpty()) {
            return;
        } // optional
        loadObjectFile(new File(objectFilename), organization);
    }

    /**
     * Loads up the given {@code Organization} with objects from the given {@code File}.
     *
     * @param objectFile   the {@code File} to load from.
     * @param organization the {@code Organization} to load into.
     */
    public static void loadObjectFile(final File objectFile, IOrganization organization) {
        if (objectFile == null) {
            return;
        } // it's optional
        if (objectFile.exists()) {
            try {
                DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
                DocumentBuilder b = f.newDocumentBuilder();
                NodeList n = b.parse(objectFile).getElementsByTagName(ELEMENT_OBJECTS);
                if (debug) LOG.debug("loadObjectFile() object node list with {} items from" +
                        " file {}", n.getLength(), objectFile.getAbsolutePath());
                setupObjects(n, organization);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                LOG.error("Error loading object file: {}", objectFile.getPath());
            }
        } else {
            if (debug) LOG.debug("{} does not exist.", objectFile);
        }
    }

    /**
     * Set up the {@code Organization} with objects from the given list of objects.
     *
     * @param objectsList  the list of objects.
     * @param organization the {@code Organization} to set up the objects.
     */
    public static void setupObjects(final NodeList objectsList,IOrganization organization) {
        for (int i = 0; i < objectsList.getLength(); i++) {
            if (debug) LOG.debug("Setting up {} environment objects.", objectsList.getLength());
            final Element objectsElement = (Element) objectsList.item(i);
            final String packageName = objectsElement.getAttribute(ATTRIBUTE_PACKAGE);
            final String className = objectsElement.getAttribute(ATTRIBUTE_TYPE);
            final String categoryName = objectsElement.getAttribute(ATTRIBUTE_CATEGORY);

            final AttributeConstructorValues[] attributes = setupAttributes(
                    objectsElement.getElementsByTagName(ELEMENT_ATTRIBUTE)
            );
            final NodeList simpleList = objectsElement.getElementsByTagName(ELEMENT_SIMPLE);

            switch (categoryName) {
                case "tangible":
                    setupSimpleTangibleObjects(getTangibleObjectClass(packageName + "." + className), attributes, simpleList);
                    break;
                case "intangible":
                    setupSimpleIntangibleObjects(getIntangibleObjectClass(packageName + "." + className), attributes, simpleList);
                    break;
                case "basic":
                    ArrayList<IAttributable> list = setupEnvironmentObjects(getWorldstateObjectClass(packageName + "." + className),
                            attributes, simpleList);
                    list.forEach(organization::addObject);
                    break;
                default:
                    LOG.error("Unknown Category: \'{}\'", categoryName);
                    System.exit(-1);
                    break;
            }
        }
    }

    /**
     * Returns the {@code Class} of an {@code ITangibleObject}.
     *
     * @param className the {@code String} representing * the {@code Class} of an {@code ITangibleObject}.
     * @return the {@code Class} of an {@code ITangibleObject}.
     */
    public static Class<? extends ITangibleObject> getTangibleObjectClass(
            final String className) {
        try {
            return Class.forName(className).asSubclass(ITangibleObject.class);
        } catch (final ClassNotFoundException e) {
            LOG.error("ERROR {}", e.getMessage());
            System.exit(-811);
        }
        return null;
    }

    /**
     * Returns the {@code Class} of an {@code IIntangibleObject}
     *
     * @param className the {@code String} representing * the {@code Class} of an {@code IIntangibleObject}.
     * @return the {@code Class} of an {@code IIntangibleObject}.
     */
    public static Class<? extends IIntangibleObject> getIntangibleObjectClass(final String className) {
        try {
            return Class.forName(className).asSubclass(IIntangibleObject.class);
        } catch (final ClassNotFoundException e) {
            LOG.error("ERROR {}", e.getMessage());
            System.exit(-810);
        }
        return null;
    }

    /**
     * Returns the {@code Class} of an {@code IAttributable}
     *
     * @param className the {@code String} representing * the {@code Class} of an {@code IIntangibleObject}.
     * @return the {@code Class} of an {@code IAttributable}.
     */
    public static Class<? extends IAttributable> getWorldstateObjectClass(final String className) {
        try {
            return Class.forName(className).asSubclass(IAttributable.class);
        } catch (final ClassNotFoundException e) {
            LOG.error("ERROR");
            System.exit(-806);
        }
        return null;
    }

 }
