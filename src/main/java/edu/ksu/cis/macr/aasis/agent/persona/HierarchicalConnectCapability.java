package edu.ksu.cis.macr.aasis.agent.persona;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;
import edu.ksu.cis.macr.aasis.agent.cc_message.connect.*;
import edu.ksu.cis.macr.aasis.agent.cc_p.ConnectionModel;
import edu.ksu.cis.macr.aasis.common.IConnectionGuidelines;
import edu.ksu.cis.macr.aasis.common.IConnections;
import edu.ksu.cis.macr.aasis.messaging.IMessagingFocus;
import edu.ksu.cis.macr.obaa_pp.objects.IDisplayInformation;
import edu.ksu.cis.macr.organization.model.InstanceGoal;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * The {@code ConnectCapability} implements communication capabilities needed to establish initial connections. To monitor
 * RabbitMQ, point a browser to (the final slash is required): http://localhost:15672/ and login with: guest / guest
 */
public class HierarchicalConnectCapability extends AbstractOrganizationCapability implements IHierarchicalConnectConnectCapability {
  private static IMessagingFocus messagingFocus ;
 protected static String COMMUNICATION_CHANNEL_ID = "HierarchicalConnectCommunicationChannel";
    private static final Logger LOG = LoggerFactory.getLogger(HierarchicalConnectCapability.class);
    private static final boolean debug = false;
    protected static Channel channel;
    protected final UniqueIdentifier myID;
    protected IPersona ec;
    protected ConcurrentLinkedQueue<IConnectMessage> localMessages = new ConcurrentLinkedQueue<>();
    protected ConcurrentLinkedQueue<IConnectMessage> remoteMessages = new ConcurrentLinkedQueue<>();
    protected Map<IConnectionGuidelines, Boolean> mapConnected = new HashMap<>();
    protected IConnections connections = null;
    protected IConnections childConnections = null;
    protected IConnections parentConnections = null;
    protected boolean allConnected = false;



    /**
     * @param owner        - the entity to which this capability belongs.
     * @param organization - the {@code Organization} in which this {@code IAgent} acts.
     */
    public HierarchicalConnectCapability(final IPersona owner, final IOrganization organization, IConnections connections) {
        super(IHierarchicalConnectConnectCapability.class, owner, organization);
        this.ec = Objects.requireNonNull(owner);
        this.setAllConnections(connections);
        this.myID = ec.getUniqueIdentifier();

    }



    /**
     * Constructs a new instance of {@code ConnectCapability}.
     *
     * @param owner        - the entity to which this capability belongs.
     * @param organization - the {@code IAgentInternalOrganization} in which this {@code IAgent} acts.
     * @param connections  - the guidelines for all authorized market connections.
     */
    public HierarchicalConnectCapability(final Class<? extends IHierarchicalConnectConnectCapability> myClass, final IPersona owner, final IOrganization organization, IConnections connections) {
        super(IHierarchicalConnectConnectCapability.class, owner, organization);
        this.ec = Objects.requireNonNull(owner);
        this.setAllConnections(connections);
        this.myID = ec.getUniqueIdentifier();

      }

    /**
     * Constructs a new instance of {@code ConnectCapability}.
     *
     * @param owner        - the entity to which this capability belongs.
     * @param organization - the {@code IAgentInternalOrganization} in which this {@code IAgent} acts.
     */
    public HierarchicalConnectCapability(final Class<? extends IHierarchicalConnectConnectCapability> myClass, final IPersona owner, final IOrganization organization) {
        super(myClass, owner, organization);
        this.ec = Objects.requireNonNull(owner);
        this.setAllConnections(connections);
        this.myID = ec.getUniqueIdentifier();

    }



    public static String getCommunicationChannelID() {
        return "TEMP";
    }

    protected synchronized static boolean alreadyInConnectionList(final String myPersona, final String other) {
        // assume I'm before other alphabetically
        String first = myPersona;
        String second = other;

        // if instead, receiver comes before sender alphabetically, adjust
        if (other.compareTo(myPersona) < 0) {
            first = other;
            second = myPersona;
        }
        final String connection = first + " - " + second;
        return ConnectionModel.includes(connection);
    }

