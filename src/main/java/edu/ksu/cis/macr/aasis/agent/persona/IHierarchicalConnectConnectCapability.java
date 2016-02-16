package edu.ksu.cis.macr.aasis.agent.persona;

import com.rabbitmq.client.ShutdownSignalException;
import edu.ksu.cis.macr.aasis.agent.cc_message.connect.IConnectMessage;
import edu.ksu.cis.macr.aasis.common.IConnectionGuidelines;
import edu.ksu.cis.macr.aasis.common.IConnections;
import edu.ksu.cis.macr.organization.model.InstanceGoal;

import java.io.IOException;
import java.util.List;

/**
 */
public interface IHierarchicalConnectConnectCapability extends ICapability, IInternalCommunicationCapability.ICommunicationChannel {



    IConnectMessage remoteRECEIVE(final String queueLink) throws IOException, ShutdownSignalException, InterruptedException;

    boolean isAllConnected();

    List<? extends IConnectionGuidelines> getUnconnectedChildren();

    List<? extends IConnectionGuidelines> getUnconnectedChildren(InstanceGoal<?> instanceGoal);

    List<? extends IConnectionGuidelines> getUnconnectedParents();

    List<? extends IConnectionGuidelines> getUnconnectedParents(InstanceGoal<?> instanceGoal);

    boolean connectToChildren();

    boolean connectToParents();

    IConnectMessage checkForRemoteConnectMessage(String other, String myPersona);

    IConnectMessage createRemoteHelloMessage(String other, String organizationAbbrev, String expectedMasterAbbrev, String myPersona);

    IConnections getAllConnections();

    IConnections getChildConnections();

    /**
     * @return - the number of getNumberOfMessages on this local messages queue
     */
    int getNumberOfMessages();

    /**
     * Get the parameters from this instance goal and use them to set the goal-specific guidelines for any child
     * connections.
     *
     * @param instanceGoal - the instance goal provided
     */
    void initializeChildConnections(InstanceGoal<?> instanceGoal);

    void init(InstanceGoal<?> instanceGoal);

    void initializeParentConnections(InstanceGoal<?> instanceGoal);

    /**
     * Find out if the connection described in the {@code IConnectionGuidelines} is currently connected.
     *
     * @param iConnectionGuidelines he {@code IConnectionGuidelines} describing the connection
     * @return true if connected, false if not.
     */
    boolean isConnected(IConnectionGuidelines iConnectionGuidelines);

    boolean registerWithExchange();

    /**
     * @param message - the ConnectMessage messages to be sent
     * @return {@code true} if the messages was sent, {@code false} otherwise.
     */
    boolean send(IConnectMessage message);

    /**
     * Set the current isConnected status for the connection described in the {@code IConnectionGuidelines}.
     *
     * @param isConnected - true to set status to connected, false to set status to not connected.
     */
    void setIsConnectedForAll(boolean isConnected);

    void triggerChildGoal(final InstanceGoal<?> instanceGoal);

    void triggerParentGoal(final InstanceGoal<?> instanceGoal);

    void connectDown();

    void connectDown(final InstanceGoal<?> instanceGoal);

    void connectUp();

    boolean checkUpConnections(InstanceGoal<?> instanceGoal);

    boolean checkUpConnections();

    void connectUp(final InstanceGoal<?> instanceGoal);

    IConnections getParentConnections();

    String getConnectionSummaryString();

    /**
     * Attempt to connect to all children.
     *
     * @return true if all connections have been made, false if not.
     */
    boolean checkDownConnections();

    /**
     * Attempt to connect to all children.
     *
     * @param ig - the instance goal
     * @return true if all connections have been made, false if not.
     */
    boolean checkDownConnections(InstanceGoal<?> ig);

    boolean isAllConnected(InstanceGoal<?> ig);


}
