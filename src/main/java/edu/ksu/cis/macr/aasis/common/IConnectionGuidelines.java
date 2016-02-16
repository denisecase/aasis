package edu.ksu.cis.macr.aasis.common;

/**
 * {@code IConnectionGuidelines} provides an interface for describing a single agent-to-agent connection.
 */
public interface IConnectionGuidelines {
    double getCombinedKW();

    /**
     * @return the expectedMasterAbbrev
     */
    String getExpectedMasterAbbrev();

    /**
     * @param expectedMasterAbbrev the expectedMasterAbbrev to set
     */
    void setExpectedMasterAbbrev(String expectedMasterAbbrev);

    String getOrgModelFolder();

    void setOrgModelFolder(String orgModelFolder);

    /**
     * @return the organizationAbbrev
     */
    String getOrganizationAbbrev();

    /**
     * @param organizationAbbrev the organizationAbbrev to set
     */
    void setOrganizationAbbrev(String organizationAbbrev);

    String getOrganizationLevel();

    void setOrganizationLevel(String organizationIPDSLevel);

    /**
     * @return the otherAgentAbbrev
     */
    String getOtherAgentAbbrev();

    /**
     * @param otherAgentAbbrev the otherAgentAbbrev to set
     */
    void setOtherAgentAbbrev(String otherAgentAbbrev);

    /**
     * @return the specificationFilePath
     */
    String getSpecificationFilePath();

    /**
     * @param specificationFilePath the specificationFilePath to set
     */
    void setSpecificationFilePath(String specificationFilePath);

    /**
     * Determines if this connection has been established.
     *
     * @return true if connected, false if not
     */
    boolean isConnected();

    void setConnected(final boolean isConnected);

    /**
     * Determines if this is a connection to a child - an agent directly lower (subordinate) in the organization.
     *
     * @return true if this is a connection to a child, false if not
     */
    boolean isConnectionToChild();

    /**
     * Determines if this is a connection to a parent - an agent directly higher (superior) in the organization.
     *
     * @return true if this is a connection to a parent, false if not
     */
    boolean isConnectionToParent();

    /**
     * Determines if this connection has been registered in the shared organization.
     * For now, we assume all connections are also part of an affiliated organization.
     * In an open system, there could be connections without registration, but not in our closed system.
     *
     * @return true if registered, false if not
     */
    boolean isRegistered();

    void setRegistered(final boolean isRegistered);
}