    protected void sendREMOTE(final IConnectMessage message) {
        LOG.error("sendREMOTE() too general - do not use. ");
        System.exit(-99);

    }

    public static String buildQueueLinkFromSenderAndReceiver(final String remoteSender, final String remoteReceiver) {
        return remoteSender + "-" + remoteReceiver;
    }



    @Override
    public IConnections getChildConnections() {
        return this.childConnections;
    }

    public synchronized void setChildConnections(final IConnections childConnections) {
        this.childConnections = childConnections;
    }

    @Override
    public boolean isAllConnected() {
        return ((parentConnectionsStillNeeded()+childConnectionsStillNeeded())<1);
    }

    @Override
    public String toString() {
        return "MarketConnectCapability{" +
                ", connections=" + connections +
                ", childConnections=" + childConnections +
                ", parentConnections=" + parentConnections +
                ", allConnected=" + allConnected +
                '}';
    }

    /**
     * Returns the {@code DisplayInformation} object containing the information for the {@code ICapability}.
     *
     * @param displayInformation the data display.
     */
    @Override
    public void populateCapabilitiesOfDisplayObject(IDisplayInformation displayInformation) {
        super.populateCapabilitiesOfDisplayObject(displayInformation);
    }

    @Override
    public void reset() {
    }

    @Override
    public void channelContent(final Object content) {
        localMessages.add((IConnectMessage) content);
    }

    @Override
    public boolean checkUpConnections(final InstanceGoal<?> instanceGoal) {
        init(instanceGoal);
        return checkUpConnections();
    }

    @Override
    public synchronized boolean checkUpConnections() {
        if (debug) LOG.debug("Beginning attempts to connect to all brokers.");
        int tot = 0;
        if (noParents()) {
            this.allConnected = true;
        } else {
            tot = this.getParentConnections().getListConnectionGuidelines().size();
            //   if (this.parentConnectionsStillNeeded() > 0)
            if (debug)
                LOG.debug("Need {} of {} connections to fully connect to brokers.", this.parentConnectionsStillNeeded(), tot);
            if (parentConnectionsStillNeeded() == 0) {
                this.allConnected = true;
            } else {
                this.getParentConnections().getListConnectionGuidelines().stream().filter(cg -> !cg.isConnected()).forEach(cg -> {
                    try {
                        boolean parentConnected = connectToParent(cg);
                        if (debug)
                            LOG.debug("Connection to broker {} success={}.", cg.getOtherAgentAbbrev(), parentConnected);
                    } catch (Exception e) {
                        LOG.error("Error attempting to connect to parent: {}", cg);
                        System.exit(-53);
                    }
                });
            }
        }
        this.allConnected = (parentConnectionsStillNeeded() == 0);
        if (parentConnectionsStillNeeded()>0) {LOG.info("{} of {} connections to fully connect to brokers.",this.parentConnectionsStillNeeded(), tot);}
        return this.allConnected;
    }

    @Override
    public void connectUp(final InstanceGoal<?> instanceGoal) {
        init(instanceGoal);
        connectUp();
    }

    @Override
    public IConnectMessage remoteRECEIVE(final String queueLink) throws IOException, ShutdownSignalException, InterruptedException {
        LOG.error("remoteRECEIVE() too general - do not use. ");
        System.exit(-99);
        return null;
    }

    @Override
    public List<? extends IConnectionGuidelines> getUnconnectedChildren() {
        List<IConnectionGuidelines> unconnected = new ArrayList<>();
        if (noChildren()) {
            return unconnected;
        }

        List<? extends IConnectionGuidelines> listConnectionGuidelines = getChildConnections().getListConnectionGuidelines();
        for (IConnectionGuidelines cg : listConnectionGuidelines){
            if (!cg.isConnected()){           unconnected.add(cg);        }
        }

        //  unconnected.addAll(listConnectionGuidelines.stream().filter(cg -> !cg.isConnected()).collect(Collectors.toList()));
        final int tot = getChildConnections().getListConnectionGuidelines().size();
        int connectionsStillNeeded = unconnected.size();
        if (debug) LOG.debug("Need {} of {} auction connections to fully connect.", connectionsStillNeeded, tot);
        return unconnected;
    }

