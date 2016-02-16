package edu.ksu.cis.macr.aasis.agent.persona;

import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

import java.util.Collection;
import java.util.Map.Entry;

/**
 * Enables EC communications.
 * author Christopher Zhong, modified Denise Case
 */
public interface  IInternalCommunicationCapability extends ICapability {

    /**
     * Adds the given {@code channel} with the associated {@code channelID}.
     *
     * @param channelID the unique {@code channelID} to be associated with the given {@code channel}.
     * @param channel   the {@code channel} to be associated with the {@code channelID}.
     * @return {@code true} if the {@code channel} was added successfully, {@code false} otherwise.
     */
    boolean addChannel(String channelID, ICommunicationChannel channel);

    /**
     * Broadcasts the given {@code content} on a {@code ICommunicationChannel} by the given {@code String} that identifies
     * the {@code ICommunicationChannel}.  If the given {@code String} identifying the {@code ICommunicationChannel} is
     * {@code null}, then the {@code content} is not associated with any channels.
     *
     * @param channelID the {@code channelID} associated with the {@code content}.
     * @param content   the {@code content} to be received by everyone.
     * @return {@code true} if the messages was sent, {@code false} otherwise.
     */
    boolean broadcast(String channelID, Object content);

    /**
     * Broadcasts the given {@code content} on a {@code ICommunicationChannel} by the given {@code String} that identifies
     * the {@code ICommunicationChannel}. This {@code content} is also sent to the sender.  If the given {@code String}
     * identifying the {@code ICommunicationChannel} is {@code null}, then the {@code content} is not associated with any
     * channels.
     *
     * @param channelID the {@code channelID} associated with the {@code content}.
     * @param content   the {@code content} to be received by everyone.
     * @return {@code true} if the messages was sent, {@code false} otherwise.
     */
    boolean broadcastIncludeSelf(String channelID, Object content);

    /**
     * Returns the current set of {@code ICommunicationChannel} and their associated unique identifiers.
     *
     * @return a {@code Collection} of {@code ICommunicationChannel} with their associated unique identifiers.
     */
    Collection<Entry<String, ICommunicationChannel>> getChannels();

    /**
     * Retrieves the content that does not belong to any channel.
     *
     * @return the content if there is one, {@code null} otherwise.
     */
    Object receive();

    /**
     * Removes the {@code ICommunicationChannel} that is associated with the given {@code channelID}.
     *
     * @param channelID the unique {@code channelID} to be removed along with the associated {@code ICommunicationChannel}.
     * @return {@code true} if the given {@code channelID} was removed, {@code false} otherwise.
     */
    boolean removeChannel(String channelID);

    /**
     * Replaces an existing {@code ICommunicationChannel} associated with the given {@code channelID} with the given {@code channel}.
     *
     * @param channelID the unique {@code channelID} of the {@code ICommunicationChannel} to be replaced.
     * @param channel   the {@code channel} to replace the existing {@code ICommunicationChannel}.
     * @return {@code true} if the {@code ICommunicationChannel} was replaced, {@code false} otherwise.
     */
    boolean replaceChannel(String channelID, ICommunicationChannel channel);

    /**
     * Sends the given {@code content} to the specified {@code receiver} with the specified {@code channelID}.  If the
     * {@code channelID} is {@code null}, then the {@code content} is not associated with any channels.
     *
     * @param agentID   the recipient for the given {@code content}.
     * @param channelID the {@code channelID} associated with the {@code content}.
     * @param content   the {@code content} to be received by the specified {@code receiver}.
     * @return {@code true} if the messages was sent, {@code false} otherwise.
     */
    boolean send(UniqueIdentifier agentID, String channelID, Object content);

    boolean sendLocal(UniqueIdentifier receiverIdentifier, String channelID, Object content);

    /**
     * The {@code ICommunicationChannel} interface allows the creation of additional extensions to the {@code
     * IInternalCommunicationCapability} interface.
     *
     * @author Christopher Zhong
     * @version $Revision: 1.4 $, $Date: 2010/05/17 17:06:22 $
     * @since 1.0
     */
    interface ICommunicationChannel {

        /**
         * The {@code content} that will be channeled by extensions.
         *
         * @param content the {@code content} to be passed along the {@code ICommunicationChannel}.
         */
        void channelContent(Object content);
    }
}
