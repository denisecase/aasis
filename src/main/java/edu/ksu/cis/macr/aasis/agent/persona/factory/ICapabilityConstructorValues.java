package edu.ksu.cis.macr.aasis.agent.persona.factory;



import edu.ksu.cis.macr.aasis.agent.persona.ICapability;

import java.lang.reflect.Constructor;

/**
 * Interface for the capability constructor values so we can extend it to new custom classes.
 */
public interface ICapabilityConstructorValues {
    public static ICapabilityConstructorValues createCapabilityConstructorValues(
            final Constructor<? extends ICapability> constructor,
            final Object... parameters) {
        return new PersonaFactory.CapabilityConstructorValues(constructor, parameters);
    }

    Constructor<? extends ICapability> getConstructor();

    Object[] getParameters();
}