    @Override
    public List<? extends IConnectionGuidelines> getUnconnectedChildren(final InstanceGoal<?> instanceGoal) {
        init(instanceGoal);
        return getUnconnectedChildren();
    }

    @Override
    public List<? extends IConnectionGuidelines> getUnconnectedParents() {
        List<IConnectionGuidelines> unconnected = new ArrayList<>();
        if (noParents()) {
            return unconnected;
        }

        List<? extends IConnectionGuidelines> listConnectionGuidelines = getParentConnections().getListConnectionGuidelines();
        for (IConnectionGuidelines cg : listConnectionGuidelines){
            if (!cg.isConnected()){           unconnected.add(cg);        }
        }
        //  unconnected.addAll(listConnectionGuidelines.stream().filter(cg -> !cg.isConnected()).collect(Collectors.toList()));
        final int tot = getParentConnections().getListConnectionGuidelines().size();
        int connectionsStillNeeded = unconnected.size();
        if (debug) LOG.debug("Need {} of {} connections to fully connect to brokers.", connectionsStillNeeded, tot);
        return unconnected;
    }

    @Override
    public List<? extends IConnectionGuidelines> getUnconnectedParents(final InstanceGoal<?> instanceGoal) {
        init(instanceGoal);
        return getUnconnectedParents();
    }

    @Override
    public IConnections getAllConnections() {
        return this.connections;
    }

    public void setAllConnections(final IConnections connections) {
        this.connections = connections;
    }

    @Override
    public int getNumberOfMessages() {
        return this.localMessages.size();
    }


    @Override
    public synchronized void initializeChildConnections(InstanceGoal<?> instanceGoal) {
        LOG.error("initializeChildConnections() too general - do not use. ");
        System.exit(-99);
    }

    /**
     * Get all parameters from this instance goal and use them to initialize the capability.
     *
     * @param instanceGoal - this instance of the specification goal
     */
    @Override
    public void init(final InstanceGoal<?> instanceGoal) {
        LOG.error("init() too general - do not use. ");
        System.exit(-99);
    }

    /**
     * Get the parameters from this instance goal and use them to set the goal-specific guidelines for any parent
     * connections.
     *
     * @param instanceGoal - this instance of the specification goal
     */
    public synchronized void initializeParentConnections(InstanceGoal<?> instanceGoal) {
        LOG.error("initializeParentConnections() too general - do not use. ");
        System.exit(-99);
    }

    @Override
    public boolean isConnected(final IConnectionGuidelines iConnectionGuidelines) {
        return mapConnected.get(iConnectionGuidelines);
    }

    @Override
    public boolean registerWithExchange() {
        LOG.error("registerWithExchange() too general - do not use. ");
        System.exit(-99);
        return false;
    }

    @Override
    public boolean send(IConnectMessage message) {
        LOG.error("send too general - do not use. ");
        System.exit(-99);
        return false;
    }

    @Override
    public synchronized void setIsConnectedForAll(boolean isConnected) {
        for (IConnectionGuidelines g : getAllConnections().getListConnectionGuidelines()) {
            setIsConnected(g, false);
        }
    }

    @Override
    public void triggerChildGoal(InstanceGoal<?> instanceGoal) {
        LOG.error("triggerChildGoal send too general - do not use. ");
        System.exit(-99);
    }

    @Override
    public void triggerParentGoal(InstanceGoal<?> instanceGoal) {
        LOG.error("triggerParentGoal send too general - do not use. ");
        System.exit(-99);
    }


