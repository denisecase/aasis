/**
 *
 * Copyright 2012
 * Kansas State University MACR Laboratory http://macr.cis.ksu.edu/
 * Department of Computing & Information Sciences
 *
 * See License.txt file for the license agreement.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package edu.ksu.cis.macr.aasis.agent.cc_message.connect;

import edu.ksu.cis.macr.aasis.agent.cc_message.BaseMessage;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

/**
 * Message class for establishing simulated secure communications with afflicated agents.
 */
public final class ConnectMessage extends BaseMessage<ConnectPerformative> implements IConnectMessage {
    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Constructs a new instance of a {@code ConnectMessage}.
     */
    private ConnectMessage() {
        super("", "", ConnectPerformative.SENDING_HELLO, null);
    }

    /**
     * Constructs a new instance of a {@code ConnectMessage}.
     *
     * @param localSender   - the UniqueIdentifier of the sending agent
     * @param localReceiver - the UniqueIdentifier of the receiving agent
     * @param performative  - the unique name of the associated messages action on the plan model
     * @param content       - the object containing the custom contents of the messages
     */
    private ConnectMessage(final UniqueIdentifier localSender, final UniqueIdentifier localReceiver,
                           final ConnectPerformative performative, final Object content) {
        super(localSender, localReceiver, performative, content);
        //LOG.debug("Created new ConnectMessage {}", this.toString());
    }

    /**
     * Constructs a new instance of a {@code ConnectMessage}.
     *
     * @param remoteSender   - the string abbreviation of the sending agent
     * @param remoteReceiver - the string abbreviation of the receiving agent
     * @param performative   - the unique name of the associated messages action on the plan model
     * @param content        - the object containing the custom contents of the messages
     */
    private ConnectMessage(final String remoteSender, final String remoteReceiver, final ConnectPerformative performative,
                           final Object content) {
        super(remoteSender, remoteReceiver, performative, content);
    }

    public static IConnectMessage createEmptyConnectMessage() {
        return new ConnectMessage();
    }

    public static IConnectMessage createLocalConnectMessage(final UniqueIdentifier localSender, final UniqueIdentifier localReceiver,
                                                            final ConnectPerformative performative, final Object content) {
        return new ConnectMessage(localSender, localReceiver, performative, content);
    }

    public static IConnectMessage createRemoteConnectMessage(final String remoteSender, final String remoteReceiver, final ConnectPerformative performative,
                                                             final Object content) {
        return new ConnectMessage(remoteSender, remoteReceiver, performative, content);
    }
}
