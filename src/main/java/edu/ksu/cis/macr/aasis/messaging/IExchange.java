package edu.ksu.cis.macr.aasis.messaging;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * Interface for defining a communications exchange.
 */
public interface IExchange {
    /**
     * A Channel is a light-weight virtual connection connectChannel running over a real TCP connection.  The connectChannel
     * is used to send commands to the broker (e.g. "create a queue").  There could be multiple channels on a single
     * connection. We could a single connection for all connection-related messaging or may create one connectChannel per
     * consumer (at the same time).
     */
    Channel getChannel();

    /**
     * A Channel is a light-weight virtual connection connectChannel running over a real TCP connection.  The connectChannel
     * is used to send commands to the broker (e.g. "create a queue").  There could be multiple channels on a single
     * connection. We could a single connection for all connection-related messaging or may create one connectChannel per
     * consumer (at the same time).
     */
    void setChannel(final Channel channel);

    /**
     * The virtual host is the environment in which AMQP entities reside. The host partitions a message brokers data into
     * distinct sets. Connection messages are hosted on "localhost".
     */
    String getExchangeHost();

    /**
     * Set the host for the exchange.
     *
     * @param exchangeHost - the host, eg. "localhost"
     */
    void setExchangeHost(final String exchangeHost);

    /**
     * The exchange is a named entity to which messages are sent. The type of exchange determines its routing behavior.
     * Different exchanges may be used based on the types of information conveyed and the desired form of routing.
     */
    String getExchangeName();

    /**
     * Set the name of the exchange.
     *
     * @param exchangeName - the unique exchange name
     */
    void setExchangeName(final String exchangeName);

    /**
     * The type of exchange determines its routing behavior. We use a single "topic" exchange for connect messages. he topic
     * exchange takes the routing-key property of a messages into account splitting the character data of the routing key and
     * binding key into words and doing pattern-matching. Words are strings separated by dots. There are two wildcard
     * characters possible in the binding key: # (matches zero or more words) and * (matches one word).  Example: the binding
     * key "*.stock.#" matches the routing keys usd.stock and eur.stock.db but not stock.nasdaq. Source:
     * http://www.infoq.com/articles/AMQP-RabbitMQ
     */
    String getExchangeType();

    /**
     * Set the type of exhange, e.g. a "topic" exchange.
     *
     * @param exchangeType - the type of exchange - see RabbitMQ for options
     */
    void setExchangeType(final String exchangeType);

    /**
     * A Connection is a TCP connection to the messages broker that could have multiple virtual connections within it. We use
     * the connectConnection for connect messages.
     */
    void setConnection(final Connection connection);
}