    @Override
    public synchronized void connectDown() {
        if (noChildren()) {
            LOG.info("Can't setup and send connects to subs as there are no children. {}", this.getChildConnections());
            return;
        }
        for (IConnectionGuidelines g : getChildConnections().getListConnectionGuidelines()) {
            if (debug) LOG.debug("Setting up queues and bindings to connect downwards: {}.", g);
            final String other = g.getOtherAgentAbbrev().trim();
            final String org = g.getOrganizationAbbrev().trim();
            final String master = g.getExpectedMasterAbbrev().trim();
            final String myPersona = ec.getUniqueIdentifier().toString();
            if (debug) LOG.debug("Agent {} connecting to child {}.", myPersona, other);

            // send a hello message to this authorized connection
            sendRemoteHelloMessage(other, org, master, myPersona);
        }
    }

    @Override
    public void connectDown(final InstanceGoal<?> instanceGoal) {
        init(instanceGoal);
        connectDown();
    }

    @Override
    public synchronized void connectUp() {
        LOG.info("Entering connectUp()");
        if (noParents()) {
            LOG.info("No parents. {}", this.getParentConnections());
            return;
        }
        for (IConnectionGuidelines g : getParentConnections().getListConnectionGuidelines()) {
            if (debug) LOG.debug("Setting up queues and bindings to connect upwards: {}.", g);
            final String other = g.getOtherAgentAbbrev().trim();
            final String org = g.getOrganizationAbbrev().trim();
            final String master = g.getExpectedMasterAbbrev().trim();
            final String myPersona = ec.getUniqueIdentifier().toString();
           LOG.info("Setting up messaging for agent {} connecting to parent {}.", myPersona, other);
            sendRemoteHelloMessage(other, org, master, myPersona);
        }
    }

    @Override
    public IConnections getParentConnections() {
        return this.parentConnections;
    }

    public synchronized void setParentConnections(IConnections parentConnections) {
        this.parentConnections = parentConnections;
    }

    @Override
    public String getConnectionSummaryString() {
        String s = ConnectionModel.getSummaryString();
        LOG.info("{}", s);
        return s;
    }

    public synchronized boolean checkDownConnections() {
        if (debug) LOG.debug("Beginning attempts to connect to all participants.");
        int tot = 0;
        if (noChildren()) {
            this.allConnected = true;
        } else {
            tot = getChildConnections().getListConnectionGuidelines().size();
            if (debug)
                LOG.debug("Need {} of {} down connections to fully connect to participants.", childConnectionsStillNeeded(), tot);
            if (childConnectionsStillNeeded() == 0) {
                this.allConnected = true;
            } else {
                for (IConnectionGuidelines cg : getChildConnections().getListConnectionGuidelines()) {
                    try {
                        boolean isConnected = connectToChild(cg);
                        if (debug)
                            LOG.debug("Connection to participant {} success={}.", cg.getOtherAgentAbbrev(), isConnected);
                    } catch (Exception e) {
                        LOG.error("Error attempting to connect to participant: {}", cg);
                        System.exit(-59);
                    }
                }
                resendToUnconnectedChildren();
                // check again and if no additional connections are still needed return true (completely connected)
                this.allConnected = (childConnectionsStillNeeded() == 0);
            }
        }

        if (childConnectionsStillNeeded() > 0) LOG.info("Exiting checkDownConnections. ALL_CONNECTED={}, Need {} of {} connections to fully connect.", this.allConnected, this.childConnectionsStillNeeded(), tot);
        return this.allConnected;
    }

    public boolean checkDownConnections(final InstanceGoal ig) {
        init(ig);
        return checkDownConnections();
    }

    @Override
    public synchronized boolean isAllConnected(final InstanceGoal<?> instanceGoal) {
        init(instanceGoal);
        this.allConnected = (parentConnectionsStillNeeded() == 0);
        return this.allConnected;
    }

