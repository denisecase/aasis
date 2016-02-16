package edu.ksu.cis.macr.aasis.agent.persona;

import edu.ksu.cis.macr.organization.model.Capability;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

import java.util.Collection;
import java.util.Map;

/**
 {@code ICapable} provides an interface for an organization capable of being organized by capabilities.
 */
public interface IEquippable {
  /**
   Returns a {@code Collection} of {@code Capability} that the {@code IExecutionComponent} possesses.

   @return a {@code Collection} of {@code Capability} that the {@code IExecutionComponent} possesses.
   */
  Collection<? extends Capability> getCapabilities();

  /**
   Returns a {@code Map} of a {@code UniqueIdentifier} to a {@code Capability}.

   @return a {@code Map} of a {@code UniqueIdentifier} to a {@code Capability}.
   */
  Map<UniqueIdentifier, ? extends Capability> getCapabilitiesMapping();

  /**
   Gets the {@code Capability} if it exists.

   @param <CapabilityType> the type of the {@code Capability}.
   @param capabilityClass the {@code Capability} to get.
   @return the {@code Capability} if it exists, {@code null} otherwise.
   */
  <CapabilityType extends Capability> CapabilityType getCapability(
          Class<CapabilityType> capabilityClass);

  /**
   Returns the score of the given {@code Capability} that the {@code IExecutionComponent} possesses.

   @param capability the score of the {@code Capability} to retrieve.
   @return the score if it exists, {@code 0.0} otherwise.
   */
  double getCapabilityScore(Capability capability);

  void reset();


}
