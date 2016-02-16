package edu.ksu.cis.macr.aasis.agent.cc_message.custom;

/**
 * The {@code EquipmentStatus} indicates the availability of a piece of equipment.
 */
public enum EquipmentStatus {
    /**
     * not currently in use, but can be brought on for service
     */
    AVAILABLE,
    /**
     * online and available
     */
    OPERATING,
    /**
     * out of service and not available at this time
     */
    OUT_OF_SERVICE
}