    @Override
    public boolean connectToChildren() {
        if (debug) LOG.debug("Beginning attempts to connect to all sub holons.");
        int tot=0;
        if (noChildren()) {
            this.allConnected = true;
        } else {
            tot = getChildConnections().getListConnectionGuidelines().size();
            if (debug)
                LOG.debug("Need {} of {} connections to fully connect to sub holons.", childConnectionsStillNeeded(), tot);
            if (childConnectionsStillNeeded() == 0) {
                this.allConnected = true;
            } else {
                for (IConnectionGuidelines cg : getChildConnections().getListConnectionGuidelines()) {
                    try {
                        boolean isConnected = connectToChild(cg);
                        if (debug)
                            LOG.debug("Connection to sub holon {} success={}.", cg.getOtherAgentAbbrev(), isConnected);
                    } catch (Exception e) {
                        LOG.error("Error attempting to connect to sub holon: {}", cg);
                        System.exit(-59);
                    }
                }
                resendToUnconnectedChildren();
                // check again and if no additional connections are still needed return true (completely connected)
                this.allConnected = (childConnectionsStillNeeded() == 0);
            }
        }
        if (childConnectionsStillNeeded() > 0){LOG.info("Need {} of {} child connections to fully connect.", childConnectionsStillNeeded(), tot);}

        return this.allConnected;
    }

    @Override
    public synchronized boolean connectToParents() {
        if (debug) LOG.debug("Beginning attempts to connect to all super holons.");
        if (noParents()) {
            this.allConnected = true;
        }
        final int tot = this.getParentConnections().getListConnectionGuidelines().size();
        if (this.parentConnectionsStillNeeded() > 0)
            if (debug) LOG.debug("Need {} of {} connections to fully connect to super holons.", this.parentConnectionsStillNeeded(), tot);
        if (parentConnectionsStillNeeded() == 0) {
            this.allConnected = true;
        }
        for (IConnectionGuidelines cg : this.getParentConnections().getListConnectionGuidelines()) {
            try {
                boolean parentConnected = connectToParent(cg);
                if (debug)
                    LOG.debug("Connection to super holon {} success={}.", cg.getOtherAgentAbbrev(), parentConnected);
            } catch (Exception e) {
                LOG.error("Error attempting to connect to parent: {}", cg);
                System.exit(-53);
            }
        }
        // loop through and send another message to any unconnected supers
        resendToUnconnectedParents();
        this.allConnected = (parentConnectionsStillNeeded() == 0);
        return this.allConnected;
    }


    @Override
    public double getFailure() {
        return MIN_FAILURE;
    }

    protected synchronized int childConnectionsStillNeeded() {
        if (noChildren()) return 0;
        final String myPersona = ec.getUniqueIdentifier().toString();
        int connectionsStillNeeded = 0;

        List<? extends IConnectionGuidelines> listConnectionGuidelines = getChildConnections().getListConnectionGuidelines();
        for (IConnectionGuidelines cg : listConnectionGuidelines) {
            if (alreadyInConnectionList(myPersona, cg.getOtherAgentAbbrev())) {
                cg.setConnected(true);
                LOG.info("Already in list: {}.",cg.getOtherAgentAbbrev());
            }
            else {
                LOG.debug("NOT already in list: me={} other={}.",myPersona, cg.getOtherAgentAbbrev());
                if (!cg.isConnected()) {

                    connectionsStillNeeded += 1;
                    LOG.debug("NOT connected: {}. Child connections still needed=",cg.getOtherAgentAbbrev(),connectionsStillNeeded);
                }
            }
        }
        final int tot = getChildConnections().getListConnectionGuidelines().size();
        if (connectionsStillNeeded >0){
            LOG.info("Need {} of {} child connections to fully connect to participants.", connectionsStillNeeded, tot);
        }
        return connectionsStillNeeded;
    }

    protected boolean noChildren() {
        if (debug) LOG.debug("Calling noChildren to see if {} has no participants.", owner.getIdentifierString());
        boolean noChildren = false;
        try {
            if (getChildConnections() == null || getChildConnections().getListConnectionGuidelines() == null ||
                    getChildConnections().getListConnectionGuidelines().isEmpty()) noChildren = true;
        } catch (Exception e) {
            LOG.error("Error checking to see if {} has no participants. childConnections = {}", owner.getIdentifierString(), this.getChildConnections());
            System.exit(-4);
        }
        if (debug) LOG.debug("{} no participants = {}", owner.getIdentifierString(), noChildren);
        return noChildren;
    }

