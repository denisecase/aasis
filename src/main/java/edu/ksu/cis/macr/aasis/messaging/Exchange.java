package edu.ksu.cis.macr.aasis.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * A class for setting up a focused communications exchange.
 */
public class Exchange implements IExchange {
    private String exchangeName;
    private String exchangeType = "topic";
    private String exchangeHost = "localhost";
    /**
     * A Connection is a TCP connection to the messages broker that could have multiple virtual connections within it. We use
     * the connectConnection for connect messages.
     */
    private Connection connection;
    /**
     * A Channel is a light-weight virtual connection connectChannel running over a real TCP connection.  The connectChannel
     * is used to send commands to the broker (e.g. "create a queue").  There could be multiple channels on a single
     * connection. We could a single connection for all connection-related messaging or may create one connectChannel per
     * consumer (at the same time).
     */
    private Channel channel;

    public Exchange(final String exchangeName) {
        this.exchangeName = exchangeName;
        this.exchangeType = "topic";
        this.exchangeHost = "localhost";
    }

    private Exchange(String exchangeName, String exchangeType, String exchangeHost) {
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        this.exchangeHost = exchangeHost;
    }

    /**
     * Create an exchange specification for a topic exchange running on the localhost.
     *
     * @param exchangeName - the name of the exchange.
     * @return - the Exchange created
     */
    public static Exchange createExchangeSpecification(final String exchangeName) {
        return new Exchange(exchangeName);
    }

    /**
     * Create an exchange specification.
     *
     * @param exchangeName - the name of the exchange.
     * @param exchangeType - the type of the exchange, default is "topic"
     * @param exchangeHost - the exchange host, default is "localhost"
     * @return - the Exchange created
     */
    public static Exchange createExchangeSpecification(final String exchangeName, final String exchangeType, final String exchangeHost) {
        return new Exchange(exchangeName, exchangeType, exchangeHost);
    }

    public com.rabbitmq.client.Channel getChannel() {
        return this.channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getExchangeHost() {
        return exchangeHost;
    }

    @Override
    public void setExchangeHost(final String exchangeHost) {
        this.exchangeHost = exchangeHost;
    }

    @Override
    public String getExchangeName() {
        return exchangeName;
    }

    @Override
    public void setExchangeName(final String exchangeName) {
        this.exchangeName = exchangeName;
    }

    @Override
    public String getExchangeType() {
        return exchangeType;
    }

    @Override
    public void setExchangeType(final String exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "exchangeName='" + exchangeName + '\'' +
                ", exchangeType='" + exchangeType + '\'' +
                ", exchangeHost='" + exchangeHost + '\'' +
                '}';
    }
}
