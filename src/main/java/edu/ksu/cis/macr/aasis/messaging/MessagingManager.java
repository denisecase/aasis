package edu.ksu.cis.macr.aasis.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code MessagingManager} singleton provides a central exchange for communication between agents.
 * Open a browser to http://localhost:15672/ when running a simulation to see associated information.
 */
public enum MessagingManager implements IMessagingManager {
    /**
     * Singleton instance of the Communications Manager (one per JVM).
     */
    INSTANCE;


    /**
     * The maximum time messages can remain on a queue before expiring (being deleted without being delivered).
     */
    public static final int MESSAGES_EXPIRE_IN_SECONDS = 60;
    /**
     * The exchange is a named entity to which messages are sent. The type of exchange determines its routing
     * behavior. For the IPDS simulation, we use different exchanges based on the types of information conveyed.
     */
    public static final Map<IMessagingFocus, Exchange> specs = new HashMap<IMessagingFocus, Exchange>() {
        {
            put(MessagingFocus.GENERAL, Exchange.createExchangeSpecification("GENERAL"));
            put(MessagingFocus.GENERAL_PARTICIPATE, Exchange.createExchangeSpecification("GENERAL_PARTICIPATE"));
        }

        private static final long serialVersionUID = 8323789558019617154L;
    };
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MessagingManager.class);
    /**
     * If true, the optional debug messages in this file will be shown. If false, they will not be issued.
     */
    private static boolean debug = false;


    public synchronized static boolean declareAndBindConsumerQueue(final IMessagingFocus focus, final String queueLink) {
        final String fullQueueName = getFullQueueName(queueLink, MessagingManager.getQueueFocus(focus));
//        final String routingKey = fullQueueName;
        Exchange spec = specs.get(focus);
        final String exchangeName = spec.getExchangeName();
        final String exchangeType = spec.getExchangeType();
        final Channel channel = spec.getChannel();
        if (debug) LOG.debug("Setup {} on Exchange = {}  ", fullQueueName, exchangeName);
        final boolean durable = false;
        final boolean exclusive = false;
        final boolean autoDelete = false;
        final String routingKey = fullQueueName;

        final int expireMillisecs = MessagingManager.MESSAGES_EXPIRE_IN_SECONDS * 1000;

        try {
            //   ConnectionFactory factory = new ConnectionFactory();
            //  factory.setHost(MessagingManager.BROKER_HOST_CONNECT);
            //   Connection tempConnection = factory.newConnection();

            //  Channel tempChannel = tempConnection.createChannel();
            channel.exchangeDeclare(exchangeName, exchangeType);

            Map<String, Object> args = new HashMap<>();
            args.put("x-message-ttl", expireMillisecs);

            channel.queueDeclare(fullQueueName, durable, exclusive, autoDelete, args);
            channel.queueBind(fullQueueName, exchangeName, routingKey);

            // tempChannel.close();
            // tempConnection.close();
            return true;
        } catch (IOException e) {
            LOG.error("\"Error declaring and binding quueue {}, {}. ", fullQueueName, e.getCause());
            System.exit(-37);
        }
        return false;
    }

    public static String getFullQueueName(final String queueLink, final String purpose) {
        return purpose + "." + queueLink;
    }

    public static String getQueueFocus(IMessagingFocus focus) {
        if (focus == MessagingFocus.GENERAL) return "GENERAL";
        if (focus == MessagingFocus.GENERAL_PARTICIPATE) return "_GENERAL";
        return "";
    }

    public static Channel getChannel(IMessagingFocus messagingFocus) {
        return MessagingManager.specs.get(messagingFocus).getChannel();
    }

    public static String getExchangeName(IMessagingFocus messagingFocus) {
        return MessagingManager.specs.get(messagingFocus).getExchangeName();
    }

    public static void main(String[] args) {
        initialize();
    }

    public static void initialize() {
        LOG.info("INITIALIZING MESSAGING CENTRAL EXCHANGES ......................................");
        try {
            for (Map.Entry<IMessagingFocus, Exchange> entry : specs.entrySet()) {
                Exchange spec = entry.getValue();
                ConnectionFactory factory = new ConnectionFactory();

                factory.setHost(spec.getExchangeHost());
                Connection conn = factory.newConnection();
                spec.setConnection(conn);
                spec.setChannel(spec.getConnection().createChannel());

                // declare exchange - created if does not exist
                spec.getChannel().exchangeDeclare(spec.getExchangeName(), spec.getExchangeType());
                specs.put(entry.getKey(), spec);
                if (debug) LOG.debug("   Exchange: {}", spec.toString());
            }
        } catch (Exception e) {
            LOG.error("Error: Could not initialize message exchanges. Verify RabbitMQ is running. README.txt for info. {}",
                    e.getMessage());
            System.exit(1);
        }
        LOG.info("\"\t SUCCESS: MessagingManager initialized. ");
    }

    public Map<IMessagingFocus, Exchange> getSpecs() {
        return MessagingManager.specs;
    }
}
