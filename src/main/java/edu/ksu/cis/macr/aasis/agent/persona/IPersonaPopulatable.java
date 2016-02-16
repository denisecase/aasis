package edu.ksu.cis.macr.aasis.agent.persona;


import edu.ksu.cis.macr.obaa_pp.objects.IAttributable;
import edu.ksu.cis.macr.obaa_pp.objects.IIntangibleObject;
import edu.ksu.cis.macr.obaa_pp.objects.ITangibleObject;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

import java.util.Collection;

/**
 * Provides an interface for the basic functionality required to populate a goal-driven {@code Organization} with agents and other environment objects.
 */
public interface IPersonaPopulatable {

    /**
     Add a persona to the organization.

     @param persona - the persona to be added.
     @return - true if added, false if not.
     */
    boolean addAgent(IPersona persona);

    /**
     * Add an intangible object to the organization.
     *
     * @param intangibleObject - the intangible object to be added.
     * @return - true if added, false if not.
     */
    boolean addIntangibleObject(IIntangibleObject intangibleObject);

    /**
     * Add an environment object to the organization.
     *
     * @param environmentObject - the environment object  to be added.
     * @return - true if added, false if not.
     */
    boolean addObject(IAttributable environmentObject);

    /**
     * Add a tangible object to the organization.
     *
     * @param tangibleObject - the tangible object to be added.
     * @return - true if added, false if not.
     */
    boolean addTangibleObject(ITangibleObject tangibleObject);
    // contains agents, objects................................................

    /**
     * Check to see if the organization includes an {@code Agent} with the given identifier.
     *
     * @param identifier - the unique identifier of the agent
     * @return - true if the agent exists in the organization, false if not
     */
    boolean containsAgent(UniqueIdentifier identifier);

    /**
     * Returns whether the given {@code IIntangibleObject} exists in the {@code Organization}.
     *
     * @param intangibleObject the {@code IIntangibleObject} to be checked.
     * @return {@code true} if the {@code IIntangibleObject} exists in the organization, {@code false} otherwise.
     */
    boolean containsIntangibleObject(IIntangibleObject intangibleObject);

    /**
     * Returns whether the given {@code ITangibleObject} exists in the {@code Organization}.
     *
     * @param tangibleObject the {@code ITangibleObject} to be checked.
     * @return {@code true} if the {@code ITangibleObject} exists in the organization, {@code false} otherwise.
     */
    boolean containsTangibleObject(ITangibleObject tangibleObject);
    // get agents, objects.....................................................

    /**
     Returns the {@code IExecutionComponent} by the given identifier.

     @param identifier the unique identifier of the {@code IAgent} to retrieve.
     @return the {@code IExecutionComponent} if it exists, {@code null} otherwise.
     */
    IPersona getAgent(UniqueIdentifier identifier);

    /**
     Returns the {@code Collection} of {@code IAgent} in the {@code Organization}.

     @return the {@code Collection} of {@code IAgent}.
     */
    Collection<IPersona> getAllPersona();

    /**
     * Returns the {@code Collection} of {@code IIntangibleObject} in the {@code Organization}.
     *
     * @return the {@code Collection} of {@code IIntangibleObject}.
     */
    Collection<IIntangibleObject> getIntangibleObjects();

    /**
     * Returns the {@code Collection} of {@code IAttributable} in the {@code Organization}.
     *
     * @return the {@code Collection} of {@code IAttributable}.
     */
    Collection<IAttributable> getObjects();

    /**
     * Returns a {@code Collection} of {@code IAttributable} of the given {@code Class}.
     *
     * @param objectClass the {@code Class} to retrieve.
     * @return a {@code Collection} of {@code IAttributable} of the given {@code Class}, {@code null} otherwise.
     */
    Collection<IAttributable> getObjectsByClass(
            Class<? extends IAttributable> objectClass);

    /**
     * Returns the {@code Collection} of {@code ITangibleObject} in the {@code Organization}.
     *
     * @return the {@code Collection} of {@code ITangibleObject}.
     */
    Collection<ITangibleObject> getTangibleObjects();

    /**
     Removes the given {@code IPersona} from the {@code Organization}.

     @param persona the {@code IPersona} to be removed.
     */
    void removePersona(IPersona persona);

    /**
     * Removes the given {@code IIntangibleObject} from the {@code Organization}.
     *
     * @param intangibleObject the {@code IIntangibleObject} to be removed.
     */
    void removeIntangibleObject(IIntangibleObject intangibleObject);

    /**
     * Removes the given {@code ITangibleObject} from the {@code Organization}.
     *
     * @param tangibleObject the {@code ITangibleObject} to be removed.
     */
    void removeTangibleObject(ITangibleObject tangibleObject);
}
