package edu.ksu.cis.macr.aasis.agent.persona;


import edu.ksu.cis.macr.organization.model.Capability;
import edu.ksu.cis.macr.organization.model.PossessesRelation;
import edu.ksu.cis.macr.organization.model.identifiers.ClassIdentifier;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CapabilityManager implements IEquippable {
    private static final Logger LOG = LoggerFactory.getLogger(CapabilityManager.class);
    /**
     * The set of {@code ICapability} that the {@code IAgent} possesses.
     */
    public final Map<UniqueIdentifier, CapabilityWrapper> capabilities = new HashMap<>();
    private UniqueIdentifier agentIdentifier;


    public CapabilityManager(UniqueIdentifier agentIdentifier) {
        if (this.agentIdentifier == null) {
            agentIdentifier = new UniqueIdentifier() {
                private static final long serialVersionUID = -8893433534203345886L;

                @Override
                public boolean equals(final Object o) {
                    return false;
                }

                @Override
                public int hashCode() {
                    return 0;
                }

                @Override
                public String toString() {
                    return "";
                }
            };
        }
        this.agentIdentifier = agentIdentifier;
    }

    @Override
    public Collection<ICapability> getCapabilities() {
        final Collection<ICapability> result = capabilities.values().stream().map(CapabilityWrapper::getCapability).collect(Collectors.toList());
        return result;
    }

    @Override
    public Map<UniqueIdentifier, ICapability> getCapabilitiesMapping() {
        final Map<UniqueIdentifier, ICapability> result = new HashMap<>();
        for (final Map.Entry<UniqueIdentifier, CapabilityWrapper> entry : capabilities
                .entrySet()) {
            result.put(entry.getKey(), entry.getValue().getCapability());
        }
        return result;
    }

    @Override
    public <CapabilityType extends Capability> CapabilityType getCapability(
            final Class<CapabilityType> capabilityClass) {
        final ClassIdentifier capabilityIdentifier = new ClassIdentifier(
                capabilityClass);
        final CapabilityWrapper capabilityWrapper = capabilities
                .get(capabilityIdentifier);
        if (capabilityWrapper == null) {
            return null;
        }
        return capabilityClass.cast(capabilityWrapper.getCapability());
    }

    @Override
    public double getCapabilityScore(final Capability capability) {
        //LOG.debug("getCapabilityScore() for {}", capability.toString());
        final ICapability iDisplayableCapability = capabilities.get(
                capability.getIdentifier()).getCapability();
        //return iDisplayableCapability == null ? PossessesRelation.MIN_SCORE
        //        : PossessesRelation.MAX_SCORE - iDisplayableCapability.getFailure();
        double score = PossessesRelation.MIN_SCORE;
        if (iDisplayableCapability != null) {
            score = PossessesRelation.MAX_SCORE - iDisplayableCapability.getFailure();
        }
        return score;
    }

    @Override
    public void reset() {

        capabilities.values().stream().filter(capability ->
                capability.getCapability().getClass() != this.getClass()).forEach(capability -> capability.getCapability().reset());
    }

    /**
     * Adds a new {@code ICapability} to the {@code IAgent}. If the {@code replace} parameter is {@code true}, then a
     * replacement operation will be done to ensure that all appropriate instances are replaced with the given {@code ICapability}.
     *
     * @param capability the {@code ICapability} to be added.
     * @param replace    {@code true} if a replacement is to be executed, {@code false} otherwise.
     */
    public void addCapability(final ICapability capability, final boolean replace) {
        if (replace) {
            final Collection<UniqueIdentifier> entriesToRemove = new ArrayList<>();
            for (final Map.Entry<UniqueIdentifier, CapabilityWrapper> entry : capabilities
                    .entrySet()) {
                final UniqueIdentifier key = entry.getKey();
                final CapabilityWrapper value = entry.getValue();
                if (value.getCapabilityClass().isAssignableFrom(
                        capability.getClass())) {
                    /*
                     * if the given capability is a subclass, all prior mappings
                     * to the 'value' instance needs to be replaced with the
                     * given instance
                     */
                    value.setCapability(capability);
                } else if (capability.getClass().isAssignableFrom(
                        value.getCapabilityClass())) {
                    /*
                     * if the given capability is a superclass, then some
                     * mappings of 'value' needs to be removed
                     */
                    entriesToRemove.add(key);
                }
            }
            entriesToRemove.forEach(capabilities::remove);
        }
        capabilities.put(capability.getIdentifier(), new CapabilityWrapper(
                capability));
    }

    /**
     * The {@code CWrapper} class encapsulates a {@code ICapability} and a {@code Class}.
     */
    public static class CapabilityWrapper {
        /**
         * The {@code Class} of the {@code ICapability}.
         */
        private final Class<? extends ICapability> capabilityClass;
        /**
         * The {@code ICapability}.
         */
        private ICapability capability;

        /**
         * Constructs a new instance of {@code CWrapper}.
         *
         * @param capability the {@code ICapability}.
         */
        public CapabilityWrapper(final ICapability capability) {
            setCapability(capability);
            capabilityClass = capability.getClass();
        }

        /**
         * Returns the {@code ICapability}.
         *
         * @return the {@code ICapability}.
         */
        ICapability getCapability() {
            return capability;
        }

        /**
         * Sets the {@code ICapability}.
         *
         * @param capability the {@code ICapability} to set.
         */
        void setCapability(final ICapability capability) {
            this.capability = capability;
        }

        /**
         * Returns the {@code Class} of the {@code ICapability}.
         *
         * @return the {@code Class} of * * * * * * * * * * * * * * * * * the {@code ICapability}.
         */
        Class<? extends ICapability> getCapabilityClass() {
            return capabilityClass;
        }
    }
}