    protected synchronized boolean connectToChild(IConnectionGuidelines cg) {
        final String other = cg.getOtherAgentAbbrev();
        final String org = cg.getOrganizationAbbrev();
        final String master = cg.getExpectedMasterAbbrev();
        final String myPersona = ec.getUniqueIdentifier().toString();

        if (alreadyInConnectionList(myPersona, other)) {
            cg.setConnected(true);
            LOG.debug("CONNECTED. Broker {} already in the conn list to participant {}.", myPersona, other);
        }
        if (cg.isConnected()) return true;

        if (debug) LOG.debug("broker {} not yet connected to participant {}. Checking for hello.", myPersona, other);
        final IConnectMessage helloMessage = checkForRemoteConnectMessage(other, myPersona);

        if (helloMessage != null) {
            if (debug) LOG.debug("RECEIVED HELLO from participant: {}.", helloMessage);

            try {
                final String sender = helloMessage.getRemoteSender();  // other
                final String receiver = helloMessage.getRemoteReceiver();  // me
                cg.setConnected(true);
                updateConnectionList(sender, receiver);
                LOG.info("EVENT: CONNECTION_ESTABLISHED. {} now connected to participant {}.",  receiver, sender);
            } catch (Exception e) {
                LOG.error("Error getting info from received hello. {}", helloMessage.toString());
                System.exit(-4);
            }
        } else {
            if (debug) LOG.debug("No connect received. broker {} sending again to participant {}.", myPersona, other);
            sendRemoteHelloMessage(other, org, master, myPersona);
        }
        return cg.isConnected();
    }

    @Override
    public IConnectMessage checkForRemoteConnectMessage(final String other, final String myPersona) {
        if (debug) LOG.debug("{} CHECKING FOR HELLO from SENDER {} to MyPERSONA {}", myPersona, other, myPersona);
        IConnectMessage recMessage = null;
        try {
            final String queueLink = buildQueueLinkFromSenderAndReceiver(other, myPersona);
            if (debug) LOG.debug("{} CHECKING FOR HELLO from SENDER {} to MyPERSONA {} on queueLink {}", myPersona, other, myPersona, queueLink);
            recMessage = remoteRECEIVE(queueLink);
            if (recMessage == null) return null;
            LOG.info("{} received REMOTE CONNECT MESSAGE: {}", myPersona, recMessage.toString());
        } catch (ShutdownSignalException | ConsumerCancelledException | IOException | InterruptedException e) {
            LOG.error("ERROR: checking for hello ({})", e.getMessage());
            System.exit(-10);
        }
        return recMessage;
    }

    protected synchronized void updateConnectionList(final String sender, final String receiver) {
        if (debug) LOG.debug("ERROR SHOULD OVERWRITE updateConnectionList() with sender ={} and receiver={}.", sender, receiver);
        // assume sender comes before receiver alphabetically
        String first = sender;
        String second = receiver;
        // if instead, reciever comes before sender alphabetically, adjust
        if (receiver.compareTo(sender) < 0) {
            first = receiver;
            second = sender;
        }
        TreeSet<String> conns = ConnectionModel.getConnectionSet();
        if (debug) LOG.debug("The connection set already had {} entries.", conns.size());
        //add it (only in the alphabetical order - don't duplicate the connection)
        final String connection = first + " - " + second + "\n";
        ConnectionModel.insertNewConnection(connection);

        final int numConnections = ConnectionModel.getConnectionSet().size();
//        final int totalPossibleParentConnections = 2 * (RunManager.getCountAllAgents() - 1);
//        final int hnum = ConnectionModel.getCountHomeConnections();
//        LOG.info("{}", ConnectionModel.getSummaryString());
//        RunManager.setInitiallyConnected(numConnections >= RunManager.INITIAL_CONNECTION_THRESHOLD_FRACTION * totalPossibleParentConnections);
}

