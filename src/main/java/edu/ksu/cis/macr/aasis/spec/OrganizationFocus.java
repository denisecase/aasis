package edu.ksu.cis.macr.aasis.spec;

/**
 * An enumeration of the focus for the organization.  Agents have an organization with a Agent(0) focus.  Agents
 * may also form External(1) organizations to achieve application-specific goals.
 */
public enum OrganizationFocus {
    /**
     * An organization with this focus is used for agent self-management.
     */
    Agent(0),
    /**
     * And organization with this focus is used to achieve application-specific goals.
     */
    External(1);

    private final int value;

    private OrganizationFocus(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of the type.  Agent = 0 and external = 1.
     *
     * @return - the integer value of the organization focus.
     */
    public int getIntegerValue() {
        return this.value;
    }
}
