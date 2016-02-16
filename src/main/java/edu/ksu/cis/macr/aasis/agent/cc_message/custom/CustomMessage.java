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
package edu.ksu.cis.macr.aasis.agent.cc_message.custom;

import edu.ksu.cis.macr.aasis.agent.cc_message.BaseMessage;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

public class CustomMessage extends BaseMessage<Performative> implements ICustomMessage {
    private static final Logger LOG = LoggerFactory.getLogger(CustomMessage.class);
    private static final long serialVersionUID = 1L;


    /**
     * Constructs a new instance of a {@code ConnectMessage}.
     */
    public CustomMessage() {
        super("", "", Performative.GOOD, null);
    }

    /**
     * Constructs a new instance of {@code PowerMessage}.
     *
     * @param sender       - the UniqueIdentifier of the sending agent
     * @param receiver     - the UniqueIdentifier of the receiving agent
     * @param performative - the unique name of the associated messages action on the plan model
     * @param content      - the object containing the custom contents of the messages
     */
    public CustomMessage(final UniqueIdentifier sender, final UniqueIdentifier receiver,
                         final Performative performative, final Object content) {
        super(sender, receiver, performative, content);
        Objects.requireNonNull(content, "content cannot be null.");
    }

    /**
     * Constructs a new instance of {@code PowerMessage}.
     *
     * @param sender       - the String name of the sending agent
     * @param receiver     - the String name of the receiving agent
     * @param performative - the unique name of the associated messages action on the plan model
     * @param content      - the object containing the custom contents of the messages
     */
    public CustomMessage(final String sender, final String receiver,
                         final Performative performative, final Object content) {
        super(sender, receiver, performative, content);
        Objects.requireNonNull(content, "Remote Power Message Content cannot be null.");
    }

    /**
     * Constructs a new instance of a default message.
     *
     * @return the message created
     */
    public static ICustomMessage create() {
        return new CustomMessage();
    }

    @Override
    public Object deserialize(final byte[] bytes) throws Exception {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInput o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
    }

    @Override
    public byte[] serialize() throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutput o = new ObjectOutputStream(b)) {
                o.writeObject(this);
            }
            return b.toByteArray();
        }
    }
}