    public synchronized boolean connectToParent(IConnectionGuidelines cg) {
        final String other = cg.getOtherAgentAbbrev();
        final String org = cg.getOrganizationAbbrev();
        final String master = cg.getExpectedMasterAbbrev();
        final String myPersona = ec.getUniqueIdentifier().toString();
        if (debug) LOG.info("participant {} attempting to connect to parent {}.", myPersona, other);

        if (alreadyInConnectionList(myPersona, other)) {
            cg.setConnected(true);
        }
        if (cg.isConnected()) {
            return true;
        }

        LOG.info("participant {} not connected to parent {} yet.", myPersona, other);
        if (debug) LOG.debug("Checking for message from parent {}.", other);
        IConnectMessage helloMessage = checkForRemoteConnectMessage(other, myPersona);

        if (helloMessage != null) {
            if (debug) LOG.debug("Received hello from broker: {}.", helloMessage);
            //    sendHelloReply(other, org, master, myPersona);

            final String sender = helloMessage.getRemoteSender();
            final String receiver = helloMessage.getRemoteReceiver();

            if (receiver.equals(myPersona) && sender.equals(other)) {
                cg.setConnected(true);
                if (debug)
                    LOG.debug("Updating connections to show {} to super {} is {}.", receiver, sender, cg.isConnected());
                updateConnectionList(sender, receiver);
                LOG.info("EVENT: CONNECTION_ESTABLISHED. participant {} now connected to {}.",  receiver, sender);
                //  wait for a bit and let others work
                try {
                    Thread.sleep(4 * 1000);
                } catch (InterruptedException e) {
                    LOG.error("Process errored out while waiting.", ec.getUniqueIdentifier().toString());
                    System.exit(-43);
                }
            } else {
                LOG.error("ERROR: {} picked up a message for {}.", myPersona, receiver);
                System.exit(-91);
            }
        } // end hello received

        else {
            LOG.debug("No connect received. participant {} sending again to broker {}.", myPersona, other);
            sendRemoteHelloMessage(other, org, master, myPersona);
        }
        return cg.isConnected();
    }

    public IInternalCommunicationCapability.ICommunicationChannel getCommunicationChannel() {
        return this;
    }

    protected synchronized int parentConnectionsStillNeeded() {
        if (noParents()) {
            return 0;
        }
        String myPersona = ec.getUniqueIdentifier().toString();
        int connectionsStillNeeded = 0;

        List<? extends IConnectionGuidelines> listConnectionGuidelines = getParentConnections().getListConnectionGuidelines();
        for (IConnectionGuidelines cg : listConnectionGuidelines) {

            if (alreadyInConnectionList(myPersona, cg.getOtherAgentAbbrev())) {
                cg.setConnected(true);
            }
            if (!cg.isConnected()) connectionsStillNeeded += 1;
        }
        final int tot = getParentConnections().getListConnectionGuidelines().size();
        if (debug)
            LOG.debug("Need {} of {} broker connections to fully connect to brokers.", connectionsStillNeeded, tot);
        return connectionsStillNeeded;
    }

    protected boolean noParents() {
        if (debug) LOG.debug("Calling noParents to see if {} has no brokers.", owner.getIdentifierString());
        boolean noParents = false;
        try {
            if (this.getParentConnections() == null || this.getParentConnections().getListConnectionGuidelines() == null ||
                    this.getParentConnections().getListConnectionGuidelines().isEmpty()) {
                noParents = true;
                LOG.info("{} has no broker.", owner.getIdentifierString());
            }
        } catch (Exception e) {
            LOG.error("Error checking to see if {} has no parents. parentConnections = {}", owner.getIdentifierString(), this.getParentConnections());
            System.exit(-3);
        }
        if (debug) LOG.debug("Return true if {} has no parents. RESULT = {}", owner.getIdentifierString(), noParents);
        return noParents;
    }

    protected void resendToUnconnectedChildren() {
        getChildConnections().getListConnectionGuidelines().stream().filter(g -> !g.isConnected()).forEach(g -> {
            if (debug) LOG.debug("Setting up queues and bindings to re-connect downwards: {}.", g);
            final String other = g.getOtherAgentAbbrev().trim();
            final String org = g.getOrganizationAbbrev().trim();
            final String master = g.getExpectedMasterAbbrev().trim();
            final String myPersona = ec.getUniqueIdentifier().toString();
            if (debug) LOG.debug("Parent {} re-connecting to child {}.", myPersona, other);
            sendRemoteHelloMessage(other, org, master, myPersona);
        });
    }

    /**
     * Send a hello message.
     *
     * @param other     - the agent receiving the message
     * @param org       - the organization abbreviation
     * @param master    - the organization master
     * @param myPersona - the agent sending the message
     * @return the IConnectMessage
     */
    protected IConnectMessage sendRemoteHelloMessage(final String other, final String org, final String master, final String
            myPersona) {
        LOG.debug("Entering sendRemoteHelloMessage(other={}, org={}, master={}, myPersona={})",other, org, master, myPersona);
        final IConnectMessage message = this.createRemoteHelloMessage(other, org, master, myPersona);
        if (debug) LOG.debug("COMPOSED remote hello message {} ", message.toString());
        try {
            sendREMOTE(message);
            if (debug) LOG.info("Remote HELLO SENT: {} ", message.toString());
        } catch (Exception e) {
            LOG.error("Remote HELLO NOT SENT: {} ", message.toString());
            System.exit(-88);
        }
        return message;
    }

    @Override
    public IConnectMessage createRemoteHelloMessage(final String other, final String organizationAbbrev, final String expectedMasterAbbrev, final String myPersona) {
        final IConnectMessageContent content = ConnectMessageContent.createConnectMessageContent(
                myPersona, other, organizationAbbrev, expectedMasterAbbrev, "hello"
        );
        return ConnectMessage.createRemoteConnectMessage(myPersona, other, ConnectPerformative.SENDING_HELLO, content);
    }

    protected void resendToUnconnectedParents() {
        getParentConnections().getListConnectionGuidelines().stream().filter(g -> !g.isConnected()).forEach(g -> {
            if (debug) LOG.debug("Setting up queues and bindings to re-connect upwards: {}.", g);
            final String other = g.getOtherAgentAbbrev().trim();
            final String org = g.getOrganizationAbbrev().trim();
            final String master = g.getExpectedMasterAbbrev().trim();
            final String myPersona = ec.getUniqueIdentifier().toString();
            if (debug) LOG.debug("Child {} re-connecting to parent {}.", myPersona, other);
            sendRemoteHelloMessage(other, org, master, myPersona);
        });
    }

    /**
     * @param childConnections - the list of all authorized connections to child or subordinate agents.
     */
    protected synchronized void setAllChildConnectionGuidelines(final IConnections childConnections) {
        this.setChildConnections(childConnections);
    }

    protected synchronized void setIsConnected(final IConnectionGuidelines iConnectionGuidelines, final boolean isConnected) {
        mapConnected.put(iConnectionGuidelines, isConnected);
    }

    protected synchronized List<? extends IConnectionGuidelines> getAllChildConnections(List<? extends IConnectionGuidelines> lstAll) {
        List<IConnectionGuidelines> lstAllChildConnections =  new ArrayList<>();
        for (IConnectionGuidelines cg : lstAll)
        {
            if (cg.isConnectionToChild()){ lstAllChildConnections.add(cg);}
        }
        return lstAllChildConnections;
    }

    protected synchronized List<? extends IConnectionGuidelines> getAllParentConnections(List<? extends IConnectionGuidelines> lstAll) {
        List<IConnectionGuidelines> justParents =  new ArrayList<>();
        for (IConnectionGuidelines cg : lstAll)
        {
            if (cg.isConnectionToParent()){ justParents.add(cg);}
        }
        return justParents;
    }



}
